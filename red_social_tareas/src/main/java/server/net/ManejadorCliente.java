package server.net;

import java.io.EOFException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import server.handler.RespuestasCliente;
import shared.dto.auth.LoginDTO;
import shared.dto.auth.RegistroDTO;
import shared.dto.user.CambioAvatarDTO;
import shared.dto.user.CambioContraDTO;
import shared.dto.user.CambioCorreoDTO;
import shared.dto.user.CambioNombreDTO;
import shared.protocol.Peticion;
import shared.protocol.Respuesta;

public class ManejadorCliente extends RespuestasCliente implements Runnable {

    private final Socket socket;

    public ManejadorCliente(Socket socket) {
        this.socket = socket;
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

    // ====== MANEJAR PETICIÓN ======
    private Respuesta procesar(Peticion req) {
        try {
            return switch (req.action) {
                case "LOGIN" -> login((LoginDTO) req.payload);
                case "REGISTER" -> registro((RegistroDTO) req.payload);
                case "CAMBIO_NOMBRE" -> cambioNombre((CambioNombreDTO) req.payload);
                case "CAMBIO_CORREO" -> cambioCorreo((CambioCorreoDTO) req.payload);
                case "CAMBIO_CONTRA" -> cambioContra((CambioContraDTO) req.payload);
                case "CAMBIO_AVATAR" -> cambioAvatar((CambioAvatarDTO) req.payload); // ✅ AÑADIDO
                default -> new Respuesta(false, "Acción no soportada: " + req.action);
            };

        } catch (ClassCastException e) {
            return new Respuesta(false, "Payload incorrecto para acción " + req.action);
        } catch (Exception e) {
            return new Respuesta(false, "Error interno: " + e.getMessage());
        }
    }
}
