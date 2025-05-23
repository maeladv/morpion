import java.io.*;
import java.net.Socket;

public class Client {
    public static void main(String[] args) {
        int port = 1200;
        String address = "localhost";
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

            BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
            String userInput;
            System.out.println("Tapez un message (ou 'exit' pour quitter) :");
            while ((userInput = consoleReader.readLine()) != null) {
                if (userInput.equalsIgnoreCase("exit"))
                    break;
                pr.println(userInput); // envoi
                String chaine = br.readLine(); // lecture
                System.out.println(chaine);
            }
            s.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}