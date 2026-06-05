package com.mycompany.polloshermanos.controllers;

import com.mycompany.polloshermanos.objects.SesionUsuario;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author carvi
 */
public class InicioGerenteController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }   
    
    @FXML
    private Button volverPantallaInicio;
    
    @FXML
    private Button btnGestionar;
    
    @FXML
    private Button btnVerAsistencia;

    @FXML
public void volverInicio() {

    try {

        // 🔥 CU-21 Paso 2:
        // El sistema solicita confirmación para cerrar sesión
        Alert confirmacion =
            new Alert(Alert.AlertType.CONFIRMATION);

        confirmacion.setTitle("Cerrar sesión");
        confirmacion.setHeaderText(null);
        confirmacion.setContentText(
            "¿Deseas cerrar sesión?"
        );

        Optional<ButtonType> resultado =
            confirmacion.showAndWait();

        // 🔥 FA-01:
        // El usuario cancela el cierre de sesión
        if (resultado.isPresent()
                && resultado.get() == ButtonType.CANCEL) {

            return;
        }

        // 🔥 Paso 6:
        // El sistema elimina la información temporal
        // de la sesión del usuario
        SesionUsuario.cerrarSesion();

        // 🔥 Paso 8:
        // Redirigir a pantalla de login
        FXMLLoader loader = new FXMLLoader(
            getClass().getResource(
                "/com/mycompany/polloshermanos/login.fxml"
            )
        );

        Parent root = loader.load();

        Stage stage =
            (Stage) volverPantallaInicio
                .getScene()
                .getWindow();

        stage.setScene(new Scene(root));
        stage.setTitle("Los Pollos Hermanos - Inicio");
        stage.show();

        // 🔥 Paso 9:
        // Mostrar mensaje de éxito
        Alert exito =
            new Alert(Alert.AlertType.INFORMATION);

        exito.setTitle("Pollos Hermanos");
        exito.setHeaderText(null);
        exito.setContentText(
            "Sesión cerrada correctamente"
        );

        exito.showAndWait();

    } catch (Exception e) {

        // 🔥 EX-01:
        // Error al cerrar sesión
        Alert error =
            new Alert(Alert.AlertType.ERROR);

        error.setTitle("Error");
        error.setHeaderText(null);
        error.setContentText(
            "Error al cerrar sesión"
        );

        error.showAndWait();

        e.printStackTrace();
    }
}
    
    @FXML
    public void abrirGestionEmpleados() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mycompany/polloshermanos/gestionarEmpleados.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) btnGestionar.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Los Pollos Hermanos - Gestión de Empleados");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    public void abrirAsistencia() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mycompany/polloshermanos/VistaAsistencia.fxml"));
            Parent root = loader.load();

            // Obtener la ventana actual desde el botón que dispara la acción
            Stage stage = (Stage) btnVerAsistencia.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Los Pollos Hermanos - Registro de Asistencia");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error al cargar");
            alert.setHeaderText(null);
            alert.setContentText("No se pudo abrir la pantalla de asistencia.");
            alert.showAndWait();
        }
    }

    
}