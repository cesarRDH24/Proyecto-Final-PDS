package com.mycompany.polloshermanos.controllers;

import com.mycompany.polloshermanos.daos.CuentaDAO;
import com.mycompany.polloshermanos.objects.Cuenta;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import lib.SqlLib;
import com.mycompany.polloshermanos.daos.FolioDAO;
import com.mycompany.polloshermanos.objects.Folio;
import com.mycompany.polloshermanos.objects.ReporteVentaItem;
import java.io.FileWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class CuentaController implements Initializable {

    @FXML
    private TableView<Cuenta> tabla;

    @FXML
    private TableColumn<Cuenta,Integer> colMesa;

    @FXML
    private TableColumn<Cuenta,Integer> colPedido;

    @FXML
    private TableColumn<Cuenta,String> colEstado;
    
    @FXML
    private TableColumn<Cuenta,String> colTotal;

    private ObservableList<Cuenta> lista =
            FXCollections.observableArrayList();

   

@Override
public void initialize(URL url, ResourceBundle rb) {

    colMesa.setCellValueFactory(
            new PropertyValueFactory<>("numeroMesa"));

    colPedido.setCellValueFactory(
            new PropertyValueFactory<>("idPedido"));

    colEstado.setCellValueFactory(
            new PropertyValueFactory<>("estadoPago"));

    colTotal.setCellValueFactory(data ->

        new SimpleStringProperty(
            "$" +
            String.format(
            "%.2f",
            data.getValue().getTotal()
            )
        )
    );

    cargarDatos();
}

    private void cargarDatos() {

        try {

            Connection con =
                    SqlLib.getInstance().getConnection();

            CuentaDAO dao =
                    new CuentaDAO(con);

            lista.clear();

            lista.addAll(
                    dao.obtenerCuentas()
            );

            tabla.setItems(lista);

        } catch(Exception e){
            e.printStackTrace();
        }
    }

    @FXML
private void abrirPago(ActionEvent event) {

    try {

        Cuenta cuenta =
                tabla.getSelectionModel()
                        .getSelectedItem();

        if(cuenta == null){

            Alert a = new Alert(Alert.AlertType.WARNING);
            a.setContentText("Selecciona una cuenta");
            a.show();

            return;
        }

        // Validar si ya está pagada
        if(!cuenta.getEstadoPago()
        .equalsIgnoreCase("Sin pago")){

            Alert a =
                    new Alert(
                    Alert.AlertType.INFORMATION);

            a.setContentText(
                    "Cuenta pagada");

            a.show();

            return;
        }

        FXMLLoader loader =
                new FXMLLoader(
                getClass().getResource(
                "/com/mycompany/polloshermanos/pago.fxml"));

        Parent root =
                loader.load();

        PagoController controller =
                loader.getController();

        controller.setPedido(
        cuenta.getIdPedido(),
        cuenta.getTotal(),
        cuenta.getNumeroMesa());

        Stage ventanaActual =
                (Stage)((Node)
                event.getSource())
                .getScene()
                .getWindow();

        ventanaActual.close();

        Stage stage =
                new Stage();

        stage.setScene(
                new Scene(root));

        stage.setTitle(
                "Registrar pago");

        stage.show();

    } catch(Exception e){

        e.printStackTrace();
    }
}
@FXML
private void ExportarFolio(ActionEvent event) {

    try {
        Cuenta cuenta =
                tabla.getSelectionModel()
                        .getSelectedItem();
        
        Connection con=
                SqlLib
                .getInstance()
                .getConnection();

        FolioDAO dao=
                new FolioDAO(
                con);

        Folio folio=
                dao.obtenerFolio(
                cuenta.getIdPedido());

        

        if(cuenta == null){

            Alert a = new Alert(Alert.AlertType.WARNING);
            a.setContentText("Selecciona una cuenta");
            a.show();

            return;
        }

        // Validar si ya está pagada
        if(cuenta.getEstadoPago()
        .equalsIgnoreCase("Sin pago")){

            Alert a =
                    new Alert(
                    Alert.AlertType.INFORMATION);

            a.setContentText(
                    "Cuenta no pagada");

            a.show();

            return;
        }
        
        LocalDate fecha = LocalDate.now();
        String fechaFormato = fecha.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        String nombreArchivo = "Folio" + folio.getNumeroFolio() + ".txt";

        try (FileWriter fw = new FileWriter(nombreArchivo)) {

            
            fw.write("===================================\n");
            fw.write("FOLIO " + folio.getNumeroFolio());
            fw.write("===================================\n");
            fw.write("PEDIDO" + folio.getIdPedido());
            fw.write("Metodo de pago: "+ folio.getMetodoPago());
            fw.write("Total: " + folio.getTotal());
            
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

    } catch(Exception e){

        e.printStackTrace();
    }
}
        
     

@FXML
private void eliminarCuenta() {

    try {

        Cuenta cuenta =
                tabla.getSelectionModel()
                .getSelectedItem();

        if(cuenta==null){

            Alert a =
                    new Alert(
                    Alert.AlertType.WARNING);

            a.setContentText(
                    "Selecciona una cuenta");

            a.show();

            return;
        }

        if(cuenta.getEstadoPago()
                .equals("Sin pago")){

            Alert a =
                    new Alert(
                    Alert.AlertType.WARNING);

            a.setContentText(
                    "Cuenta no pagada");

            a.show();

            return;
        }

        Connection con =
                SqlLib.getInstance()
                .getConnection();

        // eliminar detalles
        String sql1=
                "DELETE FROM detalle_pedido " +
                "WHERE id_pedido=?";

        PreparedStatement ps1=
                con.prepareStatement(
                sql1);

        ps1.setInt(
                1,
                cuenta.getIdPedido());

        ps1.executeUpdate();

        // eliminar pagos
        String sql2=
                "DELETE FROM pagos " +
                "WHERE id_pedido=?";

        PreparedStatement ps2=
                con.prepareStatement(
                sql2);

        ps2.setInt(
                1,
                cuenta.getIdPedido());

        ps2.executeUpdate();

        // eliminar pedido
        String sql3=
                "DELETE FROM pedidos " +
                "WHERE id_pedido=?";

        PreparedStatement ps3=
                con.prepareStatement(
                sql3);

        ps3.setInt(
                1,
                cuenta.getIdPedido());

        ps3.executeUpdate();

        cargarDatos();

        Alert a=
                new Alert(
                Alert.AlertType.INFORMATION);

        a.setContentText(
                "Cuenta eliminada");

        a.show();

    }
    catch(Exception e){

        e.printStackTrace();
    }
}


    @FXML
    private void salir(ActionEvent event) {

        try {
            Parent root = FXMLLoader.load(
                getClass().getResource("/com/mycompany/polloshermanos/login.fxml")
            );

            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
private void verFolio(){

    try{

        Cuenta cuenta=
                tabla.getSelectionModel()
                .getSelectedItem();

        if(cuenta==null){

            Alert a=
                    new Alert(
                    Alert.AlertType.WARNING);

            a.setContentText(
                    "Selecciona cuenta");

            a.show();

            return;
        }

        if(cuenta.getEstadoPago()
                .equalsIgnoreCase(
                "Sin pago")){

            Alert a=
                    new Alert(
                    Alert.AlertType.WARNING);

            a.setContentText(
                    "Cuenta no pagada");

            a.show();

            return;
        }


        Connection con=
                SqlLib
                .getInstance()
                .getConnection();

        FolioDAO dao=
                new FolioDAO(
                con);

        Folio folio=
                dao.obtenerFolio(
                cuenta.getIdPedido());

        if(folio==null){

            Alert a=
                    new Alert(
                    Alert.AlertType.WARNING);

            a.setContentText(
                    "No existe folio");

            a.show();

            return;
        }

        Alert a=
                new Alert(
                Alert.AlertType.INFORMATION);

        a.setHeaderText(
                "Ticket generado");

        a.setContentText(

        "Folio: "
        +folio.getNumeroFolio()

        +"\nPedido: "
        +folio.getIdPedido()

        +"\nMesa: "
        +folio.getNumeroMesa()

        +"\nMétodo: "
        +folio.getMetodoPago()

        +"\nTotal: $"
        +folio.getTotal()

        );

        a.show();

    }
    catch(Exception e){

        e.printStackTrace();
    }

}
    
}