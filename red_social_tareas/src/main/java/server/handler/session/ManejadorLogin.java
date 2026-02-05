package server.handler.session;

import entity.user.Usuario;
import server.handler.ManejadorAcciones;
import server.service.user.UsuarioService;
import shared.dto.auth.LoginDTO;
import shared.dto.user.UserDTO;
import shared.protocol.Respuesta;
import util.PasswordUtil;

public class ManejadorLogin implements ManejadorAcciones<LoginDTO> {

    private final UsuarioService usuarioService;

    public ManejadorLogin(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @Override
    public Respuesta handle(LoginDTO dto, String usuarioLogueado) throws Exception {

        if (dto == null || dto.nombreUsuario == null || dto.contra == null) {
            return new Respuesta(false, "Datos inválidos", null);
        }

        Usuario u = usuarioService.findByNombre(dto.nombreUsuario);
        if (u == null) return new Respuesta(false, "Usuario o contraseña incorrectos", null);

        String hashInput = PasswordUtil.hash(dto.contra);
        if (!hashInput.equals(u.getContraHash()))
            return new Respuesta(false, "Usuario o contraseña incorrectos", null);


        UserDTO user = new UserDTO(
            u.getIdUsuario(),
            u.getNombreUsuario(),
            u.getCorreo(),
            null,
            u.getAvatarImg()
        );

        return new Respuesta(true, "Login correcto", user);
    }

    @Override
    public Class<LoginDTO> payloadType() {
        return LoginDTO.class;
    }
}
