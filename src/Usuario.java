import javax.swing.*;
import java.awt.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Usuario extends JFrame {
    private static JTextArea chat;
    private static JTextField areaMensaje;
    private static DataOutputStream dataOutputStream;
    private static DataInputStream dataInputStream;
    private static String nombre;
    static String nombreFinal;
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 6000);
        dataOutputStream = new DataOutputStream(socket.getOutputStream());
        dataInputStream = new DataInputStream(socket.getInputStream());

        nombre = JOptionPane.showInputDialog("Escribe tu nombre");
        dataOutputStream.writeUTF(nombre);
        nombreFinal = dataInputStream.readUTF();
        new Usuario(nombreFinal);

        new Thread(() -> {
            try {
                String recibo;
                while (true) {
                    recibo = dataInputStream.readUTF();
                    chat.append(recibo + "\n");
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }).start();
    }

    public Usuario(String name) {
        setTitle("Grupo ("+ name + ")");
        setSize(300, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new BorderLayout());

        chat = new JTextArea();
        chat.setEditable(false);
        panel.add(new JScrollPane(chat), BorderLayout.CENTER);

        JPanel inputPanel = new JPanel(new BorderLayout());
        areaMensaje = new JTextField();
        inputPanel.add(areaMensaje, BorderLayout.CENTER);

        JButton sendButton = getjButton();
        inputPanel.add(sendButton, BorderLayout.EAST);

        panel.add(inputPanel, BorderLayout.SOUTH);

        getContentPane().add(panel);

        setVisible(true);
    }
    private JButton getjButton() {
        JButton sendButton = new JButton("Enviar");
        sendButton.addActionListener(e -> {
            try {
                String envio = areaMensaje.getText();
                dataOutputStream.writeUTF(nombreFinal +": "+envio);
                areaMensaje.setText("");
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        });
        return sendButton;
    }
}