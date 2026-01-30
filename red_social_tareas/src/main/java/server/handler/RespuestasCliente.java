package server.handler;

import entity.Usuario;
import server.service.UsuarioService;
import shared.dto.auth.LoginDTO;
import shared.dto.auth.RegistroDTO;
import shared.dto.user.CambioAvatarDTO;
import shared.dto.user.CambioContraDTO;
import shared.dto.user.CambioCorreoDTO;
import shared.dto.user.CambioNombreDTO;
import shared.dto.user.UserDTO;
import shared.protocol.Respuesta;

public abstract class RespuestasCliente {
	
	protected UsuarioService usuarioService = new UsuarioService();
	
	// ====== PROCESAR PETICIONES ======
	
	// LOGIN
//	protected Respuesta login(LoginDTO dto) {
//		Usuario u = usuarioService.findByNombreAndContra(dto.usuario, dto.password);
//		if (u == null)
//			return new Respuesta(false, "Usuario o contraseña incorrectos");
//
//		UserDTO user = new UserDTO(u.getId_usuario(), u.getNombre_usuario(), u.getCorreo(),u.getContra_hash(), u.getAvatar_img());
//		return new Respuesta(true, "Login correcto", user);
//	}

	// REGISTRO
//	protected Respuesta registro(RegistroDTO dto) {
//		if (usuarioService.existsByCorreo(dto.correo))
//			return new Respuesta(false, "Correo ya registrado");
//		if (usuarioService.existsByNombre(dto.nombreUsuario))
//			return new Respuesta(false, "Nombre ya existe");
//
//		usuarioService.save(new Usuario(dto.nombreUsuario, dto.correo, dto.password, dto.avatar));
//		return new Respuesta(true, "Registro correcto");
//	}
	
	// CAMBIO DE NOMBRE
//	protected Respuesta cambioNombre(CambioNombreDTO dto) {
//		if (usuarioService.existsByNombre(dto.nombreNuevo))
//			return new Respuesta(false, "Nombre ya existe");
//
//		Usuario u = usuarioService.findByNombre(dto.nombreUsuario);
//		if (u == null)
//			return new Respuesta(false, "Usuario no encontrado");
//
//		u.setNombre_usuario(dto.nombreNuevo);
//		usuarioService.update(u);
//		return new Respuesta(true, "Nombre cambiado correctamente");
//	}
	
	// CAMBIO DE CORREO
//	protected Respuesta cambioCorreo(CambioCorreoDTO dto) {
//		System.out.println(dto.nombreUsuario);
//		
//		if (usuarioService.existsByCorreo(dto.correoNuevo))
//			return new Respuesta(false, "Correo ya registrado");
//
//		Usuario u = usuarioService.findByNombre(dto.nombreUsuario);
//		if (u == null) {
//			System.err.println("Usuario no encontrado en cambioCorreo");
//			return new Respuesta(false, "Usuario no encontrado");
//		}
//		u.setCorreo(dto.correoNuevo);
//		usuarioService.update(u);
//		return new Respuesta(true, "Correo cambiado correctamente");
//	}
	
	// CAMBIO DE CONTRASEÑA
//	protected Respuesta cambioContra(CambioContraDTO dto) {
//		Usuario u = usuarioService.findByNombre(dto.nombreUsuario);
//		if (u == null)
//			return new Respuesta(false, "Usuario no encontrado");
//
//		u.setContra_hash(dto.contraNueva);
//		usuarioService.update(u);
//		return new Respuesta(true, "Contraseña cambiada correctamente");
//	}
	
	// CAMBIO DE AVATAR
//	protected Respuesta cambioAvatar(CambioAvatarDTO dto) {
//	    if (dto == null || dto.nuevoAvatar == null || dto.nuevoAvatar.length == 0) {
//	        return new Respuesta(false, "Avatar inválido");
//	    }
//
//	    Usuario u = usuarioService.findByNombre(dto.nombreUsuario);
//	    if (u == null)
//	        return new Respuesta(false, "Usuario no encontrado");
//
//	    u.setAvatar_img(dto.nuevoAvatar);
//	    usuarioService.update(u);
//
//	    UserDTO user = new UserDTO(
//	            u.getId_usuario(),
//	            u.getNombre_usuario(),
//	            u.getCorreo(),
//	            u.getContra_hash(),
//	            u.getAvatar_img()
//	    );
//
//	    return new Respuesta(true, "Avatar cambiado correctamente", user);
//	}

}
