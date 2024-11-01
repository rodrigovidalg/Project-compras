package Controlador;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

import Modelo.Empleado;
import Modelo.Usuario;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 *
 * @author user
 */
@WebServlet(urlPatterns = {"/sr_cIndex"})
public class sr_cIndex extends HttpServlet {
 Empleado empleado = new Empleado();
Usuario usuario= new Usuario();


    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet sr_cIndex</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet sr_cIndex at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String user = request.getParameter("txt_user");
        String pass = request.getParameter("txt_pass");

        // 2. Validar el usuario
        empleado = usuario.validar(user, pass); // Retorna un objeto Empleado

        if (empleado != null) {
            // Crear una sesión y almacenar los datos del usuario
            HttpSession session = request.getSession();
            session.setAttribute("empleado", empleado);

            // Obtener la URI solicitada
            String uri = request.getRequestURI();

            // Verificar el rol del usuario y redirigir según corresponda
            String rol = empleado.getRol();
            boolean accesoPermitido = false;

            switch (rol) {
                case "admin":
                    accesoPermitido = true; // Admin tiene acceso a todo
                    break;
                case "Ventas":
                    if (uri.contains("Principal.jsp") || uri.contains("Registro_venta.jsp")) {
                        accesoPermitido = true;
                    }
                    break;
                case "Compras":
                    if (uri.contains("Principal.jsp") || uri.contains("Registro_compra.jsp")) {
                        accesoPermitido = true;
                    }
                    break;
                case "Bodega":
                    if (uri.contains("Principal.jsp") || uri.contains("Producto.jsp")) {
                        accesoPermitido = true;
                    }
                    break;
                case "Clientes":
                    if (uri.contains("Principal.jsp") || uri.contains("Cliente.jsp")) {
                        accesoPermitido = true;
                    }
                    break;
                case "RRHH":
                    if (uri.contains("Principal.jsp") || uri.contains("Empleado.jsp")) {
                        accesoPermitido = true;
                    }
                    break;
                default:
                    break;
            }

            // Redirigir o mostrar mensaje si no tiene acceso
            if (accesoPermitido) {
                response.sendRedirect(uri); // Redirigir a la página solicitada
            } else {
                // Mostrar mensaje de acceso denegado y redirigir a Principal.jsp
                request.setAttribute("error", "No tiene acceso a esa sección.");
                request.getRequestDispatcher("Principal.jsp").forward(request, response);
            }
        } else {
            // Enviar mensaje de error y volver a index.jsp
            request.setAttribute("error", "Usuario o contraseña incorrectos");
            request.getRequestDispatcher("index.jsp").forward(request, response);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
