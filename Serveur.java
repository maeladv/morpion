import java.io.*;
import java.net.*;

public class Serveur {
    public static final int PORT = 1200;

    public static void main(String[] args) {
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

            while (true) {
                message = br.readLine();
                if (message == null) {
                    // Le client s'est déconnecté
                    break;
                }
                System.out.println("[Client] " + message);
                System.out.print("[Serveur] >>");
                userInput = consoleReader.readLine();
                if (userInput == null || userInput.equalsIgnoreCase("exit")) {
                    break;
                }
                pr.println(userInput); // envoi
            }

            // Fermez les ressources
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