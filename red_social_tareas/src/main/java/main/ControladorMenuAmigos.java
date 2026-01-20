package main;
import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class ControladorMenuAmigos {

	public class MenuAmigos {

		Button agregarAmigoBtn, peticonesAmistadBtn, misAmigosBtn;
		Stage stage;
		
		public void abrirMenuAgregarAmigos() throws IOException {
		        FXMLLoader loader = new FXMLLoader(getClass().getResource("/escenas/menuAmigos.fxml"));
		        Scene scene = new Scene(loader.load());
		        stage.setScene(scene);
		        stage.show();
		    }
		}
	}

