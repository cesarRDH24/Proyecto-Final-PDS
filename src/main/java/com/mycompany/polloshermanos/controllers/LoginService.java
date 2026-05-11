
package com.mycompany.polloshermanos.controllers;

import com.mycompany.polloshermanos.daos.EmpleadoDAO;
import com.mycompany.polloshermanos.objects.Empleado;
import com.mycompany.polloshermanos.objects.SesionUsuario;
import java.sql.Connection;

public class LoginService {

    private EmpleadoDAO dao;

    // Constructor
    public LoginService(Connection con) {
        this.dao = new EmpleadoDAO(con);
    }

    // Método principal del CU-01
    public Empleado login(String usuario, String pass, String rolSeleccionado) throws Exception {

    Empleado emp = dao.obtenerPorUsuario(usuario);

    // FA-02: usuario no existe
    if (emp == null) {
        return null; // 👈 esto es mejor que lanzar Exception
    }

    // FA-01: contraseña incorrecta
    if (!emp.getContrasena().equals(pass)) {
        throw new IllegalArgumentException("Contraseña incorrecta");
    }

    // FA-03: rol no autorizado
    if (!emp.getRol().equalsIgnoreCase(rolSeleccionado)) {
        throw new SecurityException("Acceso no autorizado");
    }

    // Sesión
    SesionUsuario.setIdEmpleado(emp.getIdEmpleado());
    SesionUsuario.setNombre(emp.getNombre());
    SesionUsuario.setRol(emp.getRol());
    SesionUsuario.setUsuario(emp.getUsuario());

    return emp;
}
}