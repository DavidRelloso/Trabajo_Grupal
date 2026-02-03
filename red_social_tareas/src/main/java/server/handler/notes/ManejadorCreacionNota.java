package server.handler.notes;

import entity.notes.Dia;
import entity.notes.Nota;
import entity.user.Usuario;
import server.handler.ManejadorAcciones;
import server.service.UsuarioService;
import server.service.notes.DiaService;
import server.service.notes.NotaService;
import server.service.notes.VisibilidadNota;
import shared.dto.notes.CrearNotaDTO;
import shared.dto.notes.CrearDiaDTO;
import shared.protocol.Respuesta;

public class ManejadorCreacionNota implements ManejadorAcciones<CrearNotaDTO> {

    private final UsuarioService usuarioService;
    private final NotaService notaService;
    private final DiaService diaService;

    public ManejadorCreacionNota(UsuarioService usuarioService, NotaService notaService, DiaService diaService) {
        this.usuarioService = usuarioService;
        this.notaService = notaService;
        this.diaService = diaService;
    }

    @Override
    public Respuesta handle(CrearNotaDTO dto, String usuarioLogueado) throws Exception {

        if (usuarioLogueado == null || usuarioLogueado.isBlank()) {
            return new Respuesta(false, "NO_LOGUEADO", null);
        }

        Usuario u = usuarioService.findByNombre(usuarioLogueado);
        if (u == null) {
            return new Respuesta(false, "Usuario no encontrado", null);
        }

        DiaService.DiaGetOrCreateResult diaRes =
                diaService.getOrCreateDia(u, dto.fecha, dto.categoria);

        Dia dia = diaRes.dia;
        boolean diaCreado = diaRes.creado;

        VisibilidadNota visibilidadEnum;
        try {
            visibilidadEnum = VisibilidadNota.valueOf(dto.visibilidad);
        } catch (Exception e) {
            return new Respuesta(false, "Visibilidad inválida: " + dto.visibilidad, null);
        }

        Nota nota = notaService.crearNota(
                dia,
                dto.titulo,
                dto.texto,
                dto.horaInicio,
                dto.horaFin,
                visibilidadEnum
        );

        CrearDiaDTO out = new CrearDiaDTO(
                diaCreado,
                dia.getIdDia(),
                nota.getIdNota(),
                dto.fecha,
                dto.categoria
        );

        return new Respuesta(true, "Nota creada correctamente", out);
    }

    @Override
    public Class<CrearNotaDTO> payloadType() {
        return CrearNotaDTO.class;
    }
}
