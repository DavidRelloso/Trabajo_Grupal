package shared.dto.friends;

import java.io.Serializable;

public class ResponderSolicitudDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	public String from;
	public String to;

	public ResponderSolicitudDTO(String from, String to) {
		this.from = from;
		this.to = to;
	}

}
