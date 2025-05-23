//ADVISSE Mael
// BEDNAROWICZ Lousion

import java.io.*;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

public class Client {

    private String address;
    private int port;

    public Client(String address, int port) {
        this.address = address;
        this.port = port;
    }
    public Client() {
        this("localhost", 1200);
    }
 

    // Fonction pour vider le buffer d'entrée
    private static void clearInputBuffer(BufferedReader reader) {
        try {
            while (reader.ready()) {
                reader.read();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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

            BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
            String userInput;
            String serverMsg;
            System.out.println("Tapez un message (ou 'exit' pour quitter) :");
            
            // Thread qui vide constamment le buffer quand ce n'est pas mon tour
            Thread bufferCleaner = new Thread(() -> {
                while (!Thread.currentThread().isInterrupted()) {
                    if (!mon_tour.get()) {
                        clearInputBuffer(consoleReader);
                    }
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        break;
                    }
                }
            });
            bufferCleaner.setDaemon(true);
            bufferCleaner.start();
            
            while (true) {
                if (mon_tour.get()) {
                    System.out.print("[Client]  >> ");
                    userInput = consoleReader.readLine();
                    if (userInput == null || userInput.equalsIgnoreCase("exit"))
                        break;
                    pr.println(userInput); // envoi
                    mon_tour.set(false);
                } else {
                    // Attendre la réponse du serveur
                    serverMsg = br.readLine();
                    if (serverMsg == null) {
                        System.out.println("Connexion au serveur perdue.");
                        break;
                    }
                    System.out.println("[Serveur] " + serverMsg);
                    mon_tour.set(true);
                }
            }
            bufferCleaner.interrupt();
            s.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}