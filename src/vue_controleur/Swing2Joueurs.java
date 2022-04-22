/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vue_controleur;

import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import modele.Direction;
import modele.Jeu;

/**
 *
 * @author malas
 */
public class Swing2Joueurs extends Swing2048 {
    private int nb_coup =0;
    protected Jeu jeu;
    private SwingSansEcouter ecran2;
    private Swing2048 parent;
    
    public Swing2Joueurs(Jeu _jeu, Swing2048 _parent) {
        super(_jeu);
        this.jeu = _jeu;
        this.parent = _parent;
        super.menuBar.setBackground(Color.GREEN);
        super.menuBar.setOpaque(true);
        super.menuItem2.setEnabled(false);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        ecran2 = new SwingSansEcouter(_jeu);
        jeu.addObserver(ecran2);
        ecran2.setVisible(true);
    }
    

    @Override
    protected void ajouterEcouteurClavier() {
        addKeyListener(new KeyAdapter() { // new KeyAdapter() { ... } est une instance de classe anonyme, il s'agit d'un
                                          // objet qui correspond au controleur dans MVC
            @Override
            public void keyPressed(KeyEvent e) {
                //incremente nb_coup aprèes chaque keypresses
                nb_coup++;
                System.out.println("nb_coup = "+nb_coup);
                switch (e.getKeyCode()) { // on regarde quelle touche a été pressée
                   
                    case KeyEvent.VK_LEFT : jeu.actionThread(Direction.gauche);break;
                    case KeyEvent.VK_RIGHT : jeu.actionThread(Direction.droite);break;
                    case KeyEvent.VK_DOWN : jeu.actionThread(Direction.bas);break;
                    case KeyEvent.VK_UP : jeu.actionThread(Direction.haut);break;

                }
                switchMenuBarColor();
                ecran2.switchMenuBarColor();
            }
        });
     
    }

    @Override
    public void showMessage() {
        if(jeu.estTermine()){
                    if(jeu.isGagnant()){
                        if(nb_coup%2==1){
                            JOptionPane.showMessageDialog(rootPane,"Congratulations, Player1 win.","Game over!", JOptionPane.INFORMATION_MESSAGE);
                        }else{
                            JOptionPane.showMessageDialog(rootPane,"Congratulations, Player2 win.","Game over!", JOptionPane.INFORMATION_MESSAGE);
                        }
                       
                    }else{
                        if(nb_coup%2==1){
                            JOptionPane.showMessageDialog(rootPane,"Player1 lost.","Game over!", JOptionPane.INFORMATION_MESSAGE);
                        }else{
                            JOptionPane.showMessageDialog(rootPane,"Player2 lost.","Game over!", JOptionPane.INFORMATION_MESSAGE);
                        }
                        
                    }
                }
    }

    @Override
    protected void actionExit() {
        int result = JOptionPane.showConfirmDialog(this, "Do you want to end this game of 2 players ?", "Exit 2 players",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);
        if (result == JOptionPane.YES_OPTION) {
            ecran2.dispose();
            parent.setExtendedState(JFrame.NORMAL);
            this.dispose();         
        }
    }
    
    
    
}
