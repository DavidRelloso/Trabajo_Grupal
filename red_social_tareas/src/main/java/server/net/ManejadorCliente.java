package server.net;

import java.io.EOFException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Map;

import entity.notes.Dia;
import server.handler.ManejadorAcciones;
import server.handler.notes.ManejadorCreacionNota;
import server.handler.session.ManejadorLogin;
import server.handler.session.ManejadorRegistro;
import server.handler.user.ManejadorCambioAvatar;
import server.handler.user.ManejadorCambioContra;
import server.handler.user.ManejadorCambioCorreo;
import server.handler.user.ManejadorCambioNombre;
import server.service.UsuarioService;
import server.service.notes.DiaService;
import server.service.notes.NotaService;
import shared.protocol.Peticion;
import shared.protocol.Respuesta;

public class ManejadorCliente implements Runnable {

    private final Socket socket;

    private final Map<String, ManejadorAcciones<?>> handlers;

    public ManejadorCliente(Socket socket) {
        this.socket = socket;

        UsuarioService usuarioService = new UsuarioService();
        NotaService notaService = new NotaService();
        DiaService diaService = new DiaService();

        // Registro de acciones
        this.handlers = Map.of(
            "LOGIN", new ManejadorLogin(usuarioService),
            "REGISTER", new ManejadorRegistro(usuarioService),
            "CAMBIO_NOMBRE", new ManejadorCambioNombre(usuarioService),
            "CAMBIO_CORREO", new ManejadorCambioCorreo(usuarioService),
            "CAMBIO_CONTRA", new ManejadorCambioContra(usuarioService),
            "CAMBIO_AVATAR", new ManejadorCambioAvatar(usuarioService),
            "CREAR_NOTA", new ManejadorCreacionNota(usuarioService, notaService, diaService)
            // "REGISTER", new RegistroHandler(usuarioService),
            // "CAMBIO_NOMBRE", new CambioNombreHandler(usuarioService),
            // ...
        );
    }

    @Override
    public void run() {
        try (ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            while (true) {
                Object obj = in.readObject();

                if (!(obj instanceof Peticion req)) {
                    out.writeObject(new Respuesta(false, "Request inválida"));
                    out.flush();
                    continue;
                }

                Respuesta resp = procesar(req);
                out.writeObject(resp);
                out.flush();
            }

        } catch (EOFException e) {
            // cliente cerró conexión
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { socket.close(); } catch (Exception ignored) {}
        }
    }

    private Respuesta procesar(Peticion req) {
    	ManejadorAcciones<?> handler = handlers.get(req.accion);
        if (handler == null) return new Respuesta(false, "Acción no soportada: " + req.accion);

        try {
            if (req.payload == null || !handler.payloadType().isInstance(req.payload)) {
                return new Respuesta(false, "Payload incorrecto para acción " + req.accion);
            }

            return invoke(handler, req.payload);

        } catch (Exception e) {
            return new Respuesta(false, "Error interno: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private static <T> Respuesta invoke(ManejadorAcciones<?> handler, Object payload) throws Exception {
    	ManejadorAcciones<T> h = (ManejadorAcciones<T>) handler;
        return h.handle((T) payload);
    }
}
