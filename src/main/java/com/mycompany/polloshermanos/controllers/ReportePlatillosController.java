/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.polloshermanos.controllers;

import com.mycompany.polloshermanos.daos.ReporteDAO;
import com.mycompany.polloshermanos.objects.ReporteProductoItem;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import lib.SqlLib;

/**
 * FXML Controller class
 *
 * @author carvi
 */
public class ReportePlatillosController implements Initializable {

    @FXML private TableView<ReporteProductoItem> tablaReporte;
    @FXML private TableColumn<ReporteProductoItem, String>  colNombre;
    @FXML private TableColumn<ReporteProductoItem, String>  colCategoria;
    @FXML private TableColumn<ReporteProductoItem, Integer> colVendido;
    @FXML private TableColumn<ReporteProductoItem, Integer> colStock;
    @FXML private Button btnVolver;
    @FXML private Button btnReabastecer;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colCategoria.setCellValueFactory(new PropertyValueFactory<>("categoria"));
        colVendido.setCellValueFactory(new PropertyValueFactory<>("totalVendido"));
        colStock.setCellValueFactory(new PropertyValueFactory<>("stockActual"));

        cargarReporte();

        tablaReporte.setRowFactory(tv -> new TableRow<ReporteProductoItem>() {
            @Override
            protected void updateItem(ReporteProductoItem item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setStyle("");
                } else if (item.getStockActual() < 10) {
                    setStyle("-fx-background-color: #ff6b6b;");
                } else {
                    setStyle("");
                }
            }
        });

        btnReabastecer.setDisable(true);
        tablaReporte.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldVal, newVal) -> btnReabastecer.setDisable(newVal == null)
        );
    }
    
    private void cargarReporte() {
        try {
            Connection con = SqlLib.getInstance().getConnection();
            ReporteDAO dao = new ReporteDAO(con);
            List<ReporteProductoItem> lista = dao.obtenerReporte();
            tablaReporte.setItems(FXCollections.observableArrayList(lista));
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("No se pudo cargar el reporte.");
            alert.showAndWait();
        }
    }
    
    @FXML
    public void volverAdmin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass()
                .getResource("/com/mycompany/polloshermanos/InicioAdmin.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) btnVolver.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Inicio Administrador");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    public void reabastecer() {
        ReporteProductoItem seleccionado = tablaReporte.getSelectionModel().getSelectedItem();

        if (seleccionado == null) {
            return;
        }

        TextInputDialog dialogo = new TextInputDialog("10");
        dialogo.setTitle("Reabastecer producto");
        dialogo.setHeaderText("Producto: " + seleccionado.getNombre());
        dialogo.setContentText("¿Cuántas unidades agregar?");

        dialogo.showAndWait().ifPresent(input -> {
            try {
                int cantidad = Integer.parseInt(input.trim());

                if (cantidad <= 0) {
                    new Alert(Alert.AlertType.WARNING,
                            "Ingresa una cantidad mayor a 0.").showAndWait();
                    return;
                }

                Connection con = SqlLib.getInstance().getConnection();
                ReporteDAO dao = new ReporteDAO(con);
                dao.reabastecerProducto(seleccionado.getIdProducto(), cantidad);

                // Recargar tabla
                cargarReporte();

                Alert exito = new Alert(Alert.AlertType.INFORMATION);
                exito.setTitle("Éxito");
                exito.setHeaderText(null);
                exito.setContentText("Stock actualizado correctamente.");
                exito.showAndWait();

            } catch (NumberFormatException e) {
                new Alert(Alert.AlertType.ERROR,
                        "Ingresa un número válido.").showAndWait();
            } catch (Exception e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR,
                        "Error al actualizar el stock.").showAndWait();
            }
        });
    }
}
