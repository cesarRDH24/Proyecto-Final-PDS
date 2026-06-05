/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.polloshermanos.controllers;

import com.mycompany.polloshermanos.daos.AsistenciaDAO;
import com.mycompany.polloshermanos.daos.EmpleadoDAO;
import com.mycompany.polloshermanos.objects.Asistencia;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author carvi
 */
public class AsistenciaController implements Initializable {

    @FXML
    private TextField txtNombre;
    @FXML
    private ComboBox<String> comboRol;
    @FXML
    private DatePicker datePickerFecha;
    @FXML
    private Button btnRegistrar;
    @FXML
    private Button regresoLogin;

    private Connection conn;
    private AsistenciaDAO asistenciaDAO;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            conn = lib.SqlLib.getInstance().getConnection();
            asistenciaDAO = new AsistenciaDAO(conn);

            // Aquí llenamos el combo con roles
            comboRol.getItems().addAll("Administrador", "Mesero", "Chef", "Recepcionista", "Cajero");

        } catch (SQLException e) {
            e.printStackTrace();
            mostrarAlerta(Alert.AlertType.ERROR, "Error de conexión", "No se pudo establecer conexión con la base de datos.");
        }
    }

    @FXML
    private void registrarAsistencia() {
        try {
            String nombre = txtNombre.getText();
            String rol = comboRol.getValue();
            LocalDate fecha = datePickerFecha.getValue();
            LocalTime hora = LocalTime.now();

            // Validación de campos vacíos
            if (nombre == null || nombre.isEmpty() || rol == null || fecha == null) {
                mostrarAlerta(Alert.AlertType.WARNING, "Campos incompletos", "Por favor llena todos los campos.");
                return;
            }

            EmpleadoDAO empleadoDAO = new EmpleadoDAO(conn);
            int idEmpleado = empleadoDAO.obtenerIdPorNombreYRol(nombre, rol);

            if (idEmpleado == -1) {
                mostrarAlerta(Alert.AlertType.ERROR, "Empleado no encontrado", "No existe un empleado con ese nombre y rol.");
                return;
            }

            if (asistenciaDAO.existeAsistencia(idEmpleado, fecha)) {
                mostrarAlerta(Alert.AlertType.WARNING, "Registro duplicado", "La asistencia ya fue registrada para este empleado en la fecha seleccionada.");
                return;
            }

            Asistencia asistencia = new Asistencia(idEmpleado, fecha, hora);
            asistenciaDAO.insertarAsistencia(asistencia);

            mostrarAlerta(Alert.AlertType.INFORMATION, "Registro exitoso", "La asistencia fue registrada correctamente.");

            txtNombre.clear();
            comboRol.setValue(null);
            datePickerFecha.setValue(null);

        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta(Alert.AlertType.ERROR, "Error inesperado", "Ocurrió un problema al registrar la asistencia.");
        }
    }

    // Método auxiliar para mostrar alertas
    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    @FXML
    public void backLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mycompany/polloshermanos/login.fxml"));
            Parent root = loader.load();

            // Usamos el botón regresoLogin para obtener la ventana actual
            Stage stage = (Stage) regresoLogin.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Los Pollos Hermanos - Login");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error al cargar");
            alert.setHeaderText(null);
            alert.setContentText("No se pudo regresar al login.");
            alert.showAndWait();
        }
    }

}
