package server.handler.user;

import entity.user.Usuario;
import server.handler.ManejadorAcciones;
import server.service.UsuarioService;
import shared.dto.user.CambioAvatarDTO;
import shared.dto.user.UserDTO;
import shared.protocol.Respuesta;

public class ManejadorCambioAvatar implements ManejadorAcciones<CambioAvatarDTO> {

	private final UsuarioService usuarioService;

	public ManejadorCambioAvatar(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}

	@Override
	public Respuesta handle(CambioAvatarDTO payload) throws Exception {
		if (payload == null || payload.nuevoAvatar == null || payload.nuevoAvatar.length == 0) {
			return new Respuesta(false, "Avatar inválido");
		}

		Usuario u = usuarioService.findByNombre(payload.nombreUsuario);
		if (u == null)
			return new Respuesta(false, "Usuario no encontrado");

		u.setAvatarImg(payload.nuevoAvatar);
		usuarioService.update(u);

		UserDTO user = new UserDTO(
				u.getIdUsuario(), 
				u.getNombreUsuario(), 
				u.getCorreo(), 
				u.getContraHash(),
				u.getAvatarImg()
		);

		return new Respuesta(true, "Avatar cambiado correctamente", user);
	}

	@Override
	public Class<CambioAvatarDTO> payloadType() {
		return CambioAvatarDTO.class;
	}

}
