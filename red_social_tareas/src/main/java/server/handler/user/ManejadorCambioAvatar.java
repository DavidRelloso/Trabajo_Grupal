package server.handler.user;

import entity.user.Usuario;
import server.handler.ManejadorAcciones;
import server.service.user.UsuarioService;
import shared.dto.user.CambioAvatarDTO;
import shared.dto.user.UserDTO;
import shared.protocol.Respuesta;

public class ManejadorCambioAvatar implements ManejadorAcciones<CambioAvatarDTO> {

    private final UsuarioService usuarioService;

    public ManejadorCambioAvatar(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @Override
    public Respuesta handle(CambioAvatarDTO payload, String usuarioLogueado) throws Exception {

        if (usuarioLogueado == null || usuarioLogueado.isBlank()) {
            return new Respuesta(false, "NO_LOGUEADO", null);
        }

        if (payload == null || payload.nuevoAvatar == null || payload.nuevoAvatar.length == 0) {
            return new Respuesta(false, "Avatar inválido", null);
        }

        Usuario u = usuarioService.findByNombre(usuarioLogueado);
        if (u == null) {
            return new Respuesta(false, "Usuario no encontrado", null);
        }

        u.setAvatarImg(payload.nuevoAvatar);
        usuarioService.update(u);

        // Recomendado: no mandar contraHash
        UserDTO user = new UserDTO(
                u.getIdUsuario(),
                u.getNombreUsuario(),
                u.getCorreo(),
                null,              // o elimina este campo del DTO si puedes
                u.getAvatarImg()
        );

        return new Respuesta(true, "Avatar cambiado correctamente", user);
    }

    @Override
    public Class<CambioAvatarDTO> payloadType() {
        return CambioAvatarDTO.class;
    }
}
