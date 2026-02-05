package controller.sceneNavigator;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;

public class NavegadorVentanas {

	private static StackPane container;

	public static void setContainer(StackPane c) {
		container = c;
	}


	public static void navegar(String fxmlPath) {
	    if (container == null) {
	        throw new IllegalStateException("container es null (falta setContainer)");
	    }

	    try {
	        Parent view = FXMLLoader.load(NavegadorVentanas.class.getResource(fxmlPath));

	        if (view instanceof Region r) {
	            r.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
	            r.prefWidthProperty().bind(container.widthProperty());
	            r.prefHeightProperty().bind(container.heightProperty());
	        }

	        container.getChildren().setAll(view);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
}
