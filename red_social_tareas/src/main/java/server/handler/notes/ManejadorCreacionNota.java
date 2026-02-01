package server.handler.notes;

import entity.notes.Dia;
import entity.notes.Nota;
import entity.user.Usuario;
import server.handler.ManejadorAcciones;
import server.service.UsuarioService;
import server.service.notes.DiaService;
import server.service.notes.NotaService;
import shared.dto.notes.CrearNotaDTO;
import shared.dto.notes.CrearNotaRespuestaDTO;
import shared.protocol.Respuesta;

public class ManejadorCreacionNota implements ManejadorAcciones<CrearNotaDTO> {

    private final UsuarioService usuarioService;
    private final NotaService notaService;
    private final DiaService diaService;

    public ManejadorCreacionNota(UsuarioService usuarioService, NotaService notaService, DiaService diaService) {
        this.notaService = notaService;
        this.usuarioService = usuarioService;
        this.diaService = diaService;
    }

    @Override
    public Respuesta handle(CrearNotaDTO dto) throws Exception {

        Usuario u = usuarioService.findByNombre(dto.nombreUsuario);
        if (u == null) return new Respuesta(false, "Usuario no encontrado");

        // Recuperamos si el dia seleccionado para la categoria seleccionada ya esta creado
        // si -> no se crea el dia/columna, 
        // no -> se crea el nuevo dia/columna
        DiaService.DiaGetOrCreateResult diaRes =
                diaService.getOrCreateDia(u, dto.fecha, dto.categoria);

        Dia dia = diaRes.dia;
        boolean diaCreado = diaRes.creado;

        // Se crea la nota en el dia seleccionado
        Nota nota = notaService.crearNota(
                dia,
                dto.titulo,
                dto.texto,
                dto.horaInicio,
                dto.horaFin,
                dto.visibilidad
        );

        // Responder al cliente si tiene que crear un dia o no
        CrearNotaRespuestaDTO out = new CrearNotaRespuestaDTO(
                diaCreado,
                dia.getIdDia(),
                nota.getIdNota()
        );

        return new Respuesta(true, "Nota creada correctamen", out);
    }

    @Override
    public Class<CrearNotaDTO> payloadType() {
        return CrearNotaDTO.class;
    }
}
