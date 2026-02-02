package Cliente;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.net.Socket;

public class ClienteTCP {

    public static byte[] solicitarInforme() throws Exception {

        Socket socket = new Socket("localhost", 5000);

        DataOutputStream out = new DataOutputStream(socket.getOutputStream());
        DataInputStream in = new DataInputStream(socket.getInputStream());

        out.writeUTF("GENERAR_INFORME_USUARIOS");

        int tamaño = in.readInt();
        byte[] pdf = new byte[tamaño];
        in.readFully(pdf);

        socket.close();
        return pdf;
    }
}
