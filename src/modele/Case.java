package modele;

import java.util.HashMap;

public class Case {
    private int valeur;
    private Jeu jeu;
    private boolean estFusionne;
    private static int id=0;   //id de case pour comparer la clé du Hashmap  par hashcode
    private int idCase; 
   // private boolean estTermine;

    public Case(int _valeur,Jeu _jeu) {
        valeur = _valeur;
        jeu = _jeu;
        estFusionne = false;
        this.idCase = ++id;
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

    public int getIdCase() {
        return idCase;
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

        return c.valeur == this.valeur && c.idCase == this.idCase;
    }

    @Override
    public int hashCode() {
        
        int val =valeur+idCase;
        return val; 
        
    }

    /*
    public void deplacer(Direction d, Point p) {
        boolean estTermine= false;
        while (!estTermine) {
            Point ptVoisin = jeu.getPointVoisin(d, p);
            Case cVoisin = jeu.getTabCases()[ptVoisin.getY()][ptVoisin.getX()];
            if (cVoisin == null) {
                jeu.deplacer(d, this,ptVoisin);

            } else if (cVoisin.getValeur() == -1) {
                estTermine = true;
            } else if (cVoisin.getValeur() == this.getValeur()) {
                System.out.println("idCase cVoisin deplacer Case = "+cVoisin.getIdCase());
                fusion(cVoisin);
                estTermine = true;
            }
        }
        
    }*/

    public void fusion(Case cVoisin, Point p) {
        if (cVoisin.estFusionne == false) {
            System.out.println("hash cVoisin_fusion avant modif = "+(cVoisin.getValeur()+cVoisin.getIdCase()));
            cVoisin.setValeur(this.valeur * 2);  //modifier sa valeur en double donc son hashvalue est changé
            cVoisin.estFusionne=true;       
            System.out.println("hash cVoisin_fusion après modif = "+(cVoisin.getValeur()+cVoisin.getIdCase()));
            jeu.enlever(p);            
        }
    }

}
