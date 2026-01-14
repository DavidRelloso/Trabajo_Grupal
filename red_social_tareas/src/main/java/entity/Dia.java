package entity;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "dia")
public class Dia {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id_dia;

	@ManyToOne
	@JoinColumn(name = "id_usuario", nullable = false)
	private Usuario usuario;

	@ManyToOne
	@JoinColumn(name = "id_categoria", nullable = false)
	private Categoria categoria;

	private LocalDate fecha;

	private boolean es_publico;

	// Getters y setters
	public Long getId_dia() {
		return id_dia;
	}

	public void setId_dia(Long id_dia) {
		this.id_dia = id_dia;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}

	public LocalDate getFecha() {
		return fecha;
	}

	public void setFecha(LocalDate fecha) {
		this.fecha = fecha;
	}

	public boolean isEs_publico() {
		return es_publico;
	}

	public void setEs_publico(boolean es_publico) {
		this.es_publico = es_publico;
	}
}
