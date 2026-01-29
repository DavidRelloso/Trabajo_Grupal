package shared;

import java.io.Serializable;

public class RegistroDTO implements Serializable {
	
  private static final long serialVersionUID = 1L;
  public String correo;
  public String usuario;
  public String password;
  public byte[] avatar;

  public RegistroDTO(String correo, String usuario, String password, byte[] avatar) {
    this.correo = correo;
    this.usuario = usuario;
    this.password = password;
    this.avatar = avatar;
  }
}
