package controllerVentana;

import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

public class VentanaPersonalController {

//DIAS
	@FXML
	private ScrollPane scrollDias;

	@FXML
	private HBox boxDias;

	@FXML
	private Button btnNext;

	@FXML
	private Button btnPrev;

// CANVAS LATERAL
	@FXML
	private StackPane offcanvas;

	@FXML
	private Button openOffcanvas;

	@FXML
	private Button closeOffcanvas;

	private boolean menuAbierto = false;


	@FXML
	public void initialize() {

		// forzar layout antes de medir
		boxDias.applyCss();
		boxDias.layout();

		// scrollPane horizontal buttons
		if (btnNext != null)
			btnNext.setOnAction(e -> next());
		if (btnPrev != null)
			btnPrev.setOnAction(e -> prev());

		// abrir menú (hamburguesa)
		if (openOffcanvas != null) {
			openOffcanvas.setOnAction(e -> toggleMenu());
		}

		// cerrar menú (botón cerrar)
		if (closeOffcanvas != null) {
			closeOffcanvas.setOnAction(e -> toggleMenu());
		}
	}

	// scroll días

	private void next() {
		// 4 columnas visibles → 1 día = 0.25
		scrollDias.setHvalue(Math.min(1.0, scrollDias.getHvalue() + 0.25));
	}

	private void prev() {
		scrollDias.setHvalue(Math.max(0.0, scrollDias.getHvalue() - 0.25));
	}

	// offcanvas menu

	private void toggleMenu() {

		TranslateTransition transition = new TranslateTransition(Duration.millis(250), offcanvas);

		if (menuAbierto) {
			// cerrar (se va a la derecha)
			transition.setFromX(0);
			transition.setToX(300);
			menuAbierto = false;
		} else {
			// abrir (entra desde la derecha)
			transition.setFromX(300);
			transition.setToX(0);
			menuAbierto = true;
		}

		transition.play();
	}
}
