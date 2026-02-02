package entity.friend;

import javax.persistence.*;

import entity.user.Usuario;

import java.time.LocalDate;

@Entity
@Table(name = "amigo")
@IdClass(AmigoId.class)
public class Amigo {

	@Id
	@ManyToOne
	@JoinColumn(name = "id_usuario1")
	private Usuario usuario1;

	@Id
	@ManyToOne
	@JoinColumn(name = "id_usuario2")
	private Usuario usuario2;

	private LocalDate fecha_amistad;

	public Usuario getUsuario1() {
		return usuario1;
	}

	public void setUsuario1(Usuario usuario1) {
		this.usuario1 = usuario1;
	}

	public Usuario getUsuario2() {
		return usuario2;
	}

	public void setUsuario2(Usuario usuario2) {
		this.usuario2 = usuario2;
	}

	public LocalDate getFecha_amistad() {
		return fecha_amistad;
	}

	public void setFecha_amistad(LocalDate fecha_amistad) {
		this.fecha_amistad = fecha_amistad;
	}
}
