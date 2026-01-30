package server.handler.user;

import entity.Usuario;
import server.handler.ManejadorAcciones;
import server.service.UsuarioService;
import shared.dto.user.CambioNombreDTO;
import shared.protocol.Respuesta;

public class ManejadorCambioNombre implements ManejadorAcciones<CambioNombreDTO>{

    private final UsuarioService usuarioService;
    
    public ManejadorCambioNombre(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }
	
	@Override
	public Respuesta handle(CambioNombreDTO payload) throws Exception {
		if (usuarioService.existsByNombre(payload.nombreNuevo))
			return new Respuesta(false, "Nombre ya existe");

		Usuario u = usuarioService.findByNombre(payload.nombreUsuario);
		if (u == null)
			return new Respuesta(false, "Usuario no encontrado");

		u.setNombre_usuario(payload.nombreNuevo);
		usuarioService.update(u);
		return new Respuesta(true, "Nombre cambiado correctamente");
	}

	@Override
	public Class<CambioNombreDTO> payloadType() {
		return CambioNombreDTO.class;
	}

}
