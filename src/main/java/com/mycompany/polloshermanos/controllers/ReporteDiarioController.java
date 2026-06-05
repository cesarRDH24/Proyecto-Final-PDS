package com.mycompany.polloshermanos.controllers;

import com.mycompany.polloshermanos.daos.ReporteVentasDAO;
import com.mycompany.polloshermanos.objects.ReporteVentaItem;
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
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import lib.SqlLib;

public class ReporteDiarioController implements Initializable {

    @FXML private DatePicker datePicker;
    @FXML private TableView<ReporteVentaItem> tablaReporte;
    @FXML private TableColumn<ReporteVentaItem, String>  colNombre;
    @FXML private TableColumn<ReporteVentaItem, String>  colCategoria;
    @FXML private TableColumn<ReporteVentaItem, Integer> colVendido;
    @FXML private TableColumn<ReporteVentaItem, Double>  colIngreso;
    @FXML private Button btnGenerar;
    @FXML private Button btnExportar;
    @FXML private Button btnVolver;

    private List<ReporteVentaItem> datosActuales;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colCategoria.setCellValueFactory(new PropertyValueFactory<>("categoria"));
        colVendido.setCellValueFactory(new PropertyValueFactory<>("totalVendido"));
        colIngreso.setCellValueFactory(new PropertyValueFactory<>("totalIngreso"));

        // Fecha de hoy por defecto
        datePicker.setValue(LocalDate.now());

        // Botón exportar deshabilitado hasta que se genere el reporte
        btnExportar.setDisable(true);
    }

    @FXML
    public void generarReporte() {
        LocalDate fecha = datePicker.getValue();

        if (fecha == null) {
            new Alert(Alert.AlertType.WARNING,
                "Selecciona una fecha.").showAndWait();
            return;
        }

        try {
            Connection con = SqlLib.getInstance().getConnection();
            ReporteVentasDAO dao = new ReporteVentasDAO(con);
            datosActuales = dao.obtenerReporteDiario(fecha);
            tablaReporte.setItems(FXCollections.observableArrayList(datosActuales));
            btnExportar.setDisable(datosActuales.isEmpty());

            if (datosActuales.isEmpty()) {
                new Alert(Alert.AlertType.INFORMATION,
                    "No hay ventas registradas para esa fecha.").showAndWait();
            }

        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,
                "Error al generar el reporte.").showAndWait();
        }
    }

    @FXML
    public void exportarReporte() {
        if (datosActuales == null || datosActuales.isEmpty()) return;

        LocalDate fecha = datePicker.getValue();
        String fechaFormato = fecha.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        String nombreArchivo = "ReporteDiario_" + fechaFormato + ".txt";

        try (FileWriter fw = new FileWriter(nombreArchivo)) {

            fw.write("===================================\n");
            fw.write("  REPORTE DIARIO - " + fechaFormato + "\n");
            fw.write("===================================\n");
            fw.write(String.format("%-25s %-12s %-10s %-10s%n",
                "Producto", "Categoría", "Vendidos", "Total"));
            fw.write("-----------------------------------\n");

            double totalDia = 0;
            for (ReporteVentaItem item : datosActuales) {
                fw.write(String.format("%-25s %-12s %-10d $%-10.2f%n",
                    item.getNombre(),
                    item.getCategoria(),
                    item.getTotalVendido(),
                    item.getTotalIngreso()));
                totalDia += item.getTotalIngreso();
            }

            fw.write("-----------------------------------\n");
            fw.write(String.format("TOTAL DEL DÍA: $%.2f%n", totalDia));
            fw.write("===================================\n");

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