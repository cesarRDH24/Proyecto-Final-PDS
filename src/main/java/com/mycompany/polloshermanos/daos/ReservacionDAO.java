package com.mycompany.polloshermanos.daos;

import com.mycompany.polloshermanos.objects.Reservacion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ReservacionDAO {

    private Connection con;

    public ReservacionDAO(Connection con) {
        this.con = con;
    }

    // =========================
    // OBTENER RESERVACIONES
    // =========================
    public List<Reservacion> obtenerReservaciones() {

        List<Reservacion> lista = new ArrayList<>();

        String sql = "SELECT r.id_reservacion, r.id_mesa, c.id_cliente, c.nombre AS cliente, " +
                     "m.numero_mesa, m.capacidad, r.fecha, r.hora, r.estado " +
                     "FROM reservaciones r " +
                     "INNER JOIN clientes c ON r.id_cliente = c.id_cliente " +
                     "INNER JOIN mesas m ON r.id_mesa = m.id_mesa " +
                     "WHERE r.estado = 'Activa'";

        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Reservacion r = new Reservacion();

                r.setIdReservacion(rs.getInt("id_reservacion"));
                r.setIdMesa(rs.getInt("id_mesa"));
                r.setIdCliente(rs.getInt("id_cliente")); // 🔥 importante
                r.setNombreCliente(rs.getString("cliente"));
                r.setNumeroMesa(rs.getInt("numero_mesa"));
                r.setCapacidad(rs.getInt("capacidad"));
                r.setFecha(rs.getString("fecha"));
                r.setHora(rs.getString("hora"));
                r.setEstado(rs.getString("estado"));

                lista.add(r);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    // =========================
    // INSERTAR RESERVACION
    // =========================
    public boolean insertarReservacion(Reservacion r) {

        String sql = "INSERT INTO reservaciones (id_mesa, id_cliente, fecha, hora, estado) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, r.getIdMesa());
            ps.setInt(2, r.getIdCliente()); // 🔥 clave
            ps.setString(3, r.getFecha());
            ps.setString(4, r.getHora());
            ps.setString(5, r.getEstado());

            ps.executeUpdate();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // =========================
    // ELIMINAR (CANCELAR)
    // =========================
    public boolean eliminarReservacion(int idReservacion) {

        String sql = "DELETE FROM reservaciones WHERE id_reservacion = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idReservacion);
            ps.executeUpdate();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}