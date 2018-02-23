package DES;

import java.awt.BorderLayout;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class DESServer extends JFrame {

    private JTextArea visor = new JTextArea();
    private ArrayList<Socket> clientes = new ArrayList<Socket>();
    DataInputStream fromClient;
    DataOutputStream toClient;

    public static void main(String[] args) throws IOException {
        new DESServer();
    }

    public DESServer() throws IOException {
        
        setLayout(new BorderLayout());
        add(new JScrollPane(visor), BorderLayout.CENTER);
        setTitle("DES Server");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        ServerSocket serverSocket = new ServerSocket(8000);
        visor.append("Server started at " + new Date() + '\n');

        int clientNo = 1;

        while (true) {

            Socket socket = serverSocket.accept();
            clientes.add(socket);

            visor.append("Starting thread for client " + clientNo
                    + " at " + new Date() + '\n');

            InetAddress inetAddress = socket.getInetAddress();
            visor.append("Client " + clientNo + "'s host name is "
                    + inetAddress.getHostName() + "\n");
            visor.append("Client " + clientNo + "'s IP Address is "
                    + inetAddress.getHostAddress() + "\n");

            HandleAClient task = new HandleAClient(socket, clientes);

            new Thread(task).start();

            clientNo++;

        }
    }

    class HandleAClient implements Runnable {

        private Socket socket;
        private ArrayList<Socket> clientes = new ArrayList<Socket>();

        public HandleAClient(Socket socket, ArrayList<Socket> sockets) {
            this.socket = socket;
            this.clientes = sockets;
        }

        public void run() {

            DataInputStream inputFromClient = null;
            try {
                inputFromClient = new DataInputStream(
                        socket.getInputStream());
                DataOutputStream outputToClient = new DataOutputStream(
                        socket.getOutputStream());
                while (true) {

                    String texto = inputFromClient.readUTF();
                    
                    visor.append(socket.getInetAddress() + " escribió: " + texto + "\n");

                        for (Socket cliente : clientes) {
                            DataInputStream entrada = new DataInputStream(cliente.getInputStream());
                            DataOutputStream salida = new DataOutputStream(cliente.getOutputStream());

                            salida.writeUTF(texto);

                        }
                }
            } catch (IOException ex) {
                Logger.getLogger(DESServer.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    inputFromClient.close();
                } catch (IOException ex) {
                    Logger.getLogger(DESServer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
}




