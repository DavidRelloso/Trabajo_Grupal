package main;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;

public class ControladorTareasMain {

	Button botonCerrarSesion;
	MenuItem diarioAbrir, amigosAbrir, ajustesAbrir;
	
	
	public void abrirAmigos() throws IOException {
		// Load the FXML file for the second scene
	    FXMLLoader loader = new FXMLLoader(getClass().getResource("../escenas/menuAmigos.fxml"));
	    // Create a new stage for the second scene
	    Stage stage = new Stage();
	    stage.setScene(new Scene(loader.load()));
	    // Set the title for the second scene
	    stage.setTitle("Menú de amigos");
	    // Show the second scene
	    stage.show();
	}
}
