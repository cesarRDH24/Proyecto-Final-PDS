/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.polloshermanos.daos;

/**
 *
 * @author ninoa
 */

import com.mycompany.polloshermanos.objects.Folio;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class FolioDAO {

    private Connection con;

    public FolioDAO(
            Connection con){

        this.con=con;
    }


    public void guardarFolio(
            Folio folio){

        try{

            String sql=
            "INSERT INTO folios("+
            "numero_folio,"+
            "id_pedido,"+
            "numero_mesa,"+
            "metodo_pago,"+
            "total)" +
            " VALUES(?,?,?,?,?)";

            PreparedStatement ps=
                    con.prepareStatement(
                    sql);

            ps.setString(
                    1,
                    folio.getNumeroFolio());

            ps.setInt(
                    2,
                    folio.getIdPedido());

            ps.setInt(
                    3,
                    folio.getNumeroMesa());

            ps.setString(
                    4,
                    folio.getMetodoPago());

            ps.setDouble(
                    5,
                    folio.getTotal());

            ps.executeUpdate();

        }
        catch(Exception e){

            e.printStackTrace();
        }

    }



    public Folio obtenerFolio(
            int idPedido){

        try{

            String sql=
            "SELECT * " +
            "FROM folios " +
            "WHERE id_pedido=?";

            PreparedStatement ps=
                    con.prepareStatement(
                    sql);

            ps.setInt(
                    1,
                    idPedido);

            ResultSet rs=
                    ps.executeQuery();

            if(rs.next()){

                return new Folio(

                rs.getString(
                "numero_folio"),

                rs.getInt(
                "id_pedido"),

                rs.getInt(
                "numero_mesa"),

                rs.getString(
                "metodo_pago"),

                rs.getDouble(
                "total")

                );
            }

        }
        catch(Exception e){

            e.printStackTrace();
        }

        return null;

    }

}
