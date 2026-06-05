package com.mycompany.polloshermanos.daos;

import com.mycompany.polloshermanos.objects.Producto;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductoDAO {

    private Connection con;

    public ProductoDAO(Connection con) {
        this.con = con;
    }

    //CU05
    public List<Producto> obtenerProductos() throws Exception {

    List<Producto> lista = new ArrayList<>();

    String sql =
        "SELECT p.id_producto, p.nombre, p.precio, p.stock, p.disponible, c.nombre AS categoria " +
        "FROM menu p " +
        "INNER JOIN categorias c ON p.id_categoria = c.id_categoria";

    PreparedStatement ps = con.prepareStatement(sql);
    ResultSet rs = ps.executeQuery();

    while (rs.next()) {

        Producto p = new Producto();

        p.setIdProducto(rs.getInt("id_producto"));
        p.setNombre(rs.getString("nombre"));
        p.setPrecio(rs.getDouble("precio"));
        p.setStock(rs.getInt("stock")); // 🔥 FALTABA
        p.setDisponible(rs.getBoolean("disponible"));
        p.setCategoria(rs.getString("categoria"));

        lista.add(p);
    }

    return lista;
}
    
     
     //Caso de uso 13 Actualizar Menu
    public Map<String, Integer> obtenerCategorias() throws Exception {
        Map<String, Integer> map = new HashMap<>();
        String sql = "SELECT id_categoria, nombre FROM categorias";
        PreparedStatement ps = con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            map.put(rs.getString("nombre"), rs.getInt("id_categoria"));
        }
        return map;
    }

    public void insertProducto(Producto p) throws Exception {
        String sql = "INSERT INTO menu (nombre, descripcion, precio, id_categoria, stock) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, p.getNombre());
        ps.setString(2, p.getDescripcion());
        ps.setDouble(3, p.getPrecio());
        ps.setInt(4, p.getIdCategoria());   // ← ahora id
        ps.setInt(5, p.getStock());
        ps.executeUpdate();
    }


    // Eliminar producto
    public void deleteProducto(int idProducto) throws Exception {
        String sql = "DELETE FROM menu WHERE id_producto = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, idProducto);
        ps.executeUpdate();
    }
    
    // Método especializado para ActualizarMenuController
    public List<Producto> obtenerProductosAdmin() throws Exception {
        List<Producto> lista = new ArrayList<>();

        String sql = "SELECT id_producto, nombre, precio FROM menu";
        PreparedStatement ps = con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            Producto p = new Producto();
            p.setIdProducto(rs.getInt("id_producto"));
            p.setNombre(rs.getString("nombre"));
            p.setPrecio(rs.getDouble("precio"));
            lista.add(p);
        }

        return lista;
    }
    
     public void descontarStock(int idProducto) throws Exception {

    String sql =
        "UPDATE menu " +
        "SET stock = stock - 1 " +
        "WHERE id_producto = ?";

    PreparedStatement ps = con.prepareStatement(sql);

    ps.setInt(1, idProducto);

    ps.executeUpdate();
}
}