package com.mycompany.polloshermanos.objects;

public class SesionUsuario {

    private static int idEmpleado;
    private static String nombre;
    private static String rol;
    private static String usuario;

    public static int getIdEmpleado() {
        return idEmpleado;
    }

    public static void setIdEmpleado(int idEmpleado) {
        SesionUsuario.idEmpleado = idEmpleado;
    }

    public static String getNombre() {
        return nombre;
    }

    public static void setNombre(String nombre) {
        SesionUsuario.nombre = nombre;
    }

    public static String getRol() {
        return rol;
    }

    public static void setRol(String rol) {
        SesionUsuario.rol = rol;
    }

    public static String getUsuario() {
        return usuario;
    }

    public static void setUsuario(String usuario) {
        SesionUsuario.usuario = usuario;
    }

    //CU-21 CERRAR SESION
    public static void cerrarSesion() {
        idEmpleado = 0;
        nombre = null;
        rol = null;
        usuario = null;
    }
}