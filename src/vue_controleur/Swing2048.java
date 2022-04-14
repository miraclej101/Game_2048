package vue_controleur;

import modele.Case;
import modele.Jeu;
import modele.Direction;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

public class Swing2048 extends JFrame implements Observer {
    private static final int PIXEL_PER_SQUARE = 60;
    // tableau de cases : i, j -> case graphique
    private JLabel[][] tabC;
    private Jeu jeu;

    public Swing2048(Jeu _jeu) {
        jeu = _jeu;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(jeu.getSize() * PIXEL_PER_SQUARE, jeu.getSize() * PIXEL_PER_SQUARE);
        tabC = new JLabel[jeu.getSize()][jeu.getSize()];

        JPanel contentPane = new JPanel(new GridLayout(jeu.getSize(), jeu.getSize()));

         for (int i = 0; i < jeu.getSize(); i++) {
            for (int j = 0; j < jeu.getSize(); j++) {
                Border border = BorderFactory.createLineBorder(Color.darkGray, 5);
                tabC[i][j] = new JLabel();
                tabC[i][j].setBorder(border);
                tabC[i][j].setHorizontalAlignment(SwingConstants.CENTER);
             
                contentPane.add(tabC[i][j]);
            }
        }
      
       
        setContentPane(contentPane);
        ajouterEcouteurClavier();
        rafraichir();
             
    }
     

    /**
     * Correspond à la fonctionnalité de Vue : affiche les données du modèle
     */
    private void rafraichir() {

        SwingUtilities.invokeLater(new Runnable() { // demande au processus graphique de réaliser le traitement
            @Override
            public void run() {
                for (int i = 0; i < jeu.getSize(); i++) {
                    for (int j = 0; j < jeu.getSize(); j++) {
                        Case c = jeu.getCase(i, j);

                        if (c == null) {
                            tabC[i][j].setText("");
                        }else{ 
                            int choix = c.getValeur();
                            switch(choix){
                                case 2:
                                    tabC[i][j].setForeground(Color.gray);
                                    break;
                                case 4:
                                    tabC[i][j].setForeground(Color.blue);
                                    break;
                                case 8:
                                    tabC[i][j].setForeground(Color.orange);
                                    break;
                                case 16:
                                    tabC[i][j].setForeground(Color.cyan);
                                    break;
                                case 32:
                                    tabC[i][j].setForeground(Color.green);
                                    break;
                            }
                            tabC[i][j].setText(c.getValeur() + "");
                        }
                            
                    }

                }
            }
        });
    }

    

    /**
     * Correspond à la fonctionnalité de Contrôleur : écoute les évènements, et
     * déclenche des traitements sur le modèle
     */
    private void ajouterEcouteurClavier() {
        addKeyListener(new KeyAdapter() { // new KeyAdapter() { ... } est une instance de classe anonyme, il s'agit d'un
                                          // objet qui correspond au controleur dans MVC
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) { // on regarde quelle touche a été pressée
                   
                    case KeyEvent.VK_LEFT : jeu.action(Direction.gauche);break;
                    case KeyEvent.VK_RIGHT : jeu.action(Direction.droite);break;
                    case KeyEvent.VK_DOWN : jeu.action(Direction.bas);break;
                    case KeyEvent.VK_UP : jeu.action(Direction.haut);break;

                }
            }
        });
    }
   
    @Override
    public void update(Observable o, Object arg) {
        rafraichir();
    }
}