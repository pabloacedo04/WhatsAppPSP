import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Servidor {
    public static void main(String[] args) {
        ArrayList<DataOutputStream> clientes = new ArrayList<>();
        try {
            ServerSocket serverSocket = new ServerSocket(6000);
            System.out.println("Servidor escuchando en el puerto 6000...");

            while (true) {
                Socket sCliente = serverSocket.accept();
                DataOutputStream dataOutputStream = new DataOutputStream(sCliente.getOutputStream());
                clientes.add(dataOutputStream);


            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
