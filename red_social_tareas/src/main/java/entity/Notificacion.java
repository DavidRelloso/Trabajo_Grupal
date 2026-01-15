package entity;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "notificacion")
public class Notificacion {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id_notificacion;

	@ManyToOne
	@JoinColumn(name = "id_usuario")
	private Usuario usuario;

	private String tipo;

	@Column(columnDefinition = "TEXT")
	private String mensaje;

	private LocalDate fecha;

	private boolean leida;

	public Long getId_notificacion() {
		return id_notificacion;
	}

	public void setId_notificacion(Long id_notificacion) {
		this.id_notificacion = id_notificacion;
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

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	public LocalDate getFecha() {
		return fecha;
	}

	public void setFecha(LocalDate fecha) {
		this.fecha = fecha;
	}

	public boolean isLeida() {
		return leida;
	}

	public void setLeida(boolean leida) {
		this.leida = leida;
	}
}
