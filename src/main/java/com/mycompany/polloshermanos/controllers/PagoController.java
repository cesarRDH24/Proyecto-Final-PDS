/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.polloshermanos.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;

/**
 * FXML Controller class
 *
 * @author ninoa
 */


import javafx.fxml.FXML;
import javafx.scene.control.*;

public class PagoController {

    @FXML
    private TextField txtMonto;

    @FXML
    private ComboBox<String> cbMetodo;

    @FXML
    private Label lblMensaje;

    @FXML
    public void initialize() {
        // Llenar métodos de pago
        cbMetodo.getItems().addAll("Efectivo", "Tarjeta", "Transferencia");
    }

    @FXML
    private void registrarPago() {
        try {
            String montoTexto = txtMonto.getText();
            String metodo = cbMetodo.getValue();

            // 🔎 Validación básica
            if (montoTexto.isEmpty() || metodo == null) {
                lblMensaje.setText("Datos incompletos");
                return;
            }

            double monto = Double.parseDouble(montoTexto);

            if (monto <= 0) {
                lblMensaje.setText("Monto inválido");
                return;
            }

            // 💳 Simulación de rechazo de tarjeta
            if (metodo.equals("Tarjeta") && monto > 20000) {
                lblMensaje.setText("Pago rechazado por el banco");
                return;
            }

            // ✅ Pago exitoso
            lblMensaje.setText("Pago registrado correctamente");

        } catch (NumberFormatException e) {
            lblMensaje.setText("Monto debe ser numérico");
        } catch (Exception e) {
            lblMensaje.setText("Error al registrar el pago");
        }
    }
}
