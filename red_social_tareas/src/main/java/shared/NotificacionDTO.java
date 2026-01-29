package shared;

import java.io.Serializable;

public class NotificacionDTO implements Serializable {
	
	private static final long serialVersionUID = 1L;
    public String titulo;
    public String texto;

    public NotificacionDTO(String titulo, String texto) {
        this.titulo = titulo;
        this.texto = texto;
    }
}