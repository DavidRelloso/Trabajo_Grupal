package shared.protocol;

import java.io.Serializable;

public class Peticion implements Serializable {

	private static final long serialVersionUID = 1L;
	public String accion; 
	public Serializable payload;

	public Peticion() {
	}

	public Peticion(String accion, Serializable payload) {
		this.accion = accion;
		this.payload = payload;
	}
}
