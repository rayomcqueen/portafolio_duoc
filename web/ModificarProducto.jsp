<%-- 
    Document   : ModificarProducto
    Created on : 28-abr-2016, 20:06:27
    Author     : cristian
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<c:choose>
    <c:when test="${empty usuarioSesion}">
        <c:redirect url="index.jsp"></c:redirect>
    </c:when>
</c:choose>
<html>
    <head>
        <script src="js/jquery.js"></script>
        <script src="js/jquery.validate.js"></script>
        <script src="js/messages_es.js"></script>
        <script src="js/jquery.blockUI.js"></script>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" type="text/css" href="css/bootstrap.min.css"/>
        <link rel="stylesheet" type="text/css" href="css/nuevosEstilos.css"/>
        <link href='https://fonts.googleapis.com/css?family=Open+Sans+Condensed:300' rel='stylesheet' type='text/css'>
        <link href='https://fonts.googleapis.com/css?family=Palanquin' rel='stylesheet' type='text/css'>
        <title>Administración de Inventario</title>
    </head>
    <script type="text/javascript">
        function marcas() {
            $(document).ready(function () {
                var idCategoria = $('#seleccionCategoria').val();

                $.get('DropdownCategoriaServlet', {
                    seleccionCategoria: idCategoria

                }, function (responseText) {
                    $('#opcion').html(responseText);

                });
            });
        }
    </script>
    <body>
        <%@include file="header.jsp" %>
        <div class="container"> 
            <h3 class="text-center" id="titulo-pagina">Administración sistema Pañol</h3>
            <h5 class="text-center" id="titulo-pagina">Escuela de comunicaciones - Duoc UC</h5>
            <br>
            <div>
                <div class="row">
                    <div class="col-md-3"></div>
                    <div class="col-md-6 centered-pills">
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
                            <li role="presentation"><a href="<c:url value="/AdminProductosServlet"/>">Administrar inventario</a></li>
                            <li role="presentation"><a href="<c:url value="/RegistroProductoServlet"/>">Registro nuevo producto</a></li>
                        </ul>
                    </div>
                    <div class="col-md-3"></div>
                </div> 
            </div>
            <div class="row">
                <div class="col-md-3"></div>
                <div class="col-md-6">
                    <c:if test="${not empty mensaje['mensajeExito']}">
                        <div id="danger-box" class="bg-success">
                            <h5 class="text-center text-success"><c:out value="${mensaje['mensajeExito']}"/></h5>
                        </div>
                    </c:if>
                    <h3 class="text-center" id="palanquin-font">Modificar productos</h3>
                    <hr>
                    <br>
                    <p id="error-form" class="text-center"><c:out value="${mapMensaje['errorExiste']}"/></p>
                    <form action="<c:url value="/ModificarProductoServlet"/>" method="post" onsubmit="return confirm('¿Está seguro de enviar los datos?');">
                        <div class="form-group col-xs-6">
                            <label>Categoría:</label>
                            <select class="form-control" id="seleccionCategoria" name="seleccionCategoria" onchange="marcas()">
                                <option value="0">--Seleccione una categoría</option>
                                <c:forEach var="dato" items="${lstCategorias}">
                                    <option value="${dato.idCategoria}" ${dato.idCategoria == producto.producto.idCategoria ? 'selected="selected"' : ''}>
                                        <c:out value="${dato.descripcion}"/>
                                    </option>
                                </c:forEach>
                            </select>
                            <p id="error-form"><c:out value="${mapMensaje['errorCategoria']}"/></p>
                        </div>
                        <div class="form-group col-xs-6">
                            <label>Marca:</label>
                            <div id="opcion">
                                <select class="form-control" id="seleccionMarca" name="seleccionMarca">
                                    <option value="0">--Seleccione una marca--</option>
                                    <c:forEach var="dato" items="${lstMarcas}">
                                    <option value="${dato.idMarca}" ${dato.idMarca == producto.producto.idMarca ? 'selected="selected"' : ''}>
                                        <c:out value="${dato.descripcion}"/>
                                    </option>
                                </c:forEach>
                                </select>
                            </div>
                            <p id="error-form"><c:out value="${mapMensaje['errorMarca']}"/></p>
                        </div>
                        <div class="form-group col-xs-6">
                            <label>Nombre artículo:</label>
                            <input type="text" class="form-control" id="nombre" 
                                   name="nombre" placeholder="cámara, notebook, proyector..."
                                   value="<c:out value="${producto.producto.nombre}"/>"/>
                            <p id="error-form"><c:out value="${mapMensaje['errorNombre']}"/></p>
                        </div>
                        <div class="form-group col-xs-6">
                            <label>Modelo:</label>
                            <input type="text" class="form-control" id="modelo" name="modelo"
                                   style="text-transform: uppercase"
                                   value="<c:out value="${producto.producto.modelo}"/>"/>
                            <p id="error-form"><c:out value="${mapMensaje['errorModelo']}"/></p>
                        </div>
                        <div class="form-group col-md-12">
                            <label>Descripción:</label>
                            <textarea type="text" class="form-control" id="descripcion" 
                                      name="descripcion" rows="6"
                                      placeholder="Detalla las características del producto..."/><c:out value="${producto.producto.descripcion}"/></textarea>
                            <p id="error-form"><c:out value="${mapMensaje['errorDescripcion']}"/></p>
                        </div>
                        <div class="row">
                            <div class="col-lg-offset-10">
                                <input type="hidden" name="idProducto" value="${producto.producto.idProducto}">
                                <button type="submit" class="btn btn-default btn-primary">Modificar</button>
                            </div>
                        </div> 
                    </form>
                </div>
            </div>
            <div class="col-md-3"></div>
        </div>
        <script src="http://cdnjs.cloudflare.com/ajax/libs/jquery-easing/1.3/jquery.easing.min.js"></script>
    </body>
</html>
