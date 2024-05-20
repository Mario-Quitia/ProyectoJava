package Servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
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
        String nombreProducto = request.getParameter("nombreProducto");
        String descripcion = request.getParameter("descripcion");
        double precio = Double.parseDouble(request.getParameter("precio"));
        String categoria = request.getParameter("categoria");
        
        // Database connection details
       String jdbcURL = "jdbc:mysql://localhost:3306/morganastore?useSSL=false";
        String dbUser = "root";
        String dbPassword = "123456789";
        String sql = "INSERT INTO productos (nombreProducto, descripcion, precio, categoria) VALUES (?, ?, ?, ?)";

        PrintWriter out = response.getWriter();
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            // Load the MySQL driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // Establish a connection
            connection = DriverManager.getConnection(jdbcURL, dbUser, dbPassword);
            
            // Create a statement
            statement = connection.prepareStatement(sql);
            statement.setString(1, nombreProducto);
            statement.setString(2, descripcion);
            statement.setDouble(3, precio);
            statement.setString(4, categoria);

            // Execute the statement
            int rows = statement.executeUpdate();
            if (rows > 0) {
                out.println("<!DOCTYPE html>");
                out.println("<html>");
                out.println("<head>");
                out.println("<title>Producto Creado</title>");            
                out.println("</head>");
                out.println("<body>");
                out.println("<h1>Producto Creado con éxito</h1>");
                out.println("<p>Nombre: " + nombreProducto + "</p>");
                out.println("<p>Descripción: " + descripcion + "</p>");
                out.println("<p>Precio: " + precio + "</p>");
                out.println("<p>Categoría ID: " + categoria + "</p>");
                out.println("</body>");
                out.println("</html>");
            }
        } catch (SQLException e) {
            e.printStackTrace(out);  // Print the stack trace to the response
            out.println("<p>Error al crear el producto: " + e.getMessage() + "</p>");
        } catch (ClassNotFoundException e) {
            e.printStackTrace(out);  // Print the stack trace to the response
            out.println("<p>Error: Driver no encontrado.</p>");
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace(out);  // Print the stack trace to the response
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
    public String getServletInfo() {
        return "CrearProductoServlet handles the product creation form data";
    }
}
