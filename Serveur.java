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
            
            // Traitez la communication via br et pr ici
            String message;
            while ((message = br.readLine()) != null) {
                System.out.println("Message reçu du client : " + message);
                pr.println("Message reçu par le server : " + message);
            }

            // Fermez les ressources si nÃ©cessaire
            pr.close();
            br.close();
            socket.close();
            ss.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}