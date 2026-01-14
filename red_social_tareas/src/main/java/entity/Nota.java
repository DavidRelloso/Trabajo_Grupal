package entity;

import javax.persistence.*;
import java.time.LocalTime;

@Entity
@Table(name = "nota")
public class Nota {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id_nota;

	@ManyToOne
	@JoinColumn(name = "id_dia", nullable = false)
	private Dia dia;

	private String titulo;

	@Column(columnDefinition = "TEXT")
	private String texto;

	private LocalTime hora;

	// Getters y setters
	public Long getId_nota() {
		return id_nota;
	}

	public void setId_nota(Long id_nota) {
		this.id_nota = id_nota;
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

	public LocalTime getHora() {
		return hora;
	}

	public void setHora(LocalTime hora) {
		this.hora = hora;
	}
}
