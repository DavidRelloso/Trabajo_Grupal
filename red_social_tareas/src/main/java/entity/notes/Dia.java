package entity.notes;

import javax.persistence.*;

import entity.user.Usuario;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
    name = "dia",
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_dia_usuario_fecha_categoria",
                columnNames = {"id_usuario", "fecha", "id_categoria"})
    }
)
public class Dia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_dia")
    private Long idDia;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_categoria", nullable = false)
    private Categoria categoria;

    @Column(nullable = false)
    private LocalDate fecha;

    @OneToMany(mappedBy = "dia", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Nota> notas = new ArrayList<>();

    public Dia() {}

    public Dia(Usuario usuario, Categoria categoria, LocalDate fecha) {
        this.usuario = usuario;
        this.categoria = categoria;
        this.fecha = fecha;
    }

    public Long getIdDia() { return idDia; }
    public Usuario getUsuario() { return usuario; }
    public Categoria getCategoria() { return categoria; }
    public LocalDate getFecha() { return fecha; }
    public List<Nota> getNotas() { return notas; }

    public void setIdDia(Long idDia) { this.idDia = idDia; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    public void setCategoria(Categoria categoria) { this.categoria = categoria; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public void addNota(Nota nota) {
        notas.add(nota);
        nota.setDia(this);
    }

    public void removeNota(Nota nota) {
        notas.remove(nota);
        nota.setDia(null);
    }
}
