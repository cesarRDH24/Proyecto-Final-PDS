package com.mycompany.polloshermanos.controllers;

import com.mycompany.polloshermanos.objects.SesionUsuario;
import java.io.IOException;
import java.util.Optional;

import javafx.event.ActionEvent;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;

import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;

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
    private Button btnPedidos;
    
    @FXML
private Button btnAgregarConsumo;

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
    public void abrirModuloPedidos(ActionEvent event) {

        try {

            Parent root = FXMLLoader.load(
                getClass().getResource(
                    "/com/mycompany/polloshermanos/MeseroPedidos.fxml"
                )
            );

            Stage stage =
                (Stage) btnPedidos.getScene().getWindow();

            stage.setScene(new Scene(root));

            stage.setTitle("Gestión de Pedidos");

            stage.show();

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
    
    @FXML
public void abrirAgregarConsumo(ActionEvent event) {

    try {

        Parent root = FXMLLoader.load(
            getClass().getResource(
                "/com/mycompany/polloshermanos/AgregarConsumo.fxml"
            )
        );

        Stage stage =
            (Stage) btnAgregarConsumo
                .getScene()
                .getWindow();

        stage.setScene(new Scene(root));

        stage.setTitle("Agregar consumo");

        stage.show();

    } catch (Exception e) {

        e.printStackTrace();

        Alert alert =
            new Alert(Alert.AlertType.ERROR);

        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(
            "No se pudo abrir el módulo."
        );

        alert.showAndWait();
    }
}

    @FXML
public void salirAlLogin() {

    try {

        // 🔥 CU-21 Paso 2
        Alert confirmacion =
            new Alert(Alert.AlertType.CONFIRMATION);

        confirmacion.setTitle("Cerrar sesión");
        confirmacion.setHeaderText(null);
        confirmacion.setContentText(
            "¿Deseas cerrar sesión?"
        );

        Optional<ButtonType> resultado =
            confirmacion.showAndWait();

        // 🔥 FA-01
        if (resultado.isPresent()
                && resultado.get() == ButtonType.CANCEL) {

            return;
        }

        // 🔥 Paso 6
        SesionUsuario.cerrarSesion();

        // 🔥 Paso 8
        FXMLLoader loader = new FXMLLoader(
            getClass().getResource(
                "/com/mycompany/polloshermanos/Login.fxml"
            )
        );

        Parent root = loader.load();

        Stage stage =
            (Stage) btnTomarPedido
                .getScene()
                .getWindow();

        stage.setScene(new Scene(root));

        stage.setTitle("Login");

        stage.show();

        // 🔥 Paso 9
        Alert exito =
            new Alert(Alert.AlertType.INFORMATION);

        exito.setTitle("Pollos Hermanos");
        exito.setHeaderText(null);
        exito.setContentText(
            "Sesión cerrada correctamente"
        );

        exito.showAndWait();

    } catch (Exception e) {

        // 🔥 EX-01
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
}