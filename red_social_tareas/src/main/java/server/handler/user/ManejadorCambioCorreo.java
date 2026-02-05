package server.handler.user;

import entity.user.Usuario;
import server.handler.ManejadorAcciones;
import server.service.user.UsuarioService;
import shared.dto.user.CambioCorreoDTO;
import shared.protocol.Respuesta;

public class ManejadorCambioCorreo implements ManejadorAcciones<CambioCorreoDTO> {

    private final UsuarioService usuarioService;

    public ManejadorCambioCorreo(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @Override
    public Respuesta handle(CambioCorreoDTO payload, String usuarioLogueado) throws Exception {

        if (usuarioLogueado == null || usuarioLogueado.isBlank()) {
            return new Respuesta(false, "NO_LOGUEADO", null);
        }

        if (payload == null || payload.correoNuevo == null || payload.correoNuevo.isBlank()) {
            return new Respuesta(false, "Correo inválido", null);
        }

        if (usuarioService.existsByCorreo(payload.correoNuevo)) {
            return new Respuesta(false, "Correo ya registrado", null);
        }

        Usuario u = usuarioService.findByNombre(usuarioLogueado);
        if (u == null) {
            return new Respuesta(false, "Usuario no encontrado", null);
        }

        u.setCorreo(payload.correoNuevo);
        usuarioService.update(u);

        return new Respuesta(true, "Correo cambiado correctamente", null);
    }

    @Override
    public Class<CambioCorreoDTO> payloadType() {
        return CambioCorreoDTO.class;
    }
}
