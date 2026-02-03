package server.handler.user;

import entity.user.Usuario;
import server.handler.ManejadorAcciones;
import server.service.UsuarioService;
import shared.dto.user.CambioNombreDTO;
import shared.protocol.Respuesta;

public class ManejadorCambioNombre implements ManejadorAcciones<CambioNombreDTO> {

    private final UsuarioService usuarioService;

    public ManejadorCambioNombre(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @Override
    public Respuesta handle(CambioNombreDTO payload, String usuarioLogueado) throws Exception {

        if (usuarioLogueado == null || usuarioLogueado.isBlank()) {
            return new Respuesta(false, "NO_LOGUEADO", null);
        }

        if (payload == null || payload.nombreNuevo == null || payload.nombreNuevo.isBlank()) {
            return new Respuesta(false, "Nombre inválido", null);
        }

        if (usuarioService.existsByNombre(payload.nombreNuevo)) {
            return new Respuesta(false, "Nombre ya existe", null);
        }

        Usuario u = usuarioService.findByNombre(usuarioLogueado);
        if (u == null) {
            return new Respuesta(false, "Usuario no encontrado", null);
        }

        u.setNombreUsuario(payload.nombreNuevo);
        usuarioService.update(u);

        return new Respuesta(true, "Nombre cambiado correctamente", payload.nombreNuevo);
    }

    @Override
    public Class<CambioNombreDTO> payloadType() {
        return CambioNombreDTO.class;
    }
}
