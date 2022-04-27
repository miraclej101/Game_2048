package vue_controleur;

import modele.Case;
import modele.Jeu;
import modele.Direction;

import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

public class Console2048 extends Thread implements Observer {

    private Jeu jeu;

    public Console2048(Jeu _jeu) {
        jeu = _jeu;
    }

    @Override
    public void run() {
        while (true) {
            afficher();

            synchronized (this) {
                ecouteEvennementClavier();
                try {
                    wait(); // lorsque le processus s'endort, le verrou sur this est relâché, ce qui permet
                            // au processus de ecouteEvennementClavier()
                    // d'entrer dans la partie synchronisée, ce verrou évite que le réveil du
                    // processus de la console (update(..)) ne soit exécuté avant
                    // que le processus de la console ne soit endormi

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    /**
     * Correspond à la fonctionnalité de Contrôleur : écoute les évènements, et
     * déclenche des traitements sur le modèle
     */
    private void ecouteEvennementClavier() {

        final Object _this = this;

        new Thread() {
            public void run() {

                synchronized (_this) {
                    boolean end = false;

                    while (!end) {
                        String s = null;
                        try {
                            s = Character.toString((char) System.in.read());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        if (s.equals("4")) {
                            end = true;
                            jeu.actionThread(Direction.gauche);
                        } else if (s.equals("8")) {
                            end = true;
                            jeu.actionThread(Direction.haut);
                        } else if (s.equals("6")) {
                            end = true;
                            jeu.actionThread(Direction.droite);
                        } else if (s.equals("2")) {
                            end = true;
                            jeu.actionThread(Direction.bas);
                        }
                    }

                }

            }
        }.start();

    }

    /**
     * Correspond à la fonctionnalité de Vue : affiche les données du modèle
     */
    private void afficher() {

        System.out.printf("\033[H\033[J"); // permet d'effacer la console (ne fonctionne pas toujours depuis la console
                                           // de l'IDE)
        System.out.println("Score = "+jeu.getScore()+" Best score = "+jeu.getBest_score());
        for (int i = 0; i < jeu.getSize(); i++) {
            for (int j = 0; j < jeu.getSize(); j++) {
                Case c = jeu.getCase(i, j);
                if (c != null) {
                    System.out.format("%5.5s", c.getValeur());
                } else {
                    System.out.format("%5.5s", "");
                }

            }
            System.out.println();
        }
        if(jeu.estTermine()){
            if(jeu.isGagnant()){
                System.out.println("Congratulations, You win!");
            }else{
                System.out.println("You lost!");
            }
        }

    }

    private void raffraichir() {
        synchronized (this) {
            try {
                notify();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        raffraichir();
    }
}
