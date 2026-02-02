package client.net;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import shared.dto.user.UserDTO;

public final class Sesion {

    public static final ClienteTCP tcp = new ClienteTCP();

    private static final ObjectProperty<UserDTO> usuario = new SimpleObjectProperty<>();

    private Sesion() {}

    public static ObjectProperty<UserDTO> usuarioProperty() {
        return usuario;
    }

    public static UserDTO getUsuario() {
        return usuario.get();
    }

    public static void setUsuario(UserDTO u) {
        usuario.set(u);
    }

    public static boolean haySesion() {
        return getUsuario() != null;
    }

    public static void conectar(String host, int puerto) throws Exception {
        tcp.conectar(host, puerto);
    }

    public static void cerrarSesion() {
        usuario.set(null);
        tcp.cerrar(); 
    }
}
