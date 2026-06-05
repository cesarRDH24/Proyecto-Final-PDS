/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.polloshermanos.daos;

import com.mycompany.polloshermanos.objects.Asistencia;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author carvi
 */
public class AsistenciaDAO {

    private Connection conn;

    public AsistenciaDAO(Connection conn) {
        this.conn = conn;
    }

    // Insertar asistencia (solo entrada)
    public void insertarAsistencia(Asistencia asistencia) {
        String sql = "INSERT INTO asistencias (id_empleado, fecha, hora_entrada) VALUES (?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, asistencia.getIdEmpleado());
            stmt.setDate(2, java.sql.Date.valueOf(asistencia.getFecha()));
            stmt.setTime(3, java.sql.Time.valueOf(asistencia.getHoraEntrada()));
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Verificar si ya existe asistencia para ese empleado en esa fecha
    public boolean existeAsistencia(int idEmpleado, LocalDate fecha) throws SQLException {
        String sql = "SELECT COUNT(*) FROM asistencias WHERE id_empleado = ? AND fecha = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idEmpleado);
            stmt.setDate(2, java.sql.Date.valueOf(fecha));
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
        return false;
    }

    // Obtener todas las asistencias con nombre del empleado
    public ObservableList<Asistencia> obtenerTodasAsistencias() throws SQLException {
        ObservableList<Asistencia> lista = FXCollections.observableArrayList();
        String sql = "SELECT a.id_asistencia, a.fecha, a.hora_entrada, a.hora_salida, e.nombre "
                + "FROM asistencias a JOIN empleados e ON a.id_empleado = e.id_empleado";

        try (PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Asistencia asistencia = new Asistencia();
                asistencia.setIdAsistencia(rs.getInt("id_asistencia"));
                asistencia.setFecha(rs.getDate("fecha").toLocalDate());
                asistencia.setHoraEntrada(rs.getTime("hora_entrada").toLocalTime());
                asistencia.setHoraSalida(rs.getTime("hora_salida") != null ? rs.getTime("hora_salida").toLocalTime() : null);
                asistencia.setNombreEmpleado(rs.getString("nombre"));
                lista.add(asistencia);
            }
        }
        return lista;
    }
}
