package controller.sceneNavigator;

import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;

public class ControladorContenedorVentanas {

	//CLASE CONTENEDOR
	//contenedor que contendra las pantallas para pasar facilmente de una a otra
    @FXML private StackPane viewContainer;

    @FXML
    private void initialize() {
        NavegadorVentanas.setContainer(viewContainer);
        NavegadorVentanas.navegar("/escenas/login/VentanaLogin.fxml");
    }
}

