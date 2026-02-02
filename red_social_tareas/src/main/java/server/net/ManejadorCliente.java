package server.net;

import java.io.EOFException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Map;

import entity.notes.Dia;
import server.handler.ManejadorAcciones;
import server.handler.friends.ManejadorAgregarAmigo;
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
import shared.dto.auth.LoginDTO;
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
        this.handlers = Map.of(
        	//Inicio sesion
            "LOGIN", new ManejadorLogin(usuarioService),
            "REGISTER", new ManejadorRegistro(usuarioService),
            
            //Cambio datos user
            "CAMBIO_NOMBRE", new ManejadorCambioNombre(usuarioService),
            "CAMBIO_CORREO", new ManejadorCambioCorreo(usuarioService),
            "CAMBIO_CONTRA", new ManejadorCambioContra(usuarioService),
            "CAMBIO_AVATAR", new ManejadorCambioAvatar(usuarioService),
            
            //Amigos
            "AGREGAR_AMIGO", new ManejadorAgregarAmigo(usuarioService, solicitudAmistadService, amigoService),
            
            //Diario
            "CARGAR_DIARIO", new ManejadorCargaDiario(usuarioService, diaService, notaService),
            "CREAR_NOTA", new ManejadorCreacionNota(usuarioService, notaService, diaService)
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

                System.out.println(
                        "SOCKET=" + socket +
                        " | accion=" + req.accion +
                        " | usuarioLogueado=" + usuarioLogueado
                    );
                
                Respuesta resp = procesar(req);
                //Registrar conexion del usuario al logearse
                if ("LOGIN".equals(req.accion) && resp.ok && req.payload instanceof LoginDTO login) {
                    usuarioLogueado = login.nombreUsuario;
                    RegistroClientes.registrar(usuarioLogueado, out);
                }
                
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
            if (req.payload == null || !handler.payloadType().isInstance(req.payload)) {
                return new Respuesta(false, "Payload incorrecto para acción " + req.accion);
            }

            return invoke(handler, req.payload, usuarioLogueado);

        } catch (Exception e) {
            return new Respuesta(false, "Error interno: " + e.getMessage());
        }
    }


    @SuppressWarnings("unchecked")
    private static <T> Respuesta invoke(ManejadorAcciones<?> handler, Object payload, String usuarioLogueado) throws Exception {
        ManejadorAcciones<T> h = (ManejadorAcciones<T>) handler;
        return h.handle((T) payload, usuarioLogueado);
    }

}
