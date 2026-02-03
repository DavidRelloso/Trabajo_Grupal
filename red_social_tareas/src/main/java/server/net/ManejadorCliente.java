package server.net;

import java.io.EOFException;
import java.io.Flushable;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import server.handler.ManejadorAcciones;
import server.handler.friends.ManejadorAceptarSolicitud;
import server.handler.friends.ManejadorAgregarAmigo;
import server.handler.friends.ManejadorRechazarSolicitud;
import server.handler.friends.ManejadorSolicitudesPendientes;
import server.handler.friends.ManejadorObtenerAmigos;
import server.handler.friends.ManejadorEliminarAmigo;
import server.handler.notes.ManejadorCargaDiario;
import server.handler.notes.ManejadorCreacionNota;
import server.handler.session.ManejadorLogin;
import server.handler.session.ManejadorRegistro;
import server.handler.user.ManejadorCambioAvatar;
import server.handler.user.ManejadorCambioContra;
import server.handler.user.ManejadorCambioCorreo;
import server.handler.user.ManejadorCambioNombre;
import server.service.UsuarioService;
import server.service.friends.AmigoService;
import server.service.friends.SolicitudAmistadService;
import server.service.notes.DiaService;
import server.service.notes.NotaService;
import shared.dto.user.UserDTO;
import shared.protocol.Peticion;
import shared.protocol.Respuesta;

public class ManejadorCliente implements Runnable {

    private final Socket socket;

    private final Map<String, ManejadorAcciones<?>> handlers;
    
    private String usuarioLogueado = null;
    
    public ManejadorCliente(Socket socket) {
        this.socket = socket;

        UsuarioService usuarioService = new UsuarioService();

        SolicitudAmistadService solicitudAmistadService = new SolicitudAmistadService();
        AmigoService amigoService = new AmigoService();
        
        NotaService notaService = new NotaService();
        DiaService diaService = new DiaService();

		// Registro de acciones
		Map<String, ManejadorAcciones<?>> handlers = new HashMap<>();

		// Inicio sesión
		handlers.put("LOGIN", new ManejadorLogin(usuarioService));
		handlers.put("REGISTER", new ManejadorRegistro(usuarioService));

		// Cambio datos user
		handlers.put("CAMBIO_NOMBRE", new ManejadorCambioNombre(usuarioService));
		handlers.put("CAMBIO_CORREO", new ManejadorCambioCorreo(usuarioService));
		handlers.put("CAMBIO_CONTRA", new ManejadorCambioContra(usuarioService));
		handlers.put("CAMBIO_AVATAR", new ManejadorCambioAvatar(usuarioService));

		// Amigos
		handlers.put("OBTENER_AMIGOS", new ManejadorObtenerAmigos(usuarioService, amigoService));
		handlers.put("AGREGAR_AMIGO", new ManejadorAgregarAmigo(usuarioService, solicitudAmistadService, amigoService));
		handlers.put("ELIMINAR_AMIGO", new ManejadorEliminarAmigo(usuarioService, amigoService));
		handlers.put("OBTENER_SOLICITUDES_PENDIENTES", new ManejadorSolicitudesPendientes(usuarioService, solicitudAmistadService));
		handlers.put("ACEPTAR_SOLICITUD_AMISTAD", new ManejadorAceptarSolicitud(usuarioService, solicitudAmistadService));
		handlers.put("RECHAZAR_SOLICITUD_AMISTAD", new ManejadorRechazarSolicitud(usuarioService, solicitudAmistadService));
		
		// Diario
		handlers.put("CARGAR_DIARIO", new ManejadorCargaDiario(usuarioService, diaService, notaService));
		handlers.put("CREAR_NOTA", new ManejadorCreacionNota(usuarioService, notaService, diaService));

		this.handlers = Map.copyOf(handlers); 


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

                System.out.println(
                        "SOCKET=" + socket +
                        " | accion=" + req.accion +
                        " | usuarioLogueado=" + usuarioLogueado
                    );
                System.out.println("REQ accion=" + req.accion + " socket=" + socket + " payload=" + req.payload);

                Respuesta resp = procesar(req);
                //Registrar conexion del usuario al logearse
                if ("LOGIN".equals(req.accion) && resp.ok && resp.data instanceof UserDTO u) {
                    usuarioLogueado = u.nombreUsuario;   // o u.getUsername() según tu DTO
                    RegistroClientes.registrar(usuarioLogueado, out);
                }
                System.out.println("Registrado ONLINE key=" + usuarioLogueado);

                synchronized (out) {
                    out.writeObject(resp);
                    out.flush();
                }

            }

        } catch (EOFException e) {
            // cliente cerró conexión
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            RegistroClientes.desregistrar(usuarioLogueado);
            try { socket.close(); } catch (Exception ignored) {}
        }
    }

    private Respuesta procesar(Peticion req) {
        ManejadorAcciones<?> handler = handlers.get(req.accion);
        if (handler == null) return new Respuesta(false, "Acción no soportada: " + req.accion);

        try {
        	if (req.payload != null && !handler.payloadType().isInstance(req.payload)) {
        	    return new Respuesta(false, "Payload incorrecto para acción " + req.accion);
        	}

            return invoke(handler, req.payload, usuarioLogueado);

        } catch (Exception e) {
            e.printStackTrace();
            Throwable root = e;
            while (root.getCause() != null) root = root.getCause();
            return new Respuesta(false, "Error interno: " + root.getMessage(), null);
        }
    }


    @SuppressWarnings("unchecked")
    private static <T> Respuesta invoke(ManejadorAcciones<?> handler, Object payload, String usuarioLogueado) throws Exception {
        ManejadorAcciones<T> h = (ManejadorAcciones<T>) handler;
        return h.handle((T) payload, usuarioLogueado);
    }

}
