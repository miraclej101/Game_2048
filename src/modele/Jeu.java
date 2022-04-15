package modele;

import java.util.Observable;
import java.util.Random;

import javax.swing.text.TabExpander;

import java.util.EventListener;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Jeu extends Observable {

    private Case[][] tabCases;
    private static Random rnd = new Random();
    private boolean gangant;

    public Jeu(int size) {
        tabCases = new Case[size][size];
        gangant = false;
        rnd();
       
    }

    public boolean isGangant() {
        return gangant;
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
        if(d == Direction.gauche){
            for (int i = 0; i < tabCases.length; i++) {
                for (int j = 1; j < tabCases.length; j++) {
                    if (tabCases[i][j] != null) {
                        Point pt = new Point(j,i);
                        deplacer(d,tabCases[i][j],pt);
                    }
                }
            }
        }else if(d == Direction.droite){
            for(int i = 0;i<tabCases.length;i++){
                for(int j=tabCases.length-2;j>=0;j--){
                    if (tabCases[i][j] != null) {
                        Point pt = new Point(j,i);  
                        deplacer(d, tabCases[i][j],pt);
                    }   
                }
            }
        }else if(d == Direction.haut){
            for (int i = 1; i < tabCases.length; i++) {
                for (int j = 0; j < tabCases.length; j++) {
                    if (tabCases[i][j] != null) {
                        Point pt = new Point(j,i);  
                        deplacer(d, tabCases[i][j],pt);
                    }
                }
            }
        }else if(d == Direction.bas){
            for(int i = tabCases.length-2;i>=0;i--){
                for(int j=0;j<tabCases.length;j++){
                    if (tabCases[i][j] != null) {
                        Point pt = new Point(j,i);  
                        deplacer(d, tabCases[i][j],pt);
                    }
                }
            }
        }
     
    }    

    public void deplacer(Direction d, Case c, Point p) {
        
        boolean estTermine= false;
        while (!estTermine) {
            Point ptVoisin = getPointVoisin(d, p);
            Case cVoisin = getVoisin(d, p);
            if (ptVoisin.getX()==-1 || ptVoisin.getY() ==-1 ){
                estTermine = true;           
            } else  if(cVoisin == null) {
                System.out.println("IdCase c deplacer Jeu = "+c.getIdCase());
                tabCases[ptVoisin.getY()][ptVoisin.getX()] = c;  //deplacer case c a nouvelle position
                int row_precedent = p.getY();
                int col_precedent = p.getX();
                tabCases[row_precedent][col_precedent] = null; //set nul à l'ancienne position de la case c
                p=ptVoisin;
            } else if (cVoisin.getValeur() == c.getValeur()) {
                System.out.println("idCase cVoisin deplacer Case = "+cVoisin.getIdCase());
                c.fusion(cVoisin, p);
                estTermine = true;
            }else{
                estTermine = true;
            }
        }
             
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
                    gangant = true; // retourne gagnant = true si un joueur termine le jeu par une case de valeur de 2048.
                    return true;
                }
                else if (tabCases[i][j] == null) {
                    return false;
                }

            }
        }
        return true;
    }

}
