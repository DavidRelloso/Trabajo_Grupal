package shared;

import java.io.Serializable;

public class UserDTO implements Serializable {
  private static final long serialVersionUID = 1L;
  public Long id;
  public String usuario;
  public String correo;

  public UserDTO(Long id, String usuario, String correo) {
    this.id = id;
    this.usuario = usuario;
    this.correo = correo;
  }
}