package shared.dto.notes;

import java.io.Serializable;

public class EliminarNotaDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	public Long idDia;
	public Long idNota;

	public EliminarNotaDTO(Long idDia, Long idNota) {
		this.idDia = idDia;
		this.idNota = idNota;
	}
}
