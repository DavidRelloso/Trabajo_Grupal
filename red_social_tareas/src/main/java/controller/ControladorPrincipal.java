package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class ControladorPrincipal {

    @FXML
    private Button registro;

    @FXML
    public void initialize() {
        registro.setOnAction(e -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("loginn.fxml"));
                Parent root = loader.load();

                ControladorLogin registroController = loader.getController();

                Stage stage = (Stage) registro.getScene().getWindow();
                stage.setScene(new Scene(root));

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }
}

