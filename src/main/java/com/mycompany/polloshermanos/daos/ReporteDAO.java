/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.polloshermanos.daos;

import com.mycompany.polloshermanos.objects.ReporteProductoItem;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author carvi
 */
public class ReporteDAO {
    private Connection con;

    public ReporteDAO(Connection con) {
        this.con = con;
    }

    public List<ReporteProductoItem> obtenerReporte() throws Exception {

        List<ReporteProductoItem> lista = new ArrayList<>();

        String sql
                = "SELECT m.id_producto, m.nombre, c.nombre AS categoria, "
                + "       COALESCE(SUM(dp.cantidad), 0) AS total_vendido, "
                + "       m.stock AS stock_actual " +
            "FROM menu m " +
            "INNER JOIN categorias c ON m.id_categoria = c.id_categoria " +
            "LEFT JOIN detalle_pedido dp ON m.id_producto = dp.id_producto " +
            "LEFT JOIN pedidos p ON dp.id_pedido = p.id_pedido " +
            "       AND p.estado != 'Cancelado' " +
            "GROUP BY m.id_producto, m.nombre, c.nombre, m.stock " +
            "ORDER BY c.nombre, m.nombre";

        PreparedStatement ps = con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            ReporteProductoItem item = new ReporteProductoItem();
            item.setNombre(rs.getString("nombre"));
            item.setCategoria(rs.getString("categoria"));
            item.setTotalVendido(rs.getInt("total_vendido"));
            item.setStockActual(rs.getInt("stock_actual"));
            item.setIdProducto(rs.getInt("id_producto"));
            lista.add(item);
        }

        return lista;
    }
    
    public void reabastecerProducto(int idProducto, int cantidad) throws Exception {
        String sql = "UPDATE menu SET stock = stock + ? WHERE id_producto = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, cantidad);
        ps.setInt(2, idProducto);
        ps.executeUpdate();
    }
}
