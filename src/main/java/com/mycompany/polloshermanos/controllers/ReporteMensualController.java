package com.mycompany.polloshermanos.controllers;

import com.mycompany.polloshermanos.daos.ReporteVentasDAO;
import com.mycompany.polloshermanos.objects.ReporteInventarioItem;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import lib.SqlLib;

public class ReporteMensualController implements Initializable {

    @FXML private TableView<ReporteInventarioItem> tablaReporte;
    @FXML private TableColumn<ReporteInventarioItem, String> colNombre;
    @FXML private TableColumn<ReporteInventarioItem, String> colUnidad;
    @FXML private TableColumn<ReporteInventarioItem, Double> colStock;
    @FXML private TableColumn<ReporteInventarioItem, Double> colMinimo;
    @FXML private TableColumn<ReporteInventarioItem, String> colEstado;
    @FXML private Button btnGenerar;
    @FXML private Button btnExportar;
    @FXML private Button btnVolver;

    private List<ReporteInventarioItem> datosActuales;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colUnidad.setCellValueFactory(new PropertyValueFactory<>("unidad"));
        colStock.setCellValueFactory(new PropertyValueFactory<>("stockActual"));
        colMinimo.setCellValueFactory(new PropertyValueFactory<>("minimo"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));

        btnExportar.setDisable(true);

        // Colorear filas críticas en rojo
        tablaReporte.setRowFactory(tv -> new TableRow<ReporteInventarioItem>() {
            @Override
            protected void updateItem(ReporteInventarioItem item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setStyle("");
                } else if ("CRÍTICO".equals(item.getEstado())) {
                    setStyle("-fx-background-color: #ff6b6b;");
                } else {
                    setStyle("");
                }
            }
        });
    }

    @FXML
    public void generarReporte() {
        try {
            Connection con = SqlLib.getInstance().getConnection();
            ReporteVentasDAO dao = new ReporteVentasDAO(con);
            datosActuales = dao.obtenerReporteInventario();
            tablaReporte.setItems(FXCollections.observableArrayList(datosActuales));
            btnExportar.setDisable(datosActuales.isEmpty());

        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,
                "Error al generar el reporte.").showAndWait();
        }
    }

    @FXML
    public void exportarReporte() {
        if (datosActuales == null || datosActuales.isEmpty()) return;

        String fecha = LocalDate.now().format(DateTimeFormatter.ofPattern("MM-yyyy"));
        String nombreArchivo = "ReporteMensual_MateriaPrima_" + fecha + ".txt";

        try (FileWriter fw = new FileWriter(nombreArchivo)) {

            fw.write("=========================================\n");
            fw.write("  REPORTE MENSUAL DE MATERIA PRIMA\n");
            fw.write("  Generado: " + LocalDate.now()
                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + "\n");
            fw.write("=========================================\n");
            fw.write(String.format("%-20s %-10s %-12s %-10s %-10s%n",
                "Ingrediente", "Unidad", "Stock actual", "Mínimo", "Estado"));
            fw.write("-----------------------------------------\n");

            for (ReporteInventarioItem item : datosActuales) {
                fw.write(String.format("%-20s %-10s %-12.2f %-10.2f %-10s%n",
                    item.getNombre(),
                    item.getUnidad(),
                    item.getStockActual(),
                    item.getMinimo(),
                    item.getEstado()));
            }

            fw.write("=========================================\n");

            Alert exito = new Alert(Alert.AlertType.INFORMATION);
            exito.setTitle("Reporte exportado");
            exito.setHeaderText(null);
            exito.setContentText("Archivo guardado como:\n" + nombreArchivo);
            exito.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,
                "Error al exportar el reporte.").showAndWait();
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
}