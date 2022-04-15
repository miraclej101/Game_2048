package modele;


public class Case {
    private int valeur;
    private Jeu jeu;
    private boolean estFusionne;
 
    public Case(int _valeur,Jeu _jeu) {
        valeur = _valeur;
        jeu = _jeu;
        estFusionne = false;
    
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

    
  
    /*
    
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
*/
    
    public void deplacer(Direction d, Point p) {
        boolean estTermine= false;
        while (!estTermine) {
            Point ptVoisin = jeu.getPointVoisin(d, p);
            Case cVoisin = jeu.getVoisin(d, p);
            if (ptVoisin.getX()==-1 || ptVoisin.getY() ==-1 ){
                estTermine = true;           
            } else  if(cVoisin == null) {
               jeu.deplacer(d, this, p);
               p = ptVoisin;
            } else if (cVoisin.getValeur() == this.getValeur()) {
                fusion(cVoisin, p);
                estTermine = true;
            }else{
                estTermine = true;
            }
        }
        
    }

    public void fusion(Case cVoisin, Point p) {
        if (cVoisin.estFusionne == false) {
            cVoisin.setValeur(this.valeur * 2);  //modifier sa valeur en double donc son hashvalue est changé
            cVoisin.estFusionne=true;       
            jeu.enlever(p);            
        }
    }

}
