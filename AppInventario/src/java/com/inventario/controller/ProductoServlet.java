package com.inventario.controller;

import com.inventario.facade.ProductoFacade;
import com.inventario.model.Producto;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "ProductoServlet", urlPatterns = {"/productos"})
public class ProductoServlet extends HttpServlet {

    // En un entorno Java EE real se usaría @Inject o @EJB,
    // pero para el taller lo instanciamos manualmente
    private ProductoFacade facade = new ProductoFacade();

    // ----------------------------------------------------------
    // Método GET → Listar productos
    // TODO: obtener lista de productos desde facade y hacer forward a productos.jsp
    // ----------------------------------------------------------
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        try {
            List<Producto> lista = facade.listar();
            req.setAttribute("productos", lista);

        } catch (Exception e) {
            req.setAttribute("error", "Error al listar productos: " + e.getMessage());
        }

        // Despachar al JSP
        req.getRequestDispatcher("/productos.jsp").forward(req, resp);
    }

    // ----------------------------------------------------------
    // Método POST → Crear producto
    // TODO: leer parámetros del form, construir Producto y llamar facade.crear
    // TODO: capturar excepciones de validación: setAttribute("error", mensaje) y forward
    // TODO: en éxito, redirect a "/productos"
    // ----------------------------------------------------------
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // Leer parámetros del formulario
        String codigo = req.getParameter("codigo");
        String nombre = req.getParameter("nombre");
        String categoria = req.getParameter("categoria");
        String precioStr = req.getParameter("precio");
        String stockStr = req.getParameter("stock");
        String activoStr = req.getParameter("activo");

        try {
            // Convertir y validar tipos
            Double precio = Double.parseDouble(precioStr);
            Integer stock = Integer.parseInt(stockStr);
            Boolean activo = (activoStr != null); // checkbox marcado = true

            // Crear objeto producto
            Producto nuevo = new Producto(codigo, nombre, categoria, precio, stock, activo);

            // Enviar a fachada
            facade.crear(nuevo);

            // Redirigir al listado (patrón PRG)
            resp.sendRedirect(req.getContextPath() + "/productos");

        } catch (Exception e) {
            // Manejo de errores y reenvío al JSP con mensaje
            req.setAttribute("error", "Error al crear producto: " + e.getMessage());
            doGet(req, resp);
        }
    }
}
