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
 * @date 10/05/2026
 */
public class InicioMeseroController {

    @FXML
    private Button btnTomarPedido;

    @FXML
    private Button btnPedidosPreparacion;

    @FXML
    private Button btnPedidosListos;

    @FXML
    public void abrirTomarPedido(ActionEvent event) throws IOException {

        Parent root = FXMLLoader.load(
            getClass().getResource(
                "/com/mycompany/polloshermanos/InicioReservaciones.fxml"
            )
        );

        Stage stage =
            (Stage) btnTomarPedido.getScene().getWindow();

        stage.setScene(new Scene(root));
        stage.setTitle("Reservaciones");
        stage.show();
    }

    @FXML
    public void abrirPedidosPreparacion(ActionEvent event) {

        System.out.println("Abrir pedidos en preparación");
    }

    @FXML
    public void abrirPedidosListos(ActionEvent event) {

        System.out.println("Abrir pedidos listos");
    }
    
    @FXML
public void salirAlLogin() {

    try {

        FXMLLoader loader = new FXMLLoader(
            getClass().getResource("/com/mycompany/polloshermanos/Login.fxml")
        );

        Parent root = loader.load();

        Stage stage = (Stage) btnTomarPedido.getScene().getWindow();

        stage.setScene(new Scene(root));
        stage.setTitle("Login");
        stage.show();

    } catch (Exception e) {
        e.printStackTrace();
    }
}
}