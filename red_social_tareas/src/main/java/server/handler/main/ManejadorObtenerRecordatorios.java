package server.handler.main;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import entity.user.Usuario;
import entity.notes.Nota;
import server.handler.ManejadorAcciones;
import server.service.notes.NotaService;
import server.service.user.UsuarioService;
import shared.dto.main.RecordatorioDTO;
import shared.protocol.Respuesta;

public class ManejadorObtenerRecordatorios implements ManejadorAcciones<LocalDate> {

    private final UsuarioService usuarioService;
    private final NotaService notaService;

    public ManejadorObtenerRecordatorios(UsuarioService usuarioService, NotaService notaService) {
        this.usuarioService = usuarioService;
        this.notaService = notaService;
    }

    @Override
    public Respuesta handle(LocalDate fecha, String usuarioLogueado) throws Exception {

        if (usuarioLogueado == null || usuarioLogueado.isBlank()) {
            return new Respuesta(false, "NO_LOGUEADO", null);
        }
        if (fecha == null) return new Respuesta(false, "FECHA_INVALIDA", null);

        Usuario u = usuarioService.findByNombre(usuarioLogueado);
        if (u == null) return new Respuesta(false, "USUARIO_NO_ENCONTRADO", null);

        List<Nota> notas = notaService.obtenerNotasUsuarioPorFecha(usuarioLogueado, fecha);

        List<RecordatorioDTO> out = notas.stream()
            .map(n -> new RecordatorioDTO(
                n.getIdNota(),
                fecha,
                (n.getHoraInicio() != null ? n.getHoraInicio().toString() : "") + " - " + n.getTitulo()
            ))
            .collect(Collectors.toList());

        return new Respuesta(true, "OK", (Serializable) out);
    }

    @Override
    public Class<LocalDate> payloadType() { return LocalDate.class; }
}
