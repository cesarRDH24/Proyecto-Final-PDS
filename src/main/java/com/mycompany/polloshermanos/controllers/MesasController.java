package com.mycompany.polloshermanos.controllers;

// JavaFX
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.util.Duration;

// Java
import java.time.*;
import java.util.*;

// Tu modelo
import com.mycompany.polloshermanos.objects.Mesa;
import com.mycompany.polloshermanos.objects.Reservacion;
import com.mycompany.polloshermanos.daos.ReservacionDAO;
import com.mycompany.polloshermanos.objects.SesionUsuario;

// Colecciones
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

// ⚠️ IMPORTA TU CONEXIÓN (ajusta el nombre si es distinto)
import lib.SqlLib;

public class MesasController {

    @FXML private Label lblNumero;
    @FXML private Label lblCapacidad;
    @FXML private Label lblEstado;
    @FXML private Label lblFecha;
    @FXML private Label lblHora;
    @FXML private Label lblTiempo;
    @FXML private Label id;

    private ReservacionDAO reservacionDAO;
    private Mesa mesaSeleccionada;

    private ObservableList<Reservacion> listaReservaciones = FXCollections.observableArrayList();

    private Map<Integer, Mesa> mesas = new HashMap<>();

    // =========================
    // INIT
    // =========================
    @FXML
    public void initialize() {

        cargarMesas();

        // 🔥 INICIALIZAR DAO
        try {
    reservacionDAO = new ReservacionDAO(
        SqlLib.getInstance().getConnection()
    );

    // cargar datos desde BD
    listaReservaciones.addAll(reservacionDAO.obtenerReservaciones());

} catch (Exception e) {
    e.printStackTrace();
}

        // 🔥 CARGAR DESDE BD
        listaReservaciones.addAll(reservacionDAO.obtenerReservaciones());
        
        iniciarCancelacionAutomatica();
    }
    
    
    private Reservacion buscarReservacion(int idMesa) {

    for (Reservacion r : listaReservaciones) {

        if (r.getIdMesa() == idMesa &&
            r.getEstado().equalsIgnoreCase("Activa")) {

            return r;
        }
    }

    return null;
}

    // =========================
    // MESAS
    // =========================
    private void cargarMesas() {
        mesas.put(1,new Mesa(1,1,2,"Disponible"));
        mesas.put(2,new Mesa(2,2,2,"Disponible"));
        mesas.put(3,new Mesa(3,3,4,"Disponible"));
        mesas.put(4,new Mesa(4,4,4,"Disponible"));
        mesas.put(5,new Mesa(5,5,6,"Disponible"));
        mesas.put(6,new Mesa(6,6,8,"Disponible"));
        mesas.put(7,new Mesa(7,7,2,"Disponible"));
        mesas.put(8,new Mesa(8,8,4,"Disponible"));
        mesas.put(9,new Mesa(9,9,6,"Disponible"));
        mesas.put(10,new Mesa(10,10,8,"Disponible"));
    }

    // =========================
    // SELECCIONAR
    // =========================
    @FXML
    private void seleccionarMesa(ActionEvent event) {

        Button btn = (Button) event.getSource();
        int numero = Integer.parseInt(btn.getText().replace("Mesa ", ""));

        mesaSeleccionada = mesas.get(numero);
        mostrarInfoMesa();
    }

    // =========================
    // BUSCAR
    // =========================
    private Reservacion buscarReservacionPorCodigo(String codigo) {

    for (Reservacion r : listaReservaciones) {

        if (r.getCodigoReserva() != null &&
            r.getCodigoReserva().equalsIgnoreCase(codigo)) {

            return r;
        }
    }

    return null;
}

    // =========================
    // MOSTRAR INFO
    // =========================
    private void mostrarInfoMesa() {

        if (mesaSeleccionada == null) return;

        lblNumero.setText("Número: " + mesaSeleccionada.getNumeroMesa());
        lblCapacidad.setText("Capacidad: " + mesaSeleccionada.getCapacidad());
        lblEstado.setText("Estado: " + mesaSeleccionada.getEstado());
        
        

        Reservacion r = buscarReservacion(mesaSeleccionada.getIdMesa());

        if (r != null) {
            lblFecha.setText("Fecha: " + r.getFecha());
            lblHora.setText("Hora: " + r.getHora());
            id.setText(r.getCodigoReserva());
            calcularTiempoRestante(r);
        } else {
            lblFecha.setText("-");
            lblHora.setText("-");
            lblTiempo.setText("-");
        }
    }

