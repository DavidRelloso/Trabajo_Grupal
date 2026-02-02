package server.net;

import java.net.ServerSocket;
import java.net.Socket;

import util.HibernateUtil;

public class ServidorTCP {

    public static void main(String[] args) {

    	// Inicializar Hibernate
        try {
            HibernateUtil.getSessionFactory();
            System.out.println("Hibernate inicializado correctamente");
        } catch (Exception e) {
            System.err.println("Error inicializando Hibernate");
            e.printStackTrace();
            return; 
        }

        // Puerto del servidor
        int puerto = 5000;

        try (ServerSocket servidor = new ServerSocket(puerto)) {
            System.out.println("Servidor escuchando en el puerto " + puerto);

            while (true) {
                Socket socket = servidor.accept();
                new Thread(new ManejadorCliente(socket)).start();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

