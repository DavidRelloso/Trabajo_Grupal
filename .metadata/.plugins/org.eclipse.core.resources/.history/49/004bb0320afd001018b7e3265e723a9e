package main;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/escenas/VentanaDiarioPersonal.fxml"));
			Parent root = loader.load();
			Scene scene = new Scene(root,1300,700);
			//scene.getStylesheets().add(getClass().getResource("applicationPrincipal.css").toExternalForm());
			//scene.getStylesheets().add(getClass().getResource("applicationLogin.css").toExternalForm());
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
