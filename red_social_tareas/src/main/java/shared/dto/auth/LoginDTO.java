package shared.dto.auth;

import java.io.Serializable;

public class LoginDTO implements Serializable {
	
	private static final long serialVersionUID = 1L;
	public String nombreUsuario;
	public String contra;

	public LoginDTO(String nombreUsuario, String contra) {
		this.nombreUsuario = nombreUsuario;
		this.contra = contra;
	}
}
