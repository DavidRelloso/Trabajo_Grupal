package server.handler.user;

import entity.Usuario;
import server.handler.ManejadorAcciones;
import server.service.UsuarioService;
import shared.dto.user.CambioCorreoDTO;
import shared.protocol.Respuesta;

public class ManejadorCambioCorreo implements ManejadorAcciones<CambioCorreoDTO> {

	private final UsuarioService usuarioService;
	
	public ManejadorCambioCorreo(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}
	
	@Override
	public Respuesta handle(CambioCorreoDTO payload) throws Exception {
		System.out.println(payload.nombreUsuario);
		
		if (usuarioService.existsByCorreo(payload.correoNuevo))
			return new Respuesta(false, "Correo ya registrado");

		Usuario u = usuarioService.findByNombre(payload.nombreUsuario);
		if (u == null) {
			System.err.println("Usuario no encontrado en cambioCorreo");
			return new Respuesta(false, "Usuario no encontrado");
		}
		u.setCorreo(payload.correoNuevo);
		usuarioService.update(u);
		return new Respuesta(true, "Correo cambiado correctamente");
	}

	@Override
	public Class<CambioCorreoDTO> payloadType() {
		return CambioCorreoDTO.class;
	}

}
