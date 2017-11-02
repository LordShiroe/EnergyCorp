<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title id="titulo" class="text-uppercase"></title>
        <%@include file="html/header.jsp" %>
        <style>
            .accordion-heading .accordion-toggle:after {
                /* symbol for "opening" panels */
                font-family: 'Glyphicons Halflings';  /* essential for enabling glyphicon */
                content: "\e114";    /* adjust as needed, taken from bootstrap.css */
                float: right;        /* adjust as needed */
                color: grey;         /* adjust as needed */
            }
            .accordion-heading .accordion-toggle.collapsed:after {
                /* symbol for "collapsed" panels */
                content: "\e080";    /* adjust as needed, taken from bootstrap.css */
            }
        </style>
    </head>
    <body class="">
        <div class="col-sm-12">
            <ol class="breadcrumb">
                <li>Estás Aquí</li><li><a href="./principal">Inicio</a></li>
                <li><a href="./usuarios/gestion">Usuarios</a></li>
                <li>Editar Permisos</li>
                <li><b id="usuarioActivo"></b></li>
            </ol>
        </div>
        <div class="col-sm-12 text-center">
            <b class="text-uppercase"
               style="font-size: 1.5em">
                admininstraci&oacute;n de permisos</b>
        </div>
        <div class="col-xs-12">
            <select class="js-example-data-array"></select>
        </div>
        <div id="aqui"></div>
        <div class="col-xs-12">
            <a href="./principal"></a>
        </div>
        <%@include file="html/footer.jsp" %>
        <script>
            var menu;
            var roles = getJson('/su010_gu111/funciones/roles', {});
            var datos = [];
            for (var i = 0; i < roles.length; i++) {
                datos.push({id: roles[i].nombre, text: roles[i].descripcion});
            }
            $(".js-example-data-array").select2({
                data: datos,
                placeholder: 'Seleccione una opción.'
            }).on('select2:select', function (evt) {
                generarListado();
            });
            function activos() {
                var marcados = $('input[name=permisos]:checked').map(function () {
                    return this.id;
                }).get();
                var desmarcados = listado = $('input[name=permisos]:not(:checked)').map(function () {
                    return this.id;
                }).get();
                var resultado = getJson('./menu/cambiar', {padre: '', id: $(".js-example-data-array").find(":selected").val(), add: marcados + "", del: desmarcados + ""});
            }
            function generarListado() {
                var rolSelected = $(".js-example-data-array").find(":selected").val();
                $('#aqui').empty();
                $("#usuarioActivo").text(rolSelected);
                var lista = "";
                $.each(getJson('./menu/permisos', {}), function (k, v) {
                    var id = v.NOMBRE.replace('/', '') + v.ID_PADRE;
                    id = id.replace(' ', '');
                    lista = '<div class = "col-xs-4 PadreAcordeonPermisos">'
                            + '<div class="accordion-group">'
                            + '<div class="accordion-heading">'
                            + '<a class="accordion-toggle btn primary-color btn-raised"'
                            + 'style="width: 100%; font-size: 1.5em;"'
                            + 'data-toggle="collapse"'
                            + 'data-parent="#parent' + id + '"'
                            + 'href="#parent' + id + '">'
                            + v.NOMBRE
                            + '</a>'
                            + '</div>'
                            + '<div id="parent' + id + '" class="accordion-body collapse in">'
                            + '<div class="accordion-inner">'
                            + getText('./menu/listar', {padre: v.ID_PADRE, id: rolSelected}) + "<br>"
                            + '</div>'
                            + '</div>'
                            + '</div>'
                            + '</div>';
                    $("#aqui").html($("#aqui").html() + lista);
                    $('#parent' + id + '').collapse('hide');
                });
                $('[data-submenu]').submenupicker();
                $('input[type=checkbox]').click(function () {
                    var cur = $(this);
                    cur.next().find('input[type=checkbox]').prop('checked', this.checked);
                    if (this.checked) {
                        cur.parents('li').children('input[type=checkbox]').prop('checked', true);
                    } else {
                        while (cur.attr('id') != 'tree' && !(cur = cur.parent().parent()).find('input:checked').length) {
                            // Keeps unchecking parent inputs as long as none on the current level are checked
                            cur.prev().prop('checked', false);
                        }
                    }
                    activos();
                });
            }
            generarListado();
        </script>
    </body>
</html>
