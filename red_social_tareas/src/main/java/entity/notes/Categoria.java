package entity.notes;

import javax.persistence.*;

import entity.user.Usuario;

@Entity
@Table(
    name = "categoria",
    uniqueConstraints = @UniqueConstraint(columnNames = {"id_usuario", "nombre"})
)
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_categoria")
    private Long idCategoria;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @Column(nullable = false)
    private String nombre;

    public Categoria() {}

    public Categoria(Usuario usuario, String nombre) {
        this.usuario = usuario;
        this.nombre = nombre;
    }

    public Long getIdCategoria() { return idCategoria; }
    public Usuario getUsuario() { return usuario; }
    public String getNombre() { return nombre; }

    public void setIdCategoria(Long idCategoria) { this.idCategoria = idCategoria; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    public void setNombre(String nombre) { this.nombre = nombre; }
}
