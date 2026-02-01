package main;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			/*FXMLLoader loader = new FXMLLoader(getClass().getResource("/escenas/principal/VentanaPantallaPrincipal.fxml"));
			Parent root = loader.load();
			Scene scene = new Scene(root);
			//scene.getStylesheets().add(getClass().getResource("applicationLogin.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();*/
			
		    Parent root = FXMLLoader.load(getClass().getResource("/escenas/main/ContenedorVentanas.fxml"));
		    Scene scene = new Scene(root);
		    primaryStage.setScene(scene);
		    primaryStage.show();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}


