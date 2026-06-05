package com.mycompany.polloshermanos.objects;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 *
 * @author carvi
 */
public class Asistencia {
    
    private int idAsistencia;
    private int idEmpleado;
    private LocalDate fecha;
    private LocalTime horaEntrada;
    private LocalTime horaSalida;
    private String nombreEmpleado;

    public Asistencia(int idEmpleado, LocalDate fecha, LocalTime horaEntrada) {
        this.idEmpleado = idEmpleado;
        this.fecha = fecha;
        this.horaEntrada = horaEntrada;
    }

    public Asistencia(){
    }
    
    public int getIdAsistencia(){ 
        return idAsistencia; 
    }
    public void setIdAsistencia(int idAsistencia){
        this.idAsistencia = idAsistencia; 
    }

    public int getIdEmpleado(){ 
        return idEmpleado; 
    }
    
    public void setIdEmpleado(int idEmpleado){
        this.idEmpleado = idEmpleado; 
    }

    public LocalDate getFecha(){
        return fecha; 
    }
    
    public void setFecha(LocalDate fecha){ 
        this.fecha = fecha; 
    }

    public LocalTime getHoraEntrada(){ 
        return horaEntrada; 
    }
    public void setHoraEntrada(LocalTime horaEntrada){ 
        this.horaEntrada = horaEntrada; 
    }

    public LocalTime getHoraSalida(){ 
        return horaSalida;
    }
    
    public void setHoraSalida(LocalTime horaSalida){ 
        this.horaSalida = horaSalida; 
    }
    
    public String getNombreEmpleado() {
        return nombreEmpleado;
    }

    public void setNombreEmpleado(String nombreEmpleado) {
        this.nombreEmpleado = nombreEmpleado;
    }
}