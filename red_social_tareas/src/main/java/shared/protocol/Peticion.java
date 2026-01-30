package shared.protocol;

import java.io.Serializable;

public class Peticion implements Serializable {

	private static final long serialVersionUID = 1L;
	public String action; // "LOGIN", "REGISTER", "SEARCH_USER"
	public Serializable payload;

	public Peticion() {
	}

	public Peticion(String action, Serializable payload) {
		this.action = action;
		this.payload = payload;
	}
}
