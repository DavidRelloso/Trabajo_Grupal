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

    public void conectar(String host, int port) throws Exception {
        socket = new Socket(host, port);
        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());
    }

    public boolean isConectado() {
        return socket != null && socket.isConnected() && !socket.isClosed();
    }
    
    public synchronized Respuesta enviar(Peticion p) throws Exception {
        out.writeObject(p);
        out.flush();
        return (Respuesta) in.readObject();
    }

    public void cerrar() {
        try { socket.close(); } catch (Exception ignored) {}
    }
}
