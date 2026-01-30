package client;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import shared.dto.user.UserDTO;

public final class Sesion {

	/*Clase que mantiene la sesion del usuario en la aplicacion cliente
	 para mantener informacion entre pantallas*/
	
    private Sesion() {} 

    private static final ObjectProperty<UserDTO> usuario = new SimpleObjectProperty<>();

    public static ObjectProperty<UserDTO> usuarioProperty() {
        return usuario;
    }

    public static UserDTO getUsuario() {
        return usuario.get();
    }

    public static void setUsuario(UserDTO u) {
        usuario.set(u);
    }

    public static void refrescar() {
        UserDTO u = usuario.get();
        usuario.set(null);
        usuario.set(u);
    }


    public static boolean haySesion() {
        return getUsuario() != null;
    }

    public static void cerrarSesion() {
        usuario.set(null);
    }
}
