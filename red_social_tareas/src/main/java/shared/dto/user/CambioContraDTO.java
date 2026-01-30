package shared.dto.user;

import java.io.Serializable;

public class CambioContraDTO implements Serializable {
	
	private static final long serialVersionUID = 1L;
	public String nombreUsuario;
	public String contraNueva;

	public CambioContraDTO(String nombreUsuario, String contraNueva) {
		this.nombreUsuario = nombreUsuario;
		this.contraNueva = contraNueva;
	}

}
