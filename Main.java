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
            // Création du morpion côté serveur (joueur O)
            Morpion morpion = new Morpion(0);
            
            // Création et démarrage du serveur
            Serveur serveur = new Serveur();
            serveur.setMorpion(morpion);
            
            // on le lance dans autre thread (comme dans un autre terminal)
            Thread serveurThread = new Thread(() -> {
                serveur.start();
            });
            serveurThread.start();
        } else if (choix == 2) {
            System.out.print("Entrez l'adresse IP du serveur : ");
            String ip = scanner.nextLine();
            System.out.print("Entrez le port du serveur : ");
            int port = scanner.nextInt();
            scanner.nextLine();
            
            // Création du morpion côté client (joueur X)
            Morpion morpion = new Morpion(1);
            
            // Création et démarrage du client
            Client client = new Client(ip, port);
            client.setMorpion(morpion);
            
            Thread clientThread = new Thread(() -> {
                client.start();
            });
            clientThread.start();
        } else {
            System.out.println("Choix invalide.");
        }
        // IMPORTANT: Ne pas fermer le scanner ici car cela fermerait System.in
        // et provoquerait des erreurs dans les threads client et serveur
        // Le scanner sera fermé automatiquement à la fin du programme
    }
}
