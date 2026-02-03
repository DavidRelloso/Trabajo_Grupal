package server.handler.session;

import entity.user.Usuario;
import server.handler.ManejadorAcciones;
import server.service.UsuarioService;
import shared.dto.auth.RegistroDTO;
import shared.protocol.Respuesta;
import util.PasswordUtil;

public class ManejadorRegistro implements ManejadorAcciones<RegistroDTO> {

    private final UsuarioService usuarioService;

    public ManejadorRegistro(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @Override
    public Respuesta handle(RegistroDTO payload, String usuarioLogueado) throws Exception {

        if (payload == null) {
            return new Respuesta(false, "Datos inválidos", null);
        }

        if (payload.correo == null || payload.correo.isBlank()
                || payload.nombreUsuario == null || payload.nombreUsuario.isBlank()
                || payload.password == null || payload.password.isBlank()) {
            return new Respuesta(false, "Campos incompletos", null);
        }

        if (usuarioService.existsByCorreo(payload.correo)) {
            return new Respuesta(false, "Correo ya registrado", null);
        }

        if (usuarioService.existsByNombre(payload.nombreUsuario)) {
            return new Respuesta(false, "Nombre ya existe", null);
        }

        String hash = PasswordUtil.hash(payload.password);

        usuarioService.save(new Usuario(
                payload.nombreUsuario,
                payload.correo,
                hash,
                payload.avatar
        ));

        return new Respuesta(true, "Registro correcto", null);
    }

    @Override
    public Class<RegistroDTO> payloadType() {
        return RegistroDTO.class;
    }
}
