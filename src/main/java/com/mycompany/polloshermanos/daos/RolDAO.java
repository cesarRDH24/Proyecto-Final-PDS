package com.mycompany.polloshermanos.daos;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RolDAO {

    private Connection con;

    public RolDAO(Connection con) {
        this.con = con;
    }

    public List<String> cargarRoles() {

        List<String> lista = new ArrayList<>();

        String sql = "SELECT nombre_rol FROM roles";

        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(rs.getString("nombre_rol"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }
}