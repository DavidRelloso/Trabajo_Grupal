package entity;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "solicitud_amistad")
public class SolicitudAmistad {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id_solicitud;

	@ManyToOne
	@JoinColumn(name = "id_emisor")
	private Usuario emisor;

	@ManyToOne
	@JoinColumn(name = "id_receptor")
	private Usuario receptor;

	private String estado; // pendiente / aceptada / rechazada

	private LocalDate fecha_envio;

	public Long getId_solicitud() {
		return id_solicitud;
	}

	public void setId_solicitud(Long id_solicitud) {
		this.id_solicitud = id_solicitud;
	}

	public Usuario getEmisor() {
		return emisor;
	}

	public void setEmisor(Usuario emisor) {
		this.emisor = emisor;
	}

	public Usuario getReceptor() {
		return receptor;
	}

	public void setReceptor(Usuario receptor) {
		this.receptor = receptor;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public LocalDate getFecha_envio() {
		return fecha_envio;
	}

	public void setFecha_envio(LocalDate fecha_envio) {
		this.fecha_envio = fecha_envio;
	}
}
