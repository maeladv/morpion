// ADVISSE Mael
// BEDNAROWICZ Lousion

import java.io.*;
import java.net.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class Serveur {
    public static final int PORT = 1200;
    private Morpion morpion;

    public Serveur() {}
    
    public void setMorpion(Morpion morpion) {
        this.morpion = morpion;
    }

  public void start() {
        try {
            ServerSocket ss = new ServerSocket(PORT);
            System.out.println("Server started on port " + PORT);

            Socket socket = ss.accept();
            System.out.println("Client connected.");
            InputStream inputStream = socket.getInputStream();
            OutputStream outputStream = socket.getOutputStream();

            InputStreamReader isr = new InputStreamReader(inputStream);
            BufferedReader br = new BufferedReader(isr);
            OutputStreamWriter osr = new OutputStreamWriter(outputStream);
            BufferedWriter bw = new BufferedWriter(osr);
            PrintWriter pr = new PrintWriter(bw, true);

            String message;
            AtomicBoolean mon_tour = new AtomicBoolean(false);
            
            // Configurer le callback pour envoyer les coups
            if (morpion != null) {
                morpion.setSendMoveCallback(gameMessage -> {
                    System.out.println("Serveur envoie: " + gameMessage);
                    pr.println(gameMessage);
                    mon_tour.set(false);  // Après avoir joué, ce n'est plus notre tour
                });
            }
            
            System.out.println("Partie de Morpion démarrée. Vous jouez avec O. Le client joue en premier.");

            // écoute permanente des messages du client
            while (true) {
                message = br.readLine();
                if (message == null) {
                    // Le client déconnecté
                    break;
                }
                
                // Vérifier si c'est un message de jeu
                if (GameState.isGameMessage(message)) {
                    GameState gameState = GameState.fromMessage(message);
                    if (gameState != null && morpion != null) {
                        morpion.updateFromGameState(gameState);
                        mon_tour.set(true);  // Après avoir reçu un coup, c'est notre tour
                    }
                }
            }

            pr.close();
            br.close();
            socket.close();
            ss.close();
            System.out.println("Connexion fermée.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}