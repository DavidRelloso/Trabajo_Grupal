package controller;

import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;

public class ControladorPrincipal {

    @FXML private GridPane paneLogin;
    @FXML private GridPane paneRegistro;

    @FXML
    private void initialize() {
        // Mostrar login por defecto
        mostrarLogin();
    }

    @FXML
    private void mostrarLogin() {
        paneLogin.setVisible(true);
        paneLogin.setManaged(true);

        paneRegistro.setVisible(false);
        paneRegistro.setManaged(false);
    }

    @FXML
    private void mostrarRegistro() {
        paneLogin.setVisible(false);
        paneLogin.setManaged(false);

        paneRegistro.setVisible(true);
        paneRegistro.setManaged(true);
    }

}
