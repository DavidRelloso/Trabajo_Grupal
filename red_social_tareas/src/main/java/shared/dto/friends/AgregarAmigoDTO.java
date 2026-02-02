package shared.dto.friends;

import java.io.Serializable;

public class AgregarAmigoDTO implements Serializable{

	private static final long serialVersionUID = 1L;
	
	public String nombreUsuario;
	public String nombreAmigo;
	
	public AgregarAmigoDTO(String nombreUsuario, String nombreAmigo) {
		this.nombreUsuario = nombreUsuario;
		this.nombreAmigo = nombreAmigo;
	}
	
}
