package main;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;

public class ControladorPrincipal {

	@FXML
	private StackPane contenedorDerecha;

	@FXML
	private Button btnRegistro;
	
	@FXML
	private Button login;

	@FXML
	public void initialize() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
			Parent loginView = loader.load();

			contenedorDerecha.getChildren().setAll(loginView);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	
	@FXML
	public void abrirLogin(ActionEvent event) {
		login.setOnAction(e -> {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
			Parent loginView = loader.load();

			contenedorDerecha.getChildren().setAll(loginView);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		});
	}
	
	
	@FXML
	public void abrirRegistro(ActionEvent event) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("registro.fxml"));
			Parent registroView = loader.load();

			contenedorDerecha.getChildren().setAll(registroView);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
