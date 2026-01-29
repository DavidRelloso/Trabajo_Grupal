package shared;

import java.io.Serializable;

public class Respuesta implements Serializable {
	
  private static final long serialVersionUID = 1L;

  public boolean ok;
  public String message;
  public Serializable data;

  public Respuesta() {}

  public Respuesta(boolean ok, String message) {
    this.ok = ok;
    this.message = message;
  }

  public Respuesta(boolean ok, String message, Serializable data) {
    this.ok = ok;
    this.message = message;
    this.data = data;
  }
}

