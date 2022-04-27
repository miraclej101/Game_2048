package modele;


public class Case  implements Cloneable{
    private int valeur;
    private Jeu jeu;
    private boolean estFusionne;
 
    // Une case a une référence sur un jeu, une valeur et un booléen estFusionne
    public Case(int _valeur,Jeu _jeu) {
        valeur = _valeur;
        jeu = _jeu;
        estFusionne = false;
    
    }
    
    // Getter qui récupère la valeur de la Case
    public int getValeur() {
        return valeur;
    }

    // Setter qui modifie la valeur de la Case
    public void setValeur(int _valeur) {
        valeur = _valeur;
    }

    // Setter qui modifie le booléen estFusionne
    public void setEstFusionne(boolean estFusionne) {
        this.estFusionne = estFusionne;
    }

    // Override de la fonction clone pour cloner notre case
    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone(); 
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
    /*
     * Permet de déplacer la case
     * Tant que le déplacement n'est pas terminé
     * on déplace la case en récuperant les coordonées de la case voisine de notre
     * grille
     * ainsi que la valeur de cette case. Si null on déplace sinon on ne déplace pas
     * Si la case voisine a la meme valeur que notre case actuel alors on va
     * fusionner
     */ 
    public void deplacer(Direction d, Point p) {
        boolean estTermine= false;
        while (!estTermine) {
            Point ptVoisin = jeu.getPointVoisin(d, p);
            Case cVoisin = jeu.getVoisin(d, p);
            if(cVoisin == null) {
               jeu.deplacer(d, this, p);
               p = ptVoisin;       
            } else if(cVoisin.getValeur()==-1 ){
              estTermine = true;    
            } else if (cVoisin.getValeur() == this.getValeur()) {
                fusion(cVoisin, p);
                estTermine = true;
            }else{
                estTermine = true;
            }
        }
        
    }
    
    /*
     * Si la case voisine n'a pas encore été fusionné lors du déplacement
     * On la fusionne en doublant la valeur de cette case
     * On récupère le score et on change celui-ci
     * On enleve aussi dans notre grille les valeurs et les positions de notre case
     * étant donné qu'elle a été fusionné
     */
    public void fusion(Case cVoisin, Point p) {
        if (cVoisin.estFusionne == false) {
            cVoisin.setValeur(this.valeur * 2);  //modifier sa valeur en double
            cVoisin.estFusionne=true;
            int score = jeu.getScore();
            score +=this.valeur*2;
            jeu.setScore(score);
    //        System.out.println("Score = "+score);
            jeu.enlever(p);     
            int bestScore = Jeu.getBest_score();    
            if(bestScore<score){
                Jeu.setBest_score(score);
                String path = "./best_score.txt";
                Jeu.writeFile(path);
                System.out.println("Best score = "+Jeu.getBest_score());
            }
        }
    }

}
