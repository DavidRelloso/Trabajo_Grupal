package shared.dto.notes;

import java.io.Serializable;

public class CrearNotaRespuestaDTO implements Serializable {
	
    private static final long serialVersionUID = 1L;

    public boolean diaCreado;
    public Long diaId;
    public Long notaId;

    public CrearNotaRespuestaDTO(boolean diaCreado, Long diaId, Long notaId) {
        this.diaCreado = diaCreado;
        this.diaId = diaId;
        this.notaId = notaId;
    }
}
