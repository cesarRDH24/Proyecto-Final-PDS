package com.mycompany.polloshermanos.controllers;

import com.mycompany.polloshermanos.daos.PedidoDAO;
import com.mycompany.polloshermanos.daos.ProductoDAO;
import com.mycompany.polloshermanos.objects.Pedido;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.time.LocalDateTime;
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
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import lib.SqlLib;

public class CancelarPedidoController implements Initializable {

    @FXML
    private TextField txtBuscar;

    @FXML
    private TableView<Pedido> tablaPedidos;

    @FXML
    private TableColumn<Pedido, Integer> colId;

    @FXML
    private TableColumn<Pedido, Integer> colMesa;

    @FXML
    private TableColumn<Pedido, String> colEstado;

    @FXML
    private TableColumn<Pedido, LocalDateTime> colFecha;
    
    @FXML
    private Button btnSalir;

    @FXML
    private Label lblMensaje;

    private ObservableList<Pedido> listaPedidos;

    private PedidoService pedidoService;

    @Override
public void initialize(URL url, ResourceBundle rb) {

    try {

        Connection con = SqlLib.getInstance().getConnection();

        PedidoDAO pedidoDAO = new PedidoDAO(con);

        ProductoDAO productoDAO = new ProductoDAO(con);

        pedidoService = new PedidoService(productoDAO, pedidoDAO);

        listaPedidos = FXCollections.observableArrayList();

        configurarTabla();

        cargarPedidos();

    } catch (Exception e) {

        mostrarError("Error al iniciar controlador");
    }
}

    private void configurarTabla() {

        colId.setCellValueFactory(new PropertyValueFactory<>("idPedido"));
        colMesa.setCellValueFactory(new PropertyValueFactory<>("idMesa"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
    }

    private void cargarPedidos() {

    try {

        listaPedidos.clear();

        Connection con = SqlLib.getInstance().getConnection();

        PedidoDAO pedidoDAO = new PedidoDAO(con);

        ResultSet rs = pedidoDAO.obtenerTodosPedidos();

        while (rs.next()) {

            Pedido p = new Pedido();

            p.setIdPedido(rs.getInt("id_pedido"));
            p.setIdMesa(rs.getInt("id_mesa"));
            p.setEstado(rs.getString("estado"));

            listaPedidos.add(p);
        }

        tablaPedidos.setItems(listaPedidos);

    } catch (Exception e) {

        mostrarError("Error al cargar pedidos");
    }
}

    @FXML
    private void buscarPedido(ActionEvent event) {

        String texto = txtBuscar.getText().trim();

        if (texto.isEmpty()) {

            tablaPedidos.setItems(listaPedidos);

            return;
        }

        ObservableList<Pedido> filtrados = FXCollections.observableArrayList();

        for (Pedido p : listaPedidos) {

            if (String.valueOf(p.getIdPedido()).contains(texto)
                    || String.valueOf(p.getIdMesa()).contains(texto)) {

                filtrados.add(p);
            }
        }

        tablaPedidos.setItems(filtrados);
    }

    @FXML
    private void cancelarPedido(ActionEvent event) {

        Pedido pedido = tablaPedidos.getSelectionModel().getSelectedItem();

        if (pedido == null) {

            mostrarError("Selecciona un pedido");

            return;
        }

        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);

        alerta.setTitle("Cancelar Pedido");
        alerta.setHeaderText(null);
        alerta.setContentText("¿Deseas cancelar el pedido?");

        if (alerta.showAndWait().get() == ButtonType.OK) {

            try {

                pedidoService.cancelarPedido(pedido.getIdPedido());

                lblMensaje.setText("Pedido cancelado correctamente");

                cargarPedidos();

            } catch (Exception e) {

                mostrarError(e.getMessage());
            }
        }
    }
    
    @FXML
public void salir(ActionEvent event) {

    try {

        Parent root = FXMLLoader.load(
            getClass().getResource(
                "/com/mycompany/polloshermanos/InicioMesero.fxml"
            )
        );

        Stage stage =
            (Stage) btnSalir.getScene().getWindow();

        stage.setScene(new Scene(root));

        stage.setTitle("Panel Mesero");

        stage.show();

    } catch (Exception e) {

        e.printStackTrace();
    }
}

    @FXML
    private void actualizarTabla(ActionEvent event) {

        cargarPedidos();
    }

    private void mostrarError(String mensaje) {

        Alert alerta = new Alert(Alert.AlertType.ERROR);

        alerta.setTitle("Error");
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);

        alerta.showAndWait();
    }
}