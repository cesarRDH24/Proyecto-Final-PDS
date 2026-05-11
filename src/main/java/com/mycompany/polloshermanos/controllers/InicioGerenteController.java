/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
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
    public void volverInicio() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mycompany/polloshermanos/login.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) volverPantallaInicio.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Los Pollos Hermanos - Inicio");
            stage.show();

        } catch (IOException e) {
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

    
}