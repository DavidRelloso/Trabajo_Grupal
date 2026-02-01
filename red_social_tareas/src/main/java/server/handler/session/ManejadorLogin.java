package server.handler.session;

import entity.user.Usuario;
import server.handler.ManejadorAcciones;
import server.service.UsuarioService;
import shared.dto.auth.LoginDTO;
import shared.dto.user.UserDTO;
import shared.protocol.Respuesta;

public class ManejadorLogin implements ManejadorAcciones<LoginDTO> {

    private final UsuarioService usuarioService;

    public ManejadorLogin(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @Override
    public Respuesta handle(LoginDTO payload) {
    	
        Usuario u = usuarioService.findByNombreAndContra(payload.usuario, payload.password);
        if (u == null) return new Respuesta(false, "Usuario o contraseña incorrectos");

        UserDTO user = new UserDTO(
	        		u.getIdUsuario(), 
	        		u.getNombreUsuario(), 
	        		u.getCorreo(),
	                u.getContraHash(), 
	                u.getAvatarImg()
        		);

        return new Respuesta(true, "Login correcto", user);
    }

    @Override
    public Class<LoginDTO> payloadType() {
        return LoginDTO.class;
    }
}
