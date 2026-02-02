package shared.dto.auth;

import java.io.Serializable;

public class RegistroDTO implements Serializable {
	
  private static final long serialVersionUID = 1L;
  public String correo;
  public String nombreUsuario;
  public String password;
  public byte[] avatar;

  public RegistroDTO(String correo, String nombreUsuario, String password, byte[] avatar) {
    this.correo = correo;
    this.nombreUsuario = nombreUsuario;
    this.password = password;
    this.avatar = avatar;
  }
}
