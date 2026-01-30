package shared.dto.user;

import java.io.Serializable;

public class CambioCorreoDTO implements Serializable {
	
	private static final long serialVersionUID = 1L;
	public String nombreUsuario;
	public String correoNuevo;

	public CambioCorreoDTO(String nombreUsuario, String correoNuevo) {
		this.nombreUsuario = nombreUsuario;
		this.correoNuevo = correoNuevo;
	}

}
