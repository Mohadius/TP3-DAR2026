package Client;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;
import Model.Operation;

public class Client {
    public static void main(String[] args) throws Exception {
        String serverIp = "127.0.0.1";
        System.out.println("Je suis un client pas encore connecté...");
        Socket socket = new Socket(InetAddress.getByName(serverIp), 12345);
        System.out.println("Je suis un client connecté");

        Scanner scanner = new Scanner(System.in);
        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

        String messageServeur = (String) in.readObject();
        System.out.println("Message du serveur :" + messageServeur);

        while (true) {
            System.out.println("Tapez une opération complète (Ex: 55 * 25) ou 'quitter' ");
            String op = scanner.nextLine();
            if (op.equalsIgnoreCase("quitter")) {
                out.writeObject(null);
                out.flush();
                break;
            }
            if (!op.matches("^\\d+\\s+[+\\-*/]\\s+\\d+$")) {
                System.out.println("Syntaxe incorrecte ! Format attendu : [nombre] [espace] [opérateur] [espace] [nombre]");
                continue;
            }
            String[] parties = op.split(" ");
            int op1 = Integer.parseInt(parties[0]);
            String operateur = parties[1];
            int op2 = Integer.parseInt(parties[2]);
            Operation operationObj = new Operation(op1, operateur, op2);

            out.writeObject(operationObj);
            out.flush();

            Object response = in.readObject();
            System.out.println("Résultat du serveur = " + response);
        }
        socket.close();
        scanner.close();
        System.out.println("Connexion fermée");
    }
}