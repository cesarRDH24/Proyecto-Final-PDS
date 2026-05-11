
package com.mycompany.polloshermanos.controllers;

import com.mycompany.polloshermanos.daos.ProductoDAO;
import com.mycompany.polloshermanos.objects.Producto;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.fxml.Initializable;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * FXML Controller class
 *
 * @author carvi
 */
public class ActualizarMenuController implements Initializable{

    @FXML private TableView<Producto> tablaProductos;
    @FXML private TableColumn<Producto, String> colPlatilloAM;
    @FXML private TableColumn<Producto, Double> colPrecioAM;
    @FXML private Button agregarProducto;
    @FXML private Button eliminarProducto;

    private ProductoDAO productoDAO;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colPlatilloAM.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colPrecioAM.setCellValueFactory(new PropertyValueFactory<>("precio"));

        try {
            // 🔑 Aquí inicializas el DAO con tu SqlLib
            productoDAO = new ProductoDAO(lib.SqlLib.getInstance().getConnection());
            cargarProductos();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void cargarProductos() throws Exception {
        List<Producto> lista = productoDAO.obtenerProductosAdmin();
        tablaProductos.getItems().setAll(lista);
    }


    
    @FXML
    private void agregarProducto(ActionEvent event) {
        Producto nuevo = new Producto();
        nuevo.setNombre("Nuevo Platillo");
        nuevo.setPrecio(99.0);

        try {
            productoDAO.insertProducto(nuevo);
            cargarProductos();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    

    
    @FXML
    private void eliminarProducto(ActionEvent event) {
        Producto seleccionado = tablaProductos.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            try {
                productoDAO.deleteProducto(seleccionado.getIdProducto());
                cargarProductos();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}