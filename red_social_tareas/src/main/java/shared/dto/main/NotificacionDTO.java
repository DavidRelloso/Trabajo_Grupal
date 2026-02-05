package shared.dto.main;

import java.io.Serializable;
import java.time.LocalDate;

public class NotificacionDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    public Long id;
    public String titulo;   
    public String texto;    
    public LocalDate fecha; 

    public NotificacionDTO(Long id, String titulo, String texto, LocalDate fecha) {
        this.id = id;
        this.titulo = titulo;
        this.texto = texto;
        this.fecha = fecha;
    }
}
