package com.mycompany.polloshermanos.controllers;

import com.mycompany.polloshermanos.daos.ReservacionDAO;
import com.mycompany.polloshermanos.objects.Reservacion;
import com.mycompany.polloshermanos.objects.SesionUsuario;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.event.ActionEvent;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import javafx.scene.input.MouseEvent;

import javafx.stage.Stage;

import lib.SqlLib;

public class InicioReservacionesController implements Initializable {

    @FXML
    private TableView<Reservacion> tablaReservaciones;

    @FXML
    private TableColumn<Reservacion, String> colCliente;

    @FXML
    private TableColumn<Reservacion, Integer> colMesa;

    @FXML
    private TableColumn<Reservacion, Integer> colCapacidad;

    @FXML
    private TableColumn<Reservacion, String> colFecha;

    @FXML
    private TableColumn<Reservacion, String> colHora;

    @FXML
    private TableColumn<Reservacion, String> colEstado;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        colCliente.setCellValueFactory(
                new PropertyValueFactory<>("nombreCliente"));

        colMesa.setCellValueFactory(
                new PropertyValueFactory<>("numeroMesa"));

        colCapacidad.setCellValueFactory(
                new PropertyValueFactory<>("capacidad"));

        colFecha.setCellValueFactory(
                new PropertyValueFactory<>("fecha"));

        colHora.setCellValueFactory(
                new PropertyValueFactory<>("hora"));

        colEstado.setCellValueFactory(
                new PropertyValueFactory<>("estado"));

        cargarTabla();
    }

    public void cargarTabla() {

    try {

        SqlLib sql = SqlLib.getInstance();

        ReservacionDAO dao =
            new ReservacionDAO(sql.getConnection());

        ObservableList<Reservacion> lista =
                FXCollections.observableArrayList(
                        dao.obtenerReservaciones()
                );

        tablaReservaciones.setItems(lista);

    } catch (Exception e) {
        e.printStackTrace();
    }
}

    @FXML
public void abrirMenu(MouseEvent event) throws IOException {

    if (event.getClickCount() == 2) {

        Reservacion r = tablaReservaciones
                .getSelectionModel()
                .getSelectedItem();

        // 🔥 VALIDACIÓN BÁSICA
        if (r == null) {

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Aviso");
            alert.setHeaderText(null);
            alert.setContentText("No hay reservación seleccionada.");
            alert.showAndWait();

            return;
        }

        // 🔥 ABRIR MENÚ
        FXMLLoader loader = new FXMLLoader(
            getClass().getResource("/com/mycompany/polloshermanos/MenuDigital.fxml")
        );

        Parent root = loader.load();

        MenuDigitalController controller = loader.getController();

        controller.setDatos(
            r.getIdMesa(),
            SesionUsuario.getIdEmpleado()
        );

        Stage stage = (Stage) tablaReservaciones.getScene().getWindow();

        stage.setScene(new Scene(root));
        stage.setTitle("Menú Digital");
        stage.show();
    }
}
    @FXML
    public void salirAlLogin(ActionEvent event) throws IOException {

        Parent root = FXMLLoader.load(
                getClass().getResource("/com/mycompany/polloshermanos/InicioMesero.fxml"));

        Stage stage =
                (Stage) tablaReservaciones.getScene().getWindow();

        stage.setScene(new Scene(root));
        stage.setTitle("Login");
        stage.show();
    }
    
    
}