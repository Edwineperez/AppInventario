package com.inventario.persistence;

import com.inventario.model.Producto;
import java.sql.*;
import java.util.*;

public class ProductoDAO {

    private final Connection con;

    public ProductoDAO(Connection con) {
        this.con = con;
    }

        // TODO: SELECT * FROM productos ORDER BY nombre ASC
        // (mapear ResultSet a List<Producto>)
    public List<Producto> listar() throws SQLException {
        List<Producto> lista = new ArrayList<>();
        String sql = "SELECT id, codigo, nombre, categoria, precio, stock, activo FROM productos ORDER BY nombre";

        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Producto p = new Producto(
                        rs.getInt("id"),
                        rs.getString("codigo"),
                        rs.getString("nombre"),
                        rs.getString("categoria"),
                        rs.getDouble("precio"),
                        rs.getInt("stock"),
                        rs.getBoolean("activo")
                );
                lista.add(p);
            }
        }
        return lista;
    }

       // TODO: SELECT * FROM productos WHERE codigo = ?
    public Optional<Producto> buscarPorCodigo(String codigo) throws SQLException {
        String sql = "SELECT * FROM productos WHERE codigo = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, codigo);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Producto p = new Producto(
                            rs.getInt("id"),
                            rs.getString("codigo"),
                            rs.getString("nombre"),
                            rs.getString("categoria"),
                            rs.getDouble("precio"),
                            rs.getInt("stock"),
                            rs.getBoolean("activo")
                    );
                    return Optional.of(p);
                }
            }
        }
        return Optional.empty();
    }

        // TODO: INSERT INTO productos (codigo, nombre, categoria, precio, stock, activo) VALUES (?, ?, ?, ?, ?, ?)
        // (usar RETURN_GENERATED_KEYS para setear id)
    public void insertar(Producto p) throws SQLException {
        String sql = "INSERT INTO productos (codigo, nombre, categoria, precio, stock, activo) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, p.getCodigo());
            ps.setString(2, p.getNombre());
            ps.setString(3, p.getCategoria());
            ps.setDouble(4, p.getPrecio());
            ps.setInt(5, p.getStock());
            ps.setBoolean(6, p.getActivo());

            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    p.setId(keys.getInt(1));
                }
            }
        }
    }

        // TODO: DELETE FROM productos WHERE id = ?
    public void eliminarPorId(int id) throws SQLException {
        String sql = "DELETE FROM productos WHERE id = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}
