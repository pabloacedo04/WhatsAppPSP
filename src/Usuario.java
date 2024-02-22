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
    public static void main(String[] args) throws IOException {
        //Conexion con el servidor
        Socket socket = new Socket("localhost", 6000);
        dataOutputStream = new DataOutputStream(socket.getOutputStream());
        dataInputStream = new DataInputStream(socket.getInputStream());

        //Comprobacion del nombre gracias al servidor
        nombre = JOptionPane.showInputDialog("Escribe tu nombre");
        dataOutputStream.writeUTF(nombre);

        String recibido = dataInputStream.readUTF();

        while(!recibido.equalsIgnoreCase("SIRVE")){
            JOptionPane.showMessageDialog(null, "Ese nombre ya existe");
            nombre = JOptionPane.showInputDialog("Escribe tu nombre");
            dataOutputStream.writeUTF(nombre);
            recibido = dataInputStream.readUTF();
        }

        //Creacion del usuario, ya con el nombre no repetido
        new Usuario(nombre);

        //Recibe mensajes del servidor hasta que se cierre el cliente y cada mensaje que recibe lo añade al area de chat
        try {
            String recibo;
            while (true) {
                recibo = dataInputStream.readUTF();
                chat.append(recibo + "\n");
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    //Constructor del usuario (Interfaz gráfica)
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
    //Accion a realizar al pulsar el boton de enviar (Se envia el mensaje al servidor y se limpia la caja de texto donde se escriben los mensajes)
    private JButton getjButton() {
        JButton sendButton = new JButton("Enviar");
        sendButton.addActionListener(e -> {
            try {
                String envio = areaMensaje.getText();
                dataOutputStream.writeUTF(nombre +": "+envio);
                areaMensaje.setText("");
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        });
        return sendButton;
    }
}