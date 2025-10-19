# 🧩 AppInventario

## 🎯 Objetivo general
Desarrollar un sistema de gestión de productos (**inventario**) con un **CRUD funcional**, aplicando **arquitectura en capas** y **buenas prácticas de desarrollo con Java EE**.  
El sistema permite listar, registrar y eliminar productos, validando las reglas de negocio definidas en el modelo.

---

## 🧠 Arquitectura del sistema
El sistema sigue la **arquitectura en capas típica** de aplicaciones empresariales Java EE, inspirada en el **patrón MVC**:

Presentación (Vista) → Controlador (Servlet) → Lógica (Fachada) → Persistencia (DAO) → Modelo (Entidad)

markdown
Copiar código

---

## 🧱 Capas y responsabilidades

| Capa | Paquete | Descripción |
|------|----------|-------------|
| **Modelo (Entidad)** | `com.inventario.model` | Representa las entidades de la BD (`Producto`). Contiene atributos, getters/setters y métodos auxiliares (`isDisponible()`). |
| **DAO (Acceso a datos)** | `com.inventario.persistence` | Ejecuta operaciones SQL (`SELECT`, `INSERT`, `DELETE`). Se comunica directamente con **MySQL**. |
| **Fachada (Lógica de negocio)** | `com.inventario.facade` | Centraliza las validaciones y orquesta las llamadas al DAO. Aplica reglas de negocio (`precio > 0`, `código único`, etc.). |
| **Controlador (Servlet)** | `com.inventario.controller` | Gestiona las peticiones HTTP (`GET` y `POST`). Llama a la fachada y envía los datos a la vista. |
| **Vista (JSP)** | `/web/productos.jsp` | Presenta los datos al usuario con **JSTL** y **HTML5 moderno**. Permite crear nuevos productos mediante formulario. |

---

## 📐 Convenciones de nombres

| Elemento | Convención | Ejemplo |
|-----------|-------------|----------|
| **Clases** | PascalCase | `Producto`, `ProductoDAO`, `ProductoFacade` |
| **Paquetes** | minúsculas | `com.inventario.controller` |
| **Métodos** | camelCase | `listarProductos()`, `buscarPorCodigo()` |
| **Atributos** | camelCase | `codigo`, `nombreProducto`, `precioUnitario` |
| **Constantes** | MAYÚSCULAS_CON_GUION | `MAX_STOCK = 1000` |
| **Tablas SQL** | minúsculas (plural) | `productos` |
| **Campos SQL** | minúsculas_subrayado | `precio_unitario` |
| **URLs / Servlets** | minúsculas | `/productos` |
| **Archivos JSP** | minúsculas | `productos.jsp` |

---

## ⚙️ Requisitos previos

| Herramienta | Versión recomendada |
|--------------|--------------------|
| **Java JDK** | 8 o superior |
| **Apache NetBeans IDE** | 12+ |
| **GlassFish Server** | 5.0 / 6.2 |
| **MySQL Server** | 8.x |
| **MySQL Connector/J** | 8.x *(colocar en `glassfish/lib`)* |

---

## 🧩 Estructura del proyecto

AppInventario/
│
├── src/java/
│ ├── com/inventario/model/Producto.java
│ ├── com/inventario/persistence/ProductoDAO.java
│ ├── com/inventario/facade/ProductoFacade.java
│ └── com/inventario/controller/ProductoServlet.java
│
├── web/
│ └── productos.jsp
│
├── WEB-INF/
│ ├── glassfish-web.xml
│ └── web.xml
│
└── README.md

pgsql
Copiar código

---

## 💾 Base de datos — `inventario_db`

### Script SQL

