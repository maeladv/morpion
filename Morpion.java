import java.awt.*;
import javax.swing.JFrame;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.function.Consumer;
import java.awt.BasicStroke;

public class Morpion extends JFrame {
    int x = 250, y = 300;
    boolean croix = false;
    char[][] morpion = new char[3][3];
    char gagnant = 'a'; // stocker le gagnant
    
    private int gameMode; // 0 pour serveur, 1 pour client
    private boolean monTour;
    private Consumer<String> sendMoveCallback;

    // gameMode = 0 pour serveur, 1 pour client
    public Morpion(int gameMode) {
        this.gameMode = gameMode;
        // Pour le client (1), c'est son tour en premier, pour le serveur (0), ce n'est pas son tour
        this.monTour = gameMode == 1;
        
        addMouseListener(new Souris(this));
        setSize(500, 600);
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(
            d.width / 2 - this.getWidth() / 2,
            d.height / 2 - this.getHeight() / 2
        );
        
        String joueur = gameMode == 1 ? "Client (X, premier)" : "Serveur (O, second)";
        setTitle("Morpion - " + joueur);
        
        // Initialiser la couleur de fond selon si c'est notre tour ou pas
        if (monTour) {
            getContentPane().setBackground(new Color(230, 255, 230)); // Vert notre tour
        } else {
            getContentPane().setBackground(new Color(255, 245, 230)); // Orange tour de l'adversaire
        }
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                morpion[i][j] = 'a';
            }
        }
    }

    public void drawCrois(Graphics g, int x_hg, int y_hg, int x_hd, int y_hd, int x_bg, int y_bg, int x_bd, int y_bd, int marge){
        Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(new BasicStroke(5)); // Épaisseur de 5 pixels
        g2d.drawLine(x_hg+marge, y_hg+marge, x_bd-marge, y_bd-marge);
        g2d.drawLine(x_hd-marge, y_hd+marge, x_bg+marge, y_bg-marge);
    }

    public char gagne(char[][] m) {
        // Vérifie chaque ligne, colonne et diagonale
        for (int i = 0; i < 3; i++) {
            // Lignes
            switch ("" + m[i][0] + m[i][1] + m[i][2]) {
                case "xxx": return 'x';
                case "ooo": return 'o';
            }
            // Colonnes
            switch ("" + m[0][i] + m[1][i] + m[2][i]) {
                case "xxx": return 'x';
                case "ooo": return 'o';
            }
        }
        // Diagonale principale
        switch ("" + m[0][0] + m[1][1] + m[2][2]) {
            case "xxx": return 'x';
            case "ooo": return 'o';
        }
        // Diagonale secondaire
        switch ("" + m[0][2] + m[1][1] + m[2][0]) {
            case "xxx": return 'x';
            case "ooo": return 'o';
        }
        return 'a'; // Personne n'a gagné
    }

    public boolean estMatchNul(char[][] m) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (m[i][j] == 'a') return false;
            }
        }
        return gagne(m) == 'a'; // vrai si toutes les cases sont remplies et pas de gagnant
    }

    public void paint(Graphics g) {
        int marge = 10;
        int decalageY = 40; // Décalage vertical de 40 pixels

        super.paint(g);
        
        // modifier l'épaisseur des traits
        Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(new BasicStroke(3)); // Épaisseur de 3 pixels pour la grille
        
        // Affichage du texte indiquant le tour du joueur
        g.setFont(new Font("Arial", Font.BOLD, 20));
        if (gagnant == 'a') {
            if (monTour) {
                g.setColor(Color.GREEN);
                g.drawString("C'est votre tour !", 170, 100);
            } else {
                g.setColor(Color.ORANGE);
                g.drawString("En attente de l'adversaire...", 120, 100);
            }
        }
        
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                g2d.drawRect((i+1)*100, (j+1)*100 + decalageY, 100, 100);
                if(morpion[i][j] == 'x') {
                    drawCrois(g, (i+1)*100, (j+1)*100 + decalageY, 
                            (i+2)*100, (j+1)*100 + decalageY, 
                            (i+1)*100, (j+2)*100 + decalageY, 
                            (i+2)*100, (j+2)*100 + decalageY, marge);
                } else if (morpion[i][j] == 'o') {
                    g2d.setStroke(new BasicStroke(5)); // Épaisseur de 5 pixels pour les cercles
                    g2d.drawOval((i+1)*100 + marge, (j+1)*100 + marge + decalageY, 
                               100 - 2*marge, 100 - 2*marge);
                }
            }
        }
        
        // Affichage gagnant
        if (gagnant == 'x' || gagnant == 'o') {
            g.setFont(new Font("Arial", Font.BOLD, 32));
            g.setColor(Color.RED);
            g.drawString("Victoire de " + gagnant + " !", 120, 80);
        } else if (gagnant == 'n') {
            g.setFont(new Font("Arial", Font.BOLD, 32));
            g.setColor(Color.BLUE);
            g.drawString("Match nul !", 150, 80);
        }
    }

    public void affect(int clicX, int clicY) {
        int decalageY = 40; 
        
        
        int i = clicX / 100 - 1;
        int j = (clicY - decalageY) / 100 - 1;
        
        // Vérifie si c'est notre tour et si le jeu n'est pas terminé
        if (monTour && gagnant == 'a' && i >= 0 && i < 3 && j >= 0 && j < 3 && morpion[i][j] == 'a') {
            // Client joue X, Serveur joue O
            morpion[i][j] = gameMode == 1 ? 'x' : 'o';
            
            // Change le tour
            monTour = false;
            
            // Changement de couleur de fond selon le tour
            getContentPane().setBackground(new Color(255, 245, 230)); // Orange tour de l'adversaire
            
            // Vérifie si quelqu'un a gagné ou match nul
            gagnant = gagne(morpion);
            if (gagnant == 'a' && estMatchNul(morpion)) {
                gagnant = 'n'; // n pour nul
            }
            
            // Envoie le coup au serveur/client
            if (sendMoveCallback != null) {
                GameState gameState = new GameState();
                
                // Copie profonde de la grille
                char[][] gridCopy = new char[3][3];
                for (int k = 0; k < 3; k++) {
                    for (int l = 0; l < 3; l++) {
                        gridCopy[k][l] = morpion[k][l];
                    }
                }
                
                gameState.setGrid(gridCopy);
                gameState.setClientTurn(gameMode == 0); // Si c'est le serveur qui joue, le prochain tour est au client
                gameState.setWinner(gagnant);
                
                System.out.println("Envoi du coup: " + i + "," + j);
                sendMoveCallback.accept(gameState.toMessage());
            }
            
            repaint();
        }
    }
    
    public void updateFromGameState(GameState state) {
        if (state == null) return;
        
        // Copie grille
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                morpion[i][j] = state.getGrid()[i][j];
            }
        }
        
        monTour = (gameMode == 1 && state.isClientTurn()) || (gameMode == 0 && !state.isClientTurn());
        gagnant = state.getWinner();
        
        // Changement de couleur de fond selon le tour
        if (monTour) {
            getContentPane().setBackground(new Color(230, 255, 230)); // Vert notre tour
        } else {
            getContentPane().setBackground(new Color(255, 245, 230)); // Orange tour de l'adversaire
        }
        
        System.out.println("Jeu mis à jour: " + (monTour ? "C'est à mon tour" : "C'est au tour de l'adversaire"));
        System.out.println("État de la grille:");
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                System.out.print(morpion[i][j] + " ");
            }
            System.out.println();
        }
        
        repaint();
    }
    
    // Méthode pour définir le callback pour envoyer les coups
    public void setSendMoveCallback(Consumer<String> callback) {
        this.sendMoveCallback = callback;
    }
}

class Souris extends MouseAdapter {
    Morpion d;

    Souris(Morpion a) {
        d = a;
    }

    public void mouseClicked(MouseEvent m) {
        int x = m.getX();
        int y = m.getY();
        d.affect(x, y);
    }
}
