<%@page contentType="text/html" pageEncoding="UTF-8"%>
<% Cookie[] cookies = request.getCookies();
    String nombre = null;
    for (Cookie cookie : cookies) {
        if (cookie.getName().equals("USER")) {
            nombre = cookie.getValue();
        }
    }

%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>EnergyCorp</title>
        <%@include file="html/header.html" %>
    </head>
    <body class="container">
        <div class="well col-xs-12">
            <form action="/login/usuarios/funciones/cambiarClave" method="post" id="formModal" name="datos" onsubmit="return false">
                <div class="col-xs-4">
                    <div class="form-group label-floating">
                        <label class="control-label" for="USUARIO">USUARIO</label>
                        <input class="form-control" id="USUARIO" type="text" name="USUARIO" value="<%=nombre%>" readonly>
                        <p class="help-block">Nombre de inicio de sesión del usuario</p>
                    </div>
                </div>
                <div class="col-xs-4">
                    <div class="form-group label-floating">
                        <label class="control-label" for="CLAVE">Nueva Contrase&ntilde;a</label>
                        <input class="form-control" id="CLAVE" type="password" name="CLAVE" value="" required>
                        <p class="help-block">Contraseña de autenticación del usuario</p>
                    </div>
                </div>
                <div class="col-xs-4">
                    <div class="form-group label-floating">
                        <label class="control-label" for="CLAVE">Verificar Contrase&ntilde;a</label>
                        <input class="form-control" id="CLAVE_V" type="password" name="CLAVE_V" value="" required>
                        <p class="help-block">Confirmar Contraseña de autenticación del usuario</p>
                    </div>
                </div>
                <div class="col-xs-12">
                    <div class="btn-group">
                        <input type="hidden" id="ignorar_vacio" value="1">
                        <a href="#" class="btn btn-default btn-raised" id="cancelar" data-dismiss="modal">Cancelar</a>
                        <a type="submit" class="btn btn-info btn-raised" id="guardar" onclick="guardar()">guardar</a>
                    </div>
                </div>
            </form>
        </div>


        <div class="modal fade" id="respuestaServidor" tabindex="-1" role="dialog" aria-labelledby="respuestaServidorLabel">
            <div class="modal-dialog" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                        <h4 class="modal-title text-center" id="respuestaServidorLabel">REPUESTA DEL SERVIDOR</h4>
                    </div>
                    <div class="modal-body">
                        <div class="well col-sm-12">
                            <div align="center">
                                <h2><b class="text-info" id="respuestaMsg">CAMBIOS REALIZADOS</b></h2>
                            </div>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <a href="#" class="btn btn-default btn-raised" data-dismiss="modal">aceptar</a>
                    </div>
                </div>
            </div>
        </div>
        <%@include file="html/footer.html" %>
        <script type="text/javascript">

            function guardar() {
                if ($("#CLAVE").val() == $("#CLAVE_V").val()) {
                    $("#formModal").ajaxSubmit({
                        beforeSubmit: function (arr, $form, options) {
                            var conteo = 0;
                            $.each(arr, function (x) {
                                if (arr[x].value == "")
                                    conteo++;
                            });
                            if (conteo > 0) {
                                alert("Debe ingresar todos los campos");
                            }
                            return conteo == 0 ? true : false;
                        },
                        resetForm: true,
                        success: function (e) {
                            notificar(e);
                        }
                    });
                } else {
                    alert("Las claves no son iguales");
                }

            }
            $(function () {
            });
        </script>
    </body>
</html>

