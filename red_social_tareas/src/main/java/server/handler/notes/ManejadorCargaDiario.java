package server.handler.notes;

import java.io.Serializable;
import java.util.List;

import entity.user.Usuario;
import server.handler.ManejadorAcciones;
import server.service.UsuarioService;
import server.service.notes.DiaService;
import server.service.notes.NotaService;
import shared.dto.notes.DiaConNotasDTO;
import shared.protocol.Respuesta;

public class ManejadorCargaDiario implements ManejadorAcciones<String> {

    private final UsuarioService usuarioService;
    private final DiaService diaService;
    private final NotaService notaService;

    public ManejadorCargaDiario(UsuarioService usuarioService, DiaService diaService, NotaService notaService) {
        this.usuarioService = usuarioService;
        this.diaService = diaService;
        this.notaService = notaService;
    }

    @Override
    public Respuesta handle(String nombreUsuario) throws Exception {

        if (nombreUsuario == null || nombreUsuario.trim().isEmpty()) {
            return new Respuesta(false, "Nombre de usuario vacío");
        }

        Usuario u = usuarioService.findByNombre(nombreUsuario);
        if (u == null) return new Respuesta(false, "Usuario no encontrado");

        List<DiaConNotasDTO> diario = diaService.cargarDiario(nombreUsuario, notaService);
        return new Respuesta(true, "Diario cargado", (Serializable) diario);
    }

    @Override
    public Class<String> payloadType() {
        return String.class;
    }
}
