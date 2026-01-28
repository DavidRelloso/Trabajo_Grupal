package servidor;

import java.net.ServerSocket;
import java.net.Socket;

public class ServidorTCP {

    public static void main(String[] args) {
    	
        int puerto = 5000;

        try (ServerSocket servidor = new ServerSocket(puerto)) {
            System.out.println("Servidor escuchando en el puerto " + puerto);

            while (true) {
                Socket socket = servidor.accept();
                //System.out.println("Cliente conectado");

                //coger la IP del cliente que ha hecho la peticion
                String clientIP = socket.getInetAddress().getHostAddress(); 
               // System.out.println("Cliente conectado desde: " + clientIP);
                
                // Crear un nuevo hilo para manejar las peticiones de los clientes
                Thread hilo = new Thread(new ManejadorCliente(socket));
                hilo.start();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
   
}
