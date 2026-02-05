package server.handler.notes;

import entity.notes.Dia;
import entity.notes.Nota;
import entity.user.Usuario;
import server.handler.ManejadorAcciones;
import server.service.UsuarioService;
import server.service.notes.DiaService;
import server.service.notes.NotaService;
import shared.dto.notes.EliminarNotaDTO;
import shared.protocol.Respuesta;

public class ManejadorEliminarNota implements ManejadorAcciones<EliminarNotaDTO> {

	private final UsuarioService usuarioService;
	private final NotaService notaService;
	private final DiaService diaService;

	public ManejadorEliminarNota(UsuarioService usuarioService, NotaService notaService, DiaService diaService) {
		this.usuarioService = usuarioService;
		this.notaService = notaService;
		this.diaService = diaService;
	}

	@Override
	public Respuesta handle(EliminarNotaDTO dto, String usuarioLogueado) throws Exception {
		if (usuarioLogueado == null || usuarioLogueado.isBlank())
			return new Respuesta(false, "NO_LOGUEADO", null);
		if (dto == null || dto.idDia == null || dto.idNota == null)
			return new Respuesta(false, "Datos incompletos", null);

		Usuario u = usuarioService.findByNombre(usuarioLogueado);
		if (u == null)
			return new Respuesta(false, "Usuario no encontrado", null);

		Dia dia = diaService.findById(dto.idDia);
		if (dia == null || !dia.getUsuario().getIdUsuario().equals(u.getIdUsuario()))
			return new Respuesta(false, "Día no válido", null);

		Nota nota = notaService.findById(dto.idNota);
		if (nota == null || !nota.getDia().getIdDia().equals(dia.getIdDia()))
			return new Respuesta(false, "Nota no encontrada en ese día", null);

		notaService.eliminarNota(nota);
		return new Respuesta(true, "Nota eliminada", null);
	}

	@Override
	public Class<EliminarNotaDTO> payloadType() {
		return EliminarNotaDTO.class;
	}
}
