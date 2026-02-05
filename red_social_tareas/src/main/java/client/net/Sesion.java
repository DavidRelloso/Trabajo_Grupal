package client.net;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import shared.dto.user.UserDTO;

public final class Sesion {

    public static final ClienteTCP tcp = new ClienteTCP();

    //ip y puerto del servidor
    public static final String HOST = "192.168.1.132";
    public static final int PORT = 5000;

    private static final ObjectProperty<UserDTO> usuario = new SimpleObjectProperty<>();

    private Sesion() {}

    public static void asegurarConexion() throws Exception {
        if (!tcp.isConectado()) {
            tcp.conectar(HOST, PORT);
        }
    }

    public static ObjectProperty<UserDTO> usuarioProperty() { return usuario; }
    public static UserDTO getUsuario() { return usuario.get(); }
    public static void setUsuario(UserDTO u) { usuario.set(u); }
    public static boolean haySesion() { return getUsuario() != null; }

    public static void cerrarSesion() {
        usuario.set(null);
        tcp.cerrar();
    }
}
