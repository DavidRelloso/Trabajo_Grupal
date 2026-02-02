package controller.main;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.Parent;
import javafx.scene.control.Accordion;

public class ControladorNotificaciones {

    @FXML private Button btnBorrarNoti;
    @FXML private TitledPane tpPrincipalNoti;
    @FXML private Label lblTextoNoti;
    
    public void setInfoNoti(String nombreAmigo, String textoNoti) {
    	tpPrincipalNoti.setText(nombreAmigo);
    	lblTextoNoti.setText(textoNoti);
    	eliminarNoti();
    }
    
    private void eliminarNoti() {
    	
    	btnBorrarNoti.setOnAction(e -> {
            Parent padre = tpPrincipalNoti.getParent();

            if (padre == null) {
                System.out.println("El TitledPane no tiene padre");
                return;
            }
            else if (padre instanceof Accordion accordion) {
                accordion.getPanes().remove(tpPrincipalNoti);
                System.out.println("Eliminado de un Accordion");
            }
            else {
                System.out.println("Padre no soportado: " + padre.getClass());
            }
        });
    }
}
