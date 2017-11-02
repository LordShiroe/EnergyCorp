<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title></title>
        <style>
            md-input-container .md-errors-spacer {
                min-height: 0 !important;
            }
            .right {
                float: right;
                width: 200px;
                padding: 10px;
            }
            .st-selected{
                background: #216eff !important;
                color: white !important;
            }
            .btn {
                margin: 5px 0 !important;
                font-size: .7em !important;
            }
            .panel.panel-default>.panel-heading, .panel>.panel-heading{
                background-color: #2196f3 !important;
            }
            .nav-tabs {
                background: #2196f3 !important;
            }
            td{
                font-size: 16px !important;
            }
        </style>
        <%@include file="html/header.jsp" %>
    </head>
    <body class="ng-cloak" ng-app="caos">
        <div ng-controller="MainController as mn">
            <div class="col-xs-10 col-xs-push-1">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <h3 class="text-uppercase"><b>Gesti&oacute;n de usuarios</b></h3>
                    </div>                    
                    <div class='panel-body'>
                        <div class="row">
                            <div class="col-sm-6 form-inline">
                                <label>Mostrar <select class="input-sm form-control"
                                                       ng-model="mn.pagina" ng-readonly="true">
                                        <option ng-repeat="page in mn.pages" value="{{page.value}}">{{page.value}}</option>
                                    </select> registros
                                </label>
                            </div>
                            <div class="col-sm-6 form-inline" style="text-align: right">
                                <label>Buscar: <input type="text" ng-model="query" class="form-control input-sm"/></label>
                            </div>
                        </div>
                        <table id="table" name="Usuarios" search-watch-model="query" st-table='mn.displayedCollection' st-safe-src='mn.respuesta' class='table table-bordered table-condensed table-hover table-responsive table-striped'>
                            <thead>
                                <tr>
                                    <th class='text-center vertical-center' st-sort='username'>Usuario</th>
                                    <th class='text-center vertical-center' st-sort='estadoSesion'>Estado</th>
                                    <th class='text-center vertical-center' st-sort='nombre'>Nombre</th>
                                    <th class='text-center vertical-center' st-sort='email'>E-Mail</th>
                                    <th class='text-center vertical-center' st-sort='rol'>Rol</th>
                                    <th class='text-center vertical-center' st-sort='fechaCreacion'>Fecha Creación</th>
                                    <th class='text-center vertical-center' st-sort='ultimoAcceso'>Ultimo Ingreso</th>
                                    <th class='text-center'></th>
                                    <th class='text-center'></th>
                            </thead>
                            <tbody>
                                <tr ng-repeat='res in mn.displayedCollection'>
                                    <td class='text-center vertical-center' style="vertical-align:middle">{{res.username}}</td>
                                    <td class='text-center vertical-center' style="vertical-align:middle">{{res.estadoSesion}}</td>
                                    <td class='text-center vertical-center' style="vertical-align:middle">{{res.nombre|uppercase}}</td>
                                    <td class='text-center vertical-center' style="vertical-align:middle">{{res.email|lowercase}}</td>
                                    <td class='text-center vertical-center' style="vertical-align:middle">{{res.rol}}</td>
                                    <td class='text-center vertical-center' style="vertical-align:middle">{{res.fechaCreacion|date:"EEEE, d MMMM, y"}}</td>
                                    <td class=' text-center vertical-center' style="vertical-align:middle">{{res.ultimoAcceso| date:"EEEE, d MMMM, y"}}</td>
                                    <td class='col-sm-1'>
                                        <button type='button' ng-click='mn.editatElemento(res)' class='btn btn-sm btn-success'>
                                            <i class='glyphicon glyphicon-edit'></i> Editar Usuario
                                        </button>
                                    </td>
                                    <td class='col-sm-1'>
                                        <button type='button' ng-click='mn.eliminar(res)' class='btn btn-sm btn-danger'>
                                            <i class='glyphicon glyphicon glyphicon-remove'></i> Eliminar Usuario
                                        </button>
                                    </td>
                                </tr>
                            </tbody>
                            <tfoot>
                                <tr>
                                    <td colspan="9" class="text-center">
                                        <div st-pagination="" st-items-by-page="mn.pagina" st-displayed-pages="7"></div>
                                    </td>
                                </tr>
                            </tfoot>
                        </table>
                        <div class='panel-body'>
                            <div class='right'>
                                <button type='button' ng-click='mn.insertarElemento()' class='btn btn-raised btn-primary'>
                                    <i class='glyphicon glyphicon-plus'></i> Crear Usuario Nuevo
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <%@include file="html/footer.jsp" %>
        <script src="./script.js"></script>
        <script src="/estaticos/directives/directives.js"></script>
        <script src="/estaticos/services/services.js"></script>
        <script src="/estaticos/factories/factories.js"></script>
        <script src="/estaticos/filters/filters.js"></script>
    </body>
</html>
