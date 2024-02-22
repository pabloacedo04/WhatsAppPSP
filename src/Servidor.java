import java.io.*;
import java.net.*;
import java.util.*;

public class Servidor {

    //ArrayList de mensajes y nombres, para enviar los mensajes anteriores a un nuevo cliente y controlar que los nombres no se repiten
    static ArrayList<String> mensajes = new ArrayList<>();
    static ArrayList<String> nombres = new ArrayList<>();
    public static void main(String[] args) {
        //Creo un Arraylist de DataOutPutStreams para guardar el output de cada socket de cada cliente que se conecte con el servidor
        ArrayList<DataOutputStream> clientes = new ArrayList<>();
        try {
            //Se inicia el servidor en el puerto 6000
            ServerSocket serverSocket = new ServerSocket(6000);
            System.out.println("Servidor escuchando en el puerto 6000...");

            //Entra en un bucle infinito, por lo que el servidor hay que pararlo manualmente
            while(true) {
                //Por cada cliente que se conecte con el servidor creo su dataoutputstream y datainputstream, guardando el primero en el arraylist clientes
                Socket sCliente = serverSocket.accept();
                DataOutputStream dataOutputStream = new DataOutputStream(sCliente.getOutputStream());
                DataInputStream dataInputStream = new DataInputStream(sCliente.getInputStream());
                clientes.add(dataOutputStream);

                //Comprobacion de nombre, hasta que el usuario no utilice un nombre no usado anteriormente no se iniciara
                String nombreRecibido = dataInputStream.readUTF();

                while(nombres.contains(nombreRecibido)){
                    dataOutputStream.writeUTF("EXISTE");
                    nombreRecibido = dataInputStream.readUTF();
                }
                dataOutputStream.writeUTF("SIRVE");
                nombres.add(nombreRecibido);

                //Hilo por cada cliente
                new Thread(() -> {
                    try {
                        //Si hay mensajes previos a la conexion del cliente se le envian
                        if(mensajes!=null){
                            try{
                                for (String mensaje : mensajes) {
                                    dataOutputStream.writeUTF(mensaje);
                                }
                            }
                            catch (Exception e){
                                System.out.println(e.getMessage());
                            }
                        }
                        //Comunicacion entre cliente y servidor
                        String mensaje;
                        while (true) {
                            mensaje = dataInputStream.readUTF();
                            mensajes.add(mensaje);
                            //El servidor coge el mensaje recibido por un cliente y se lo pasa a todos
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
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}