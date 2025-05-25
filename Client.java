//ADVISSE Mael
// BEDNAROWICZ Lousion

import java.io.*;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

public class Client {

    private String address;
    private int port;
    private AtomicBoolean mon_tour = new AtomicBoolean(true);
    private Morpion morpion;

    public Client(String address, int port) {
        this.address = address;
        this.port = port;
    }
    public Client() {
        this("localhost", 1200);
    }
    
    public void setMorpion(Morpion morpion) {
        this.morpion = morpion;
    }
    

  public void start() {
        try {
            Socket s = new Socket(address, port);

            InputStream is = s.getInputStream();
            OutputStream os = s.getOutputStream();

            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            OutputStreamWriter osr = new OutputStreamWriter(os);
            BufferedWriter bw = new BufferedWriter(osr);
            PrintWriter pr = new PrintWriter(bw, true);

            String serverMsg;
            System.out.println("Connexion établie avec le serveur. Vous jouez avec X.");
            
                        
            // Envoyer l'état initial du jeu au serveur (pour débloquer le serveur)
            if (morpion != null) {
                GameState etatInitial = new GameState();
                pr.println(etatInitial.toMessage());
                System.out.println("Client envoie l'état initial: " + etatInitial.toMessage());
            }

            // Configurer le callback pour envoyer les coups
            if (morpion != null) {
                morpion.setSendMoveCallback(message -> {
                    System.out.println("Client envoie: " + message);
                    pr.println(message);
                    mon_tour.set(false);  // Après avoir joué, ce n'est plus notre tour
                });
            }
            
            // Mode écoute permanente des messages du serveur
            while (true) {
                // Attendre la réponse du serveur
                serverMsg = br.readLine();
                if (serverMsg == null) {
                    System.out.println("Connexion au serveur perdue.");
                    break;
                }
                
                System.out.println("Client a reçu: " + serverMsg);
                // Vérifier si c'est un message de jeu
                if (GameState.isGameMessage(serverMsg)) {
                    GameState gameState = GameState.fromMessage(serverMsg);
                    if (gameState != null && morpion != null) {
                        morpion.updateFromGameState(gameState);
                        mon_tour.set(true);  // Après avoir reçu un coup, c'est notre tour
                    }
                }
            }
            s.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}