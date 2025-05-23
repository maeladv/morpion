import java.util.Scanner;
import java.net.*; // Ajout de l'import pour les sockets
import java.io.*; // Ajout de l'import pour les flux d'entrée/sortie

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Choisissez le mode :");
        System.out.println("1. Serveur");
        System.out.println("2. Client");
        System.out.print(">> ");
        int choix = scanner.nextInt();
        scanner.nextLine(); // consomme le retour à la ligne

        if (choix == 1) {
            Serveur serveur = new Serveur();
            serveur.start();
        } else if (choix == 2) {
            System.out.print("Entrez l'adresse IP du serveur : ");
            String ip = scanner.nextLine();
            System.out.print("Entrez le port du serveur : ");
            int port = scanner.nextInt();
            scanner.nextLine();
            Client client = new Client(ip, port);
            client.start();
        } else {
            System.out.println("Choix invalide.");
        }
        scanner.close();
    }
}
