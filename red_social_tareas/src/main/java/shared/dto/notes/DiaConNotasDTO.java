package shared.dto.notes;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

public class DiaConNotasDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    public Long idDia;
    public LocalDate fecha;
    public String categoria;
    public List<NotaDiarioDTO> notas;

    public DiaConNotasDTO(Long idDia, LocalDate fecha, String categoria, List<NotaDiarioDTO> notas) {
        this.idDia = idDia;
        this.fecha = fecha;
        this.categoria = categoria;
        this.notas = notas;
    }
}
