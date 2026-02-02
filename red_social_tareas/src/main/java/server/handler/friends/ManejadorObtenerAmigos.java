package server.handler.friends;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

import entity.user.Usuario;
import server.handler.ManejadorAcciones;
import server.service.UsuarioService;
import server.service.friends.AmigoService;
import shared.dto.friends.AmigoDTO;
import shared.protocol.Respuesta;

public class ManejadorObtenerAmigos implements ManejadorAcciones<Void> {

    private final UsuarioService usuarioService;
    private final AmigoService amigoService;

    public ManejadorObtenerAmigos(UsuarioService usuarioService, AmigoService amigoService) {
        this.usuarioService = usuarioService;
        this.amigoService = amigoService;
    }

    @Override
    public Respuesta handle(Void payload, String usuarioLogueado) throws Exception {

        if (usuarioLogueado == null) {
            return new Respuesta(false, "Usuario no logeado", null);
        }

        //busca usuario por nombre y comprueba si existe
        Usuario u = usuarioService.findByNombre(usuarioLogueado);
        if (u == null || u.getIdUsuario() == null) {
            return new Respuesta(false, "USUARIO_INVALIDO", null);
        }

        //recoge los amigos del usuario
        List<Usuario> amigos = amigoService.obtenerAmigos(u.getIdUsuario());

        List<AmigoDTO> amigosDTO = amigos.stream()
                .map(amigo -> new AmigoDTO(amigo.getNombreUsuario()))
                .collect(Collectors.toList());

        return new Respuesta(true, "OK", (Serializable) amigosDTO);
    }

    @Override
    public Class<Void> payloadType() {
        return Void.class;
    }

	@Override
	public Respuesta handle(Void dto) throws Exception {
		return null;
	}
}
