import java.awt.*;
import javax.swing.JFrame;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Morpion extends JFrame {
    int x = 250, y = 300;
    boolean croix = false;
    char[][] morpion = new char[3][3];
    char gagnant = 'a'; // Ajouté pour stocker le gagnant
    

    public Morpion() {
        addMouseListener(new Souris(this));
        setSize(500, 600);
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(
            d.width / 2 - this.getWidth() / 2,
            d.height / 2 - this.getHeight() / 2
        );
        setVisible(true);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                morpion[i][j] = 'a';
            }
        }
    }

    public void drawCrois(Graphics g, int x_hg, int y_hg, int x_hd, int y_hd, int x_bg, int y_bg, int x_bd, int y_bd, int marge){
        g.drawLine(x_hg+marge, y_hg+marge, x_bd-marge, y_bd-marge);
        g.drawLine(x_hd-marge, y_hd+marge, x_bg+marge, y_bg-marge);
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

        super.paint(g);
        // Dessin grille
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                g.drawRect((i+1)*100, (j+1)*100, 100, 100);
                if(morpion[i][j] == 'x') {
                    drawCrois(g, (i+1)*100, (j+1)*100, (i+2)*100, (j+1)*100, (i+1)*100, (j+2)*100, (i+2)*100, (j+2)*100, marge);
                } else if (morpion[i][j] == 'o') {
                    g.drawOval((i+1)*100 + marge, (j+1)*100 + marge, 100 - 2*marge, 100 - 2*marge);
                }
            }
        }
        // Affichage du gagnant
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
        int i = clicX / 100 - 1;
        int j = clicY / 100 - 1;
        if (gagnant == 'a' && i >= 0 && i < 3 && j >= 0 && j < 3 && morpion[i][j] == 'a') {
            morpion[i][j] = croix ? 'o' : 'x';
            croix = !croix;
            gagnant = gagne(morpion);
            if (gagnant == 'a' && estMatchNul(morpion)) {
                gagnant = 'n'; // n pour nul
            }
        }
        repaint();
    }

    // public static void main(String[] args) {
    //     Morpion a = new Morpion();
    // }
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
