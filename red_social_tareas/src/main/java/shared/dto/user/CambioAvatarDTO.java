package shared.dto.user;

import java.io.Serializable;

public class CambioAvatarDTO implements Serializable {
	
	private static final long serialVersionUID = 1L;
	public String nombreUsuario;
	public byte[] nuevoAvatar;

	public CambioAvatarDTO(String nombreUsuario, byte[] nuevoAvatar) {
		this.nombreUsuario = nombreUsuario;
		this.nuevoAvatar = nuevoAvatar;
	}

}
