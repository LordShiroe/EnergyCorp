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
        <title class="center-block">MENU PRINCIPAL</title>
        <%@include file="html/header.html" %>
    </head>
    <body class="">
        <%@include file="html/menu.jsp" %>
        <div class="container" id="aqui"></div>
        <%@include file="html/footer.html" %>
        <script>
            var menu;
            $(function () {
                var lista = "";
                $.each(getJson('/login/permisos/menu/padres', {}), function (k, v) {
                    lista += "<div class='col-sm-3'><a class='btn btn-raised' style='width:100%' "
                            + "onclick=abrir_ventana(\"/login/permisos/ver/" + v.ID_PADRE + "\",'_blank')>"
                            + "<img src='/estaticos/sistema/img/Iconos/" + v.ID_PADRE + ".png' class='row center-block img-responsive' width='30%'>"
                            + "<span class='row blue-text'>" + v.NOMBRE + "</span>"
                            + "</a></div><br>";
                });
                $("#aqui").html(lista);
                $('[data-submenu]').submenupicker();
            });
        </script>

    </body>
</html>
