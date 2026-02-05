package server.handler.friends;

import java.io.Serializable;
import java.util.List;

import server.handler.ManejadorAcciones;
import server.service.friends.SolicitudAmistadService;
import server.service.user.UsuarioService;
import shared.dto.friends.SolicitudAmistadDTO;
import shared.protocol.Respuesta;

public class ManejadorSolicitudesPendientes implements ManejadorAcciones<Object> {

    private final UsuarioService usuarioService;
    private final SolicitudAmistadService solicitudService;

    public ManejadorSolicitudesPendientes(UsuarioService usuarioService, SolicitudAmistadService solicitudService) {
        this.usuarioService = usuarioService;
        this.solicitudService = solicitudService;
    }

    @Override
    public Respuesta handle(Object dto, String usuarioLogueado) {
        if (usuarioLogueado == null || usuarioLogueado.isBlank()) {
            return new Respuesta(false, "No autenticado");
        }

        var receptor = usuarioService.findByNombre(usuarioLogueado);
        if (receptor == null) return new Respuesta(false, "Usuario no encontrado");

        var solicitudes = solicitudService.obtenerPendientesParaReceptor(receptor.getIdUsuario());

        List<SolicitudAmistadDTO> out = solicitudes.stream()
            .map(s -> new SolicitudAmistadDTO(
                s.getEmisor().getNombreUsuario(),
                s.getReceptor().getNombreUsuario()
            ))
            .toList();

        return new Respuesta(true, "OK", (Serializable) out);
    }

    @Override
    public Class<Object> payloadType() {
        return Object.class; 
    }

}
