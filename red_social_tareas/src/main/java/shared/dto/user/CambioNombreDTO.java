package shared.dto.user;

import java.io.Serializable;

public class CambioNombreDTO implements Serializable {
	
	private static final long serialVersionUID = 1L;
	public String nombreUsuario;
	public String nombreNuevo;

	public CambioNombreDTO(String nombreUsuario, String nombreNuevo) {
		this.nombreUsuario = nombreUsuario;
		this.nombreNuevo = nombreNuevo;
	}

}
