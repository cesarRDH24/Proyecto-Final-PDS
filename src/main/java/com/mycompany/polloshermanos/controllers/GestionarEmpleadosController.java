/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.polloshermanos.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import com.mycompany.polloshermanos.daos.EmpleadoDAO;
import com.mycompany.polloshermanos.objects.Empleado;
import java.io.IOException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

/**
 * FXML Controller class
 *
 * @author carvi
 */
public class GestionarEmpleadosController {
    
    @FXML private TableView<Empleado> tablaEmpleados;
    @FXML private TableColumn<Empleado, Integer> colId;
    @FXML private TableColumn<Empleado, String> colNombre;
    @FXML private TableColumn<Empleado, String> colTelefono;
    @FXML private TableColumn<Empleado, String> colCorreo;
    @FXML private TableColumn<Empleado, String> colUsuario;
    @FXML private TableColumn<Empleado, String> colRol;
    @FXML private TableColumn<Empleado, String> colFechaIngreso;
    
    @FXML private TextField txtNombre;
    @FXML private TextField txtTelefono;
    @FXML private TextField txtCorreo;
    @FXML private TextField txtUsuario;
    @FXML private PasswordField txtContrasena;
    @FXML private ComboBox<String> comboRol;
    @FXML private DatePicker dateIngreso;

    @FXML private Button agregarEmpleado;
    @FXML private Button editarEmpleado;
    @FXML private Button eliminarEmpleado;
    @FXML private Button regresarMenuGerente;
    
    private ObservableList<Empleado> listaEmpleados;
    private EmpleadoDAO empleadoDAO;
    
    @FXML
    public void initialize() {
        try {
            // Usar tu SqlLib para obtener la conexión compartida
            Connection con = lib.SqlLib.getInstance().getConnection();
            empleadoDAO = new EmpleadoDAO(con);

            configurarColumnas();
            cargarEmpleados();

            // Roles fijos en ComboBox
            comboRol.setItems(FXCollections.observableArrayList(
                    "Gerente", "Recepcionista", "Mesero", "Cajero", "Chef", "Administrador"
            ));

            // Cuando seleccionas un empleado en la tabla, llenar los campos
            tablaEmpleados.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
                if (newSel != null) {
                    txtNombre.setText(newSel.getNombre());
                    txtTelefono.setText(newSel.getTelefono());
                    txtCorreo.setText(newSel.getCorreo());
                    txtUsuario.setText(newSel.getUsuario());
                    txtContrasena.setText(newSel.getContrasena());
                    comboRol.setValue(newSel.getRol());
                    dateIngreso.setValue(java.time.LocalDate.parse(newSel.getFechaIngreso()));
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error al inicializar GestionarEmpleadosController: " + e.getMessage());
        }
    }
    
    private void configurarColumnas() {
        colId.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getIdEmpleado()).asObject());
        colNombre.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getNombre()));
        colTelefono.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getTelefono()));
        colCorreo.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getCorreo()));
        colUsuario.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getUsuario()));
        colRol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getRol()));
        colFechaIngreso.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getFechaIngreso()));
    }


    private void cargarEmpleados() {
        listaEmpleados = FXCollections.observableArrayList(empleadoDAO.obtenerTodos());
        tablaEmpleados.setItems(listaEmpleados);
    }

    private boolean telefonoValido(String telefono) {
        // Solo números y exactamente 10 dígitos
        return telefono.matches("\\d{10}");
    }

    private boolean telefonoUnico(String telefono, Integer idActual) {
        for (Empleado e : listaEmpleados) {
            if (e.getTelefono().equals(telefono)) {
                // Si estamos editando, permitir el mismo teléfono del empleado actual
                if (idActual == null || e.getIdEmpleado() != idActual) {
                    return false;
                }
            }
        }
        return true;
    }

    @FXML
    private void agregarEmpleado() {
        try {
            if (txtNombre.getText().isEmpty() || txtTelefono.getText().isEmpty()
                    || txtCorreo.getText().isEmpty() || txtUsuario.getText().isEmpty()
                    || txtContrasena.getText().isEmpty() || comboRol.getValue() == null
                    || dateIngreso.getValue() == null) {
                mostrarAlerta("Campos incompletos", "Debes llenar todos los campos.");
                return;
            }

            if (!telefonoValido(txtTelefono.getText())) {
                mostrarAlerta("Teléfono inválido", "El número debe tener exactamente 10 dígitos.");
                return;
            }

            if (!telefonoUnico(txtTelefono.getText(), null)) {
                mostrarAlerta("Teléfono duplicado", "Ya existe un empleado con ese número.");
                return;
            }

            Empleado emp = new Empleado();
            emp.setNombre(txtNombre.getText());
            emp.setTelefono(txtTelefono.getText());
            emp.setCorreo(txtCorreo.getText());
            emp.setUsuario(txtUsuario.getText());
            emp.setContrasena(txtContrasena.getText());
            emp.setRol(comboRol.getValue());
            emp.setIdRol(comboRol.getSelectionModel().getSelectedIndex() + 1);
            emp.setFechaIngreso(dateIngreso.getValue().toString());

            empleadoDAO.insertar(emp);
            cargarEmpleados();
            mostrarAlerta("Éxito", "Empleado agregado correctamente.");

        } catch (Exception e) {
            mostrarAlerta("Error", "Ocurrió un error al agregar: " + e.getMessage());
        }
    }

    @FXML
    private void editarEmpleado() {
        try {
            Empleado emp = tablaEmpleados.getSelectionModel().getSelectedItem();
            if (emp == null) {
                mostrarAlerta("Selección requerida", "Debes seleccionar un empleado para editar.");
                return;
            }

            if (!telefonoValido(txtTelefono.getText())) {
                mostrarAlerta("Teléfono inválido", "El número debe tener exactamente 10 dígitos.");
                return;
            }

            if (!telefonoUnico(txtTelefono.getText(), emp.getIdEmpleado())) {
                mostrarAlerta("Teléfono duplicado", "Ya existe otro empleado con ese número.");
                return;
            }

            emp.setNombre(txtNombre.getText());
            emp.setTelefono(txtTelefono.getText());
            emp.setCorreo(txtCorreo.getText());
            emp.setUsuario(txtUsuario.getText());
            emp.setContrasena(txtContrasena.getText());
            emp.setRol(comboRol.getValue());
            emp.setIdRol(comboRol.getSelectionModel().getSelectedIndex() + 1);
            emp.setFechaIngreso(dateIngreso.getValue().toString());

            empleadoDAO.actualizar(emp);
            cargarEmpleados();
            mostrarAlerta("Éxito", "Empleado editado correctamente.");

        } catch (Exception e) {
            mostrarAlerta("Error", "Ocurrió un error al editar: " + e.getMessage());
        }
    }
    
    @FXML
    private void eliminarEmpleado() {
        try {
            Empleado emp = tablaEmpleados.getSelectionModel().getSelectedItem();
            if (emp == null) {
                mostrarAlerta("Selección requerida",
                        "Debes seleccionar un empleado para eliminar.");
                return;
            }

            empleadoDAO.eliminar(emp.getIdEmpleado());
            cargarEmpleados();
            mostrarAlerta("Éxito", "Empleado eliminado correctamente.");

        } catch (Exception e) {
            mostrarAlerta("Error", "Ocurrió un error al eliminar: " + e.getMessage());
        }
    }
    
    // Método auxiliar para mostrar alertas
    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    @FXML
    private void regresarMenuGerente() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mycompany/polloshermanos/inicioGerente.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) regresarMenuGerente.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Los Pollos Hermanos - Inicio Gerente");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
    
}