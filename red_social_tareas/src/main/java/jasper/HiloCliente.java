package jasper;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import net.sf.jasperreports.engine.JRException;

public class HiloCliente implements Runnable {

    private Socket socket;

    public HiloCliente(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {

        try {
            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());

            String comando = in.readUTF();

            if (comando.equals("GENERAR_INFORME_USUARIOS")) {

                byte[] pdf = ReportGenerator.generarInformeUsuarios();

                out.writeInt(pdf.length);
                out.write(pdf);
                out.flush();
            }

            socket.close();

        } catch (IOException | JRException e) {
            e.printStackTrace();
        }
    }
}

