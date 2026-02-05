package server.handler.notes;

import java.io.Serializable;
import java.util.List;

import entity.user.Usuario;
import server.handler.ManejadorAcciones;
import server.service.notes.DiaService;
import server.service.notes.NotaService;
import server.service.user.UsuarioService;
import shared.dto.notes.DiaConNotasDTO;
import shared.protocol.Respuesta;

public class ManejadorCargaDiario implements ManejadorAcciones<Void> {

    private final UsuarioService usuarioService;
    private final DiaService diaService;
    private final NotaService notaService;

    public ManejadorCargaDiario(UsuarioService usuarioService, DiaService diaService, NotaService notaService) {
        this.usuarioService = usuarioService;
        this.diaService = diaService;
        this.notaService = notaService;
    }

    @Override
    public Respuesta handle(Void payload, String usuarioLogueado) throws Exception {

        if (usuarioLogueado == null || usuarioLogueado.isBlank()) {
            return new Respuesta(false, "NO_LOGUEADO", null);
        }

        Usuario u = usuarioService.findByNombre(usuarioLogueado);
        if (u == null) return new Respuesta(false, "Usuario no encontrado", null);

        List<DiaConNotasDTO> diario = diaService.cargarDiario(usuarioLogueado, notaService);
        return new Respuesta(true, "Diario cargado", (Serializable) diario);
    }

    @Override
    public Class<Void> payloadType() {
        return Void.class;
    }
}
