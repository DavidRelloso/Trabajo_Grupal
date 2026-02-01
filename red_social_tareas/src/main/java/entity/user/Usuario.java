package entity.user;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;

import entity.notes.Categoria;

@Entity
@Table(name = "usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long idUsuario;

    @Column(name = "nombre_usuario", unique = true, nullable = false)
    private String nombreUsuario;

    @Column(unique = true, nullable = false)
    private String correo;

    @Column(name = "contra_hash", nullable = false)
    private String contraHash;

    @Lob
    @Column(name = "avatar_img")
    private byte[] avatarImg;

	@OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Categoria> categorias = new ArrayList<>();

    public Usuario() {}

    public Usuario(String nombreUsuario, String correo, String contraHash, byte[] avatarImg) {
        this.nombreUsuario = nombreUsuario;
        this.correo = correo;
        this.contraHash = contraHash;
        this.avatarImg = avatarImg;
    }


	public void addCategoria(String nombre) {
		Categoria c = new Categoria();
		c.setNombre(nombre);
		c.setUsuario(this); 
		categorias.add(c);
	}

    public List<Categoria> getCategorias() {
        return categorias;
    }

    public Long getIdUsuario() { return idUsuario; }
    public String getNombreUsuario() { return nombreUsuario; }
    public String getCorreo() { return correo; }
    public String getContraHash() { return contraHash; }
    public byte[] getAvatarImg() { return avatarImg; }

    public void setIdUsuario(Long idUsuario) { this.idUsuario = idUsuario; }
    public void setNombreUsuario(String nombreUsuario) { this.nombreUsuario = nombreUsuario; }
    public void setCorreo(String correo) { this.correo = correo; }
    public void setContraHash(String contraHash) { this.contraHash = contraHash; }
    public void setAvatarImg(byte[] avatarImg) { this.avatarImg = avatarImg; }
}
