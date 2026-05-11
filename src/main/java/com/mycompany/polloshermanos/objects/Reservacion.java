package com.mycompany.polloshermanos.objects;

public class Reservacion {

    private int idReservacion;
    private int idCliente;
    private int idMesa;

    private String nombreCliente;   // 🔥 nuevo
    private int numeroMesa;        // 🔥 nuevo
    private int capacidad;         // 🔥 nuevo

    private String fecha;
    private String hora;
    private String codigoReserva;
    private String estado;

    public Reservacion() {
    }

    public Reservacion(int idReservacion, int idCliente, int idMesa,
                       String nombreCliente, int numeroMesa, int capacidad,
                       String fecha, String hora,
                       String codigoReserva, String estado) {

        this.idReservacion = idReservacion;
        this.idCliente = idCliente;
        this.idMesa = idMesa;
        this.nombreCliente = nombreCliente;
        this.numeroMesa = numeroMesa;
        this.capacidad = capacidad;
        this.fecha = fecha;
        this.hora = hora;
        this.codigoReserva = codigoReserva;
        this.estado = estado;
    }

    // =========================
    // GETTERS Y SETTERS
    // =========================

    public int getIdReservacion() {
        return idReservacion;
    }

    public void setIdReservacion(int idReservacion) {
        this.idReservacion = idReservacion;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public int getIdMesa() {
        return idMesa;
    }

    public void setIdMesa(int idMesa) {
        this.idMesa = idMesa;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public int getNumeroMesa() {
        return numeroMesa;
    }

    public void setNumeroMesa(int numeroMesa) {
        this.numeroMesa = numeroMesa;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getCodigoReserva() {
        return codigoReserva;
    }

    public void setCodigoReserva(String codigoReserva) {
        this.codigoReserva = codigoReserva;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}