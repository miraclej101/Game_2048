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
import java.util.Observable;
import java.util.Observer;
import javax.swing.border.LineBorder;

public class Swing2048 extends JFrame implements Observer {
    private static final int PIXEL_PER_SQUARE = 60;
    // tableau de cases : i, j -> case graphique
    private JLabel[][] tabC;
    private Jeu jeu;
    private JLabel lbl_score,lbl_best_score;

    public Swing2048(Jeu _jeu) {
        jeu = _jeu;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(jeu.getSize() * PIXEL_PER_SQUARE, (jeu.getSize()+1) * PIXEL_PER_SQUARE);
        tabC = new JLabel[jeu.getSize()][jeu.getSize()];
        lbl_score = new JLabel(); 
        LineBorder lineBorder = new LineBorder(Color.BLUE);
        Border score_border = BorderFactory.createTitledBorder(lineBorder,"Score");
        lbl_score.setBorder(score_border);
        lbl_score.setHorizontalAlignment(SwingConstants.RIGHT);
        lbl_best_score = new JLabel();
        Border best_score_border = BorderFactory.createTitledBorder(lineBorder,"Best");
        lbl_best_score.setBorder(best_score_border);
        lbl_best_score.setHorizontalAlignment(SwingConstants.RIGHT);
        
        JPanel contentPane = new JPanel(new GridLayout(jeu.getSize()+1, jeu.getSize()));
        contentPane.add(lbl_score);
        contentPane.add(lbl_best_score);
        for(int i=0;i<2;i++){
           contentPane.add(new JLabel()); //ajouter blank labels à grid contentPane  
        }
       
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
                lbl_score.setText(jeu.getScore()+"");
                lbl_best_score.setText(Jeu.getBest_score()+"");
                for (int i = 0; i < jeu.getSize(); i++) {
                    for (int j = 0; j < jeu.getSize(); j++) {
                        Case c = jeu.getCase(i, j);

                        if (c == null) {
                            tabC[i][j].setText("");
                            tabC[i][j].setBackground(Color.WHITE);
                        }else{ 
                            int choix = c.getValeur();
                            switch(choix){
                                 case 2:
                                    tabC[i][j].setBackground(Color.CYAN);
                                    break;
                                case 4:
                                    tabC[i][j].setBackground(Color.decode("#E9D7BD"));
                                    break;
                                case 8:
                                    tabC[i][j].setBackground(Color.decode("#C88E52"));
                                    break;
                                case 16:
                                    tabC[i][j].setBackground(Color.decode("#C06C3D"));
                                    break;
                                case 32:
                                    tabC[i][j].setBackground(Color.ORANGE);
                                    break;
                                case 64:
                                    tabC[i][j].setBackground(Color.decode("#F9825C"));
                                    break;
                                case 128:
                                    tabC[i][j].setBackground(Color.PINK);
                                    break;
                                default:
                                    tabC[i][j].setBackground(Color.decode("#E6DCD1"));
                                    break;
                            }
                            tabC[i][j].setText(c.getValeur() + "");
                            tabC[i][j].setOpaque(true);
                        }
                            
                    }

                }
                if(jeu.isEstTermine()){
                    if(jeu.isGagnant()){
                        JOptionPane.showMessageDialog(rootPane,"Congratulations, vous avez gagné.","Jeu termine", JOptionPane.INFORMATION_MESSAGE);
                    }else{
                        JOptionPane.showMessageDialog(rootPane,"Vous avez perdu.","Jeu termine", JOptionPane.INFORMATION_MESSAGE);
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
                   
                    case KeyEvent.VK_LEFT : jeu.actionThread(Direction.gauche);break;
                    case KeyEvent.VK_RIGHT : jeu.actionThread(Direction.droite);break;
                    case KeyEvent.VK_DOWN : jeu.actionThread(Direction.bas);break;
                    case KeyEvent.VK_UP : jeu.actionThread(Direction.haut);break;

                }
            }
        });
    }
   
    @Override
    public void update(Observable o, Object arg) {
        rafraichir();
    }
}