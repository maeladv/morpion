// ADVISSE Mael
// BEDNAROWICZ Lousion

import java.io.Serializable;

public class GameState implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private char[][] grid = new char[3][3];
    private boolean clientTurn; // true si c'est au client de jouer
    private char winner = 'a'; // 'a' pour aucun, 'x', 'o', ou 'n' pour match nul
    
    public GameState() {
        // Initialiser la grille vide
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                grid[i][j] = 'a';
            }
        }
        clientTurn = true; // Le client commence
    }
    
    public char[][] getGrid() {
        return grid;
    }
    
    public void setGrid(char[][] grid) {
        this.grid = grid;
    }
    
    public boolean isClientTurn() {
        return clientTurn;
    }
    
    public void setClientTurn(boolean clientTurn) {
        this.clientTurn = clientTurn;
    }
    
    public char getWinner() {
        return winner;
    }
    
    public void setWinner(char winner) {
        this.winner = winner;
    }
    
    // Convertit l'état du jeu en format string pour l'envoi via sockets
    public String toMessage() {
        StringBuilder sb = new StringBuilder("GAME:");
        // Ajout de la grille
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                sb.append(grid[i][j]);
            }
        }
        // Ajout du tour et du gagnant
        sb.append(":" + (clientTurn ? "C" : "S") + ":" + winner);
        return sb.toString();
    }
    
    // Parse un message pour construire un GameState
    public static GameState fromMessage(String message) {
        if (!message.startsWith("GAME:")) {
            return null;
        }
        
        String[] parts = message.split(":");
        if (parts.length < 3) {
            return null;
        }
        
        GameState state = new GameState();
        
        // Récupérer la grille
        String gridStr = parts[1];
        if (gridStr.length() == 9) {
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    state.grid[i][j] = gridStr.charAt(i*3 + j);
                }
            }
        }
        
        // Récupérer le tour
        if (parts[2].equals("C")) {
            state.clientTurn = true;
        } else if (parts[2].equals("S")) {
            state.clientTurn = false;
        }
        
        // Récupérer le gagnant
        if (parts.length > 3 && parts[3].length() > 0) {
            state.winner = parts[3].charAt(0);
        }
        
        return state;
    }
    
    // Vérifie si un message est un message de jeu
    public static boolean isGameMessage(String message) {
        return message != null && message.startsWith("GAME:");
    }
}