    // =========================
    // RESERVAR
    // =========================
    @FXML
    private void reservarMesa() {

        if (mesaSeleccionada == null) {
            error("Selecciona una mesa");
            return;
        }

        if (!mesaSeleccionada.getEstado().equals("Disponible")) {
            error("No disponible");
            return;
        }

        Dialog<Reservacion> dialog = new Dialog<>();
        dialog.setTitle("Reservación");

        TextField nombre = new TextField();
        DatePicker fecha = new DatePicker();
        TextField hora = new TextField();

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        grid.add(new Label("Nombre:"),0,0);
        grid.add(nombre,1,0);
        grid.add(new Label("Fecha:"),0,1);
        grid.add(fecha,1,1);
        grid.add(new Label("Hora:"),0,2);
        grid.add(hora,1,2);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(btn -> {
            if (btn == ButtonType.OK) {

                Reservacion r = new Reservacion();

                r.setIdMesa(mesaSeleccionada.getIdMesa());
                r.setNombreCliente(nombre.getText());
                r.setFecha(fecha.getValue().toString());
                r.setHora(hora.getText());
                r.setEstado("Activa");
                
                

                // 🔥 TEMPORAL
                r.setIdCliente(1);
                
                
            String codigoReserva =
                "RSV" +
                (int)(Math.random()*9000 + 1000);
            
            r.setCodigoReserva(codigoReserva);
            
                    System.out.println(
                    "Codigo generado: "
                    + codigoReserva
                );

                    System.out.println(
                        "Codigo en objeto: "
                        + r.getCodigoReserva()
                            
                    );

                return r;
            }
            return null;
        });

        Optional<Reservacion> result = dialog.showAndWait();

        result.ifPresent(r -> {

            if (reservacionDAO.insertarReservacion(r)) {

                listaReservaciones.add(r);
                mesaSeleccionada.setEstado("Reservada");
                
                id.setText(r.getCodigoReserva());

                info("Guardado en BD");
                mostrarInfoMesa();

            } else {
                error("Error al guardar");
            }
        });
    }

    // =========================
    // ASIGNAR
    // =========================
    @FXML
    private void asignarMesa() {

        if (mesaSeleccionada == null) {
            error("Selecciona mesa");
            return;
        }

        if (!mesaSeleccionada.getEstado().equals("Disponible")) {
            error("No disponible");
            return;
        }

        TextInputDialog dialog = new TextInputDialog();
        dialog.setHeaderText("Nombre cliente");

        Optional<String> result = dialog.showAndWait();

        result.ifPresent(nombre -> {

            Reservacion r = new Reservacion();

            r.setIdMesa(mesaSeleccionada.getIdMesa());
            r.setNombreCliente(nombre);
            r.setFecha(LocalDate.now().toString());
            r.setHora(LocalTime.now().toString());
            r.setEstado("Activa");
            r.setIdCliente(1);
            

                            
                            
                            
        
            

            
            

            if (reservacionDAO.insertarReservacion(r)) {

                listaReservaciones.add(r);
                mesaSeleccionada.setEstado("Ocupada");
                
              

                info("Asignado en BD");
                
                
                
                mostrarInfoMesa();
            }
        });
    }

    // =========================
    // CANCELAR
    // =========================
    @FXML
    private void cancelarReservacion() {

        if (mesaSeleccionada == null) {
            error("Selecciona mesa");
            return;
        }

        Reservacion r = buscarReservacion(mesaSeleccionada.getIdMesa());

        if (r == null) {
            error("No hay reservación");
            return;
        }

        listaReservaciones.remove(r);
        mesaSeleccionada.setEstado("Disponible");

        info("Cancelada");
        mostrarInfoMesa();
    }
    
