package server.net;

import java.io.ObjectOutputStream;
import java.util.concurrent.ConcurrentHashMap;

public class RegistroClientes {

	private static final ConcurrentHashMap<String, ObjectOutputStream> ONLINE = new ConcurrentHashMap<>();

	public static void registrar(String nombreUsuario, ObjectOutputStream out) {
	    ONLINE.put(nombreUsuario, out);
	    System.out.println("ONLINE_REGISTER key=[" + nombreUsuario + "] size=" + ONLINE.size());
	}


    public static void desregistrar(String nombreUsuario) {
        if (nombreUsuario != null) ONLINE.remove(nombreUsuario);
    }

    public static boolean isOnline(String nombreUsuario) {
    	
        return ONLINE.containsKey(nombreUsuario);
    }

    public static void enviarParaUsuario(String nombreUsuario, Object mensaje) {
        ObjectOutputStream out = ONLINE.get(nombreUsuario);
        System.out.println("ONLINE_SEND key=[" + nombreUsuario + "] existe=" + (out != null) +
                " obj=" + (mensaje == null ? "null" : mensaje.getClass().getName()));
        if (out == null) return;

        synchronized (out) {
            try {
                out.writeObject(mensaje);
                out.flush();
            } catch (Exception e) {
                ONLINE.remove(nombreUsuario);
                e.printStackTrace();
            }
        }
    }

	
}
