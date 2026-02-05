package shared.dto.reports;

import java.io.Serializable;

public class UsuarioInformeDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	private final Integer id_usuario;
    private final String nombre_usuario;
    private final String correo;
    private final String contraseña_hash;

    public UsuarioInformeDTO(Integer id_usuario, String nombre_usuario, String correo, String contraseña_hash) {
        this.id_usuario = id_usuario;
        this.nombre_usuario = nombre_usuario;
        this.correo = correo;
        this.contraseña_hash = contraseña_hash;
    }

    public Integer getId_usuario() { return id_usuario; }
    public String getNombre_usuario() { return nombre_usuario; }
    public String getCorreo() { return correo; }
    public String getContraseña_hash() { return contraseña_hash; }
}
