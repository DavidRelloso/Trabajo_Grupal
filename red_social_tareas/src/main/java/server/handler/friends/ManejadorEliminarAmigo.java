package server.handler.friends;

import entity.user.Usuario;
import server.handler.ManejadorAcciones;
import server.net.RegistroClientes;
import server.service.friends.AmigoService;
import server.service.user.UsuarioService;
import shared.dto.friends.AmigoDTO;
import shared.protocol.Respuesta;

public class ManejadorEliminarAmigo implements ManejadorAcciones<AmigoDTO> {

    private final UsuarioService usuarioService;
    private final AmigoService amigoService;

    public ManejadorEliminarAmigo(UsuarioService usuarioService, AmigoService amigoService) {
        this.usuarioService = usuarioService;
        this.amigoService = amigoService;
    }

    @Override
    public Respuesta handle(AmigoDTO dto, String usuarioLogueado) throws Exception {

        if (usuarioLogueado == null) {
            return new Respuesta(false, "Usuario no logueado", null);
        }

        if (dto == null || dto.nombreUsuario == null || dto.nombreUsuario.isBlank()) {
            return new Respuesta(false, "Datos de amigo inválidos", null);
        }

        Usuario usuario = usuarioService.findByNombre(usuarioLogueado);
        if (usuario == null) {
            return new Respuesta(false, "USUARIO_INVALIDO", null);
        }

        Usuario amigo = usuarioService.findByNombre(dto.nombreUsuario);
        if (amigo == null) {
            return new Respuesta(false, "AMIGO_NO_EXISTE", null);
        }

        if (!amigoService.sonAmigos(usuario.getIdUsuario(), amigo.getIdUsuario())) {
            return new Respuesta(false, "NO_SON_AMIGOS", null);
        }

        amigoService.eliminarAmistad(usuario.getIdUsuario(), amigo.getIdUsuario());

        if (RegistroClientes.isOnline(amigo.getNombreUsuario())) {
            Respuesta push = new Respuesta(
                    true,
                    "AMIGO_ELIMINADO",
                    new AmigoDTO(usuario.getNombreUsuario())
            );
            RegistroClientes.enviarParaUsuario(amigo.getNombreUsuario(), push);
        }

        return new Respuesta(true, "Amigo eliminado correctamente", null);
    }

    @Override
    public Class<AmigoDTO> payloadType() {
        return AmigoDTO.class;
    }

}
