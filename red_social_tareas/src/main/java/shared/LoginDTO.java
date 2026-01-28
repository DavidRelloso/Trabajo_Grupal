package shared;

import java.io.Serializable;

public class LoginDTO implements Serializable {
  private static final long serialVersionUID = 1L;
  public String usuario;
  public String password;

  public LoginDTO(String usuario, String password) {
    this.usuario = usuario;
    this.password = password;
  }
}
