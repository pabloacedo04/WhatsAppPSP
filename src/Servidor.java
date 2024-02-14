import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(6000);
            System.out.println("Servidor escuchando en el puerto 6000...");

            while (true) {
                Socket sCliente = serverSocket.accept();

            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
