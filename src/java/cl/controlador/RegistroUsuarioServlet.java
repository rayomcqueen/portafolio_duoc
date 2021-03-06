/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.controlador;

import cl.dominio.Carrera;
import cl.dominio.Perfil;
import cl.dominio.Usuario;
import cl.persistencia.UsuarioDAO;
import cl.servicio.Servicio;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

/**
 *
 * @author Doterzer
 */
@WebServlet(name = "RegistroUsuarioServlet", urlPatterns = {"/RegistroUsuarioServlet"})
public class RegistroUsuarioServlet extends HttpServlet {

    @Resource(mappedName = "jdbc/portafolio")
    private DataSource ds;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Map<String, String> mapMensaje = new HashMap<>();
        mapMensaje.put("errorRut", "**Ingrese solo números**");

        HttpSession session = request.getSession();

        Usuario usuarioS = (Usuario) session.getAttribute("usuarioSesion");

        if (usuarioS == null) {
            request.getRequestDispatcher("index.jsp").forward(request, response);
        } else if (usuarioS.getIdPerfil() == 120) {
            request.getRequestDispatcher("HomePanolero.jsp").forward(request, response);
        } else {

            try (Connection con = ds.getConnection()) {

                Servicio servicio = new Servicio(con);

                ArrayList<Carrera> lstCarreras = servicio.listarCarreras();
                ArrayList<Perfil> lstPerfiles = servicio.listarPerfilesFiltro(usuarioS.getIdPerfil());

                request.setAttribute("mapMensajeGet", mapMensaje);
                request.setAttribute("lstCarreras", lstCarreras);
                request.setAttribute("lstPerfiles", lstPerfiles);
                request.getRequestDispatcher("RegistroUsuario.jsp").forward(request, response);

            } catch (SQLException e) {
                throw new RuntimeException("Error en la conexion bd", e);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();

        Usuario usuarioS = (Usuario) session.getAttribute("usuarioSesion");

        String rut = request.getParameter("rut");
        String digito = request.getParameter("digito");
        String nombres = request.getParameter("nombres");
        String apellidos = request.getParameter("apellidos");
        String telefono = request.getParameter("telefono");
        String direccion = request.getParameter("direccion");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String perfil = request.getParameter("seleccionPerfil");
        String rutCompleto = "";
        Map<String, String> mapMensaje = new HashMap<>();
        Map<String, String> mapMensajeExito = new HashMap<>();

        if (usuarioS == null) {
            request.getRequestDispatcher("index.jsp").forward(request, response);
        } else if (usuarioS.getIdPerfil() == 120) {
            request.getRequestDispatcher("HomePanolero.jsp").forward(request, response);
        } else {

            try (Connection con = ds.getConnection()) {

                Servicio servicio = new Servicio(con);
                ArrayList<Perfil> lstPerfiles = servicio.listarPerfilesFiltro(usuarioS.getIdPerfil());
                ArrayList<Carrera> lstCarreras = servicio.listarCarreras();

                if (!servicio.verificadorRut(rut, digito)) {
                    mapMensaje.put("errorRut", "**Ingrese un rut válido**");
                } else {
                    rutCompleto = rut + digito;
                }

                ArrayList<Usuario> lista = servicio.buscarUsuarioRut(rutCompleto.toUpperCase());

                Usuario usuarioExiste = null;

                for (Usuario usuario : lista) {
                    usuarioExiste = new Usuario();
                    usuarioExiste.setRut(usuario.getRut());
                    usuarioExiste.setNombres(usuario.getNombres());
                    usuarioExiste.setApellidos(usuario.getApellidos());
                    usuarioExiste.setTelefono(usuario.getTelefono());
                    usuarioExiste.setDireccion(usuario.getDireccion());
                    usuarioExiste.setEmail(usuario.getEmail());
                    usuarioExiste.setPassword(usuario.getPassword());
                    usuarioExiste.setActivo(usuario.getActivo());
                    usuarioExiste.setIdPerfil(usuario.getIdPerfil());
                    usuarioExiste.setIdCarrera(usuario.getIdCarrera());
                }

                Usuario usuario = new Usuario();

                if (rut.isEmpty()) {
                    mapMensaje.put("errorRut", "**Debe ingresar rut**");
                } else if (!rut.isEmpty() && rut.length() > 8) {
                    mapMensaje.put("errorRut", "**El valor es demasiado largo**");
                } else if (usuarioExiste != null) {
                    mapMensaje.put("errorRut", "**El rut ya está registrado**");
                } else {
                    usuario.setRut(rutCompleto.toUpperCase());
                }

                if (nombres.isEmpty()) {
                    mapMensaje.put("errorNombre", "**Debe ingresar nombre**");
                } else if (!nombres.isEmpty() && nombres.length() > 50) {
                    mapMensaje.put("errorNombre", "**El valor es demasiado largo**");
                } else {
                    usuario.setNombres(nombres);
                }

                if (apellidos.isEmpty()) {
                    mapMensaje.put("errorApellidos", "**Debe ingresar apellido**");
                } else if (!apellidos.isEmpty() && apellidos.length() > 50) {
                    mapMensaje.put("errorApellidos", "**El valor es demasiado largo**");
                } else {
                    usuario.setApellidos(apellidos);
                }

                if (telefono.isEmpty()) {
                    mapMensaje.put("errorTelefono", "**Debe ingresar un teléfono**");
                } else if (telefono.length() > 10) {
                    mapMensaje.put("errorTelefono", "**El número es inválido**");
                } else {
                    usuario.setTelefono(telefono);
                }

                if (direccion.isEmpty()) {
                    mapMensaje.put("errorDireccion", "**Debe ingresar una dirección**");
                } else if (!direccion.isEmpty() && direccion.length() > 200) {
                    mapMensaje.put("errorDireccion", "**El contenido es demasiado largo**");
                } else {
                    usuario.setDireccion(direccion);
                }

                if (email.isEmpty()) {
                    mapMensaje.put("errorEmail", "**Debe ingresar email**");
                } else if (!email.isEmpty() && email.length() > 50) {
                    mapMensaje.put("errorEmail", "**El valor es demasiado largo**");
                } else {
                    usuario.setEmail(email);
                }

                if (password.isEmpty()) {
                    mapMensaje.put("errorPassword", "**Debe ingresar password**");
                } else if (!password.isEmpty() && password.length() > 10) {
                    mapMensaje.put("errorPassword", "**Contraseña demasiado larga**");
                } else {
                    usuario.setPassword(password);
                }

                if (perfil.equals("0")) {
                    mapMensaje.put("errorPerfil", "**Debe ingresar un perfil**");
                } else {
                    int idPerfil = Integer.parseInt(perfil);
                    usuario.setIdPerfil(idPerfil);
                }

                if (Integer.parseInt(perfil) == 130) {
                    int idCarrera = Integer.parseInt(request.getParameter("seleccionCarrera"));
                    usuario.setIdCarrera(idCarrera);
                } else {
                    usuario.setIdCarrera(0);
                }

                /*
                if (request.getParameter("carrera") != null) {
                    int idCarrera = Integer.parseInt(request.getParameter("carrera"));
                    usuario.setIdCarrera(idCarrera);
                } else {
                    usuario.setIdCarrera(0);
                }
                 */
                usuario.setActivo((byte) 1);

                if (mapMensaje.isEmpty()) {

                    servicio.registrarUsuario(usuario);
                    mapMensajeExito.put("mensaje", "El usuario fue registrado con éxito");
                    request.setAttribute("mapMensajeExito", mapMensajeExito);
                    request.setAttribute("lstPerfiles", lstPerfiles);
                    request.setAttribute("lstCarreras", lstCarreras);
                    request.getRequestDispatcher("/RegistroUsuario.jsp").forward(request, response);
                } else {
                    request.setAttribute("mapMensaje", mapMensaje);
                    request.setAttribute("lstPerfiles", lstPerfiles);
                    request.setAttribute("lstCarreras", lstCarreras);
                    request.getRequestDispatcher("RegistroUsuario.jsp").forward(request, response);
                }

            } catch (SQLException e) {
                throw new RuntimeException("Error en la conexión a bd", e);
            }

        }

    }

}
