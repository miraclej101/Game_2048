/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vue_controleur;

import java.awt.Color;
import java.util.Locale;
import javax.swing.JFrame;
import modele.Jeu;

/**
 *
 * @author malas
 */
public class SwingSansEcouter extends Swing2048 {
    
    public SwingSansEcouter(Jeu _jeu) {
        super(_jeu);
        super.menuItem2.setEnabled(false);
        super.menuItem3.setEnabled(false);
        super.menuBar.setBackground(Color.LIGHT_GRAY);
        super.menuBar.setOpaque(true);
        setLocation(750,300);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    }

    @Override
    public void showMessage() {
       
    }

    @Override
    protected void ajouterEcouteurClavier() {
       
    }
    
}
