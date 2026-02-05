package server.handler.friends;

import server.handler.ManejadorAcciones;
import server.service.friends.SolicitudAmistadService;
import server.service.user.UsuarioService;
import shared.dto.friends.ResponderSolicitudDTO;
import shared.protocol.Respuesta;

public class ManejadorRechazarSolicitud implements ManejadorAcciones<ResponderSolicitudDTO> {

	private final UsuarioService usuarioService;
	private final SolicitudAmistadService solicitudService;

	public ManejadorRechazarSolicitud(UsuarioService usuarioService, SolicitudAmistadService solicitudService) {
		this.usuarioService = usuarioService;
		this.solicitudService = solicitudService;
	}

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

		boolean ok = solicitudService.rechazarPendiente(fromUser.getIdUsuario(), toUser.getIdUsuario());
		return ok ? new Respuesta(true, "Solicitud rechazada") : new Respuesta(false, "No existe solicitud pendiente");
	}

	@Override
	public Class<ResponderSolicitudDTO> payloadType() {
		return ResponderSolicitudDTO.class;
	}

}
