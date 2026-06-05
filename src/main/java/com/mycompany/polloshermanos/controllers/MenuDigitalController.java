package com.mycompany.polloshermanos.controllers;

import com.mycompany.polloshermanos.daos.PedidoDAO;
import com.mycompany.polloshermanos.daos.ProductoDAO;
import com.mycompany.polloshermanos.objects.Producto;
import java.io.IOException;
import java.net.URL;
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

public class MenuDigitalController implements Initializable {

    @FXML
    private TableView<Producto> tablaMenu;

    @FXML
    private TableColumn<Producto, String> colNombre;

    @FXML
    private TableColumn<Producto, String> colCategoria;

    @FXML
    private TableColumn<Producto, Double> colPrecio;

    // 🔥 CAMBIO
    @FXML
    private TableColumn<Producto, Integer> colStock;

    private PedidoService pedidoService;

    private ObservableList<Producto> carrito = FXCollections.observableArrayList();

    private int idMesa;
    private int idEmpleado;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colCategoria.setCellValueFactory(new PropertyValueFactory<>("categoria"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));

        // 🔥 CAMBIO
        colStock.setCellValueFactory(new PropertyValueFactory<>("stock"));

        try {

            SqlLib sql = SqlLib.getInstance();

            ProductoDAO productoDAO = new ProductoDAO(sql.getConnection());
            PedidoDAO pedidoDAO = new PedidoDAO(sql.getConnection());

            pedidoService = new PedidoService(productoDAO, pedidoDAO);

            tablaMenu.setItems(
                FXCollections.observableArrayList(
                    productoDAO.obtenerProductos()
                )
            );

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setDatos(int idMesa, int idEmpleado) {

        this.idMesa = idMesa;
        this.idEmpleado = idEmpleado;

        System.out.println("ID MESA RECIBIDA: " + idMesa);
    }

    @FXML
    public void agregarProducto() {

        Producto p = tablaMenu.getSelectionModel().getSelectedItem();

        if (p == null) {
            alerta("Selecciona un producto");
            return;
        }

        // 🔥 VALIDAR STOCK
        if (p.getStock() <= 0) {
            alerta("Producto agotado");
            return;
        }

        carrito.add(p);

        alerta("Producto agregado");
    }

    @FXML
    public void quitarProducto() {

        Producto p = tablaMenu.getSelectionModel().getSelectedItem();

        if (p == null) {
            alerta("Selecciona producto a quitar.");
            return;
        }

        carrito.remove(p);

        alerta("Producto quitado.");
    }

    @FXML
    public void confirmarPedido() {

        try {

            if (carrito.isEmpty()) {
                alerta("No hay productos.");
                return;
            }

            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/mycompany/polloshermanos/ConfirmacionPedido.fxml")
            );

            Parent root = loader.load();

            ConfirmacionPedidoController controller = loader.getController();

            controller.setDatos(carrito, idMesa, idEmpleado);

            Stage stage = (Stage) tablaMenu.getScene().getWindow();

            stage.setScene(new Scene(root));
            stage.setTitle("Confirmación de pedido");
            stage.show();

        } catch (Exception e) {

            e.printStackTrace();

            alerta("Error al continuar");
        }
    }

    @FXML
    public void salir(ActionEvent event)
            throws IOException {

        Parent root = FXMLLoader.load(
                getClass().getResource(
                        "/com/mycompany/polloshermanos/InicioReservaciones.fxml"));

        Stage stage =
                (Stage) tablaMenu.getScene()
                        .getWindow();

        stage.setScene(new Scene(root));
        stage.show();
    }

    public void alerta(String msg) {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        alert.setTitle("Pollos Hermanos");
        alert.setHeaderText(null);
        alert.setContentText(msg);

        alert.showAndWait();
    }
}