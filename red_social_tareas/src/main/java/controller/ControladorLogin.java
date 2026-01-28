package controller;

import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.util.Duration;

public class ControladorLogin {

    @FXML
    private StackPane paneLogin;

    @FXML
    private StackPane paneRegistro;

    @FXML
    private void initialize() {
        // Estado inicial: mostrar login sin animación
        paneLogin.setOpacity(1);
        paneLogin.setVisible(true);
        paneLogin.setManaged(true);

        paneRegistro.setOpacity(0);
        paneRegistro.setVisible(false);
        paneRegistro.setManaged(false);
    }

    @FXML
    private void mostrarLogin() {
        animarCambio(paneLogin, paneRegistro);
    }

    @FXML
    private void mostrarRegistro() {
        animarCambio(paneRegistro, paneLogin);
    }

    /**
     * Aplica una animación de fade al cambiar de un panel a otro.
     *
     * @param panelMostrar panel que se quiere mostrar
     * @param panelOcultar panel que se quiere ocultar
     */
    private void animarCambio(StackPane panelMostrar, StackPane panelOcultar) {
        // Si ya está mostrando ese panel, no hacemos nada
        if (panelMostrar.isVisible()) return;

        // Aseguramos estado inicial del que se muestra
        panelMostrar.setVisible(true);
        panelMostrar.setManaged(true);
        panelMostrar.setOpacity(0); // empieza transparente

        // Fade OUT del que se oculta
        FadeTransition fadeOut = new FadeTransition(Duration.millis(250), panelOcultar);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);

        // Fade IN del que se muestra
        FadeTransition fadeIn = new FadeTransition(Duration.millis(250), panelMostrar);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);

        // Cuando termine el fadeOut, ocultamos del layout el panelOcultar
        fadeOut.setOnFinished(e -> {
            panelOcultar.setVisible(false);
            panelOcultar.setManaged(false);
        });

        // Ejecutar ambos a la vez
        ParallelTransition transicion = new ParallelTransition(fadeOut, fadeIn);
        transicion.play();
    }
}
