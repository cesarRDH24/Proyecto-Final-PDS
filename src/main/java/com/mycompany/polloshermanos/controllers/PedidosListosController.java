package com.mycompany.polloshermanos.controllers;

import com.mycompany.polloshermanos.daos.PedidoDAO;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.scene.control.Button;
import javafx.scene.control.Label;

import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import javafx.stage.Stage;

import lib.SqlLib;

public class PedidosListosController implements Initializable {

    @FXML
    private VBox contenedorPedidos;

    private PedidoDAO pedidoDAO;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        try {

            SqlLib sql = SqlLib.getInstance();

            Connection con = sql.getConnection();

            pedidoDAO = new PedidoDAO(con);

            cargarPedidosListos();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void cargarPedidosListos() {

        try {

            contenedorPedidos.getChildren().clear();

            ResultSet rs = pedidoDAO.obtenerPedidosListos();

            while (rs.next()) {

                int idPedido = rs.getInt("id_pedido");
                int numeroMesa = rs.getInt("numero_mesa");

                crearTarjeta(idPedido, numeroMesa);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void crearTarjeta(int idPedido, int mesa) {

    HBox card = new HBox();

    card.setSpacing(15);

    card.setStyle(
        "-fx-background-color: #28a745;" +
        "-fx-padding: 15;" +
        "-fx-background-radius: 10;"
    );

    Label lblPedido = new Label(
        "Pedido #" + idPedido + " - Mesa " + mesa
    );

    lblPedido.setStyle(
        "-fx-text-fill: white;" +
        "-fx-font-size: 18px;" +
        "-fx-font-weight: bold;"
    );

    Button btnEntregar = new Button("Entregar");

    btnEntregar.setStyle(
        "-fx-background-color: white;" +
        "-fx-text-fill: #28a745;" +
        "-fx-font-weight: bold;" +
        "-fx-background-radius: 8;"
    );

    btnEntregar.setOnAction(e -> {

        try {

            pedidoDAO.marcarComoEntregado(idPedido);

            contenedorPedidos.getChildren().remove(card);

            System.out.println(
                "Pedido entregado: " + idPedido
            );

        } catch (Exception ex) {

            ex.printStackTrace();
        }
    });

    card.getChildren().addAll(lblPedido, btnEntregar);

    contenedorPedidos.getChildren().add(card);
}

    @FXML
    public void regresar() {

        try {

            FXMLLoader loader = new FXMLLoader(
                getClass().getResource(
                    "/com/mycompany/polloshermanos/MeseroPedidos.fxml"
                )
            );

            Parent root = loader.load();

            Stage stage =
                (Stage) contenedorPedidos.getScene().getWindow();

            stage.setScene(new Scene(root));

            stage.setTitle("Panel Mesero");

            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}