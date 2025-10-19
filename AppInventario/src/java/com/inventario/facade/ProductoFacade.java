package com.inventario.facade;

import com.inventario.model.Producto;
import com.inventario.persistence.ProductoDAO;
import jakarta.annotation.Resource;
import javax.sql.DataSource;
import java.sql.Connection;
import java.util.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class ProductoFacade {

    // Inyección del DataSource configurado en GlassFish
    @Resource(lookup = "jdbc/inventarioPool")
    private DataSource ds;
    
        public ProductoFacade() {
        try {
            InitialContext ctx = new InitialContext();
            ds = (DataSource) ctx.lookup("jdbc/inventarioPool");
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    // ---------------------------------------------------------
    // Validaciones de negocio
    // ---------------------------------------------------------
    private void validar(Producto p) throws Exception {
        if (p.getCodigo() == null || p.getCodigo().length() < 3)
            throw new Exception("El código debe tener al menos 3 caracteres.");
        if (p.getNombre() == null || p.getNombre().length() < 5)
            throw new Exception("El nombre debe tener al menos 5 caracteres.");
        if (p.getCategoria() == null ||
            !(p.getCategoria().equals("Electronicos") ||
              p.getCategoria().equals("Accesorios") ||
              p.getCategoria().equals("Muebles") ||
              p.getCategoria().equals("Ropa")))
            throw new Exception("La categoría no es válida.");
        if (p.getPrecio() == null || p.getPrecio() <= 0)
            throw new Exception("El precio debe ser mayor a 0.");
        if (p.getStock() == null || p.getStock() < 0)
            throw new Exception("El stock no puede ser negativo.");
    }

    // ---------------------------------------------------------
    // Listar productos
    // ---------------------------------------------------------
    public List<Producto> listar() throws Exception {
        try (Connection con = ds.getConnection()) {
            ProductoDAO dao = new ProductoDAO(con);
            return dao.listar();  // delega la consulta
        }
    }

    // ---------------------------------------------------------
    // Crear producto (con validación y unicidad de código)
    // ---------------------------------------------------------
    public void crear(Producto p) throws Exception {
        validar(p);

        try (Connection con = ds.getConnection()) {
            ProductoDAO dao = new ProductoDAO(con);

            // Verificar unicidad del código
            Optional<Producto> existente = dao.buscarPorCodigo(p.getCodigo());
            if (existente.isPresent()) {
                throw new Exception("Ya existe un producto con el código: " + p.getCodigo());
            }

            dao.insertar(p);
        }
    }

    // ---------------------------------------------------------
    // Eliminar producto por ID
    // ---------------------------------------------------------
    public void eliminar(int id) throws Exception {
        try (Connection con = ds.getConnection()) {
            ProductoDAO dao = new ProductoDAO(con);
            dao.eliminarPorId(id);
        }
    }
}
