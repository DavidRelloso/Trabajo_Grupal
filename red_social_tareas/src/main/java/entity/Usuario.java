package entity;

import javax.persistence.*;

@Entity
@Table(name = "usuario")
public class Usuario {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id_usuario;

	@Column(unique = true, nullable = false)
	private String nombre_usuario;

	@Column(unique = true, nullable = false)
	private String correo;

	@Column(nullable = false)
	private String contraseña_hash;

	private String avatar_url;

	private boolean modo_oscuro;

	public Long getId_usuario() {
		return id_usuario;
	}

	public void setId_usuario(Long id_usuario) {
		this.id_usuario = id_usuario;
	}

	public String getNombre_usuario() {
		return nombre_usuario;
	}

	public void setNombre_usuario(String nombre_usuario) {
		this.nombre_usuario = nombre_usuario;
	}

	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public String getContraseña_hash() {
		return contraseña_hash;
	}

	public void setContraseña_hash(String contraseña_hash) {
		this.contraseña_hash = contraseña_hash;
	}

	public String getAvatar_url() {
		return avatar_url;
	}

	public void setAvatar_url(String avatar_url) {
		this.avatar_url = avatar_url;
	}

	public boolean isModo_oscuro() {
		return modo_oscuro;
	}

	public void setModo_oscuro(boolean modo_oscuro) {
		this.modo_oscuro = modo_oscuro;
	}
}
