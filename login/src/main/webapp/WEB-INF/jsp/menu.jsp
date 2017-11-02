<%--
    Document   : menu
    Created on : 18-mar-2016, 18:53:18
    Author     : jose
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title id="titulo" class="text-uppercase"></title>
        <%@include file="html/header.html" %>
    </head>
    <body class="">
        <div id="menu_encabezado"></div>
        <div class="col-xs-12 text-center">
            <b id="titulo_centrado" style="font-size: 2em;"></b>
        </div>
        <div class="col-sm-12">
            <div id="id_enlace">
                <div class="col-sm-4 pull-right">
                    <div class="row-fluid col-sm-12">
                        <div class="col-sm-2">
                            <b>Ventana</b>
                        </div>
                        <div class="col-sm-8">
                            <select name="ventanaSelector" id="ventanaSelector" style="width: 100%"></select>
                        </div>
                        <div class="col-sm-2">
                            <button type="button" class="close" style="display: flex; align-items: center;">
                                <span aria-hidden="true" onclick="cerrarAplicacion()" style="font-size: 1.5em">&times;</span>
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <%@include file="html/footer.html" %>
        <script>
            $(function () {
                $("#ventanaSelector").select2({
                    placeholder: "Seleccione una Opcion",
                    language: "es"
                }).on("select2:select", function (e) {
                    cargaAplicacion(e.target.value);
                });
            });
        </script>

    </body>
</html>
