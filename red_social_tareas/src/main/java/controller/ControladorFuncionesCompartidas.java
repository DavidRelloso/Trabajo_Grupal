package controller;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

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
        try (Socket s = new Socket("192.168.1.132", 5000);
             ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(s.getInputStream())) {

            out.writeObject(req);
            out.flush();
            return (Respuesta) in.readObject();
        }
    }
    
    
    
    
}
