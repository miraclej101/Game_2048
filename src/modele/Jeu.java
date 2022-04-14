package modele;

import java.util.Observable;
import java.util.Random;

import javax.swing.text.TabExpander;

import java.util.EventListener;
import java.util.HashMap;

public class Jeu extends Observable {

    private Case[][] tabCases;
    private static Random rnd = new Random(4);
    private HashMap<Case, Point> map = new HashMap<>();
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

    public HashMap<Case, Point> getMap() {
        return map;
    }
 
    public void rnd() {
        Jeu jeu = this;
        new Thread() { // permet de libérer le processus graphique ou de la console
            public void run() {
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
            }

        }.start();

        setChanged();
        notifyObservers();

    }
    
    public Case getVoisin(Direction d, Case c) {
       
        Point p = getPointVoisin(d, c);
        if(p.getX()==-1 || p.getY()==-1){ //une case au bord du tableau
             return new Case(-1,this);
        }
        
        return tabCases[p.getY()][p.getX()];
    }
    
    public Point getPointVoisin(Direction d, Case c){
        int i =-1;
        int j = -1;
        if (d == Direction.gauche) {
            Point p = map.get(c);
            i = p.getY();
            j = p.getX() - 1;
        }else if (d == Direction.droite) {
            Point p = map.get(c);
            i = p.getY();
            j = p.getX() + 1;
        }else if (d == Direction.haut) {
            Point p = map.get(c);
            i = p.getY() - 1;
            j = p.getX();
        }else if (d == Direction.bas) {
            Point p = map.get(c);
            i = p.getY() + 1;
            j = p.getX();
        }
        
        if (i < 0 || i >= tabCases.length || j < 0 || j >= tabCases.length) {
            i=-1;
            j=-1;
        }
        return new Point(j,i);
        
    }
    public void action(Direction d) {
        Jeu jeu = this;
        new Thread() { // permet de libérer le processus graphique ou de la console
            public void run() {
                if(d == Direction.gauche){
                    for (int i = 0; i < tabCases.length; i++) {
                        for (int j = 0; j < tabCases.length; j++) {
                            if (tabCases[i][j] != null) {
                                tabCases[i][j].deplacer(d);
                            }
                        }
                    }
                }else if(d == Direction.droite){
                    for(int i = 0;i<tabCases.length;i++){
                        for(int j=tabCases.length-1;j>=0;j--){
                            if (tabCases[i][j] != null) {
                               tabCases[i][j].deplacer(d);
                            }   
                        }
                    }
                }else if(d == Direction.haut){
                     for (int i = 0; i < tabCases.length; i++) {
                        for (int j = 0; j < tabCases.length; j++) {
                            if (tabCases[i][j] != null) {
                                tabCases[i][j].deplacer(d);
                            }
                        }
                    }
                }else if(d == Direction.bas){
                    for(int i = tabCases.length-1;i>=0;i--){
                        for(int j=0;j<tabCases.length;j++){
                            if (tabCases[i][j] != null) {
                                tabCases[i][j].deplacer(d);
                            }
                        }
                    }
                }
                  if (!jeuTermine()) {
                      nouvelleCase(); //lancer une nouvelle case à chaque tour si le jeu n'est pas encore terminé.
                }
              //Affecter estFusionne des cases non null en false après chaque coup.
             for(int i=0;i<tabCases.length;i++){
                 for(int j=0;j<tabCases.length;j++){
                     if(tabCases[i][j]!=null){
                         tabCases[i][j].setEstFusionne(false);
                     }
                 }
             }

            }  
        }.start();
         setChanged();
        notifyObservers();
    }    

    public void deplacer(Direction d, Case c) {
        Point p = getPointVoisin(d, c);
        int i = p.getY();
        int j = p.getX();

        tabCases[i][j] = c;  //deplacer case c a nouvelle position
        int row_precedent = map.get(c).getY();
        int col_precedent = map.get(c).getX();
        tabCases[row_precedent][col_precedent] = null; //set nul à l'ancienne position de la case c
        map.put(c, p);   //update hashmap ;la case c avec une nouvelle position
    }

    public void enlever(Case c) {
        int i = map.get(c).getY();
        int j = map.get(c).getX();

        tabCases[i][j] = null;
        map.remove(c);
    }

    public void nouvelleCase() {
        boolean termine = false;

        do {
            int choix = rnd.nextInt(2);
            int val = 2;

            switch (choix) {
                case 0:
                    val = 2;
                case 1:
                    val = 4;
            }

            int i = rnd.nextInt(4);
            int j = rnd.nextInt(4);

            if (tabCases[i][j] == null) {
                Case newCase = new Case(val,this);   //créer une nouvelle case avec une valeur de 2 ou 4
                tabCases[i][j] = newCase;
                Point p =new Point(j,i); //affecter cette case à la position random
                map.put(newCase, p);  //ajouter cette case et sa position dans hashmap 
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
