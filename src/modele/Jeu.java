package modele;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Observable;
import java.util.Random;

import javax.swing.text.TabExpander;

import java.util.EventListener;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Jeu extends Observable {

    private Case[][] tabCases;
    private static Random rnd = new Random();
    private boolean gagnant;
    private boolean estTermine;
    private int score;
    private static int best_score =0;
    private Case[][] tabCoupHistorique;
    private int quota_undo =2; //nombre de fois qu'on peut reprendre d'un coup précédent
    private int nb_coup =0;  //nb_coup pour détermine quel joueur donne un coup passant 
    
    // Constructeur de notre classe Jeu
    public Jeu(int size) {
        tabCases = new Case[size][size];
        tabCoupHistorique = new Case[size][size];
        gagnant = false;
        estTermine = false;
        score = 0;
        rnd();
       
    }
    
    //Renvoyer le boolean de la variable gagnant qui determine si un joueur gagne à la fin du jeu.
    public boolean isGagnant() {
        return gagnant;
    }
    
    // Getter qui récupère la taille de notre tableau de Case
    public int getSize() {
        return tabCases.length;
    }

    // Getter qui récupère la valeur de la cASE
    public Case getCase(int i, int j) {
        return tabCases[i][j];
    }

    // Booléen pour déterminer si le jeu est terminé
    public boolean estTermine() {
        return estTermine;
    }

    // Getter qui récupère le score
    public int getScore() {
        return score;
    }
    
    // Getter qui récupère le meilleur score
    public static int getBest_score() {
        return best_score;
    }

    // Getter qui récupère le quota pour annuler un coup
    public int getQuota_undo() {
        return quota_undo;
    }
    
    // Setter qui modifie le score
    public void setScore(int score) {
        this.score = score;
    }
    
    // Setter qui modifie le meilleur score
    public static void setBest_score(int best_score) {
        Jeu.best_score = best_score;
    }

    // Getter qui récupère l'hitorique de notre tableau de Case
    public Case[][] getTabCoupHistorique() {
        return tabCoupHistorique;
    }

    //Getter qui récupère un nombre de coup déjà effectue dans le jeu
    public int getNb_coup() {
        return nb_coup;
    }
    
    /*
     * On parcourt notre tableau de Case pour le cloner afin de stocker la grille
     * avant déplacement
     */
    private void saveTabCases(){
        for(int i=0;i<tabCases.length;i++){
            for(int j=0;j<tabCases.length;j++){
                try {
                    Case c = tabCases[i][j];
                    if(c==null){
                       tabCoupHistorique[i][j] = null;
                    }else{
                       tabCoupHistorique[i][j] = (Case) tabCases[i][j].clone();  
                    }    
                } catch (CloneNotSupportedException ex) {
                    Logger.getLogger(Jeu.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    
     /*
     * On parcourt notre tableau de case afin de mettre les valeurs de notre grille
     * avec les anciennes valeurs avant déplacement, fonction appelé pour annuler un
     * coup
     */
    public void reprendreCoup(){
        for(int i = 0; i<tabCoupHistorique.length;i++){
           for(int j =0;j<tabCoupHistorique.length;j++){
               tabCases[i][j] = tabCoupHistorique[i][j];
           }
       }
       quota_undo--;  //un nombre de quota undo décrémenté par 1
        setChanged();
        notifyObservers(); 
    }
    
     // Pour lire les fichiers best_score.txt
    private int readFile(String path){
        int score_readfile = 0; 
        try {
            File file = new File(path);
            Scanner sc = new Scanner(file);
            if(sc.hasNextLine()){
                score_readfile = sc.nextInt();
            }
            sc.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Jeu.class.getName()).log(Level.SEVERE, null, ex);
        }
        return score_readfile;
    }
    
    // Pour ecrire dans le fichier best_score.txt le score
    public static void writeFile(String path){
        try {
            FileWriter fWriter = new FileWriter(path);
            fWriter.write(best_score+"");
            fWriter.close();
        } catch (IOException ex) {
            Logger.getLogger(Jeu.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public void rnd() {
    //    Jeu jeu = this;
                 // On initialise le score à 0 à chaque nouvelle partie
                score = 0;
                //i,intialiser nb_coup = 0 pour la version 2 Players
                nb_coup=0;
                //On initialise notre grille du 2048, tous les positions sont null;
                for (int i = 0; i < tabCases.length; i++) {
                    for (int j = 0; j < tabCases.length; j++) {  
                        tabCases[i][j] = null;
                    }
                }
                // On initialise 2 case aléatoire en valeur 2 ou 4
                for(int i=0;i<2;i++){
                    nouvelleCase();  
                }
                // On lit le meilleur score de la partie
                String path = "./best_score.txt";  //lire la meilleurs score du jeu
                int score_read = readFile(path);
                /*
                * On affecte best_score si le meilleur lu par le fichier
                * est plus important que la variable best score de la class
                */
                if(best_score<score_read){    
                   best_score = score_read;
                }
                // Réinitialiser le quota_un à 2 lors qu'un nouveau jeu est lancé
                quota_undo = 2;
                // Réinitialiser le booléen estTermine du jeu après qu'un nouveau jeu est lancé
                estTermine = false; 
             
        setChanged();
        notifyObservers();

    }
    
     /*
     * On appelle getPointVoisin pour récuperer les coordonées de cette case
     * si au bord d'une case on return une nouvelle case avec valeur -1
     * Sinon on return la valeur de cette case voisine
     */
    public Case getVoisin(Direction d, Point p) {
       
        Point pt = getPointVoisin(d, p);
        if(pt.getX()==-1 || pt.getY()==-1){ //une case au bord du tableau
            Case cVoisin = new Case(-1,this);
             return cVoisin;
        }
        
        return tabCases[pt.getY()][pt.getX()];
    }
    
    /*
     * En fonction de la direction indiqué, on récupère les coordonées de cette case
     * voisine
     */
    public Point getPointVoisin(Direction d, Point p){
        int i =-1;
        int j = -1;
   //     System.out.println("idcase c getPointVoisin = "+c.getIdCase());
        if (d == Direction.gauche) {          
            i = p.getY();
            j = p.getX() - 1;
        }else if (d == Direction.droite) {       
            i = p.getY();
            j = p.getX() + 1;
        }else if (d == Direction.haut) {
            i = p.getY() - 1;
            j = p.getX();
        }else if (d == Direction.bas) {
            i = p.getY() + 1;
            j = p.getX();
        }
        // Si on tombe sur les bords d'une case on affcte i et j égale à -1, et renvoie ce point 
        if (i < 0 || i >= tabCases.length || j < 0 || j >= tabCases.length) {
            i=-1;
            j=-1;
        }
        Point pt = new Point(j,i);
        return pt;
        
    }
    
    // Initialise la fusion des cases à false après chaque déplacement
    public void initCaseFusion(){
        for(int i=0;i<tabCases.length;i++){
            for(int j=0;j<tabCases.length;j++){
                if(tabCases[i][j]!=null){
                    tabCases[i][j].setEstFusionne(false);
                }
            }
        }
    }
    // Classe interne pour la mise en place de thread dans le déplacement
    class KeyThread extends Thread{
        private Direction d;
        // On prends un paramètre de la direction indiqué (KeyListenner), 
        KeyThread(Direction _d){
            d = _d;
        }
        // Au lancement de Keythread on va run la fonction action
        @Override
        public void run() {
          action(d);
        } 
    }
    
    // actionThread va instancer un thread pour le déplacement
    public void actionThread(Direction d){
        KeyThread t = new KeyThread(d);
        // start du thread
        t.start();  
        try {
            // attendre jusqu'à la fin du thread
            t.join();
            nb_coup++;  //incrementer nb_coup pour utiliser dans la version 2 Players
        } catch (InterruptedException ex) {
            Logger.getLogger(Jeu.class.getName()).log(Level.SEVERE, null, ex);
        }
        /* Si le jeu n'est pas terminé, à chaque déplacement on va initialiser une
        * nouvelleCase
        */
        if (!jeuTermine()) {
            nouvelleCase(); //lancer une nouvelle case à chaque tour si le jeu n'est pas encore terminé.
        }
        //Affecter estFusionne des cases non null en false après chaque coup.
        initCaseFusion();
        setChanged();
        notifyObservers();      
    }
    
    /*
     * En fonction de la direction indiqué par l'utilisateur nous allons créer un
     * nouveau point
     * et nous déplaçons cette case en appellant la fonction deplacer dans Case qui
     * va déplacer celui-ci
     */
    public synchronized void action(Direction d){
        // Avant le déplacement on enregistre notre grille dans tabCoupHistorique
        saveTabCases();
        if(d == Direction.gauche){
            for (int i = 0; i < tabCases.length; i++) {
                for (int j = 1; j < tabCases.length; j++) {
                    if (tabCases[i][j] != null) {
                        Point pt = new Point(j,i);
                        tabCases[i][j].deplacer(d,pt);
                    }
                }
            }
        }else if(d == Direction.droite){
            for(int i = 0;i<tabCases.length;i++){
                for(int j=tabCases.length-2;j>=0;j--){
                    if (tabCases[i][j] != null) {
                        Point pt = new Point(j,i);  
                        tabCases[i][j].deplacer(d,pt);
                    }   
                }
            }
        }else if(d == Direction.haut){
            for (int i = 1; i < tabCases.length; i++) {
                for (int j = 0; j < tabCases.length; j++) {
                    if (tabCases[i][j] != null) {
                        Point pt = new Point(j,i);  
                        tabCases[i][j].deplacer(d,pt);
                    }
                }
            }
        }else if(d == Direction.bas){
            for(int i = tabCases.length-2;i>=0;i--){
                for(int j=0;j<tabCases.length;j++){
                    if (tabCases[i][j] != null) {
                        Point pt = new Point(j,i);  
                        tabCases[i][j].deplacer(d,pt);
                    }
                }
            }
        }
     
    }    

     /*
     * Fonction utilisé par la Case dans deplacer()
     * Permet de récuperer les coordoonées de la case voisine
     * et de déplacer notre case actuel c à cette nouvelle position
     * on récupère les coordoonées actuel pour les mettres à null
     */
    public void deplacer(Direction d, Case c, Point p) {
       
         Point ptVoisin = getPointVoisin(d, p);
         tabCases[ptVoisin.getY()][ptVoisin.getX()] = c;  //deplacer case c a nouvelle position
         int row_precedent = p.getY();
         int col_precedent = p.getX();
         tabCases[row_precedent][col_precedent] = null; //set nul à l'ancienne position de la case c
                     
    }

    // Enlever une valeur de la case de la grille en mettant nul 
    public void enlever(Point p) {
        int i = p.getY();
        int j = p.getX();

        tabCases[i][j] = null;
    }

    // Permet d'initialiser dans 2 cases aléatoires la valeur de 2 ou 4
    public void nouvelleCase() {
        boolean termine = false;
       
        do {
            int choix = rnd.nextInt(2);
            int val = 2;

            if(choix==0){
                val = 2;
            }else{
                val = 4;
            }

            int i = rnd.nextInt(4);
            int j = rnd.nextInt(4);

            if (tabCases[i][j] == null) {
                Case newCase = new Case(val,this);   //créer une nouvelle case avec une valeur de 2 ou 4
                tabCases[i][j] = newCase;
                termine = true;
            }
        } while (!termine);
    }

     /*
     * On parcourt la grille de jeu et on vérifie
     * les conditions de victoire ou de défaite
     * Si la case n'est plus null et si la valeur est 2048 alors victoire
     * Sinon, le joueur perd.
     */
    public boolean jeuTermine() {
        for(int i = 0; i < tabCases.length; i++) {
            for (int j = 0; j < tabCases.length; j++) {
                if(tabCases[i][j]!=null && tabCases[i][j].getValeur()==2048){
                    gagnant = true; // retourne gagnant = true si un joueur termine le jeu par une case de valeur de 2048.
                    estTermine = true;
                    return true;
                }
                else if (tabCases[i][j] == null) {
                    return false;
                }

            }
        }
        estTermine = true;
        return true;
    }

}
