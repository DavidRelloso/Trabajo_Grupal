package server.handler.friends;

import java.time.LocalDate;

import entity.user.SolicitudAmistad;
import server.handler.ManejadorAcciones;
import server.net.RegistroClientes;
import server.service.friends.AmigoService;
import server.service.friends.SolicitudAmistadService;
import server.service.user.UsuarioService;
import shared.dto.friends.AgregarAmigoDTO;
import shared.dto.friends.SolicitudAmistadDTO;
import shared.protocol.Respuesta;

public class ManejadorAgregarAmigo implements ManejadorAcciones<AgregarAmigoDTO> {

    private final UsuarioService usuarioService;
    private final SolicitudAmistadService solicitudAmistadService;
    private final AmigoService amigoService;

    public ManejadorAgregarAmigo(
            UsuarioService usuarioService,
            SolicitudAmistadService solicitudAmistadService,
            AmigoService amigoService
    ) {
        this.usuarioService = usuarioService;
        this.solicitudAmistadService = solicitudAmistadService;
        this.amigoService = amigoService;
    }

    @Override
    public Respuesta handle(AgregarAmigoDTO dto, String usuarioLogueado) {

        // 0) autenticación
        if (usuarioLogueado == null || usuarioLogueado.isBlank()) {
            return new Respuesta(false, "No autenticado");
        }

        // 1) seguridad: si el DTO trae nombreUsuario, validarlo contra la sesión
        if (dto.nombreUsuario != null && !dto.nombreUsuario.equals(usuarioLogueado)) {
            return new Respuesta(false, "Usuario inválido");
        }

        // 2) datos
        String fromName = usuarioLogueado;
        String toName = (dto.nombreAmigo != null) ? dto.nombreAmigo.trim() : "";

        if (toName.isBlank()) {
            return new Respuesta(false, "Nombre de amigo inválido");
        }

        if (fromName.equals(toName)) {
            return new Respuesta(false, "No puedes enviarte solicitud a ti mismo");
        }

        // 3) buscar usuarios
        var fromUser = usuarioService.findByNombre(fromName);
        var toUser = usuarioService.findByNombre(toName);

        if (fromUser == null) {
            return new Respuesta(false, "Sesión inválida (usuario no encontrado)");
        }

        if (toUser == null) {
            return new Respuesta(false, "El usuario no existe");
        }

        Long fromId = fromUser.getIdUsuario();
        Long toId = toUser.getIdUsuario();

        // 4) comprobar si ya son amigos
        if (amigoService.sonAmigos(fromId, toId)) {
            return new Respuesta(false, "Ya sois amigos");
        }

        // 5) comprobar si ya hay solicitud pendiente (en ambos sentidos)
        if (solicitudAmistadService.existePendienteEntre(fromId, toId)) {
            return new Respuesta(false, "Ya existe una solicitud pendiente");
        }

        // 6) guardar solicitud como PENDIENTE
        SolicitudAmistad s = new SolicitudAmistad();
        s.setEmisor(fromUser);
        s.setReceptor(toUser);
        s.setEstado("PENDIENTE");
        s.setFecha_envio(LocalDate.now());

        solicitudAmistadService.save(s);
        System.out.println(
        	    "PUSH_CHECK toName=[" + toName + "] " +
        	    "online=" + RegistroClientes.isOnline(toName) +
        	    " | usuarioLogueado=[" + usuarioLogueado + "]" +
        	    " | fromName=[" + fromName + "]"
        	);
        // 7) notificación push (si el receptor está online)
        if (RegistroClientes.isOnline(toName)) {
            Respuesta push = new Respuesta(
                    true,
                    "NUEVA_SOLICITUD_AMISTAD",
                    new SolicitudAmistadDTO(fromName, toName)
            );
            RegistroClientes.enviarParaUsuario(toName, push);
        }

        // respuesta al emisor
        return new Respuesta(true, "Solicitud enviada");
    }

    @Override
    public Class<AgregarAmigoDTO> payloadType() {
        return AgregarAmigoDTO.class;
    }

}
