/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.polloshermanos.controllers;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;

/**
 * FXML Controller class
 *
 * @author ninoa
 */


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import lib.SqlLib;

public class PagoController {

    @FXML
    private TextField txtMonto;

    @FXML
    private ComboBox<String> cbMetodo;

    @FXML
    private Label lblMensaje;
    
    private int idPedido;

    @FXML
    public void initialize() {
        // Llenar métodos de pago
        cbMetodo.getItems().addAll("Efectivo", "Tarjeta", "Transferencia");
    }
    

public void recibirPedido(int idPedido){

    this.idPedido=idPedido;

    System.out.println(
            "Pedido recibido: "
            + idPedido);

}

  @FXML
private void registrarPago() {
    try {
        String metodo = cbMetodo.getValue();
        if (metodo == null) {
            lblMensaje.setText("Seleccione método");
            return;
        }

        Connection con = SqlLib.getInstance().getConnection();

        // Verifica que el pedido exista
        String verificar = "SELECT COUNT(*) FROM pedidos WHERE id_pedido=?";
        PreparedStatement psVerificar = con.prepareStatement(verificar);
        psVerificar.setInt(1, idPedido);
        ResultSet rs = psVerificar.executeQuery();
        rs.next();
        if (rs.getInt(1) == 0) {
            lblMensaje.setText("La cuenta ya fue eliminada");
            return;
        }

        // ✅ Verifica que no esté ya pagado
        String verificarPago = "SELECT COUNT(*) FROM pagos WHERE id_pedido = ?";
        PreparedStatement psPago = con.prepareStatement(verificarPago);
        psPago.setInt(1, idPedido);
        ResultSet rsPago = psPago.executeQuery();
        rsPago.next();
        if (rsPago.getInt(1) > 0) {
            lblMensaje.setText("Este pedido ya fue pagado");
            return;
        }

        double monto = Double.parseDouble(txtMonto.getText());

        // ✅ Folio único con UUID
        String folio = "TK" + java.util.UUID.randomUUID()
                                             .toString()
                                             .substring(0, 12)
                                             .toUpperCase();

        String sql = "INSERT INTO pagos(id_pedido, metodo_pago, total, folio_ticket)" +
                     " VALUES(?,?,?,?)";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, idPedido);
        ps.setString(2, metodo);
        ps.setDouble(3, monto);
        ps.setString(4, folio);
        ps.executeUpdate();

        // ✅ Actualiza estado del pedido
        String actualizarEstado =
            "UPDATE pedidos SET estado = 'Pagado' WHERE id_pedido = ?";
        PreparedStatement psActualizar = con.prepareStatement(actualizarEstado);
        psActualizar.setInt(1, idPedido);
        psActualizar.executeUpdate();

        lblMensaje.setText("Pago registrado correctamente ✔");

    } catch (Exception e) {
        e.printStackTrace();
        lblMensaje.setText("Error al registrar");
    }
}
    
    public void setPedido(
        int idPedido,
        double total){

            this.idPedido=idPedido;

            txtMonto.setText(
            String.valueOf(total));

            txtMonto.setEditable(false);

        }
        @FXML
        private void salir(ActionEvent event) {

            try {
                Parent root = FXMLLoader.load(
                getClass().getResource("/com/mycompany/polloshermanos/cuentas.fxml")
                );

                Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                stage.setScene(new Scene(root));

        } catch (Exception e) {
            e.printStackTrace();
        }
        
        
    }
    
}