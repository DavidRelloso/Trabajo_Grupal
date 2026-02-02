package controller;

import client.net.Sesion;
import javafx.scene.control.Alert;
import shared.protocol.Peticion;
import shared.protocol.Respuesta;

public abstract class ControladorFuncionesCompartidas {

	protected void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
		Alert alert = new Alert(tipo);
		alert.setTitle(titulo);
		alert.setHeaderText(null);
		alert.setContentText(mensaje);
		alert.showAndWait();
	}
	
    protected Respuesta enviar(Peticion req) throws Exception {
        return Sesion.tcp.enviar(req);
    }
    
    
    
    
}
