<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Inventario de Productos</title>
    <style>
        :root {
            --color-principal: #0056b3;
            --color-secundario: #f5f7fa;
            --color-acento: #00bcd4;
            --color-texto: #333;
            --color-error: #e74c3c;
            --color-exito: #27ae60;
        }

        body {
            font-family: 'Segoe UI', Roboto, sans-serif;
            background: var(--color-secundario);
            margin: 0;
            padding: 0;
        }

        header {
            background: var(--color-principal);
            color: white;
            padding: 1rem 2rem;
            text-align: center;
            box-shadow: 0 2px 4px rgba(0,0,0,0.2);
        }

        main {
            max-width: 1100px;
            margin: 2rem auto;
            background: white;
            padding: 2rem;
            border-radius: 12px;
            box-shadow: 0 4px 12px rgba(0,0,0,0.1);
        }

        h2 {
            color: var(--color-principal);
            text-align: center;
            margin-bottom: 1rem;
        }

        .error {
            background: #fce4e4;
            color: var(--color-error);
            padding: 0.75rem 1rem;
            border-left: 4px solid var(--color-error);
            margin-bottom: 1.5rem;
            border-radius: 8px;
        }

        table {
            width: 100%;
            border-collapse: collapse;
            margin-bottom: 2rem;
            font-size: 0.95rem;
        }

        th {
            background: var(--color-principal);
            color: white;
            padding: 10px;
            text-align: left;
            font-weight: 600;
        }

        td {
            padding: 10px;
            border-bottom: 1px solid #ddd;
        }

        tr:hover {
            background: #f1f5ff;
            transition: 0.2s;
        }

        .activo {
            color: var(--color-exito);
            font-weight: bold;
        }

        .inactivo {
            color: var(--color-error);
            font-weight: bold;
        }

        form {
            display: flex;
            flex-wrap: wrap;
            gap: 15px;
            align-items: center;
            justify-content: center;
            padding: 1rem;
            background: var(--color-secundario);
            border-radius: 12px;
        }

        form input, form select {
            padding: 0.6rem;
            border: 1px solid #ccc;
            border-radius: 8px;
            outline: none;
            transition: 0.3s;
        }

        form input:focus, form select:focus {
            border-color: var(--color-principal);
            box-shadow: 0 0 5px rgba(0,86,179,0.3);
        }

        button {
            background: var(--color-acento);
            color: white;
            border: none;
            padding: 0.7rem 1.2rem;
            border-radius: 8px;
            font-size: 1rem;
            cursor: pointer;
            transition: 0.3s;
        }

        button:hover {
            background: #0097a7;
        }

        footer {
            text-align: center;
            padding: 1rem;
            color: #777;
            font-size: 0.9rem;
        }
    </style>
</head>
<body>

<header>
    <h1>🛒 Sistema de Inventario</h1>
</header>

<main>
    <h2>Listado de Productos</h2>

    <!-- Mostrar error si existe -->
    <c:if test="${not empty error}">
        <div class="error">${error}</div>
    </c:if>

    <!-- Tabla de productos -->
    <table>
        <thead>
        <tr>
            <th>ID</th>
            <th>Código</th>
            <th>Nombre</th>
            <th>Categoría</th>
            <th>Precio</th>
            <th>Stock</th>
            <th>Estado</th>
        </tr>
        </thead>
        <tbody>
        <c:choose>
            <c:when test="${empty productos}">
                <tr>
                    <td colspan="7" style="text-align:center; color:#888;">No hay productos registrados</td>
                </tr>
            </c:when>
            <c:otherwise>
                <c:forEach var="p" items="${productos}">
                    <tr>
                        <td>${p.id}</td>
                        <td>${p.codigo}</td>
                        <td>${p.nombre}</td>
                        <td>${p.categoria}</td>
                        <td>$ ${p.precio}</td>
                        <td>${p.stock}</td>
                        <td class="${p.activo ? 'activo' : 'inactivo'}">
                            <c:choose>
                                <c:when test="${p.activo}">Activo</c:when>
                                <c:otherwise>Inactivo</c:otherwise>
                            </c:choose>
                        </td>
                    </tr>
                </c:forEach>
            </c:otherwise>
        </c:choose>
        </tbody>
    </table>

    <!-- Formulario -->
    <h2>Agregar nuevo producto</h2>
    <form method="post" action="${pageContext.request.contextPath}/productos">
        <input name="codigo" placeholder="Código" required minlength="3"/>
        <input name="nombre" placeholder="Nombre del producto" required minlength="5"/>
        <select name="categoria" required>
            <option disabled selected>Seleccionar categoría</option>
            <option>Electronicos</option>
            <option>Accesorios</option>
            <option>Muebles</option>
            <option>Ropa</option>
        </select>
        <input name="precio" type="number" step="0.01" min="0.01" placeholder="Precio" required/>
        <input name="stock" type="number" min="0" placeholder="Stock" required/>
        <label>
            <input name="activo" type="checkbox" checked/> Activo
        </label>
        <button type="submit">💾 Guardar producto</button>
    </form>
</main>

<footer>
    © 2025 — Proyecto académico de Inventario en Java EE
</footer>

</body>
</html>
