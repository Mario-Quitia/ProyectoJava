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

@WebServlet(name = "editarybuscarServlet", urlPatterns = {"/editarybuscarServlet"})
public class editarybuscarServlet extends HttpServlet {

    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/morganastore?useSSL=false";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "123456789";

    private Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("Error al cargar el controlador JDBC: " + e.getMessage());
        }
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String action = request.getParameter("action");

        int idProducts = Integer.parseInt(request.getParameter("idProducts"));
        String nombreProducto = request.getParameter("nombreProducto");
        String descripcion = request.getParameter("descripcion");
        double precio = 0;
        if (request.getParameter("precio") != null && !request.getParameter("precio").isEmpty()) {
            precio = Double.parseDouble(request.getParameter("precio"));
        }
        String categoria = request.getParameter("categoria");

        PrintWriter out = response.getWriter();

        try (Connection connection = getConnection()) {
            if ("buscar".equals(action)) {
                buscarProducto(out, connection, idProducts);
            } else if ("editar".equals(action)) {
                editarProducto(out, connection, idProducts, nombreProducto, descripcion, precio, categoria);
            } else if ("eliminar".equals(action)) {
                eliminarProducto(out, connection, idProducts);
            }
        } catch (SQLException e) {
            e.printStackTrace(out);
            out.println("<p>Error en la operación: " + e.getMessage() + "</p>");
        }
    }

    private void buscarProducto(PrintWriter out, Connection connection, int idProducts) throws SQLException {
        String sql = "SELECT * FROM productos WHERE idProducts = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, idProducts);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    out.println("<h1>Producto Encontrado</h1>");
                    out.println("<p>Id producto: " + resultSet.getInt("idProducts") + "</p>");
                    out.println("<p>Nombre: " + resultSet.getString("nombreProducto") + "</p>");
                    out.println("<p>Descripción: " + resultSet.getString("descripcion") + "</p>");
                    out.println("<p>Precio: " + resultSet.getDouble("precio") + "</p>");
                    out.println("<p>Categoría: " + resultSet.getString("categoria") + "</p>");
                } else {
                    out.println("<h1>Producto No Encontrado</h1>");
                }
            }
        }
    }

    private void editarProducto(PrintWriter out, Connection connection, int idProducts, String nombreProducto, String descripcion, double precio, String categoria) throws SQLException {
        String sql = "UPDATE productos SET nombreProducto = ?, descripcion = ?, precio = ?, categoria = ? WHERE idProducts = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, nombreProducto);
            statement.setString(2, descripcion);
            statement.setDouble(3, precio);
            statement.setString(4, categoria);
            statement.setInt(5, idProducts);

            int rows = statement.executeUpdate();
            if (rows > 0) {
                out.println("<h1>Producto Actualizado</h1>");
            } else {
                out.println("<h1>Error al Actualizar Producto</h1>");
            }
        }
    }

    private void eliminarProducto(PrintWriter out, Connection connection, int idProducts) throws SQLException {
        String sql = "DELETE FROM productos WHERE idProducts = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, idProducts);

            int rows = statement.executeUpdate();
            if (rows > 0) {
                out.println("<h1>Producto Eliminado</h1>");
            } else {
                out.println("<h1>Error al Eliminar Producto</h1>");
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
