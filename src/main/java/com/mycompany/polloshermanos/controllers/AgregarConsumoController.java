package com.mycompany.polloshermanos.controllers;

import com.mycompany.polloshermanos.daos.PedidoDAO;
import com.mycompany.polloshermanos.daos.ProductoDAO;
import com.mycompany.polloshermanos.objects.Pedido;
import com.mycompany.polloshermanos.objects.Producto;
import lib.SqlLib;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AgregarConsumoController implements Initializable {
    
  @FXML private TableView<Pedido> tablaPedidos;
@FXML private TableColumn<Pedido, Integer> colIdPedido;
@FXML private TableColumn<Pedido, Integer> colMesa;    // es Integer porque tienes idMesa
@FXML private TableColumn<Pedido, String>  colEstado;
@FXML private Button btnSeleccionarPedido;

    @FXML private TableView<Producto> tablaProductos;
    @FXML private TableColumn<Producto, String>  colNombre;
    @FXML private TableColumn<Producto, Double>  colPrecio;
    @FXML private TableColumn<Producto, Integer> colStock;
    @FXML private TableColumn<Producto, String>  colCategoria;

    @FXML private TableView<Producto> tablaCarrito;
    @FXML private TableColumn<Producto, String> colCarritoNombre;
    @FXML private TableColumn<Producto, Double> colCarritoPrecio;

    @FXML private Label  lblIdPedido;
    @FXML private Label  lblTotal;
    @FXML private Button btnAgregar;
    @FXML private Button btnConfirmar;
    @FXML private Button btnCancelar;
    @FXML private Button btnRegresar;

    private int idPedidoActivo;
    private List<Producto> carrito = new ArrayList<>();

    private Connection    con;
    private PedidoDAO     pedidoDAO;
    private ProductoDAO   productoDAO;
    private PedidoService pedidoService;

    @Override
public void initialize(URL url, ResourceBundle rb) {

    try {

        SqlLib sql = SqlLib.getInstance();

        con = sql.getConnection();

        pedidoDAO     = new PedidoDAO(con);
        productoDAO   = new ProductoDAO(con);
        pedidoService = new PedidoService(productoDAO, pedidoDAO);

        configurarColumnasProductos();
        configurarColumnasCarrito();
        cargarProductosDisponibles();
        configurarColumnasPedidos();
        cargarPedidosActivos();

    } catch (Exception e) {

        mostrarError("Error al inicializar: " + e.getMessage());
    }
}

    public void setPedidoActivo(int idPedido) {
        this.idPedidoActivo = idPedido;
        lblIdPedido.setText("Pedido #" + idPedido);
        actualizarTotal();
    }

    private void configurarColumnasProductos() {
        colNombre   .setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colPrecio   .setCellValueFactory(new PropertyValueFactory<>("precio"));
        colStock    .setCellValueFactory(new PropertyValueFactory<>("stock"));
        colCategoria.setCellValueFactory(new PropertyValueFactory<>("categoria"));
    }

    private void configurarColumnasCarrito() {
        colCarritoNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colCarritoPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
    }

    private void cargarProductosDisponibles() throws Exception {
        List<Producto> todos = productoDAO.obtenerProductos();
        ObservableList<Producto> disponibles = FXCollections.observableArrayList();
        for (Producto p : todos) {
            if (p.isDisponible() && p.getStock() > 0) {
                disponibles.add(p);
            }
        }
        tablaProductos.setItems(disponibles);
    }

    @FXML
    public void agregarAlCarrito(ActionEvent event) {
        Producto seleccionado = tablaProductos.getSelectionModel().getSelectedItem();

        if (seleccionado == null) {
            mostrarAlerta("Selecciona un producto del menú primero.");
            return;
        }
        if (!seleccionado.isDisponible() || seleccionado.getStock() <= 0) {
            mostrarAlerta("Producto no disponible.");
            return;
        }

        carrito.add(seleccionado);
        tablaCarrito.setItems(FXCollections.observableArrayList(carrito));
    }

    @FXML
    public void confirmarConsumo(ActionEvent event) {
        if (carrito.isEmpty()) {
            mostrarAlerta("Agrega al menos un producto antes de confirmar.");
            return;
        }
        try {
            double totalActualizado = pedidoService.agregarConsumo(idPedidoActivo, carrito);
            mostrarInfo("Consumo agregado correctamente");

            carrito.clear();
            tablaCarrito.getItems().clear();
            lblTotal.setText(String.format("Total: $%.2f", totalActualizado));
            cargarProductosDisponibles();

        } catch (Exception e) {
            mostrarError(e.getMessage());
        }
    }

    @FXML
    public void cancelarAgregado(ActionEvent event) {
        carrito.clear();
        tablaCarrito.getItems().clear();
        mostrarAlerta("Operación cancelada. El pedido no fue modificado.");
    }

    @FXML
    public void regresar(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(
                getClass().getResource(
                    "/com/mycompany/polloshermanos/InicioMesero.fxml"
                )
            );
            Stage stage = (Stage) btnRegresar.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Panel Mesero");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void actualizarTotal() {
        try {
            double total = pedidoDAO.calcularTotal(idPedidoActivo);
            lblTotal.setText(String.format("Total: $%.2f", total));
        } catch (Exception e) {
            lblTotal.setText("Total: $0.00");
        }
    }

    private void mostrarAlerta(String mensaje) {
        Alert a = new Alert(Alert.AlertType.WARNING);
        a.setTitle("Aviso");
        a.setHeaderText(null);
        a.setContentText(mensaje);
        a.showAndWait();
    }

    private void mostrarInfo(String mensaje) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle("Éxito");
        a.setHeaderText(null);
        a.setContentText(mensaje);
        a.showAndWait();
    }

    private void mostrarError(String mensaje) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setTitle("Error");
        a.setHeaderText(null);
        a.setContentText(mensaje);
        a.showAndWait();
    }
    
    private void configurarColumnasPedidos() {
    colIdPedido.setCellValueFactory(new PropertyValueFactory<>("idPedido"));
    colMesa    .setCellValueFactory(new PropertyValueFactory<>("idMesa"));
    colEstado  .setCellValueFactory(new PropertyValueFactory<>("estado"));
}

private void cargarPedidosActivos() {
    try {
        ResultSet rs = pedidoDAO.obtenerPedidosActivos();
        ObservableList<Pedido> lista = FXCollections.observableArrayList();
        while (rs.next()) {
            Pedido p = new Pedido();
            p.setIdPedido(rs.getInt("id_pedido"));
            p.setIdMesa  (rs.getInt("numero_mesa"));  // viene del JOIN en el SQL
            p.setEstado  (rs.getString("estado"));
            lista.add(p);
        }
        tablaPedidos.setItems(lista);
    } catch (Exception e) {
        mostrarError("Error al cargar pedidos: " + e.getMessage());
    }
}

@FXML
public void seleccionarPedido(ActionEvent event) {
    Pedido seleccionado = tablaPedidos.getSelectionModel().getSelectedItem();

    if (seleccionado == null) {
        mostrarAlerta("Selecciona un pedido de la lista primero.");
        return;
    }

    idPedidoActivo = seleccionado.getIdPedido();
    lblIdPedido.setText("Pedido #" + idPedidoActivo + " | Mesa " + seleccionado.getIdMesa());
    actualizarTotal();
    mostrarInfo("Pedido seleccionado. Ahora agrega los productos.");
}
}