import javax.swing.*;
import java.awt.*;

public class Usuario extends JFrame{
    private static JTextArea chat;
    private static JTextField areaMensaje;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Usuario::new);
    }

    public Usuario() {
        setTitle("Grupo");
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

        });
        return sendButton;
    }
}