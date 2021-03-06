<%-- 
    Document   : RegistroPrestamo
    Created on : 13-may-2016, 10:07:38
    Author     : cristian
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<c:choose>
    <c:when test="${usuarioSesion.idPerfil == 100}">
        <c:redirect url="HomeJefeCarrera.jsp"></c:redirect>
    </c:when>
    <c:when test="${usuarioSesion.idPerfil == 110}">
        <c:redirect url="HomeCoordinador.jsp"></c:redirect>
    </c:when>
    <c:when test="${empty usuarioSesion}">
        <c:redirect url="index.jsp"></c:redirect>
    </c:when>
</c:choose>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" type="text/css" href="css/bootstrap.min.css"/>
        <link rel="stylesheet" type="text/css" href="css/nuevosEstilos.css"/>
        <link href='https://fonts.googleapis.com/css?family=Open+Sans+Condensed:300' rel='stylesheet' type='text/css'>
        <link href='https://fonts.googleapis.com/css?family=Palanquin' rel='stylesheet' type='text/css'>
        <title>Administración Préstamos</title>
    </head>
    <body>
        <%@include file="header.jsp" %>
        <div class="container">
            <h3 class="text-center" id="titulo-pagina">Administración sistema Pañol</h3>
            <h5 class="text-center" id="titulo-pagina">Escuela de comunicaciones - Duoc UC</h5>
            <div class="row col-lg-offset-0">
                <div class="col-md-0"></div>
                <div class="col-md-12">
                    <div class="centered-pills">
                        <ul class="nav nav-pills" id="palanquin-font">
                            <c:choose>
                                <c:when test="${usuarioSesion.idPerfil == 100}">
                                    <li role="presentation"><a href="<c:url value="HomeJefeCarrera.jsp"/>">Inicio</a></li>
                                    </c:when>
                                    <c:when test="${usuarioSesion.idPerfil == 120}">
                                    <li role="presentation"><a href="<c:url value="HomePanolero.jsp"/>">Inicio</a></li>
                                    </c:when>
                                    <c:when test="${usuarioSesion.idPerfil == 110}">
                                    <li role="presentation"><a href="<c:url value="HomeCoordinador.jsp"/>">Inicio</a></li>
                                    </c:when>
                                </c:choose>
                            <li role="presentation"><a href="<c:url value="AdminSolicitudes.jsp"/>">Administrar solicitudes</a></li>
                            <li role="presentation"><a href="<c:url value="AdminPrestamos.jsp"/>">Préstamos</a></li>
                            <li role="presentation"><a href="<c:url value="AdminDevolucion.jsp"/>">Devoluciones</a></li>
                        </ul>
                    </div>
                    <br>
                    <c:if test="${not empty mapMensajeExito}">
                        <div class="bg-success" id="danger-box">
                            <h5 class="text-center text-success"><c:out value="${mapMensajeExito['mensajeExito']}"/></h5>
                        </div>
                    </c:if>
                    <br>
                    <div class="row">
                        <div class="col-md-12">
                            <br>
                            <div class="col-md-4 col-lg-offset-2">
                                <h2>Detalle Préstamo:</h2>
                                <hr>
                                <h5>Datos solicitante:</h5>
                                <c:forEach end="0" var="dato" items="${lstSolicitud}">
                                    <label id="info-form">Rut: <c:out value="${dato.usuario.rut}"/></label>
                                    <br>
                                    <label id="info-form">Nombre: <c:out value="${dato.usuario.nombres} ${dato.usuario.apellidos}"/></label>
                                    <br>
                                    <c:choose>
                                        <c:when test="${empty dato.carrera.descripcion}">
                                            <label id="info-form">Carrera: No aplica</label>
                                        </c:when>
                                        <c:otherwise>
                                            <label id="info-form">Carrera: ${dato.carrera.descripcion}</label>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                                <div class="col-md-4 col-lg-offset-1">
                                    <c:forEach var="datoPrestamo" items="${lstPrestamo}">
                                        <h2>Nro Ticket: #<c:out value="${datoPrestamo.idPrestamo}"/></h2>
                                        <hr>
                                        <label id="info-form">Fecha solicitud online: <fmt:formatDate value="${dato.solicitud.fechaSolicitud}" type="date"/></label>
                                        <br>
                                        <label id="info-form">Fecha retiro: <fmt:formatDate value="${datoPrestamo.fechaRetiro}" type="date"/></label>
                                        <br>
                                        <label id="info-form">Fecha entrega: <fmt:formatDate value="${datoPrestamo.fechaEstimadaEntrega}" type="date"/></label>
                                        <br>
                                        <c:if test="${datoPrestamo.prestamoEspecial == 0}">
                                            <label id="info-form">Solicitud especial: No</label>
                                        </c:if>
                                        <c:if test="${datoPrestamo.prestamoEspecial == 1}">
                                            <label id="info-form">Solicitud especial: Si</label>
                                        </c:if>
                                    </c:forEach>
                                </div>
                            </div>
                        </div>
                        <br>
                        <div class="row col-lg-offset-0">
                            <div class="col-md-12">
                                <h3>Detalle productos:</h3>
                                <div class="row col-lg-offset-11">
                                    <div class="col-md-5">
                                        <form action="TicketPrestamoServlet" method="post">
                                            <button type="submit" class="btn btn-default" aria-label="Left Align" title="Descargar PDF">
                                                <span class="glyphicon glyphicon-save-file" aria-hidden="true"></span>
                                            </button>
                                        </form>
                                    </div>
                                </div>
                                <br>
                                <table class="table">
                                    <tbody>
                                        <c:forEach var="dato" items="${lstSolicitud}">
                                            <tr class="bg-primary">
                                                <th class="text-center">Imagen</th>
                                                <th class="text-center">Id</th>
                                                <th class="text-center">producto</th>
                                                <th class="text-center">Modelo</th>
                                                <th class="text-center">Marca</th>
                                                <th class="text-center">Cantidad</th>
                                                <th class="text-center">Nro Serie</th>
                                            </tr>
                                            <tr>
                                                <td><img src="${dato.producto.rutaImagen}" width="70" height="70"></td>
                                                <td class="text-center"><c:out value="${dato.producto.idProducto}"/></td>
                                                <td class="text-center"><c:out value="${dato.producto.nombre}"/></td>
                                                <td class="text-center"><c:out value="${dato.producto.modelo}"/></td>
                                                <td class="text-center"><c:out value="${dato.marca.descripcion}"/></td>
                                                <td class="text-center"><c:out value="${dato.detalleSolicitud.cantidad}"/></td>
                                                <td class="bg-info">
                                                    <c:forEach var="datoItem" items="${lstItems}">
                                                        <c:if test="${dato.detalleSolicitud.idProducto == datoItem.idProducto}">
                                                            <table>
                                                                <tr>
                                                                    <td>
                                                                        <h5 class="text-capitalize"><strong><c:out value="${datoItem.nroSerie}"/></strong></h5>
                                                                    </td>
                                                                </tr>
                                                            </table>
                                                        </c:if> 
                                                    </c:forEach>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </c:forEach>
                <br>
            </div>
            <div class="col-md-0"></div>
        </div>
    </body>
</html>