<%--
    Document   : login
    Created on : 25/07/2017, 02:41:24 PM
    Author     : Carlos Angarita
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta http-equiv="Cache-Control" content="cache">
        <title>EnergyCorp</title>
        <%@include file="html/header.html" %>
    </head>
    <body class="container">
        <div class="col-sm-4"></div>
        <div class="col-sm-4 well">
            <form method="post" action="/login/autentica" id="login" name="login">
                <fieldset>
                    <img src="/estaticos/sistema/img/logo.png" class="img-rounded img-responsive center-block" alt="EnergyCorp" width="30%">
                    <legend class="text-center">Ingreso al Sistema</legend>
                    <div class="form-group label-floating">
                        <label class="control-label" for="sid">Nombre de Usuario</label>
                        <input class="form-control" id="sid" name="sid" type="text" required>
                        <p class="help-block">Ingrese el usuario suminsitrado su Admistrador</p>
                    </div>
                    <div class="form-group label-floating">
                        <label class="control-label" for="pwd">Contrase&ntilde;a</label>
                        <input class="form-control" id="pwd" name="pwd" type="password" required>
                        <p class="help-block">Ingrese la Contrase&ntilde;a del usuario suministrado</p>
                    </div>
                    <button class="btn btn-raised btn-primary btn-block" type="submit">Ingresar</button>
                </fieldset>
            </form>
        </div>
        <script>
            var mostrar = true;
        </script>
        <%@include file="html/footer.html" %>
        <script type="text/javascript">
            $(function () {
                var respuesta = getUrlParameter("error");
                if (respuesta) {
                    alert(respuesta);
                    window.location.href = "/login/";
                }
            });
        </script>
    </body>
</html>
