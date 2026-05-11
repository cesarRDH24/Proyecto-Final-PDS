package com.mycompany.polloshermanos.controllers;

import com.mycompany.polloshermanos.daos.PedidoDAO;
import com.mycompany.polloshermanos.daos.ProductoDAO;
import com.mycompany.polloshermanos.objects.Producto;
import java.io.IOException;
import java.net.URL;
import java.util.List;
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
import javafx.stage.Stage;
import lib.SqlLib;

public class ConfirmacionPedidoController implements Initializable {

    @FXML
    private TableView<Producto> tablaProductos;

    @FXML
    private TableColumn<Producto, String> colNombre;

    @FXML
    private TableColumn<Producto, Double> colPrecio;

    private List<Producto> productos;
    private int idMesa;
    private int idEmpleado;

    private PedidoService pedidoService;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        colNombre.setCellValueFactory(
                new PropertyValueFactory<>("nombre"));

        colPrecio.setCellValueFactory(
                new PropertyValueFactory<>("precio"));

        try {
            SqlLib sql = SqlLib.getInstance();

            ProductoDAO productoDAO = new ProductoDAO(sql.getConnection());
            PedidoDAO pedidoDAO = new PedidoDAO(sql.getConnection());

            pedidoService = new PedidoService(productoDAO, pedidoDAO);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 🔥 RECIBE TODO DESDE EL MENÚ
    public void setDatos(List<Producto> productos, int idMesa, int idEmpleado) {
        this.productos = productos;
        this.idMesa = idMesa;
        this.idEmpleado = idEmpleado;
        cargarTabla();
    }

    public void cargarTabla() {

        if (productos == null) return;

        ObservableList<Producto> lista =
                FXCollections.observableArrayList(productos);

        tablaProductos.setItems(lista);
    }

    public void alerta(String msg) {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Pollos Hermanos");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    // 🔥 AQUÍ SE EJECUTA EL CASO DE USO REAL
    @FXML
    public void confirmarPedido(ActionEvent event) {

        try {

            int idPedido = pedidoService.crearPedido(
                    idMesa,
                    idEmpleado,
                    productos
            );

            pedidoService.confirmarPedido(idPedido);

            alerta("Pedido enviado a cocina");

            Parent root = FXMLLoader.load(
                    getClass().getResource("/com/mycompany/polloshermanos/InicioReservaciones.fxml"));

            Stage stage = (Stage) tablaProductos.getScene().getWindow();

            stage.setScene(new Scene(root));
            stage.show();
            System.out.println("CONFIRMACION -> MESA: " + idMesa);
            System.out.println("CONFIRMACION -> EMPLEADO: " + idEmpleado);

        } catch (Exception e) {
            e.printStackTrace(); 
            //FA-02
            System.out.println("Error de conexión con la base de datos");
            alerta(e.getMessage());
        }
        System.out.println("ID MESA FINAL: " + idMesa);
    }

    //FA-03
    @FXML
    public void cancelar() throws IOException {

        Parent root = FXMLLoader.load(
                getClass().getResource("/com/mycompany/polloshermanosfx/MenuDigital.fxml"));

        Stage stage = (Stage) tablaProductos.getScene().getWindow();

        stage.setScene(new Scene(root));
        stage.show();
    }
}