package controller.sceneNavigator;

import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;

public class ControladorContenedorVentanas {

    @FXML private StackPane viewContainer;

    @FXML
    private void initialize() {
        NavegadorVentanas.setContainer(viewContainer);
        NavegadorVentanas.navegar("/escenas/login/VentanaLogin.fxml");
    }
}

