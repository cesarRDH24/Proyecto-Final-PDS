/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.polloshermanos.daos;

import com.mycompany.polloshermanos.objects.ReporteInventarioItem;
import com.mycompany.polloshermanos.objects.ReporteVentaItem;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author carvi
 */
public class ReporteVentasDAO {
    private Connection con;

    public ReporteVentasDAO(Connection con) {
        this.con = con;
    }

    public List<ReporteVentaItem> obtenerReporteDiario(LocalDate fecha) throws Exception {

        List<ReporteVentaItem> lista = new ArrayList<>();

        String sql
                = "SELECT m.nombre, c.nombre AS categoria, "
                + "       SUM(dp.cantidad) AS total_vendido, "
                + "       SUM(dp.subtotal) AS total_ingreso "
                + "FROM detalle_pedido dp "
                + "INNER JOIN menu m ON dp.id_producto = m.id_producto "
                + "INNER JOIN categorias c ON m.id_categoria = c.id_categoria "
                + "INNER JOIN pedidos p ON dp.id_pedido = p.id_pedido "
                + "WHERE DATE(p.fecha) = ? "
                + "  AND p.estado != 'Cancelado' "
                + "GROUP BY m.id_producto, m.nombre, c.nombre "
                + "ORDER BY total_vendido DESC";

        PreparedStatement ps = con.prepareStatement(sql);
        ps.setDate(1, java.sql.Date.valueOf(fecha));
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            ReporteVentaItem item = new ReporteVentaItem();
            item.setNombre(rs.getString("nombre"));
            item.setCategoria(rs.getString("categoria"));
            item.setTotalVendido(rs.getInt("total_vendido"));
            item.setTotalIngreso(rs.getDouble("total_ingreso"));
            lista.add(item);
        }

        return lista;
    }
    
    public List<ReporteVentaItem> obtenerReporteMensual(int mes, int anio) throws Exception {

        List<ReporteVentaItem> lista = new ArrayList<>();

        String sql
                = "SELECT m.nombre, c.nombre AS categoria, "
                + "       SUM(dp.cantidad) AS total_vendido, "
                + "       SUM(dp.subtotal) AS total_ingreso "
                + "FROM detalle_pedido dp "
                + "INNER JOIN menu m ON dp.id_producto = m.id_producto "
                + "INNER JOIN categorias c ON m.id_categoria = c.id_categoria "
                + "INNER JOIN pedidos p ON dp.id_pedido = p.id_pedido "
                + "WHERE MONTH(p.fecha) = ? "
                + "  AND YEAR(p.fecha) = ? "
                + "  AND p.estado != 'Cancelado' "
                + "GROUP BY m.id_producto, m.nombre, c.nombre "
                + "ORDER BY total_vendido DESC";

        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, mes);
        ps.setInt(2, anio);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            ReporteVentaItem item = new ReporteVentaItem();
            item.setNombre(rs.getString("nombre"));
            item.setCategoria(rs.getString("categoria"));
            item.setTotalVendido(rs.getInt("total_vendido"));
            item.setTotalIngreso(rs.getDouble("total_ingreso"));
            lista.add(item);
        }

        return lista;
    }
    
    public List<ReporteInventarioItem> obtenerReporteInventario() throws Exception {

        List<ReporteInventarioItem> lista = new ArrayList<>();

        String sql
                = "SELECT nombre, unidad, stock, minimo "
                + "FROM inventario "
                + "ORDER BY nombre";

        PreparedStatement ps = con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            ReporteInventarioItem item = new ReporteInventarioItem();
            item.setNombre(rs.getString("nombre"));
            item.setUnidad(rs.getString("unidad"));
            item.setStockActual(rs.getDouble("stock"));
            item.setMinimo(rs.getDouble("minimo"));

            // Determinar estado automáticamente
            if (rs.getDouble("stock") <= rs.getDouble("minimo")) {
                item.setEstado("CRÍTICO");
            } else {
                item.setEstado("OK");
            }

            lista.add(item);
        }

        return lista;
    }
    
    public void descontarInventario(int idPedido) throws Exception {

        String sql
                = "UPDATE inventario inv "
                + "INNER JOIN menu_inventario mi ON inv.id_producto_inv = mi.id_producto_inv "
                + "INNER JOIN detalle_pedido dp ON mi.id_producto = dp.id_producto "
                + "SET inv.stock = inv.stock - (mi.cantidad_usada * dp.cantidad) "
                + "WHERE dp.id_pedido = ?";

        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, idPedido);
        ps.executeUpdate();
    }
}
