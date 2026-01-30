package server.handler.user;

import entity.Usuario;
import server.handler.ManejadorAcciones;
import server.service.UsuarioService;
import shared.dto.user.CambioContraDTO;
import shared.protocol.Respuesta;

public class ManejadorCambioContra implements ManejadorAcciones<CambioContraDTO> {

	private final UsuarioService usuarioService;
	
	public ManejadorCambioContra(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}
	
	@Override
	public Respuesta handle(CambioContraDTO payload) throws Exception {
		Usuario u = usuarioService.findByNombre(payload.nombreUsuario);
		if (u == null)
			return new Respuesta(false, "Usuario no encontrado");

		u.setContra_hash(payload.contraNueva);
		usuarioService.update(u);
		return new Respuesta(true, "Contraseña cambiada correctamente");
	}

	@Override
	public Class<CambioContraDTO> payloadType() {
		return CambioContraDTO.class;
	}

}
