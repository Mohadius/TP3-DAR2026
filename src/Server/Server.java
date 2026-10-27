package Server;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import Model.Operation;
public class Server {
    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket = new ServerSocket();
        InetAddress localAddr = InetAddress.getByName("0.0.0.0");
        serverSocket.bind(new InetSocketAddress(localAddr, 12345));
        System.out.println("Je suis un serveur en attente la connexion d'un client");
        int clients = 0;
        while (true) {
            Socket clientSocket = serverSocket.accept();
            clients += 1;
            System.out.println("Nouveau client conecté: " + clientSocket.getRemoteSocketAddress() + "(client numero " + clients + ")");
            ClientProcess clientProcess = new ClientProcess(clientSocket, clients);
            new Thread(clientProcess).start();
        }
    }

    static class ClientProcess implements Runnable {
        private Socket socket;
        private int clientId;

        public ClientProcess(Socket socket, int clientId) {
            this.socket = socket;
            this.clientId = clientId;
        }

        @Override
        public void run() {
            try {
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                out.writeObject("Vous etes le client numero " + clientId);
                out.flush();
                while (true) {
                    Operation opObjet = (Operation) in.readObject();
                    if (opObjet == null) {
                        System.out.println("Le client a demandé à se déconnecter");
                        break;
                    }
                    int op1 = opObjet.getOp1();
                    String operateur = opObjet.getOp();
                    int op2 = opObjet.getOp2();
                    System.out.println("Opération récue du client numero  : " + clientId + " - Opération " + op1 + " " + operateur + " " + op2);
                    int res = 0;
                    if (operateur.equals("+")) res = op1 + op2;
                    else if (operateur.equals("-")) res = op1 - op2;
                    else if (operateur.equals("*")) res = op1 * op2;
                    else if (operateur.equals("/")) {
                        if (op2 == 0) {
                            out.writeObject("Erreur: Division par zero");
                            out.flush();
                            continue;
                        }
                        res = op1 / op2;
                    }
                    out.writeObject(res);
                    out.flush();
                }
                socket.close();
            } catch (Exception e) {
                System.out.println("Erreur avec le client numero " + clientId + " : " + e.getMessage());
            }
        }
    }
}
