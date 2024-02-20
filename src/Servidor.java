import java.io.*;
import java.net.*;
import java.util.*;

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

                new Thread(() -> {
                    try {
                        DataInputStream dataInputStream = new DataInputStream(sCliente.getInputStream());
                        String mensaje;

                        while (true) {
                            mensaje = dataInputStream.readUTF();
                            for (DataOutputStream client : clientes) {
                                try {
                                    client.writeUTF(mensaje);
                                } catch (IOException e) {
                                    System.out.println(e.getMessage());
                                }
                            }
                        }
                    } catch (IOException e) {
                        System.out.println(e.getMessage());
                    }
                }).start();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
