package server;

import java.io.EOFException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import entity.Usuario;
import service.UsuarioService;
import shared.LoginDTO;
import shared.Peticion;
import shared.RegistroDTO;
import shared.Respuesta;
import shared.UserDTO;

public class ManejadorCliente extends Thread {

  private final Socket socket;
  private final UsuarioService usuarioService = new UsuarioService();

  public ManejadorCliente(Socket socket) {
    this.socket = socket;
  }

  @Override
  public void run() {
    try (
      ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
      ObjectInputStream in = new ObjectInputStream(socket.getInputStream())
    ) {
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
    try {
      return switch (req.action) {
        case "LOGIN" -> login((LoginDTO) req.payload);
        case "REGISTER" -> register((RegistroDTO) req.payload);
        default -> new Respuesta(false, "Acción no soportada: " + req.action);
      };
    } catch (ClassCastException e) {
      return new Respuesta(false, "Payload incorrecto para acción " + req.action);
    } catch (Exception e) {
      return new Respuesta(false, "Error interno: " + e.getMessage());
    }
  }

  private Respuesta login(LoginDTO dto) {
    Usuario u = usuarioService.findByNombreAndContra(dto.usuario, dto.password);
    if (u == null) return new Respuesta(false, "Usuario o contraseña incorrectos");

    UserDTO user = new UserDTO(u.getId_usuario(), u.getNombre_usuario(), u.getCorreo());
    return new Respuesta(true, "Login correcto", user);
  }

  private Respuesta register(RegistroDTO dto) {
    if (usuarioService.existsByCorreo(dto.correo)) return new Respuesta(false, "Correo ya registrado");
    if (usuarioService.existsByNombre(dto.usuario)) return new Respuesta(false, "Nombre ya existe");

    usuarioService.save(new Usuario(dto.usuario, dto.correo, dto.password, dto.avatar));
    return new Respuesta(true, "Registro correcto");
  }
}
