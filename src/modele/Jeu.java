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
    
    public Jeu(int size) {
        tabCases = new Case[size][size];
        tabCoupHistorique = new Case[size][size];
        gagnant = false;
        estTermine = false;
        score = 0;
        rnd();
       
    }

    public boolean isGagnant() {
        return gagnant;
    }
      
    public int getSize() {
        return tabCases.length;
    }

    public Case getCase(int i, int j) {
        return tabCases[i][j];
    }

    public Case[][] getTabCases() {
        return tabCases;
    }

    public boolean isEstTermine() {
        return estTermine;
    }

    public int getScore() {
        return score;
    }
    public static int getBest_score() {
        return best_score;
    }

    public int getQuota_undo() {
        return quota_undo;
    }
    
    public void setScore(int score) {
        this.score = score;
    }

    public static void setBest_score(int best_score) {
        Jeu.best_score = best_score;
    }

    public Case[][] getTabCoupHistorique() {
        return tabCoupHistorique;
    }

    
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
        Jeu jeu = this;
     //   new Thread() { // permet de libérer le processus graphique ou de la console
     //       public void run() {
                  //initialiser 2 case aléatoire des valeurs 2 ou 4
                for (int i = 0; i < tabCases.length; i++) {
                    for (int j = 0; j < tabCases.length; j++) {  
                        tabCases[i][j] = null;
                    }
                }
                for(int i=0;i<2;i++){
                    jeu.nouvelleCase();  
                }
                String path = "./best_score.txt";  //lire la meilleurs score du jeu
                int score_read = readFile(path);
                //affecter best_score si la meilleurs lu par le fichier est plus important 
                //que la variable best_score de la class
                if(best_score<score_read){    
                   best_score = score_read;
                }
                //reinitialiser quota_un =2 lors qu'un nouveau jeu lance.
                quota_undo = 2;
    //            System.out.println("Best score = "+best_score);
                /*
                int r;

                for (int i = 0; i < tabCases.length; i++) {
                    for (int j = 0; j < tabCases.length; j++) {
                        r = rnd.nextInt(3);

                        switch (r) {
                            case 0:
                                tabCases[i][j] = null;
                                break;
                            case 1:
                                Case c = new Case(2,jeu);
                                tabCases[i][j] = c;
                                map.put(c, new Point(j, i));
                                break;
                            case 2:
                                Case c4 = new Case(4,jeu);
                                tabCases[i][j] = c4;
                                map.put(c4, new Point(j, i));
                                break;
                        }
                    }
                }*/
         //   }

       // }.start();

        setChanged();
        notifyObservers();

    }
    
    public Case getVoisin(Direction d, Point p) {
       
        Point pt = getPointVoisin(d, p);
        if(pt.getX()==-1 || pt.getY()==-1){ //une case au bord du tableau
            Case cVoisin = new Case(-1,this);
             return cVoisin;
        }
        
        return tabCases[pt.getY()][pt.getX()];
    }
    
    public Point getPointVoisin(Direction d, Point p){
        int i =-1;
        int j = -1;
   //     System.out.println("idcase c getPointVoisin = "+c.getIdCase());
        if (d == Direction.gauche) {          
       //     Point p = map.get(c);
            i = p.getY();
            j = p.getX() - 1;
        }else if (d == Direction.droite) {       
        //    Point p = map.get(c);
            i = p.getY();
            j = p.getX() + 1;
        }else if (d == Direction.haut) {
           // Point p = map.get(c);
            i = p.getY() - 1;
            j = p.getX();
        }else if (d == Direction.bas) {
         //  Point p = map.get(c);
            i = p.getY() + 1;
            j = p.getX();
        }
        
        if (i < 0 || i >= tabCases.length || j < 0 || j >= tabCases.length) {
            i=-1;
            j=-1;
        }
        Point pt = new Point(j,i);
        return pt;
        
    }
    public void initCaseFusion(){
        for(int i=0;i<tabCases.length;i++){
            for(int j=0;j<tabCases.length;j++){
                if(tabCases[i][j]!=null){
                    tabCases[i][j].setEstFusionne(false);
                }
            }
        }
    }
   ///classe interne    
    class KeyThread extends Thread{
        private Direction d;
        KeyThread(Direction _d){
            d = _d;
        }
        @Override
        public void run() {
          action(d);
        } 
    }
    public void actionThread(Direction d){
        KeyThread t = new KeyThread(d);
        t.start();
        try {
            t.join();
        } catch (InterruptedException ex) {
            Logger.getLogger(Jeu.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (!jeuTermine()) {
            nouvelleCase(); //lancer une nouvelle case à chaque tour si le jeu n'est pas encore terminé.
        }
        //Affecter estFusionne des cases non null en false après chaque coup.
        initCaseFusion();
        setChanged();
        notifyObservers();      
    }
    
    public synchronized void action(Direction d){
        //enregistrer les positions des case dans tabCoupHistorique avant de deplacer les cases
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

    public void deplacer(Direction d, Case c, Point p) {
       
         Point ptVoisin = getPointVoisin(d, p);
         tabCases[ptVoisin.getY()][ptVoisin.getX()] = c;  //deplacer case c a nouvelle position
         int row_precedent = p.getY();
         int col_precedent = p.getX();
         tabCases[row_precedent][col_precedent] = null; //set nul à l'ancienne position de la case c
                     
    }

    public void enlever(Point p) {
        int i = p.getY();
        int j = p.getX();

        tabCases[i][j] = null;
    }

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
