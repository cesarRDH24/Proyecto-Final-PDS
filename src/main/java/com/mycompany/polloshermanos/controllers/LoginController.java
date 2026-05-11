package com.mycompany.polloshermanos.controllers;

import com.mycompany.polloshermanos.objects.Empleado;
import com.mycompany.polloshermanos.controllers.LoginService;
import com.mycompany.polloshermanos.daos.RolDAO;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lib.SqlLib;


public class LoginController implements Initializable {
    
    private Connection con;

    @FXML
    private TextField usertf;

    @FXML
    private TextField passwordtf;

    @FXML
    private Button inicioSesion;

    @FXML
    private ComboBox<String> comboRol;
    
    @FXML
    private Button btnMenu;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        comboRol.setPromptText("Seleccione rol");

        try {
            SqlLib sql = SqlLib.getInstance();
            con = sql.getConnection();

            RolDAO rolDAO = new RolDAO(con);

            comboRol.getItems().addAll(
                rolDAO.cargarRoles()
            );

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void mostrarAlerta(String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle("Pollos Hermanos");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    @FXML
public void iniciar() {

    String usuario = usertf.getText();
    String contrasena = passwordtf.getText();
    String rolSeleccionado = comboRol.getValue();

    if (rolSeleccionado == null) {
        mostrarAlerta("Selecciona un rol", Alert.AlertType.WARNING);
        return;
    }

    try {

    LoginService service = new LoginService(con);

    Empleado emp = service.login(usuario, contrasena, rolSeleccionado);

    if (emp == null) {
        mostrarAlerta("Usuario no encontrado", Alert.AlertType.ERROR);
        return;
    }

    abrirPanel(emp);

} catch (IllegalArgumentException e) {
    mostrarAlerta("Contraseña incorrecta", Alert.AlertType.ERROR);

} catch (SecurityException e) {
    mostrarAlerta("Acceso no autorizado", Alert.AlertType.ERROR);

} catch (java.sql.SQLException e) {
    mostrarAlerta("No se pudo validar el acceso", Alert.AlertType.ERROR);

} catch (Exception e) {
    mostrarAlerta("Sistema temporalmente no disponible", Alert.AlertType.ERROR);
}
}

    public void abrirPanel(Empleado emp) throws IOException {

        String ruta = "";

        switch (emp.getRol().toLowerCase()) {

            case "gerente":
                ruta = "/com/mycompany/polloshermanos/inicioGerente.fxml";
                break;

            case "recepcionista":
                ruta = "/com/mycompany/polloshermanos/mesas.fxml";
                break;

            case "mesero":
                ruta = "/com/mycompany/polloshermanos/InicioMesero.fxml";
                break;

            case "cajero":
                ruta = "/com/mycompany/polloshermanos/pago.fxml";
                break;

            case "chef":
                ruta = "/com/mycompany/polloshermanos/inicioChef.fxml";
                break;

            case "administrador":
                ruta = "/com/mycompany/polloshermanos/inicioAdmin.fxml";
                break;

            default:
                System.out.println("Rol no reconocido");
                return;
        }

        FXMLLoader loader =
            new FXMLLoader(getClass().getResource(ruta));

        Parent root = loader.load();

        Stage stage =
            (Stage) inicioSesion.getScene().getWindow();

        stage.setScene(new Scene(root));
        stage.setTitle("Panel - " + emp.getRol());
        stage.show();
    }
    
    @FXML
    public void verMenuCliente(ActionEvent event) {
        try {
            // Paso 1: Crea instancia del controlador manualmente
            VerMenuController controller = new VerMenuController();

            // Paso 2: Configura FXMLLoader con el controlador
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/mycompany/polloshermanos/verMenu.fxml")
            );
            loader.setController(controller);  // Inyecta el controlador

            Parent root = loader.load();
            Scene scene = new Scene(root, 800, 600);  // Define tamaño inicial opcional

            Stage stage = new Stage();
            stage.setTitle("Menú de Cliente");  // Corrige título
            stage.initModality(Modality.WINDOW_MODAL);  // Modal (bloquea ventana anterior)
            stage.initOwner((Stage) ((Node) event.getSource()).getScene().getWindow());  // Vincula a padre
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            mostrarAlerta("Error al abrir menú: " + e.getMessage(), Alert.AlertType.ERROR);
            // Para debug en consola
        }
    }
}