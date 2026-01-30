package Cliente;

import javafx.concurrent.Task;
import javafx.fxml.FXML;

import java.io.File;
import java.io.FileOutputStream;

public class Controlador {

    @FXML
    private void generarInforme() {

        Task<Void> tarea = new Task<>() {
            @Override
            protected Void call() throws Exception {

                byte[] pdf = ClienteTCP.solicitarInforme();

                FileOutputStream fos = new FileOutputStream("informe_usuarios.pdf");
                fos.write(pdf);
                fos.close();

                return null;
            }
        };

        tarea.setOnSucceeded(e -> {
            System.out.println("Informe generado correctamente");
            abrirPDF("informe_usuarios.pdf");
        });

        new Thread(tarea).start();
    }

    private void abrirPDF(String ruta) {
        try {
            java.awt.Desktop.getDesktop().open(new File(ruta));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
