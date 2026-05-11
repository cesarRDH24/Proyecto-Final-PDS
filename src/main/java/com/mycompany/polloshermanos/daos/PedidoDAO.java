package com.mycompany.polloshermanos.daos;

import com.mycompany.polloshermanos.objects.Producto;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class PedidoDAO {

    private Connection con;

    public PedidoDAO(Connection con) {
        this.con = con;
    }

    public int crearPedido(int idMesa, int idEmpleado) throws Exception {

        String sql = "INSERT INTO pedidos(id_mesa,id_empleado,estado) VALUES(?,?, 'Pendiente')";

        PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

        ps.setInt(1, idMesa);
        ps.setInt(2, idEmpleado);

        ps.executeUpdate();

        ResultSet rs = ps.getGeneratedKeys();

        if (rs.next()) {
            return rs.getInt(1);
        }

        throw new Exception("No se pudo crear pedido");
    }

    public void agregarProducto(int idPedido, Producto p) throws Exception {

        String sql = "INSERT INTO detalle_pedido(id_pedido,id_producto,cantidad,subtotal) VALUES(?,?,1,?)";

        PreparedStatement ps = con.prepareStatement(sql);

        ps.setInt(1, idPedido);
        ps.setInt(2, p.getIdProducto());
        ps.setDouble(3, p.getPrecio());

        ps.executeUpdate();
    }

    public void confirmarPedido(int idPedido) throws Exception {

        String sql = "UPDATE pedidos SET estado='Preparando' WHERE id_pedido=?";

        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, idPedido);
        ps.executeUpdate();
    }
    
    //CU06
    
    public ResultSet obtenerPedidosPendientes() throws Exception {

    String sql = "SELECT p.id_pedido, m.numero_mesa " +
                 "FROM pedidos p " +
                 "INNER JOIN mesas m ON p.id_mesa = m.id_mesa " +
                 "WHERE p.estado = 'Preparando'";

    PreparedStatement ps = con.prepareStatement(sql);

    return ps.executeQuery();
}
    
    public ResultSet obtenerDetallePedido(int idPedido) throws Exception {

    String sql = "SELECT pr.nombre " +
                 "FROM detalle_pedido dp " +
                 "INNER JOIN menu pr ON dp.id_producto = pr.id_producto " +
                 "WHERE dp.id_pedido = ?";

    PreparedStatement ps = con.prepareStatement(sql);
    ps.setInt(1, idPedido);

    return ps.executeQuery();
}
    
    //CU07
    public void marcarComoListo(int idPedido) throws Exception {

    String sql = "UPDATE pedidos SET estado='Listo' WHERE id_pedido=?";

    PreparedStatement ps = con.prepareStatement(sql);

    ps.setInt(1, idPedido);

    ps.executeUpdate();
}
}