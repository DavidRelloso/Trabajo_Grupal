package shared.dto.notes;

import java.io.Serializable;
import java.time.LocalDate;

public class CrearDiaDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    public boolean diaCreado;
    public Long idDia;
    public Long idNota;

    public LocalDate fecha;
    public String categoria;

    public CrearDiaDTO(
    		boolean diaCreado, Long idDia, Long idNota, 
    		LocalDate fecha, String categoria
    	) {
        this.diaCreado = diaCreado;
        this.idDia = idDia;
        this.idNota = idNota;
        this.fecha = fecha;
        this.categoria = categoria;
    }
}
