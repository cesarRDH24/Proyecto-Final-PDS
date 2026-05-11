
package com.mycompany.polloshermanos.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author carvi
 */
public class InicioAdminController implements Initializable {

    /**
     * Initializes the controller class.
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
    public void volverLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mycompany/polloshermanos/login.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) btnVolver.getScene().getWindow(); // obtener la ventana actual
            stage.setScene(new Scene(root));
            stage.setTitle("Los Pollos Hermanos - Login");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    public void administrarMenu() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mycompany/polloshermanos/actualizarMenu.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Actualizar Menú");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
}
    
}