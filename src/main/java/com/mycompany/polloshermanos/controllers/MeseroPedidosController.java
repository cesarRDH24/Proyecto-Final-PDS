package com.mycompany.polloshermanos.controllers;

import java.io.IOException;

import javafx.event.ActionEvent;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.scene.control.Button;

import javafx.stage.Stage;

/**
 *
 * @author cesar
 * @date 13/05/2026
 */
public class MeseroPedidosController {

    @FXML
    private Button btnPreparacion;

    @FXML
    private Button btnListos;

    @FXML
    private Button btnCancelar;

    @FXML
    private Button btnRegresar;

    @FXML
    public void abrirPedidosPreparacion(ActionEvent event) {

        try {

            Parent root = FXMLLoader.load(
                getClass().getResource(
                    "/com/mycompany/polloshermanos/PedidosPreparacion.fxml"
                )
            );

            Stage stage =
                (Stage) btnPreparacion.getScene().getWindow();

            stage.setScene(new Scene(root));

            stage.setTitle("Pedidos en Preparación");

            stage.show();

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    @FXML
    public void abrirPedidosListos(ActionEvent event) {

        try {

            Parent root = FXMLLoader.load(
                getClass().getResource(
                    "/com/mycompany/polloshermanos/PedidosListos.fxml"
                )
            );

            Stage stage =
                (Stage) btnCancelar.getScene().getWindow();

            stage.setScene(new Scene(root));

            stage.setTitle("Pedidos Listos");

            stage.show();

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    @FXML
    public void abrirCancelarPedido(ActionEvent event) {

        try {

            Parent root = FXMLLoader.load(
                getClass().getResource(
                    "/com/mycompany/polloshermanos/CancelarPedido.fxml"
                )
            );

            Stage stage =
                (Stage) btnListos.getScene().getWindow();

            stage.setScene(new Scene(root));

            stage.setTitle("Cancelar Pedidos");

            stage.show();

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    @FXML
    public void regresar(ActionEvent event) {

        try {

            Parent root = FXMLLoader.load(
                getClass().getResource(
                    "/com/mycompany/polloshermanos/InicioMesero.fxml"
                )
            );

            Stage stage =
                (Stage) btnRegresar.getScene().getWindow();

            stage.setScene(new Scene(root));

            stage.setTitle("Panel Mesero");

            stage.show();

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}