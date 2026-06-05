package com.mycompany.polloshermanos.daos;

import com.mycompany.polloshermanos.objects.Producto;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

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

    String sql =
    "INSERT INTO detalle_pedido(" +
    "id_pedido," +
    "id_producto," +
    "cantidad," +
    "precio_unitario," +
    "subtotal) " +
    "VALUES(?,?,1,?,?)";

    PreparedStatement ps =
            con.prepareStatement(sql);

    ps.setInt(
            1,
            idPedido);

    ps.setInt(
            2,
            p.getIdProducto());

    ps.setDouble(
            3,
            p.getPrecio());

    ps.setDouble(
            4,
            p.getPrecio());

    ps.executeUpdate();

    ps.close();

    System.out.println(
        "Producto agregado al pedido "
        + idPedido
    );
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
    //inicioMesero
    public ResultSet obtenerPedidosListos() throws Exception {

        String sql =
            "SELECT p.id_pedido, m.numero_mesa " +
            "FROM pedidos p " +
            "INNER JOIN mesas m ON p.id_mesa = m.id_mesa " +
            "WHERE p.estado = 'Listo'";

        PreparedStatement ps = con.prepareStatement(sql);

        return ps.executeQuery();
    }
    
    public void marcarComoEntregado(int idPedido) throws Exception {

        String sql = "UPDATE pedidos SET estado='Entregado' WHERE id_pedido=?";

        PreparedStatement ps = con.prepareStatement(sql);

        ps.setInt(1, idPedido);

        ps.executeUpdate();
    }
    
    //pedidos en preparacion
    public ResultSet obtenerPedidosPreparando() throws Exception {

        String sql =
            "SELECT p.id_pedido, m.numero_mesa " +
            "FROM pedidos p " +
            "INNER JOIN mesas m ON p.id_mesa = m.id_mesa " +
            "WHERE p.estado = 'Preparando'";

        PreparedStatement ps = con.prepareStatement(sql);

        return ps.executeQuery();
    }
    
    //CU11
public void cancelarPedido(int idPedido) throws Exception {

    String sql = "UPDATE pedidos SET estado='Cancelado' WHERE id_pedido=?";

    PreparedStatement ps = con.prepareStatement(sql);

    ps.setInt(1, idPedido);

    ps.executeUpdate();

    ps.close();
}

public ResultSet obtenerTodosPedidos() throws Exception {

    String sql =
        "SELECT id_pedido, id_mesa, estado, fecha " +
        "FROM pedidos";

    PreparedStatement ps = con.prepareStatement(sql);

    return ps.executeQuery();
}

    // --- CU-19: Obtener estado de un pedido ---
public String obtenerEstadoPedido(int idPedido) throws Exception {

    String sql = "SELECT estado FROM pedidos WHERE id_pedido = ?";

    PreparedStatement ps = con.prepareStatement(sql);
    ps.setInt(1, idPedido);
    ResultSet rs = ps.executeQuery();

    if (rs.next()) {
        return rs.getString("estado");
    }
    throw new Exception("Pedido no encontrado: " + idPedido);
}

// --- CU-19: Calcular total del pedido ---
public double calcularTotal(int idPedido) throws Exception {

    String sql =
        "SELECT COALESCE(SUM(subtotal), 0) AS total " +
        "FROM detalle_pedido " +
        "WHERE id_pedido = ?";

    PreparedStatement ps = con.prepareStatement(sql);
    ps.setInt(1, idPedido);
    ResultSet rs = ps.executeQuery();

    if (rs.next()) {
        return rs.getDouble("total");
    }
    return 0.0;
}

// --- CU-19: Obtener pedidos activos (sin Cancelado ni Pagado) ---
public ResultSet obtenerPedidosActivos() throws Exception {

    String sql =
        "SELECT p.id_pedido, m.numero_mesa, p.estado " +
        "FROM pedidos p " +
        "INNER JOIN mesas m ON p.id_mesa = m.id_mesa " +
        "WHERE p.estado NOT IN ('Cancelado', 'Pagado') " +
        "ORDER BY p.fecha DESC";

    PreparedStatement ps = con.prepareStatement(sql);
    return ps.executeQuery();
}

    // CU-19: Regresar pedido a cocina cambiando estado a Preparando
public void actualizarEstado(int idPedido, String nuevoEstado) throws Exception {
    String sql = "UPDATE pedidos SET estado = ? WHERE id_pedido = ?";
    PreparedStatement ps = con.prepareStatement(sql);
    ps.setString(1, nuevoEstado);
    ps.setInt(2, idPedido);
    ps.executeUpdate();
}

   
}