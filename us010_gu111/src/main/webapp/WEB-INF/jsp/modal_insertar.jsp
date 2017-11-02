<div class="row">
    <div class="col-lg-12">
        <div class="tabs-container">
            <uib-tabset>
                <uib-tab>
                    <uib-tab-heading>
                        Datos de Usuario
                    </uib-tab-heading>
                    <div class="panel-body">
                        <form name="datosUsuario">
                            <fieldset>
                                <div class="row">
                                    <input-text titulo="Nombre de Usuario" valor="selected.username" col="6" required="true"  ></input-text>
                                    <input-password titulo="Contraseña" valor="selected.password" col="6" required="true"  ></input-password>
                                    <input-text titulo="Nombre Completo" valor="selected.nombre" col="6" required="true"></input-text>
                                    <input-text titulo="Email" valor="selected.email" col="6" required="true"></input-text>
                                    <select-datos titulo="Rol" valor="selected.rol" datos="mn.roles" col="6" required="true" value="nombre" label="descripcion"></select-datos>
                                </div>
                            </fieldset>
                            <div class="col-xs-12 text-center">
                                <button class="btn btn-raised btn-info" ng-click="registrar(datosUsuario)">guardar</button>
                                <button class="btn btn-raised btn-danger" type="submit" ng-click="cancel()"><strong>cancelar</strong></button>
                            </div>
                        </form>
                    </div>
                </uib-tab>
            </uib-tabset>
        </div>
    </div>
</div>