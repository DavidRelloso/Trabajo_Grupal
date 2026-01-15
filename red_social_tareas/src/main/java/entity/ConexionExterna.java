package entity;

import javax.persistence.*;

@Entity
@Table(name = "conexion_externa")
public class ConexionExterna {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id_conexion;

	@ManyToOne
	@JoinColumn(name = "id_usuario")
	private Usuario usuario;

	private String tipo; // steam / spotify

	private String token;

	private String estado;

	
	public Long getId_conexion() {
		return id_conexion;
	}

	public void setId_conexion(Long id_conexion) {
		this.id_conexion = id_conexion;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}
}
