package client.net;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import shared.dto.friends.SolicitudAmistadDTO;
import shared.protocol.Peticion;
import shared.protocol.Respuesta;

public class ClienteTCP {

    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    private Thread readerThread;


    private final ConcurrentLinkedQueue<SolicitudAmistadDTO> pendientes = new ConcurrentLinkedQueue<>();
    private final BlockingQueue<Respuesta> respuestas = new LinkedBlockingQueue<>();
    private final List<Consumer<SolicitudAmistadDTO>> solicitudListeners = new CopyOnWriteArrayList<>();

    //METODO PARA QUE EL CLIENTE CONECTE CON EL SERVIDOR
    //synchronized para que no haya problemas al intentar conectarse varios usuarios al mismo timempo
    public synchronized void conectar(String host, int port) throws Exception {
        cerrar();

        System.out.println("Conectando a " + host + ":" + port);
        socket = new Socket(host, port);
        out = new ObjectOutputStream(socket.getOutputStream());
        in  = new ObjectInputStream(socket.getInputStream());
        System.out.println("Conectado. local=" + socket.getLocalPort() +
                " remote=" + socket.getInetAddress() + ":" + socket.getPort());

        iniciarLector();
    }

    public synchronized boolean isConectado() {
        return socket != null && socket.isConnected() && !socket.isClosed();
    }

    public void addSolicitudListener(Consumer<SolicitudAmistadDTO> listener) {
        System.out.println(" addSolicitudListener()");
        solicitudListeners.add(listener);

        SolicitudAmistadDTO dto;
        while ((dto = pendientes.poll()) != null) {
            System.out.println("↩️ Reproduciendo pendiente: " + dto.getFromUsername());
            listener.accept(dto);
        }
    }


    public void removeSolicitudListener(Consumer<SolicitudAmistadDTO> listener) {
        solicitudListeners.remove(listener);
    }

    private void iniciarLector() {
        readerThread = new Thread(() -> {
            try {
                while (isConectado()) {
                    Object obj = in.readObject();

                    if (obj instanceof Respuesta r) {
                        System.out.println(
                                " Recibida Respuesta: message=[" + r.message + "], data=" +
                                (r.data == null ? "null" : r.data.getClass().getName())
                            );
                    	
                        if ("NUEVA_SOLICITUD_AMISTAD".equals(r.message)
                                && r.data instanceof SolicitudAmistadDTO dto) {

                            System.out.println(" PUSH recibido. listeners=" + solicitudListeners.size());

                            if (solicitudListeners.isEmpty()) {
                                pendientes.offer(dto);
                                System.out.println(" Guardado en pendientes: " + dto.getFromUsername());
                            } else {
                                for (var l : solicitudListeners) l.accept(dto);
                            }
                            continue;
                        }

                        respuestas.offer(r);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, "tcp-reader");

        readerThread.setDaemon(true);
        readerThread.start();
    }

    
    public byte[] solicitarInformeUsuarios() throws Exception {
        Respuesta r = enviar(new Peticion("GENERAR_INFORME_USUARIOS", null));

        if (!r.ok) throw new RuntimeException(r.message);

        if (!(r.data instanceof byte[] pdf)) {
            throw new RuntimeException("Respuesta inválida: se esperaba byte[] pero llegó " +
                    (r.data == null ? "null" : r.data.getClass().getName()));
        }
        return pdf;
    }

    public synchronized Respuesta enviar(Peticion p) throws Exception {
        if (!isConectado()) throw new IllegalStateException("No conectado al servidor");

        synchronized (out) {
            out.writeObject(p);
            out.flush();
        }

        Respuesta r = respuestas.poll(15, TimeUnit.SECONDS);
        if (r == null) return new Respuesta(false, "Timeout esperando respuesta del servidor");
        return r;
    }


    public synchronized void cerrar() {
        try { if (in != null) in.close(); } catch (Exception ignored) {}
        try { if (out != null) out.close(); } catch (Exception ignored) {}
        try { if (socket != null) socket.close(); } catch (Exception ignored) {}

        socket = null;
        out = null;
        in  = null;

        respuestas.clear();
    }

}
