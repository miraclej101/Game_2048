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
    protected final JMenuBar menuBar;
    protected JMenuItem menuItem1,menuItem2,menuItem3;


    public Swing2048(Jeu _jeu) {
        // Référence de notre jeu
        jeu = _jeu;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // On déclare la taille de notre 2048
        setSize(jeu.getSize() * PIXEL_PER_SQUARE, (jeu.getSize()+1) * PIXEL_PER_SQUARE);
        
        // JLabel pour mettre notre grille du 2048
        tabC = new JLabel[jeu.getSize()][jeu.getSize()];
        
        // JLabel pour mettre le score
        lbl_score = new JLabel(); 
        LineBorder lineBorder = new LineBorder(Color.BLUE);
        Border score_border = BorderFactory.createTitledBorder(lineBorder,"Score");
        lbl_score.setBorder(score_border);
        lbl_score.setHorizontalAlignment(SwingConstants.RIGHT);
        Dimension d_lbl = new Dimension(80, 50);
        lbl_score.setPreferredSize(d_lbl);
        
        // JLabel pour le best score
        lbl_best_score = new JLabel();
        Border best_score_border = BorderFactory.createTitledBorder(lineBorder,"Best score");
        lbl_best_score.setBorder(best_score_border);
        lbl_best_score.setHorizontalAlignment(SwingConstants.RIGHT);
        lbl_best_score.setPreferredSize(d_lbl);
        
        // JButton pour annuler un coup
        ImageIcon img_undo = new ImageIcon(PATH_ICON_UNDO);
        btn_undo = new JButton(img_undo);
        btn_undo.setPreferredSize(new Dimension(40, 40));
        btn_undo.setEnabled(false);
        
        // JPanel pour inserer le score et le best score
        JPanel scorePane = new JPanel(new FlowLayout(FlowLayout.LEFT));
        scorePane.add(lbl_score);
        scorePane.add(lbl_best_score);
        scorePane.add(btn_undo);
        
        // Notre barre de menu de navigation
        menuBar = new JMenuBar();
        JMenu settingsMenu = new JMenu("Settings");
        // En fonction de ce que l'utilisateur clique sur le menu déroulante dans la
        // barre de menu de navigation on appelle une fonction
        menuItem1 = new JMenuItem("New game");
        menuItem2 = new JMenuItem("2 Players");
        menuItem3 = new JMenuItem("Exit");
        menuItem1.addActionListener(e -> actionNewGame());
        menuItem2.addActionListener(e -> action2Joueurs());
        menuItem3.addActionListener(e -> actionExit());
        settingsMenu.add(menuItem1);
        settingsMenu.add(menuItem2);
        settingsMenu.add(menuItem3);
        menuBar.add(settingsMenu);
        setJMenuBar(menuBar);
        
        // On créer un JPanel
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
       
        // On veut afficher le panel contentPane qui contient tous nos JPanel
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
              showMessage();
               
            }
        });
    }

    

    /**
     * Correspond à la fonctionnalité de Contrôleur : écoute les évènements, et
     * déclenche des traitements sur le modèle
     */
    protected void ajouterEcouteurClavier() {
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

    // Affichage d'un pop-up pour confirmer si l'utilisateur veut quitter
    protected void actionExit() {
        int result = JOptionPane.showConfirmDialog(this, "Do you want to end this game ?", "Exit",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);
        if (result == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    // Affichage d'un pop-up pour confirmer si l'utilisateur veut nouvelle partie et
    // va appeller rnd()
    private void actionNewGame() {
       int result = JOptionPane.showConfirmDialog(this, "Do you want a new try ?", "New game",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);
        if (result == JOptionPane.YES_OPTION) {
            jeu.rnd();
            btn_undo.setEnabled(false);
        }
    }
    
    // Affichage d'un pop-up pour notifier l'utilisateur s'il a gagné ou perdu
    public void showMessage(){
         if(jeu.estTermine()){
                    if(jeu.isGagnant()){
                        JOptionPane.showMessageDialog(rootPane,"Congratulations, you win.","Game over!", JOptionPane.INFORMATION_MESSAGE);
                    }else{
                        JOptionPane.showMessageDialog(rootPane,"You lost.","Game over!", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
    }

     // Création d'un nouvelle instance de Jeu et de Swing2Joueurs pour avoir 2
    // interfaces Swing pour le mode 2 joueur
    private void action2Joueurs() {
        Jeu newJeu = new Jeu(4);
        Swing2Joueurs ecran1 = new Swing2Joueurs(newJeu,this);
        newJeu.addObserver(ecran1);
        ecran1.setLocation(500,300);
        ecran1.setVisible(true);
        this.setState(JFrame.ICONIFIED); //minimise la fenêtre après avoir ouvert un écran de 2 joueurs
    }
    
    // Change la couleur de la barre de menu de navigation. Si la couleur est verte
    // on change par gris et vice-versa pour le mode 2 joueurs.
    public void switchMenuBarColor(){
        Color color;
        if(menuBar.getBackground() == Color.GREEN){
           color = Color.LIGHT_GRAY;
        }else{
           color = Color.GREEN; 
        }
        menuBar.setBackground(color);
        menuBar.setOpaque(true);
    }
     
}