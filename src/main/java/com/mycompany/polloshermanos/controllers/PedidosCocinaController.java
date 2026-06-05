package com.mycompany.polloshermanos.controllers;

import com.mycompany.polloshermanos.daos.PedidoDAO;
import com.mycompany.polloshermanos.objects.SesionUsuario;
import lib.SqlLib;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;

import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javafx.util.Duration;

public class PedidosCocinaController implements Initializable {

    @FXML
    private VBox contenedorPedidos;

    private PedidoDAO pedidoDAO;
    private PedidoService pedidoService;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        try {
            SqlLib sql = SqlLib.getInstance();
            Connection con = sql.getConnection();

            pedidoDAO = new PedidoDAO(con);
            
            pedidoService = new PedidoService(null, pedidoDAO);

        } catch (Exception e) {
            e.printStackTrace();
        }

        recargarPedidos();

        Timeline timeline = new Timeline(
            new KeyFrame(Duration.seconds(5), e -> recargarPedidos())
        );

        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    public void recargarPedidos() {

        try {

            contenedorPedidos.getChildren().clear();

            ResultSet rs = pedidoDAO.obtenerPedidosPendientes();

            while (rs.next()) {

                int idPedido = rs.getInt("id_pedido");
                int numeroMesa = rs.getInt("numero_mesa");

                List<String> productos = new ArrayList<>();

                ResultSet detalle = pedidoDAO.obtenerDetallePedido(idPedido);

                while (detalle.next()) {
                    productos.add(detalle.getString("nombre"));
                }

                crearTarjetaPedido(idPedido, numeroMesa, productos);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void crearTarjetaPedido(int idPedido, int mesa, List<String> productos) {

        HBox card = new HBox();
        card.setSpacing(10);
        card.setStyle("-fx-background-color: #2c2c2c; -fx-padding: 10; -fx-background-radius: 10;");

        VBox info = new VBox();
        info.setSpacing(5);

        Label lblMesa = new Label("Mesa " + mesa);
        lblMesa.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");

        info.getChildren().add(lblMesa);

        for (String p : productos) {
            Label lblProd = new Label("• " + p);
            lblProd.setStyle("-fx-text-fill: white;");
            info.getChildren().add(lblProd);
        }

        Button btnListo = new Button("✔ Listo");
        btnListo.setStyle("-fx-background-color: #28a745; -fx-text-fill: white; -fx-font-weight: bold;");

        btnListo.setOnAction(e -> {
            marcarComoListo(idPedido);
            contenedorPedidos.getChildren().remove(card);
        });

        card.getChildren().addAll(info, btnListo);

        contenedorPedidos.getChildren().add(card);
    }

    private void marcarComoListo(int idPedido) {

    try {

        pedidoService.marcarComoListo(idPedido);

        System.out.println("Pedido listo: " + idPedido);

    } catch (Exception e) {

        e.printStackTrace();
    }
}

    @FXML
    public void salirAlLogin() {

        try {

            Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);

            confirmacion.setTitle("Cerrar sesión");
            confirmacion.setHeaderText(null);
            confirmacion.setContentText( "¿Deseas cerrar sesión?");

            Optional<ButtonType> resultado = confirmacion.showAndWait();

            if (resultado.isPresent() && resultado.get() == ButtonType.CANCEL) {
                return;
            }

            SesionUsuario.cerrarSesion();
            FXMLLoader loader = new FXMLLoader( getClass().getResource(  "/com/mycompany/polloshermanos/Login.fxml" ) );
            Parent root = loader.load();

            Stage stage = (Stage) contenedorPedidos .getScene().getWindow();

            stage.setScene(new Scene(root));
            stage.setTitle("Login");
            stage.show();

            Alert exito = new Alert(Alert.AlertType.INFORMATION);

            exito.setTitle("Pollos Hermanos");
            exito.setHeaderText(null);
            exito.setContentText( "Sesión cerrada correctamente");
            exito.showAndWait();

        } catch (Exception e) {

            Alert error =
                new Alert(Alert.AlertType.ERROR);

            error.setTitle("Error");
            error.setHeaderText(null);
            error.setContentText( "Error al cerrar sesión");

            error.showAndWait();
            e.printStackTrace();
        }
    }
}
