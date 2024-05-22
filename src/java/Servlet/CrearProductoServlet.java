package Servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "CrearProductoServlet", urlPatterns = {"/CrearProductoServlet"})
public class CrearProductoServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        // Extracting form data
        int idProducts = Integer.parseInt(request.getParameter("idProducts"));
        String nombreProducto = request.getParameter("nombreProducto");
        String descripcion = request.getParameter("descripcion");
        double precio = Double.parseDouble(request.getParameter("precio"));
        String categoria = request.getParameter("categoria");

        // Database connection details
        String jdbcURL = "jdbc:mysql://localhost:3306/morganastore?useSSL=false";
        String dbUser = "root";
        String dbPassword = "123456789";
        String sql = "INSERT INTO productos (idProducts, nombreProducto, descripcion, precio, categoria) VALUES (?, ?, ?, ?, ?)";

        PrintWriter out = response.getWriter();
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            // Load the MySQL driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Check for duplicate ID
            String sqlCheck = "SELECT * FROM productos WHERE idProducts = ?";
            connection = DriverManager.getConnection(jdbcURL, dbUser, dbPassword);
            PreparedStatement checkStatement = connection.prepareStatement(sqlCheck);
            checkStatement.setInt(1, idProducts);
            ResultSet resultSet = checkStatement.executeQuery();

            if (resultSet.next()) {
                // ID already exists, display alert
                out.println("<!DOCTYPE html>");
                out.println("<html>");
                out.println("<head>");
                out.println("<title>Error: Producto no creado</title>");
                out.println("</head>");
                out.println("<body>");
                out.println("<h1><script>alert('Error: Ya existe un producto con el ID ingresado.')</script>Producto no creado</h1>");
                out.println("<p>Por favor, ingrese un ID único para el producto.</p>");
                out.println("</body>");
                out.println("</html>");
            } else {
                // Proceed with product creation if ID is unique
                statement = connection.prepareStatement(sql);
                statement.setInt(1, idProducts);
                statement.setString(2, nombreProducto);
                statement.setString(3, descripcion);
                statement.setDouble(4, precio);
                statement.setString(5, categoria);

                int rows = statement.executeUpdate();
                if (rows > 0) {
                    out.println("<!DOCTYPE html>");
                    out.println("<html>");
                    out.println("<head>");
                    out.println("<title>Producto Creado</title>");
                    out.println("</head>");
                    out.println("<body>");
                    out.println("<h1>Producto Creado con éxito</h1>");
                    out.println("<p>Id producto: " + idProducts + "</p>");
                    out.println("<p>Nombre: " + nombreProducto + "</p>");
                    out.println("<p>Descripción: " + descripcion + "</p>");
                    out.println("<p>Precio: " + precio + "</p>");
                    out.println("<p>Categoría ID: " + categoria + "</p>");
                    out.println("</body>");
                    out.println("</html>");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(out); // Print the stack trace to the response
            out.println("<p>Error al crear el producto: " + e.getMessage() + "</p>");
        } catch (ClassNotFoundException e) {
            e.printStackTrace(out); // Print the stack trace to the response
            out.println("<p>Error al cerrar la conexión: " + e.getMessage() + "</p>");
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace(out); // Print the stack trace to the response
                out.println("<p>Error al cerrar la conexión: " + e.getMessage() + "</p>");
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
}
