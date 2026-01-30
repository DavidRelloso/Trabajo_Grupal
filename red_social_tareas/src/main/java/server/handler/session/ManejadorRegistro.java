package server.handler.session;

import entity.Usuario;
import server.handler.ManejadorAcciones;
import server.service.UsuarioService;
import shared.dto.auth.RegistroDTO;
import shared.protocol.Respuesta;

public class ManejadorRegistro implements ManejadorAcciones<RegistroDTO> {

    private final UsuarioService usuarioService;
	
    public ManejadorRegistro(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

	@Override
	public Respuesta handle(RegistroDTO payload) {
		if (usuarioService.existsByCorreo(payload.correo))
			return new Respuesta(false, "Correo ya registrado");
		if (usuarioService.existsByNombre(payload.nombreUsuario))
			return new Respuesta(false, "Nombre ya existe");

		usuarioService.save(new Usuario(payload.nombreUsuario, payload.correo, payload.password, payload.avatar));
		return new Respuesta(true, "Registro correcto");
	}
	
    @Override
    public Class<RegistroDTO> payloadType() {
        return RegistroDTO.class;
    }
	
}
