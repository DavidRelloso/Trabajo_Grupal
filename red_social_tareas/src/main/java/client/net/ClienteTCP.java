package client.net;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import shared.protocol.Peticion;
import shared.protocol.Respuesta;

public class ClienteTCP {

    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    public synchronized void conectar(String host, int port) throws Exception {
        cerrar();
        System.out.println("Conectando a " + host + ":" + port);
        socket = new Socket(host, port);
        out = new ObjectOutputStream(socket.getOutputStream());
        in  = new ObjectInputStream(socket.getInputStream());
        System.out.println("Conectado. local=" + socket.getLocalPort() +
                " remote=" + socket.getInetAddress() + ":" + socket.getPort());

    }

    public synchronized boolean isConectado() {
        return socket != null && socket.isConnected() && !socket.isClosed();
    }

    public synchronized Respuesta enviar(Peticion p) throws Exception {
        if (!isConectado()) throw new IllegalStateException("No conectado al servidor");
        out.writeObject(p);
        out.flush();
        return (Respuesta) in.readObject();
    }

    public synchronized void cerrar() {
        try { if (in != null) in.close(); } catch (Exception ignored) {}
        try { if (out != null) out.close(); } catch (Exception ignored) {}
        try { if (socket != null) socket.close(); } catch (Exception ignored) {}
        in = null; out = null; socket = null;
    }

}
