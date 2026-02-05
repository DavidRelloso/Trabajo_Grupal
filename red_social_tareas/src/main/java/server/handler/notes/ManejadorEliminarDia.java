package server.handler.notes;

import entity.notes.Dia;
import entity.user.Usuario;
import server.handler.ManejadorAcciones;
import server.service.notes.DiaService;
import server.service.user.UsuarioService;
import shared.dto.notes.EliminarDiaDTO;
import shared.protocol.Respuesta;

public class ManejadorEliminarDia implements ManejadorAcciones<EliminarDiaDTO> {

    private final UsuarioService usuarioService;
    private final DiaService diaService;

    public ManejadorEliminarDia(UsuarioService usuarioService, DiaService diaService) {
        this.usuarioService = usuarioService;
        this.diaService = diaService;
    }

    @Override
    public Respuesta handle(EliminarDiaDTO dto, String usuarioLogueado) throws Exception {
        if (usuarioLogueado == null || usuarioLogueado.isBlank()) return new Respuesta(false, "NO_LOGUEADO", null);
        if (dto == null || dto.idDia == null) return new Respuesta(false, "Datos incompletos", null);

        Usuario u = usuarioService.findByNombre(usuarioLogueado);
        if (u == null) return new Respuesta(false, "Usuario no encontrado", null);

        Dia dia = diaService.findById(dto.idDia);
        if (dia == null || !dia.getUsuario().getIdUsuario().equals(u.getIdUsuario()))
            return new Respuesta(false, "Día no válido", null);

		diaService.eliminarDia(dia); // debe eliminar también sus notas (cascade/orphanRemoval)
        return new Respuesta(true, "Día eliminado", null);
    }

    @Override
    public Class<EliminarDiaDTO> payloadType() { return EliminarDiaDTO.class; }
}