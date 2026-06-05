package com.mycompany.polloshermanos.daos;

import com.mycompany.polloshermanos.objects.Cuenta;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CuentaDAO {

    private Connection con;

    public CuentaDAO(Connection con){
        this.con = con;
    }

    public List<Cuenta> obtenerCuentas() {

    List<Cuenta> lista =
            new ArrayList<>();

    try {

        String sql =
    "SELECT " +
    "p.id_pedido, " +
    "m.numero_mesa, " +
    "COALESCE(pa.metodo_pago,'Sin pago') AS metodo_pago, " +
    "COALESCE(SUM(dp.subtotal),0) AS total " +
    "FROM pedidos p " +
    "JOIN mesas m " +
    "ON p.id_mesa = m.id_mesa " +
    "LEFT JOIN detalle_pedido dp " +
    "ON p.id_pedido = dp.id_pedido " +
    "LEFT JOIN pagos pa " +
    "ON p.id_pedido = pa.id_pedido " +
    "GROUP BY p.id_pedido,m.numero_mesa,pa.metodo_pago";

PreparedStatement ps =
        con.prepareStatement(sql);

ResultSet rs =
        ps.executeQuery();

while(rs.next()){

    Cuenta cuenta =
            new Cuenta(
            rs.getInt("numero_mesa"),   // primero mesa
            rs.getInt("id_pedido"),     // luego pedido
            rs.getString("metodo_pago"), // método
            rs.getDouble("total")       // total
            );

    System.out.println(
        "Pedido: "
        + rs.getInt("id_pedido")
        + " Total: "
        + rs.getDouble("total")
    );

    lista.add(cuenta);
}

    } catch(Exception e) {

        e.printStackTrace();
    }

    return lista;
}
}