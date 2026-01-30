package shared.dto.user;

import java.io.Serializable;

public class UserDTO implements Serializable {
	
	private static final long serialVersionUID = 1L;
	public Long id;
	public String nombreUsuario;
	public String correo;
	public String contra;
	public byte[] avatarImg;

	public UserDTO(Long id, String usuario, String correo, String contra, byte[] avatar) {
		this.id = id;
		this.nombreUsuario = usuario;
		this.correo = correo;
		this.contra = contra;
		this.avatarImg = avatar;
	}
}
