/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.polloshermanos.controllers;

import com.mycompany.polloshermanos.daos.AsistenciaDAO;
import com.mycompany.polloshermanos.objects.Asistencia;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author carvi
 */
public class VistaAsistenciaController implements Initializable {

    @FXML
    private TableView<Asistencia> tablaAsistencias;
    @FXML
    private TableColumn<Asistencia, String> colNombre;
    @FXML
    private TableColumn<Asistencia, LocalDate> colFecha;
    @FXML
    private TableColumn<Asistencia, LocalTime> colHora;
    @FXML
    private Button btnRegresarGerente;

    private Connection conn;
    private AsistenciaDAO asistenciaDAO;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            conn = lib.SqlLib.getInstance().getConnection();
            asistenciaDAO = new AsistenciaDAO(conn);

            // Configurar columnas
            colNombre.setCellValueFactory(new PropertyValueFactory<>("nombreEmpleado"));
            colFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
            colHora.setCellValueFactory(new PropertyValueFactory<>("horaEntrada"));

            // Cargar datos
            ObservableList<Asistencia> lista = asistenciaDAO.obtenerTodasAsistencias();
            tablaAsistencias.setItems(lista);

        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error de conexión");
            alert.setHeaderText(null);
            alert.setContentText("No se pudo cargar la asistencia.");
            alert.showAndWait();
        }
    }

    @FXML
    public void backInicioGerente() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mycompany/polloshermanos/inicioGerente.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) btnRegresarGerente.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Los Pollos Hermanos - Inicio Gerente");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error al cargar");
            alert.setHeaderText(null);
            alert.setContentText("No se pudo regresar al inicio del gerente.");
            alert.showAndWait();
        }
    }

}
