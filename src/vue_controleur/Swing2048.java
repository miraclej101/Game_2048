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
    private static final String PATH_ICON_UNDO = "./60214_undo_icon.png";
    // tableau de cases : i, j -> case graphique
    private JLabel[][] tabC;
    private Jeu jeu;
    private JLabel lbl_score,lbl_best_score;
    private JButton btn_undo;
 //   private JPanel contentPane;

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
        Dimension d_lbl = new Dimension(80, 50);
        lbl_score.setPreferredSize(d_lbl);
        lbl_best_score = new JLabel();
        Border best_score_border = BorderFactory.createTitledBorder(lineBorder,"Best score");
        lbl_best_score.setBorder(best_score_border);
        lbl_best_score.setHorizontalAlignment(SwingConstants.RIGHT);
        lbl_best_score.setPreferredSize(d_lbl);
        ImageIcon img_undo = new ImageIcon(PATH_ICON_UNDO);
        btn_undo = new JButton(img_undo);
        btn_undo.setPreferredSize(new Dimension(40, 40));
        btn_undo.setEnabled(false);
        JPanel scorePane = new JPanel(new FlowLayout(FlowLayout.LEFT));
        scorePane.add(lbl_score);
        scorePane.add(lbl_best_score);
        scorePane.add(btn_undo);
        
        final JMenuBar menuBar = new JMenuBar();
        JMenu settingsMenu = new JMenu("Settings");
        JMenuItem newMenuItem1 = new JMenuItem("New game");
        JMenuItem newMenuItem2 = new JMenuItem("Exit");
        newMenuItem2.addActionListener(e -> actionExit());
        newMenuItem1.addActionListener(e -> actionNewGame());
        settingsMenu.add(newMenuItem1);
        settingsMenu.add(newMenuItem2);
        menuBar.add(settingsMenu);
        setJMenuBar(menuBar);
        
        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.add(scorePane,BorderLayout.NORTH);
        JPanel gridPane = new JPanel(new GridLayout(jeu.getSize(), jeu.getSize()));
  
         for (int i = 0; i < jeu.getSize(); i++) {
            for (int j = 0; j < jeu.getSize(); j++) {
                Border border = BorderFactory.createLineBorder(Color.darkGray, 5);
                tabC[i][j] = new JLabel();
                tabC[i][j].setBorder(border);
                tabC[i][j].setHorizontalAlignment(SwingConstants.CENTER);
             
                gridPane.add(tabC[i][j]);
            }
        }
       
        contentPane.add(gridPane, BorderLayout.CENTER);
        setContentPane(contentPane);
        ajouterEcouteurClavier();
        btn_undo.addActionListener(e->actionPerformed());
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
                if(jeu.estTermine()){
                    if(jeu.isGagnant()){
                        JOptionPane.showMessageDialog(rootPane,"Congratulations, you win.","Game over!", JOptionPane.INFORMATION_MESSAGE);
                    }else{
                        JOptionPane.showMessageDialog(rootPane,"You lost.","Game over!", JOptionPane.INFORMATION_MESSAGE);
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
                //setEnable button undo pour rendre possbile la reprise d'un coup précédent 
                if(jeu.getQuota_undo()>0){
                    btn_undo.setEnabled(true);
                }
      
                switch (e.getKeyCode()) { // on regarde quelle touche a été pressée
                   
                    case KeyEvent.VK_LEFT : jeu.actionThread(Direction.gauche);break;
                    case KeyEvent.VK_RIGHT : jeu.actionThread(Direction.droite);break;
                    case KeyEvent.VK_DOWN : jeu.actionThread(Direction.bas);break;
                    case KeyEvent.VK_UP : jeu.actionThread(Direction.haut);break;

                }
            }
        });
        this.setFocusable(true);
        this.requestFocusInWindow();
       
    }
   
    @Override
    public void update(Observable o, Object arg) {
        rafraichir();
    }

    private void actionPerformed() {
       this.requestFocusInWindow(); //rendre focus sur le panel encore une fois pour capturer les keyEvents
       //restituer les positions des cases dans tabCase comme un coup précédent
       jeu.reprendreCoup();
       if(jeu.getQuota_undo()<=0){
          btn_undo.setEnabled(false); 
       }
       
    }

    private void actionExit() {
        int result = JOptionPane.showConfirmDialog(this, "Do you want to end this game ?", "Exit",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);
        if (result == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    private void actionNewGame() {
       int result = JOptionPane.showConfirmDialog(this, "Do you want a new try ?", "New game",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);
        if (result == JOptionPane.YES_OPTION) {
            jeu.rnd();
            btn_undo.setEnabled(false);
        }
    }
}