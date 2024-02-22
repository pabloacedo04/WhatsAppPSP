import java.io.*;
import java.net.*;
import java.util.*;

public class Servidor {
    static ArrayList<String> mensajes = new ArrayList<>();
    static ArrayList<String> nombres = new ArrayList<>();
    public static void main(String[] args) {
        ArrayList<DataOutputStream> clientes = new ArrayList<>();
        try {
            ServerSocket serverSocket = new ServerSocket(6000);
            System.out.println("Servidor escuchando en el puerto 6000...");

            while(true) {
                Socket sCliente = serverSocket.accept();
                DataOutputStream dataOutputStream = new DataOutputStream(sCliente.getOutputStream());
                DataInputStream dataInputStream = new DataInputStream(sCliente.getInputStream());
                clientes.add(dataOutputStream);

                String nombreRecibido = dataInputStream.readUTF();

                while(nombres.contains(nombreRecibido)){
                    dataOutputStream.writeUTF("EXISTE");
                    nombreRecibido = dataInputStream.readUTF();
                }
                dataOutputStream.writeUTF("SIRVE");
                nombres.add(nombreRecibido);

                new Thread(() -> {
                    try {
                        if(mensajes!=null){
                            try{
                                escribirHistorial(mensajes, sCliente);
                            }
                            catch (Exception e){
                                System.out.println(e.getMessage());
                            }
                        }
                        String mensaje;
                        while (true) {
                            mensaje = dataInputStream.readUTF();
                            mensajes.add(mensaje);
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

    private static void escribirHistorial(ArrayList <String> mensajes, Socket socket) throws IOException {
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());
        for (String mensaje : mensajes) {
            out.writeUTF(mensaje);
        }
    }
}