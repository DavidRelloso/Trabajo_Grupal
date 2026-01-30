package shared.dto;

import java.io.Serializable;
import java.time.LocalDate;

public class RecordatorioDTO implements Serializable {
	
	private static final long serialVersionUID = 1L;
	public LocalDate fecha;
	public String texto;

	public RecordatorioDTO(LocalDate fecha, String texto) {
		this.fecha = fecha;
		this.texto = texto;
	}
}