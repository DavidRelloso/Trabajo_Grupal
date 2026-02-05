package shared.dto.main;

import java.io.Serializable;
import java.time.LocalDate;

public class RecordatorioDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    public Long idNota;      // o null si no quieres
    public LocalDate fecha;
    public String texto;

    public RecordatorioDTO(Long idNota, LocalDate fecha, String texto) {
        this.idNota = idNota;
        this.fecha = fecha;
        this.texto = texto;
    }
}
