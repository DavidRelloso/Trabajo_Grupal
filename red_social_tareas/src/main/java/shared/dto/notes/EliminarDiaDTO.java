package shared.dto.notes;

import java.io.Serializable;

public class EliminarDiaDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	public Long idDia;

	public EliminarDiaDTO(Long idDia) {
		this.idDia = idDia;
	}
}