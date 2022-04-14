package modele;

import java.util.HashMap;

public class Case {
    private int valeur;
    private Jeu jeu;
    private boolean estFusionne;
  //  private static int id=0;   //id de case pour comparer la clé du Hashmap  par hashcode
   // private boolean estTermine;

    public Case(int _valeur,Jeu _jeu) {
        valeur = _valeur;
        jeu = _jeu;
        estFusionne = false;
    //    id++;
    }

    public int getValeur() {
        return valeur;
    }

    public void setValeur(int _valeur) {
        valeur = _valeur;
    }

    public void setEstFusionne(boolean estFusionne) {
        this.estFusionne = estFusionne;
    }
    
    
    @Override
    public boolean equals(Object obj) {
        if (obj==null || !(obj instanceof Case)) {
            return false;
        }
        if(obj==this){
            return true;
        }

        Case c = (Case) obj;

        return c.valeur == this.valeur && c.jeu == this.jeu;
    }

    @Override
    public int hashCode() {
        int val =jeu.hashCode()+valeur;
        return val; 
    }

    
    public void deplacer(Direction d) {
        boolean estTermine= false;
        while (!estTermine) {
            Case cVoisin = jeu.getVoisin(d, this);

            if (cVoisin == null) {
                jeu.deplacer(d, this);

            } else if (cVoisin.getValeur() == -1) {
                estTermine = true;
            } else if (cVoisin.getValeur() == this.getValeur()) {
                fusion(cVoisin);
                estTermine = true;
            }
        }
        
    }

    public void fusion(Case cVoisin) {
        if (cVoisin.estFusionne == false) {
            HashMap<Case,Point> mp = jeu.getMap();
            Point p = (Point) mp.get(cVoisin);  //extraire la position de cVoisin
            mp.remove(cVoisin); //retirer la clé de cVoisin du hashmap avant de modifier sa valeur car la clé stockée en hashvalue précedent
            cVoisin.setValeur(this.valeur * 2);  //modifier sa valeur en double donc son hashvalue est changé
            mp.put(cVoisin,p);  //ajouter cVoisin comme une nouvelle clé et une nouvelle valeur
         //   jeu.getTabCases()[p.getY()][p.getX()]=cVoisin;
            jeu.enlever(this);
            cVoisin.estFusionne=true;                   
        }
    }

}
