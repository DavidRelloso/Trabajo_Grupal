package entity.notes;

import javax.persistence.*;

import server.service.notes.VisibilidadNota;

import java.time.LocalTime;

@Entity
@Table(name = "nota")
public class Nota {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_nota")
    private Long idNota;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_dia", nullable = false)
    private Dia dia;

    @Column(nullable = false)
    private String titulo;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String texto;

    @Column(nullable = false)
    private LocalTime horaInicio;

    // opcional
    private LocalTime horaFin;

    @Enumerated(EnumType.STRING)
    @Column(name = "visibilidad", nullable = false)
    private VisibilidadNota visibilidad;

    public Nota() {}

    public Nota(Dia dia, String titulo, String texto,
                LocalTime horaInicio, LocalTime horaFin,
                VisibilidadNota visibilidad) {
        this.dia = dia;
        this.titulo = titulo;
        this.texto = texto;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.visibilidad = visibilidad;
    }

    public Long getIdNota() { return idNota; }
    public Dia getDia() { return dia; }
    public String getTitulo() { return titulo; }
    public String getTexto() { return texto; }
    public LocalTime getHoraInicio() { return horaInicio; }
    public LocalTime getHoraFin() { return horaFin; }
    public VisibilidadNota getVisibilidad() { return visibilidad; }

    public void setIdNota(Long idNota) { this.idNota = idNota; }
    public void setDia(Dia dia) { this.dia = dia; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public void setTexto(String texto) { this.texto = texto; }
    public void setHoraInicio(LocalTime horaInicio) { this.horaInicio = horaInicio; }
    public void setHoraFin(LocalTime horaFin) { this.horaFin = horaFin; }
    public void setVisibilidad(VisibilidadNota visibilidad) { this.visibilidad = visibilidad; }
}
