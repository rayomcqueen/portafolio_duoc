package cl.controlador;

import cl.dominio.DetalleDevolucion;
import cl.dominio.Devolucion;
import cl.dominio.HistorialClienteDevolucion;
import cl.dominio.Prestamo;
import cl.dominio.Usuario;
import cl.dto.DetallePrestamoDTO;
import cl.dto.UsuarioPrestamoDTO;
import cl.servicio.Servicio;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
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
 * @author cristian
 */
@WebServlet(name = "AdminDevolucionServlet", urlPatterns = {"/AdminDevolucionServlet"})
public class AdminDevolucionServlet extends HttpServlet {

    @Resource(mappedName = "jdbc/portafolio")
    private DataSource ds;

    static ArrayList<HistorialClienteDevolucion> listaHcd = new ArrayList<>();
    static ArrayList<DetalleDevolucion> listaDev = new ArrayList<>();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idPrestamo = request.getParameter("idPrestamo");
        Map<String, String> mapMensaje = new HashMap<>();
        ArrayList<DetallePrestamoDTO> lista = new ArrayList();
        ArrayList<UsuarioPrestamoDTO> listaUsuario = new ArrayList();

        HttpSession session = request.getSession();

        Usuario usuarioS = (Usuario) session.getAttribute("usuarioSesion");

        if (usuarioS == null) {
            request.getRequestDispatcher("index.jsp").forward(request, response);
        } else {

            try (Connection con = ds.getConnection()) {

                Servicio servicio = new Servicio(con);

                if (idPrestamo.isEmpty()) {
                    mapMensaje.put("errorPrestamo", "**Debe ingresar el número de ticket**");
                } else {
                    lista = servicio.buscarDetallePrestamoPorId(Integer.parseInt(idPrestamo));
                    listaUsuario = servicio.buscarUsuarioPrestamoPorId(Integer.parseInt(idPrestamo));
                }

                if (!idPrestamo.isEmpty() && lista.isEmpty()) {
                    mapMensaje.put("errorPrestamo", "**El número de ticket no es válido**");
                }

                if (mapMensaje.isEmpty()) {

                    request.setAttribute("lstUsuarioPrestamo", listaUsuario);
                    request.setAttribute("lstDetallePrestamo", lista);
                    request.getRequestDispatcher("AdminDevolucion.jsp").forward(request, response);
                } else {

                    request.setAttribute("mensajeError", mapMensaje);
                    request.getRequestDispatcher("AdminDevolucion.jsp").forward(request, response);
                }

            } catch (SQLException e) {
                throw new RuntimeException("Error en la conexión a bd", e);
            }

        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String idItemSeleccionado = request.getParameter("nroSerieOculto");
        String observacion = request.getParameter("observacion");
        String descripcion = request.getParameter("opcionDevolucion");

        String idPrestamo = request.getParameter("idPrestamoPost");
        Map<String, String> mapMensaje = new HashMap<>();
        ArrayList<DetallePrestamoDTO> lista = new ArrayList();
        ArrayList<UsuarioPrestamoDTO> listaUsuario = new ArrayList();
        Usuario us = new Usuario();
        Usuario usPanolero = new Usuario();
        HistorialClienteDevolucion hcd = new HistorialClienteDevolucion();
        DetalleDevolucion detalleDevolucion = new DetalleDevolucion();

        HttpSession session = request.getSession();

        Usuario usuarioS = (Usuario) session.getAttribute("usuarioSesion");

        if (usuarioS == null) {
            request.getRequestDispatcher("index.jsp").forward(request, response);
        } else {

            try (Connection con = ds.getConnection()) {

                Servicio servicio = new Servicio(con);

                lista = servicio.buscarDetallePrestamoPorId(Integer.parseInt(idPrestamo));
                listaUsuario = servicio.buscarUsuarioPrestamoPorId(Integer.parseInt(idPrestamo));

                for (int i = 0; i <= 0; i++) {

                    us = listaUsuario.get(i).getUsuario();
                    usPanolero = lista.get(i).getUsuario();
                }

                int idDevolucion = servicio.idDevolucionDisponible();

                Timestamp fecha = new Timestamp(System.currentTimeMillis());

                hcd.setIdDevolucion(idDevolucion);
                hcd.setFecha(fecha);
                hcd.setDescripcion(descripcion);
                hcd.setNroSerie(idItemSeleccionado);
                hcd.setObservacion(observacion);
                hcd.setRut(us.getRut());

                detalleDevolucion.setIdDevolucion(idDevolucion);
                detalleDevolucion.setNroSerie(idItemSeleccionado);
                detalleDevolucion.setRut(usPanolero.getRut());

                //registro de devolución items
                int indice = -1;

                for (int i = 0; i < listaHcd.size(); i++) {
                    HistorialClienteDevolucion hcd1 = listaHcd.get(i);
                    if (hcd1.getNroSerie().equals(hcd.getNroSerie())) {

                        indice = i;

                        break;
                    }
                }

                if (indice == -1) {
                    listaHcd.add(hcd);
                    listaDev.add(detalleDevolucion);
                } else {
                    hcd.setDescripcion(descripcion);
                    hcd.setObservacion(observacion);

                    listaHcd.set(indice, hcd);
                }
                //fin registro de devolución

                request.setAttribute("idItemServlet", request.getParameter("nroSerieOculto"));

                request.setAttribute("observacion", hcd.getObservacion());
                request.setAttribute("contadorDetalle", lista.size());
                request.setAttribute("contadorHcd", listaHcd.size());
                request.setAttribute("lstHistorial", listaHcd);
                request.setAttribute("lstDetalleDev", idDevolucion);
                request.setAttribute("lstUsuarioPrestamo", listaUsuario);
                request.setAttribute("lstDetallePrestamo", lista);
                request.getRequestDispatcher("AdminDevolucion.jsp").forward(request, response);

            } catch (SQLException e) {
                throw new RuntimeException("Error en la conexión a bd", e);
            }

        }
    }

}