```sql
CREATE DATABASE inventario_db;
USE inventario_db;

CREATE TABLE productos (
  id INT AUTO_INCREMENT PRIMARY KEY,
  codigo VARCHAR(32) NOT NULL UNIQUE,
  nombre VARCHAR(120) NOT NULL,
  categoria VARCHAR(60) NOT NULL,
  precio DECIMAL(12,2) NOT NULL,
  stock INT NOT NULL DEFAULT 0,
  activo TINYINT(1) NOT NULL DEFAULT 1
);

INSERT INTO productos (codigo, nombre, categoria, precio, stock, activo) VALUES
('ABC123', 'Mouse Logitech M170', 'Electronicos', 79.99, 10, 1);
🚀 Pasos para ejecutar el sistema
1️⃣ Crear la base de datos
Ejecuta el script anterior en MySQL Workbench o desde la terminal:

bash
Copiar código
mysql -u root -p < inventario_db.sql
2️⃣ Configurar el Pool JDBC en GlassFish
Copia mysql-connector-j-8.x.x.jar a la carpeta:

bash
Copiar código
glassfish5/glassfish/lib/
Reinicia GlassFish y luego en la consola de administración:

Ruta:
Resources → JDBC → Connection Pools → New

Configuración:

Nombre: InventarioPool

Resource Type: javax.sql.DataSource

Database Vendor: MySQL

Propiedades:

bash
Copiar código
URL = jdbc:mysql://localhost:3306/inventario_db?serverTimezone=UTC&allowPublicKeyRetrieval=true&useSSL=false
User = inv_user
Password = inv_pass
Driver Classname = com.mysql.cj.jdbc.Driver
Haz clic en Ping → debe mostrar “Ping Succeeded”.

Luego crea el recurso JNDI:
Resources → JDBC → JDBC Resources → New

JNDI Name: jdbc/inventarioPool

Pool Name: InventarioPool

3️⃣ Configurar el proyecto en NetBeans
Crea un nuevo proyecto Web Java EE con GlassFish.

Asegúrate de usar Java EE 8.

Agrega los paquetes y clases del sistema.

Coloca productos.jsp en /web/.

4️⃣ Desplegar el proyecto
En NetBeans:
Clic derecho en el proyecto → Run o Deploy.

Verifica que aparezca el mensaje:
Application deployed successfully.

Abre en el navegador:
👉 http://localhost:8080/InventarioWeb/productos

5️⃣ Probar el CRUD
Agrega un nuevo producto con el formulario.

Verifica que aparezca en la tabla.

Intenta insertar un código duplicado → debe mostrar un error.

Revisa los datos directamente en MySQL:

sql
Copiar código
SELECT * FROM productos;
🧠 Reglas de negocio implementadas
Código ≥ 3 caracteres y único

Nombre ≥ 5 caracteres

Categoría ∈ {Electronicos, Accesorios, Muebles, Ropa}

Precio > 0

Stock ≥ 0

Activo = true/false

💡 Decisiones técnicas y diseño
Patrón DAO + Fachada para separación de responsabilidades

Uso de PreparedStatement para prevenir inyección SQL

Patrón PRG (Post/Redirect/Get) para evitar reenvíos duplicados

Validaciones HTML5 y de negocio en el servidor

Vista moderna y responsiva en JSP con CSS puro

Sistema preparado para extenderse a módulos de proveedores, usuarios y ventas

🧱 Modelo Entidad–Relación (ER)
diff
Copiar código
+-------------------+
|     PRODUCTOS     |
+-------------------+
| id         (PK)   |
| codigo     (UQ)   |
| nombre            |
| categoria         |
| precio            |
| stock             |
| activo            |
+-------------------+
📌 PK: Clave primaria
📌 UQ: Clave única

Cumple 3FN (Tercera Forma Normal).
Base sólida para futuras entidades relacionadas (proveedores, ventas, usuarios).

📈 Diagrama UML (flujo de arquitectura)
nginx
Copiar código
Producto
   │
   │ usa
   ↓
ProductoDAO
   │
   │ llamado por
   ↓
ProductoFacade
   │
   │ invocado desde
   ↓
ProductoServlet
   │
   │ muestra datos en
   ↓
productos.jsp
