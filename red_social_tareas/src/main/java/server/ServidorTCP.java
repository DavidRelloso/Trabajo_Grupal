package server;

import java.net.ServerSocket;
import java.net.Socket;

public class ServidorTCP {

    public static void main(String[] args) {
    	
        int puerto = 5000;

        try (ServerSocket servidor = new ServerSocket(puerto)) {
            System.out.println("Servidor escuchando en el puerto " + puerto);

            while (true) {
                Socket socket = servidor.accept();
                String clientIP = socket.getInetAddress().getHostAddress(); 
                System.out.println("Cliente conectado desde: " + clientIP);

                Thread hilo = new Thread(new ManejadorCliente(socket));
                hilo.start();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
   
}