    // =========================
// CANCELACIÓN AUTOMÁTICA
// =========================
private void iniciarCancelacionAutomatica() {

    // Revisará cada minuto
    Timeline timeline = new Timeline(
        new KeyFrame(Duration.minutes(1), e -> {
            verificarReservacionesExpiradas();
        })
    );

    timeline.setCycleCount(Timeline.INDEFINITE);
    timeline.play();

    // Ejecuta una vez al iniciar
    verificarReservacionesExpiradas();
}


private void verificarReservacionesExpiradas() {

    LocalDateTime ahora = LocalDateTime.now();

    for (Reservacion r : listaReservaciones) {

        try {

            // FA-02: Reservación ya atendida
            if (r.getEstado().equalsIgnoreCase("Atendida")) {
                continue;
            }

            // Solo revisar activas/pendientes
            if (!r.getEstado().equalsIgnoreCase("Activa")) {
                continue;
            }

            LocalDate fecha = LocalDate.parse(
                    r.getFecha()
            );

            LocalTime hora = LocalTime.parse(
                    r.getHora()
            );

            LocalDateTime fechaReserva =
                    LocalDateTime.of(fecha,hora);

            long minutosTranscurridos =
        java.time.Duration
        .between(fechaReserva, ahora)
        .toMinutes();

            // Flujo principal:
            // si pasaron más de 15 min
            if (minutosTranscurridos > 15) {

                r.setEstado("Cancelada");

                Mesa mesa = mesas.get(
                        r.getIdMesa()
                );

                if (mesa != null) {
                    mesa.setEstado("Disponible");
                }

                // Intentar actualizar BD
                try {

                    // Debes tener este método en DAO
                    reservacionDAO.actualizarReservacion(r);

                    System.out.println(
                        "Reservación cancelada automáticamente"
                    );

                } catch (Exception ex) {

                    // EX-01
                    System.out.println(
                        "Error al actualizar reservación: "
                        + ex.getMessage()
                    );
                }

            }

        } catch(Exception e){

            System.out.println(
                "Error verificando reservación: "
                + e.getMessage()
            );
        }
    }

    mostrarInfoMesa();
}

    // =========================
    // TIEMPO
    // =========================
    private void calcularTiempoRestante(Reservacion r) {

        try {
            LocalDateTime ahora = LocalDateTime.now();
            LocalDateTime reserva = LocalDateTime.of(
                LocalDate.parse(r.getFecha()),
                LocalTime.parse(r.getHora())
            );

            long min = java.time.Duration
        .between(ahora, reserva)
        .toMinutes();
            lblTiempo.setText("Tiempo restante: " + min + " min");

        } catch (Exception e) {
            lblTiempo.setText("-");
        }
    }

    // =========================
    // SALIR
    // =========================
    @FXML
private void salir(ActionEvent event) {

    try {

        Alert confirmacion =
            new Alert(Alert.AlertType.CONFIRMATION);

        confirmacion.setTitle("Cerrar sesión");
        confirmacion.setHeaderText(null);
        confirmacion.setContentText(
            "¿Deseas cerrar sesión?"
        );

        Optional<ButtonType> resultado =
            confirmacion.showAndWait();

        if (resultado.isPresent()
                && resultado.get() == ButtonType.CANCEL) {

            return;
        }

        SesionUsuario.cerrarSesion();

        Parent root = FXMLLoader.load(
            getClass().getResource(
                "/com/mycompany/polloshermanos/login.fxml"
            )
        );

        Stage stage =
            (Stage) ((Button) event.getSource())
                .getScene()
                .getWindow();

        stage.setScene(new Scene(root));

        Alert exito =
            new Alert(Alert.AlertType.INFORMATION);

        exito.setTitle("Pollos Hermanos");
        exito.setHeaderText(null);
        exito.setContentText(
            "Sesión cerrada correctamente"
        );

        exito.showAndWait();

    } catch (Exception e) {

        Alert error =
            new Alert(Alert.AlertType.ERROR);

        error.setTitle("Error");
        error.setHeaderText(null);
        error.setContentText(
            "Error al cerrar sesión"
        );

        error.showAndWait();

        e.printStackTrace();
    }
}

    // =========================
    // ALERTAS
    // =========================
    private void error(String msg) {
        new Alert(Alert.AlertType.ERROR, msg).showAndWait();
    }

    private void info(String msg) {
        new Alert(Alert.AlertType.INFORMATION, msg).showAndWait();
    }
    
    @FXML
private void buscarPorCodigo() {

    TextInputDialog dialog = new TextInputDialog();

    dialog.setTitle("Buscar Reservación");
    dialog.setHeaderText("Ingrese el código de reservación");
    dialog.setContentText("Código:");

    Optional<String> resultado = dialog.showAndWait();

    if (resultado.isEmpty()) {
        return;
    }

    String codigo = resultado.get().trim();

    Reservacion r = buscarReservacionPorCodigo(codigo);

    if (r == null) {

        error("No se encontró la reservación");
        return;
    }

    Mesa mesa = mesas.get(r.getIdMesa());

    if (mesa != null) {

        mesaSeleccionada = mesa;

        lblNumero.setText("Número: " + mesa.getNumeroMesa());
        lblCapacidad.setText("Capacidad: " + mesa.getCapacidad());
        lblEstado.setText("Estado: " + mesa.getEstado());

        lblFecha.setText("Fecha: " + r.getFecha());
        lblHora.setText("Hora: " + r.getHora());

        calcularTiempoRestante(r);

        id.setText(r.getCodigoReserva());

        info("Reservación encontrada");
    }
}
}