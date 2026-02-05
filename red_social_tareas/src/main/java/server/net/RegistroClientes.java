package server.net;

import java.io.ObjectOutputStream;
import java.util.concurrent.ConcurrentHashMap;

public class RegistroClientes {

    // Usuarios conectados: usuario -> stream de salida hacia su socket
    private static final ConcurrentHashMap<String, ObjectOutputStream> ONLINE =
            new ConcurrentHashMap<>();

    // Registra a un usuario como conectado
    public static void registrar(String nombreUsuario, ObjectOutputStream out) {
        ONLINE.put(nombreUsuario, out);
        System.out.println("ONLINE_REGISTER key=[" + nombreUsuario + "] size=" + ONLINE.size());
    }

    // Elimina a un usuario del registro (desconexión / error)
    public static void desregistrar(String nombreUsuario) {
        if (nombreUsuario != null)
            ONLINE.remove(nombreUsuario);
    }

    // Comprueba si un usuario está online
    public static boolean isOnline(String nombreUsuario) {
        return ONLINE.containsKey(nombreUsuario);
    }

    public static void enviarParaUsuario(String nombreUsuario, Object mensaje) {
        ObjectOutputStream out = ONLINE.get(nombreUsuario);

        // Si no está conectado, no se envía nada
        if (out == null)
            return;

        synchronized (out) {
            try {
                out.writeObject(mensaje);
                out.flush();
            } catch (Exception e) {
                // Si falla el envío, se asume desconexión
                ONLINE.remove(nombreUsuario);
                e.printStackTrace();
            }
        }
    }
}
