package servidor;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ServidorTCP {

    public static void main(String[] args) throws Exception {
        ServerSocket server = new ServerSocket(5000);
        System.out.println("Servidor escuchando en puerto 5000...");

        while (true) {
            Socket socket = server.accept();
            new Thread(new HiloCliente(socket)).start();
        }
    }
}

