import java.io.*;
import java.net.Socket;

public class Client {
    public static void main(String[] args) {
        int port = 1200;
        String address = "172.20.10.2";
        try {
            Socket s = new Socket(address, port);

            InputStream is = s.getInputStream();
            OutputStream os = s.getOutputStream();

            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            OutputStreamWriter osr = new OutputStreamWriter(os);
            BufferedWriter bw = new BufferedWriter(osr);
            PrintWriter pr = new PrintWriter(bw, true);
            // Morpion a = new Morpion();

            // Thread pour lire les messages du serveur
            Thread serverListener = new Thread(() -> {
                String serverMsg;
                try {
                    while ((serverMsg = br.readLine()) != null) {
                        System.out.println("[Serveur] " + serverMsg);
                    }
                } catch (IOException e) {
                    System.out.println("Connexion au serveur perdue.");
                }
            });
            serverListener.start();

            BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
            String userInput;
            System.out.println("Tapez un message (ou 'exit' pour quitter) :");
            System.out.print("[Client] ");
            while ((userInput = consoleReader.readLine()) != null) {
                System.out.print("[Client] ");
                if (userInput.equalsIgnoreCase("exit"))
                    break;
                pr.println(userInput); // envoi
            }
            s.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}