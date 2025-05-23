//ADVISSE Mael
// BEDNAROWICZ Lousion

import java.io.*;
import java.net.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class Serveur {
    public static final int PORT = 1200;

    public Serveur() {}

  
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

            BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
            String message, userInput;
            AtomicBoolean mon_tour = new AtomicBoolean(false);
            
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
                if (!mon_tour.get()) {
                    message = br.readLine();
                    if (message == null) {
                        // Le client s'est déconnecté
                        break;
                    }
                    System.out.println("[Client] " + message);
                    mon_tour.set(true);
                } else {
                    System.out.print("[Serveur] >> ");
                    userInput = consoleReader.readLine();
                    if (userInput == null || userInput.equalsIgnoreCase("exit")) {
                        break;
                    }
                    pr.println(userInput); // envoi
                    mon_tour.set(false);
                }
            }

            // Fermez les ressources
            bufferCleaner.interrupt();
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