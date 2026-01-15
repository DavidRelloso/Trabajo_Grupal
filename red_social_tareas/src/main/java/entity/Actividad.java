package entity;

import javax.persistence.*;
import java.time.LocalTime;

@Entity
@Table(name = "actividad")
public class Actividad {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id_actividad;

	@ManyToOne
	@JoinColumn(name = "id_dia", nullable = false)
	private Dia dia;

	private String titulo;

	@Column(columnDefinition = "TEXT")
	private String texto;

	private LocalTime hora_inicio;

	private LocalTime hora_fin;

	public Long getId_actividad() {
		return id_actividad;
	}

	public void setId_actividad(Long id_actividad) {
		this.id_actividad = id_actividad;
	}

	public Dia getDia() {
		return dia;
	}

	public void setDia(Dia dia) {
		this.dia = dia;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}

	public LocalTime getHora_inicio() {
		return hora_inicio;
	}

	public void setHora_inicio(LocalTime hora_inicio) {
		this.hora_inicio = hora_inicio;
	}

	public LocalTime getHora_fin() {
		return hora_fin;
	}

	public void setHora_fin(LocalTime hora_fin) {
		this.hora_fin = hora_fin;
	}
}
