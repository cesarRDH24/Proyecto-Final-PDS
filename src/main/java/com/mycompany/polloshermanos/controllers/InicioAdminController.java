package com.mycompany.polloshermanos.controllers;

import com.mycompany.polloshermanos.objects.SesionUsuario;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
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
public class InicioAdminController implements Initializable {

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private Button actualizarMenu;

    @FXML
    private Button btnVolver;
    
    @FXML
    private Button btnReporte;
    
    @FXML
    private Button btnReporteDiario;
    
    @FXML
    private Button btnReporteMensual;

    @FXML
    public void volverLogin() {

        try {

            Alert confirmacion
                    = new Alert(Alert.AlertType.CONFIRMATION);

            confirmacion.setTitle("Cerrar sesión");
            confirmacion.setHeaderText(null);
            confirmacion.setContentText(
                    "¿Deseas cerrar sesión?"
            );

            Optional<ButtonType> resultado
                    = confirmacion.showAndWait();

            if (resultado.isPresent()
                    && resultado.get() == ButtonType.CANCEL) {

                return;
            }

            SesionUsuario.cerrarSesion();

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource(
                            "/com/mycompany/polloshermanos/login.fxml"
                    )
            );

            Parent root = loader.load();

            Stage stage
                    = (Stage) btnVolver
                            .getScene()
                            .getWindow();

            stage.setScene(new Scene(root));
            stage.setTitle("Los Pollos Hermanos - Login");
            stage.show();

            Alert exito
                    = new Alert(Alert.AlertType.INFORMATION);

            exito.setTitle("Pollos Hermanos");
            exito.setHeaderText(null);
            exito.setContentText(
                    "Sesión cerrada correctamente"
            );

            exito.showAndWait();

        } catch (Exception e) {

            Alert error
                    = new Alert(Alert.AlertType.ERROR);

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
    public void administrarMenu(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mycompany/polloshermanos/actualizarMenu.fxml"));
            Parent root = loader.load();

            // Reutilizar la ventana actual en lugar de crear una nueva
            Stage stage = (Stage) actualizarMenu.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Actualizar Menú");
            // No uses stage.show() porque ya está visible

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    public void abrirReporte(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass()
                    .getResource("/com/mycompany/polloshermanos/reportePlatillos.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) btnReporte.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Reporte de Platillos y Bebidas");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    public void abrirReporteDiario(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass()
                    .getResource("/com/mycompany/polloshermanos/reporteDiario.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) btnReporteDiario.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Reporte Diario de Ventas");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    public void abrirReporteMensual(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass()
                    .getResource("/com/mycompany/polloshermanos/reporteMensual.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) btnReporteMensual.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Reporte Mensual de Ventas");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
