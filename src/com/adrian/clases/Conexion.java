package com.adrian.clases;

import java.sql.*;

public class Conexion {
    private static Conexion instance;
    private static Connection conn = null;
    private Statement stmt;
    private ResultSet rs;

    private Conexion() {
        // cadena de conexión
        String url = "jdbc:sqlite:identifier.sqlite";

        // hago la conexion en el constructor
        // creo un Statement para reusar
        try {
            conn = DriverManager.getConnection(url);
            stmt = conn.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Base del Singleton
     * @return instancia de la clase Model
     */
    public static Conexion getInstance(){
        // solo hago el new si es null
        if (instance == null){
            instance = new Conexion();
        }
        // devuelvo siempre la unica instancia
        return instance;
    }

    // métodos generales que hacen consultas/inserciones/etc

    public String getCasillaNombre(int id){
        String sql = "SELECT Nombre FROM casillas WHERE ID=" + id + " LIMIT 1";
        String resultado = "";

        try {
            // recojo los resultados
            rs = stmt.executeQuery(sql);
            // loop por los resultados
            // aunque al poner LIMIT 1 nos garantizamos solo un resultado
            while (rs.next()) {
                resultado = rs.getString("Nombre");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        // devuelvo lo obtenido
        return resultado;
    }
    public int getCasillaPrecio(int id){
        String sql = "SELECT Precio FROM casillas WHERE ID=" + id + " LIMIT 1";
        int resultado = 0;

        try {
            // recojo los resultados
            rs = stmt.executeQuery(sql);
            // loop por los resultados
            // aunque al poner LIMIT 1 nos garantizamos solo un resultado
            while (rs.next()) {
                resultado = rs.getInt("Precio");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        // devuelvo lo obtenido
        return resultado;
    }
    public int getCasillaCodigo(int id){
        String sql = "SELECT Codigo FROM casillas WHERE ID=" + id + " LIMIT 1";
        int resultado = 0;

        try {
            // recojo los resultados
            rs = stmt.executeQuery(sql);
            // loop por los resultados
            // aunque al poner LIMIT 1 nos garantizamos solo un resultado
            while (rs.next()) {
                resultado = rs.getInt("Codigo");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        // devuelvo lo obtenido
        return resultado;
    }
    public int getCasillaImpuesto(int id){
        String sql = "SELECT Impuesto FROM casillas WHERE ID=" + id + " LIMIT 1";
        int resultado = 0;

        try {
            // recojo los resultados
            rs = stmt.executeQuery(sql);
            // loop por los resultados
            // aunque al poner LIMIT 1 nos garantizamos solo un resultado
            while (rs.next()) {
                resultado = rs.getInt("Impuesto");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        // devuelvo lo obtenido
        return resultado;
    }
    public int getCasillaPrecioCasa(int id){
        String sql = "SELECT PrecioCasa FROM casillas WHERE ID=" + id + " LIMIT 1";
        int resultado = 0;

        try {
            // recojo los resultados
            rs = stmt.executeQuery(sql);
            // loop por los resultados
            // aunque al poner LIMIT 1 nos garantizamos solo un resultado
            while (rs.next()) {
                resultado = rs.getInt("PrecioCasa");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        // devuelvo lo obtenido
        return resultado;
    }
}
