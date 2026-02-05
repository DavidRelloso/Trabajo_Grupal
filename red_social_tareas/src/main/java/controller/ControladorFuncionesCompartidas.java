package controller;

import client.net.Sesion;
import javafx.scene.control.Alert;
import shared.protocol.Peticion;
import shared.protocol.Respuesta;

public abstract class ControladorFuncionesCompartidas {

	//MOSTRAR ALERTAS PERSONALIZADAS
    protected void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    // RECIBIR RESPUES DEÑ SERVIDOR
    protected Respuesta enviar(Peticion req) throws Exception {
        Sesion.asegurarConexion();    
        return Sesion.tcp.enviar(req);
    }
}
