package com.mycompany.polloshermanos.controllers;

import com.mycompany.polloshermanos.daos.ProductoDAO;
import com.mycompany.polloshermanos.objects.Producto;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
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
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.stage.Stage;

public class VerMenuController implements Initializable {
    
    @FXML
    private Button btnRegresar;

    @FXML
    private TableView<Producto> tablaIzquierda;
    
    @FXML
    private TableColumn<Producto, String> colPlatillo;
    
    @FXML
    private TableColumn<Producto, Double> colPrecio;

    @FXML
    private TableView<Producto> tablaDerecha;
    
    @FXML
    private TableColumn<Producto, String> colPlatillo2;
    
    @FXML
    private TableColumn<Producto, Double> colPrecio2;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            Connection con = lib.SqlLib.getInstance().getConnection();
            ProductoDAO dao = new ProductoDAO(con);
            List<Producto> productos = dao.obtenerProductos();

            int mitad = productos.size() / 2;
            ObservableList<Producto> listaIzquierda = FXCollections.observableArrayList(productos.subList(0, mitad));
            ObservableList<Producto> listaDerecha = FXCollections.observableArrayList(productos.subList(mitad, productos.size()));

            colPlatillo.setCellValueFactory(new PropertyValueFactory<>("nombre"));
            colPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));

            colPlatillo2.setCellValueFactory(new PropertyValueFactory<>("nombre"));
            colPrecio2.setCellValueFactory(new PropertyValueFactory<>("precio"));

            tablaIzquierda.setItems(listaIzquierda);
            tablaDerecha.setItems(listaDerecha);

        } catch (Exception e) {
        }
    }
    
}