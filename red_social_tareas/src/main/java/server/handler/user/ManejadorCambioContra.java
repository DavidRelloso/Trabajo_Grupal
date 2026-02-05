package server.handler.user;

import entity.user.Usuario;
import server.handler.ManejadorAcciones;
import server.service.user.UsuarioService;
import shared.dto.user.CambioContraDTO;
import shared.protocol.Respuesta;
import util.PasswordUtil;

public class ManejadorCambioContra implements ManejadorAcciones<CambioContraDTO> {

    private final UsuarioService usuarioService;

    public ManejadorCambioContra(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @Override
    public Respuesta handle(CambioContraDTO payload, String usuarioLogueado) throws Exception {

        if (usuarioLogueado == null || usuarioLogueado.isBlank()) {
            return new Respuesta(false, "NO_LOGUEADO", null);
        }

        if (payload == null || payload.contraNueva == null || payload.contraNueva.isBlank()) {
            return new Respuesta(false, "Contraseña inválida", null);
        }

        Usuario u = usuarioService.findByNombre(usuarioLogueado);
        if (u == null) {
            return new Respuesta(false, "Usuario no encontrado", null);
        }

        String hashNueva = PasswordUtil.hash(payload.contraNueva);
        u.setContraHash(hashNueva);

        usuarioService.update(u);

        return new Respuesta(true, "Contraseña cambiada correctamente", null);
    }

    @Override
    public Class<CambioContraDTO> payloadType() {
        return CambioContraDTO.class;
    }
}
