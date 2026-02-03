package shared.dto.notes;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

public class CrearNotaDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    public String categoria;
    public LocalDate fecha;

    public String titulo;
    public String texto;

    public LocalTime horaInicio;
    public LocalTime horaFin; 

    public String visibilidad; 

    public CrearNotaDTO(
            LocalDate fecha, String categoria, 
            String titulo, String texto,
            LocalTime horaInicio, LocalTime horaFin, 
            String visibilidad) {

        this.categoria = categoria;
        this.fecha = fecha;
        this.titulo = titulo;
        this.texto = texto;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.visibilidad = visibilidad;
    }
}
