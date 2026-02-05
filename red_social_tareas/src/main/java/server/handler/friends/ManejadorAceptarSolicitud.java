package server.handler.friends;

import server.handler.ManejadorAcciones;
import server.service.friends.SolicitudAmistadService;
import server.service.user.UsuarioService;
import shared.dto.friends.ResponderSolicitudDTO;
import shared.protocol.Respuesta;

public class ManejadorAceptarSolicitud implements ManejadorAcciones<ResponderSolicitudDTO> {

	private final UsuarioService usuarioService;
	private final SolicitudAmistadService solicitudService;

	public ManejadorAceptarSolicitud(UsuarioService usuarioService, SolicitudAmistadService solicitudService) {
		this.usuarioService = usuarioService;
		this.solicitudService = solicitudService;
	}

	//VALIDAR SOLICITUD AMISTAD
	@Override
	public Respuesta handle(ResponderSolicitudDTO dto, String usuarioLogueado) {
		if (usuarioLogueado == null || usuarioLogueado.isBlank())
			return new Respuesta(false, "No autenticado");
		if (dto == null || dto.from == null || dto.to == null)
			return new Respuesta(false, "Datos inválidos");

		String fromName = dto.from.trim();
		String toName = dto.to.trim();

		if (!toName.equals(usuarioLogueado))
			return new Respuesta(false, "Acción inválida");

		var fromUser = usuarioService.findByNombre(fromName);
		var toUser = usuarioService.findByNombre(toName);
		if (fromUser == null || toUser == null)
			return new Respuesta(false, "Usuario no encontrado");

		//	si encuentra los dos usuarios al darle a aceptar el clliente se hacen amigos
		boolean ok = solicitudService.aceptarPendiente(fromUser.getIdUsuario(), toUser.getIdUsuario());
		return ok ? new Respuesta(true, "Solicitud aceptada") : new Respuesta(false, "No existe solicitud pendiente");
	}

	@Override
	public Class<ResponderSolicitudDTO> payloadType() {
		return ResponderSolicitudDTO.class;
	}

}
