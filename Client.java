import java.io.*;
import java.net.Socket;

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

            // Thread pour lire les messages du serveur
            Thread serverListener = new Thread(() -> {
                String serverMsg;
                try {
                    while ((serverMsg = br.readLine()) != null) {
                        System.out.println("[Serveur] " + serverMsg);
                        System.out.print("[Client] >> ");
                    }
                } catch (IOException e) {
                    System.out.println("Connexion au serveur perdue.");
                }
            });
            serverListener.start();

            BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
            String userInput;
            System.out.println("Tapez un message (ou 'exit' pour quitter) :");
            System.out.print("[Client]  >> ");
            while ((userInput = consoleReader.readLine()) != null) {
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