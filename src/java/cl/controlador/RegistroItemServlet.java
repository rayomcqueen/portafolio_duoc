/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.controlador;

import cl.dominio.Item;
import cl.dominio.Usuario;
import cl.dto.ProductoMarcaDTO;
import cl.servicio.Servicio;
import java.io.IOException;
import static java.lang.Integer.parseInt;
import java.sql.Connection;
import java.sql.SQLException;
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
@WebServlet(name = "RegistroItemServlet", urlPatterns = {"/RegistroItemServlet"})
public class RegistroItemServlet extends HttpServlet {

    @Resource(mappedName = "jdbc/portafolio")
    private DataSource ds;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();

        Usuario usuarioS = (Usuario) session.getAttribute("usuarioSesion");

        if (usuarioS == null) {
            request.getRequestDispatcher("index.jsp").forward(request, response);
        } else {

            try (Connection con = ds.getConnection()) {

                int idProducto = parseInt(request.getParameter("idProducto"));

                Servicio servicio = new Servicio(con);

                ArrayList<ProductoMarcaDTO> listaProductos = servicio.productosPorId(idProducto);

                request.setAttribute("lstProductos", listaProductos);
                request.getRequestDispatcher("RegistroItem.jsp").forward(request, response);

            } catch (SQLException e) {
                throw new RuntimeException("Error en la conexion bd", e);
            }

        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String nroSerie = request.getParameter("nroserie");
        int idProducto = parseInt(request.getParameter("idProducto"));
        Map<String, String> mapMensaje = new HashMap<>();
        Item item = new Item();
        ArrayList<Item> listaItem = new ArrayList<>();

        HttpSession session = request.getSession();

        Usuario usuarioS = (Usuario) session.getAttribute("usuarioSesion");

        if (usuarioS == null) {
            request.getRequestDispatcher("index.jsp").forward(request, response);
        } else {

            try (Connection con = ds.getConnection()) {

                Servicio servicio = new Servicio(con);

                listaItem = servicio.itemPorId(nroSerie);

                try {

                    if (nroSerie.isEmpty()) {
                        mapMensaje.put("mensaje", "Debe ingresar el Nro de serie");
                    } else if (!listaItem.isEmpty()) {
                        mapMensaje.put("mensaje", "El elemento ya existe en inventario");
                    } else {
                        item.setNroSerie(nroSerie);
                        item.setActivo((byte) 1);
                        item.setIdProducto(idProducto);
                        item.setPrestamo((byte) 0);
                        mapMensaje.put("mensaje", "El elemento fue exitosamente registrado");
                        servicio.registrarItem(item);
                    }

                } catch (Exception e) {
                    throw new RuntimeException("Error registrar item", e);
                }

                ArrayList<ProductoMarcaDTO> listaProductos = servicio.productosPorId(idProducto);
                request.setAttribute("mapMensaje", mapMensaje);
                request.setAttribute("lstProductos", listaProductos);
                request.getRequestDispatcher("RegistroItem.jsp").forward(request, response);

            } catch (SQLException e) {
                throw new RuntimeException("Error en la conexion bd", e);
            }

        }
    }

}
