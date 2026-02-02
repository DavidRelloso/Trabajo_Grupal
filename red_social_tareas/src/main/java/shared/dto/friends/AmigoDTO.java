package shared.dto.friends;

import java.io.Serializable;

public class AmigoDTO implements Serializable{
	
	private static final long serialVersionUID = 1L;
	public String nombreUsuario;
	
	public AmigoDTO(String nombreUsuario) {
		this.nombreUsuario = nombreUsuario;
	}

}
