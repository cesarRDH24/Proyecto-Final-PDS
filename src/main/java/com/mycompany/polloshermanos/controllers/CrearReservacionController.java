/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.polloshermanos.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;


// JavaFX
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

// Java
import java.util.UUID;

// Tu modelo
import com.mycompany.polloshermanos.objects.Mesa;
/**
 * FXML Controller class
 *
 * @author ninoa
 */
public class CrearReservacionController {

    @FXML private TextField txtCliente;
    @FXML private DatePicker datePicker;
    @FXML private TextField txtHora;

    private Mesa mesa;

    public void setMesa(Mesa mesa) {
        this.mesa = mesa;
    }

    @FXML
    private void crearReservacion() {

        if (txtCliente.getText().isEmpty() || datePicker.getValue() == null || txtHora.getText().isEmpty()) {
            mostrarError("Completa todos los campos");
            return;
        }

        try {
            String nombreCliente = (txtCliente.getText());

            String fecha = datePicker.getValue().toString();
            String hora = txtHora.getText();

            String codigo = UUID.randomUUID().toString().substring(0,8);

            // INSERT A BD
            System.out.println("Reservación creada:");
            System.out.println("Mesa: " + mesa.getIdMesa());

            cerrarVentana();

        } catch (NumberFormatException e) {
            mostrarError("ID Cliente inválido");
        }
    }

    private void cerrarVentana() {
        Stage stage = (Stage) txtCliente.getScene().getWindow();
        stage.close();
    }

    private void mostrarError(String msg) {
        Alert a = new Alert(Alert.AlertType.ERROR, msg);
        a.show();
    }
}