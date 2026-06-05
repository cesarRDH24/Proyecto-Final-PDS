package com.mycompany.polloshermanos.controllers;

import com.mycompany.polloshermanos.daos.ProductoDAO;
import com.mycompany.polloshermanos.objects.Producto;
import java.io.IOException;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.fxml.Initializable;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ActualizarMenuController implements Initializable {

    @FXML private TableView<Producto> tablaProductos;
    @FXML private TableColumn<Producto, String> colPlatilloAM;
    @FXML private TableColumn<Producto, Double> colPrecioAM;
    @FXML private Button agregarProducto;
    @FXML private Button eliminarProducto;
    @FXML private javafx.scene.control.TextField txtNombre;
    @FXML private javafx.scene.control.TextField txtPrecio;
    @FXML private javafx.scene.control.ComboBox<String> comboCategoria;
    @FXML private javafx.scene.control.TextField txtStock;
    @FXML private javafx.scene.control.TextField txtDescripcion;
    @FXML private Button btnVolverAdmin;

    private ProductoDAO productoDAO;
    private Map<String, Integer> categoriasMap = new HashMap<>();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colPlatilloAM.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colPrecioAM.setCellValueFactory(new PropertyValueFactory<>("precio"));

        try {
            productoDAO = new ProductoDAO(lib.SqlLib.getInstance().getConnection());
            cargarCategorias();
            cargarProductosDB();
        } catch (Exception e) {
            mostrarError("Error al inicializar", e.getMessage());
        }
    }

    private void cargarCategorias() throws Exception {
        categoriasMap = productoDAO.obtenerCategorias();
        comboCategoria.getItems().setAll(categoriasMap.keySet());
    }

    private void cargarProductosDB() throws Exception {
        List<Producto> lista = productoDAO.obtenerProductosAdmin();
        tablaProductos.getItems().setAll(lista);
    }

    @FXML
    private void cargarProductos(ActionEvent event) {
        try {
            cargarProductosDB();
        } catch (Exception e) {
            mostrarError("Error al cargar productos", e.getMessage());
        }
    }

    @FXML
    private void agregarProducto(ActionEvent event) {
        try {
            if (txtNombre.getText().isEmpty() || txtPrecio.getText().isEmpty() ||
                txtDescripcion.getText().isEmpty() || txtStock.getText().isEmpty() ||
                comboCategoria.getValue() == null) {
                mostrarAdvertencia("Campos incompletos", "Debes llenar todos los campos antes de agregar un producto.");
                return;
            }

            String nombre = txtNombre.getText();
            double precio = Double.parseDouble(txtPrecio.getText());
            String descripcion = txtDescripcion.getText();
            int stock = Integer.parseInt(txtStock.getText());
            int idCategoria = categoriasMap.get(comboCategoria.getValue());

            Producto nuevo = new Producto();
            nuevo.setNombre(nombre);
            nuevo.setPrecio(precio);
            nuevo.setDescripcion(descripcion);
            nuevo.setStock(stock);
            nuevo.setIdCategoria(idCategoria);

            productoDAO.insertProducto(nuevo);
            cargarProductosDB();

            mostrarInfo("Éxito", "El platillo se agregó correctamente.");

            txtNombre.clear();
            txtPrecio.clear();
            txtDescripcion.clear();
            txtStock.clear();
            comboCategoria.getSelectionModel().clearSelection();
        } catch (NumberFormatException nfe) {
            mostrarAdvertencia("Formato inválido", "Precio y stock deben ser números.");
        } catch (Exception e) {
            mostrarError("Error al agregar producto", e.getMessage());
        }
    }

    @FXML
    private void eliminarProducto(ActionEvent event) {
        Producto seleccionado = tablaProductos.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarAdvertencia("Selección requerida", "Debes escoger un platillo para eliminar.");
            return;
        }
        try {
            productoDAO.deleteProducto(seleccionado.getIdProducto());
            cargarProductosDB();
            mostrarInfo("Éxito", "El platillo se eliminó correctamente.");
        } catch (Exception e) {
            mostrarError("Error al eliminar producto", e.getMessage());
        }
    }

    @FXML
    private void volverInicioAdmin(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mycompany/polloshermanos/inicioAdmin.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) btnVolverAdmin.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Los Pollos Hermanos - Administrador");
            stage.show();
        } catch (IOException e) {
            mostrarError("Error al volver", e.getMessage());
        }
    }

    // Métodos auxiliares para mostrar mensajes
    private void mostrarInfo(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void mostrarAdvertencia(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void mostrarError(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}