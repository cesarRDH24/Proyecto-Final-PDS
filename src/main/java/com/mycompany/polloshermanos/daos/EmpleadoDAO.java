
package com.mycompany.polloshermanos.daos;

import java.sql.Connection;
import com.mycompany.polloshermanos.objects.Empleado;
import java.sql.PreparedStatement;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;


public class EmpleadoDAO {

    private Connection con;

    public EmpleadoDAO(Connection con) {
        this.con = con;
    }

    public Empleado obtenerPorUsuario(String usuario) {

        Empleado emp = null;

        String sql =
            "SELECT e.*, r.nombre_rol " +
            "FROM empleados e " +
            "INNER JOIN roles r ON e.id_rol = r.id_rol " +
            "WHERE e.usuario = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, usuario);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                emp = new Empleado();
                emp.setIdEmpleado(rs.getInt("id_empleado"));
                emp.setNombre(rs.getString("nombre"));
                emp.setContrasena(rs.getString("contraseña"));
                emp.setRol(rs.getString("nombre_rol"));
                emp.setUsuario(rs.getString("usuario"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return emp;
    }
    
    //Parte Caso de uso 17 Gestionar empleados.
    // Obtener todos los empleados
    public List<Empleado> obtenerTodos() {
        List<Empleado> lista = new ArrayList<>();
        String sql = "SELECT e.*, r.nombre_rol FROM empleados e INNER JOIN roles r ON e.id_rol = r.id_rol";
        try (PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Empleado emp = new Empleado();
                emp.setIdEmpleado(rs.getInt("id_empleado"));
                emp.setNombre(rs.getString("nombre"));
                emp.setTelefono(rs.getString("telefono"));
                emp.setCorreo(rs.getString("correo"));
                emp.setUsuario(rs.getString("usuario"));
                emp.setContrasena(rs.getString("contraseña"));
                emp.setIdRol(rs.getInt("id_rol"));
                emp.setRol(rs.getString("nombre_rol"));
                emp.setFechaIngreso(rs.getString("fecha_ingreso")); // aquí como String
                lista.add(emp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

// Insertar nuevo empleado
    public void insertar(Empleado emp) {
        String sql = "INSERT INTO empleados (nombre, telefono, correo, usuario, contraseña, id_rol, fecha_ingreso) VALUES (?,?,?,?,?,?,?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, emp.getNombre());
            ps.setString(2, emp.getTelefono());
            ps.setString(3, emp.getCorreo());
            ps.setString(4, emp.getUsuario());
            ps.setString(5, emp.getContrasena());
            ps.setInt(6, emp.getIdRol());
            ps.setString(7, emp.getFechaIngreso()); // como String
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

// Actualizar empleado
    public void actualizar(Empleado emp) {
        String sql = "UPDATE empleados SET nombre=?, telefono=?, correo=?, usuario=?, contraseña=?, id_rol=?, fecha_ingreso=? WHERE id_empleado=?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, emp.getNombre());
            ps.setString(2, emp.getTelefono());
            ps.setString(3, emp.getCorreo());
            ps.setString(4, emp.getUsuario());
            ps.setString(5, emp.getContrasena());
            ps.setInt(6, emp.getIdRol());
            ps.setString(7, emp.getFechaIngreso()); // como String
            ps.setInt(8, emp.getIdEmpleado());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

// Eliminar empleado
    public void eliminar(int idEmpleado) {
        String sql = "DELETE FROM empleados WHERE id_empleado=?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idEmpleado);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    //CASO DE USO 18
    // Obtener id_empleado a partir de nombre y rol
    public int obtenerIdPorNombreYRol(String nombre, String rol) {
        int idEmpleado = -1;
        String sql = "SELECT e.id_empleado "
                + "FROM empleados e "
                + "INNER JOIN roles r ON e.id_rol = r.id_rol "
                + "WHERE e.nombre = ? AND r.nombre_rol = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nombre);
            ps.setString(2, rol);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                idEmpleado = rs.getInt("id_empleado");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return idEmpleado;
    }
}



