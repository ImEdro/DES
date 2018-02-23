package DES;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import static java.awt.image.ImageObserver.WIDTH;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class DESClient extends JFrame {

    private JTextField escritor = new JTextField();
    private JTextArea visor = new JTextArea();
    private JButton adjuntar = new JButton("Adjuntar", new ImageIcon("adjuntar.png"));
    private DataOutputStream toServer;
    private DataInputStream fromServer;
    private Socket socket;

    public DESClient() {
        JPanel p = new JPanel();
        p.setLayout(new BorderLayout());
        p.add(new JLabel("Ingrese su mensaje "), BorderLayout.WEST);
        p.add(escritor, BorderLayout.CENTER);
        adjuntar.setToolTipText("Adjuntar Archivo");
        adjuntar.addActionListener(new Listener());
        p.add(adjuntar, BorderLayout.EAST);

        setLayout(new BorderLayout());
        add(new JScrollPane(visor), BorderLayout.CENTER);
        add(p, BorderLayout.SOUTH);

        escritor.addActionListener(new Listener());

        setTitle("DES Client");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        try {

            socket = new Socket("localhost", 8000);
            fromServer = new DataInputStream(socket.getInputStream());
            toServer = new DataOutputStream(socket.getOutputStream());

        } catch (IOException ex) {
            visor.append(ex.toString() + '\n');
        }
    }

    private class Listener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            try {

                String texto = escritor.getText().trim();
                String cifrado = null;
                String clave = null;
                try {
                    cifrado = Cifrado.cifrado(texto);
                    clave = Cifrado.clave();
                } catch (Exception ex) {
                    Logger.getLogger(DESClient.class.getName()).log(Level.SEVERE, null, ex);
                }
                toServer.writeUTF("Texto: " + cifrado + " Clave: " + clave);
                toServer.flush();
                escritor.setText("");
            } catch (IOException ex) {
                System.err.println(ex);
            }
        }
    }

    public static void main(String[] args) {
        new DESClient();
    }
}
