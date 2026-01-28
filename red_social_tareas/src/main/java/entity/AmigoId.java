package entity;

import java.io.Serializable;
import java.util.Objects;

public class AmigoId implements Serializable {

	private Long usuario1;
	private Long usuario2;

	public AmigoId() {
	}

	public AmigoId(Long usuario1, Long usuario2) {
		this.usuario1 = usuario1;
		this.usuario2 = usuario2;
	}

	public Long getUsuario1() {
		return usuario1;
	}

	public void setUsuario1(Long usuario1) {
		this.usuario1 = usuario1;
	}

	public Long getUsuario2() {
		return usuario2;
	}

	public void setUsuario2(Long usuario2) {
		this.usuario2 = usuario2;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof AmigoId))
			return false;
		AmigoId that = (AmigoId) o;
		return Objects.equals(usuario1, that.usuario1) && Objects.equals(usuario2, that.usuario2);
	}

	@Override
	public int hashCode() {
		return Objects.hash(usuario1, usuario2);
	}
}
