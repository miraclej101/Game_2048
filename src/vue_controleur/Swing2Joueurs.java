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
 * La classe qui gère la fenêtre mère de la mode 2Joueurs
 * @author Malasri Janumporn, Anh-Kiet VO
 */
public class Swing2Joueurs extends Swing2048 {
    
    private Jeu jeu;
    private SwingSansEcouter ecran2;
    private Swing2048 parent;

/**
 * Le constructeur de la classe prends un paramètre de la classe Jeu et celui de
 * la classe Swing2048 pour la fenêtre mère.
 * @param _jeu
 * @param _parent 
 */    
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
    
    /*
    * Override la mèthod d'ajouterEcouterClavier pour enlever la partie du quota_undo (bouton de reprise du coup)
    */
    @Override
    protected void ajouterEcouteurClavier() {
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
                //incremente nb_coup aprèes chaque keypresses par la classe Jeu
                System.out.println("nb_coup = "+jeu.getNb_coup());
                switchMenuBarColor();
                ecran2.switchMenuBarColor();
            }
        });
         }

    /*
    *Override showMessage pour montrer les messages différentes dans le mode 2joueurs
    */
    @Override
    public void showMessage() {
        if(jeu.estTermine()){
                    if(jeu.isGagnant()){
                        if(jeu.getNb_coup()%2==1){
                            JOptionPane.showMessageDialog(rootPane,"Congratulations, Player1 win.","Game over!", JOptionPane.INFORMATION_MESSAGE);
                        }else{
                            JOptionPane.showMessageDialog(rootPane,"Congratulations, Player2 win.","Game over!", JOptionPane.INFORMATION_MESSAGE);
                        }
                       
                    }else{
                        if(jeu.getNb_coup()%2==1){
                            JOptionPane.showMessageDialog(rootPane,"Player1 lost.","Game over!", JOptionPane.INFORMATION_MESSAGE);
                        }else{
                            JOptionPane.showMessageDialog(rootPane,"Player2 lost.","Game over!", JOptionPane.INFORMATION_MESSAGE);
                        }
                        
                    }
                }
    }

    /*
    *Override actionExit pour fermer les deux fenêtres dans le mode 2Joueurs
    et restituer la fenêtre du jeu classique (fenêtre parent).
    */
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
