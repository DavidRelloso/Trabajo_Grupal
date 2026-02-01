package shared.dto.notes;

import java.io.Serializable;
import java.time.LocalTime;

public class NotaDiarioDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    public Long idNota;
    public String titulo;
    public String texto;
    public LocalTime horaInicio;
    public LocalTime horaFin;
    public String visibilidad;

    public NotaDiarioDTO(Long idNota, String titulo, String texto, LocalTime horaInicio, LocalTime horaFin, String visibilidad) {
        this.idNota = idNota;
        this.titulo = titulo;
        this.texto = texto;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.visibilidad = visibilidad;
    }
}
