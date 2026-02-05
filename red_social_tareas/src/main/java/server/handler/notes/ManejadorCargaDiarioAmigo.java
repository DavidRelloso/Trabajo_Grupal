package server.handler.notes;

import java.io.Serializable;
import java.util.List;

import entity.user.Usuario;
import server.handler.ManejadorAcciones;
import server.service.friends.AmigoService;
import server.service.notes.DiaService;
import server.service.notes.NotaService;
import server.service.user.UsuarioService;
import shared.dto.notes.DiaConNotasDTO;
import shared.protocol.Respuesta;

public class ManejadorCargaDiarioAmigo implements ManejadorAcciones<String> {

    private final UsuarioService usuarioService;
    private final DiaService diaService;
    private final NotaService notaService;
    private final AmigoService amigoService;

    public ManejadorCargaDiarioAmigo(UsuarioService usuarioService, DiaService diaService, NotaService notaService, AmigoService amigoService) {
        this.usuarioService = usuarioService;
        this.diaService = diaService;
        this.notaService = notaService;
        this.amigoService = amigoService;
    }

    @Override
    public Respuesta handle(String nombreAmigo, String usuarioLogueado) throws Exception {

        if (usuarioLogueado == null || usuarioLogueado.isBlank()) {
            return new Respuesta(false, "NO_LOGUEADO", null);
        }

        if (nombreAmigo == null || nombreAmigo.isBlank()) {
            return new Respuesta(false, "AMIGO_INVALIDO", null);
        }

        Usuario yo = usuarioService.findByNombre(usuarioLogueado);
        if (yo == null) return new Respuesta(false, "USUARIO_NO_ENCONTRADO", null);

        Usuario amigo = usuarioService.findByNombre(nombreAmigo);
        if (amigo == null) return new Respuesta(false, "AMIGO_NO_ENCONTRADO", null);

        // Seguridad: solo si sois amigos
        if (!amigoService.sonAmigos(yo.getIdUsuario(), amigo.getIdUsuario())) {
            return new Respuesta(false, "NO_SON_AMIGOS", null);
        }

        // Cargar diario del amigo filtrado (PUBLICO + COMPARTIR)
        List<DiaConNotasDTO> diario = diaService.cargarDiarioAmigo(nombreAmigo, notaService);

        return new Respuesta(true, "OK", (Serializable) diario);
    }

    @Override
    public Class<String> payloadType() {
        return String.class;
    }
}
