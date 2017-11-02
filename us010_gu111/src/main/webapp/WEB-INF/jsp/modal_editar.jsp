<div class="row">
    <div class="col-lg-12">
        <div class="tabs-container">
            <uib-tabset>
                <uib-tab>
                    <uib-tab-heading>
                        Datos del Usuario {{selected.username}}
                    </uib-tab-heading>
                    <div class="panel-body">
                        <form name="datosUsuario">
                            <fieldset>
                                <input-text titulo="Nombre" valor="selected.nombre" col="6" readonly="false" required="true"></input-text>
                                <input-text titulo="Email" valor="selected.email" col="6" readonly="false" required="true"></input-text>
                                <select-datos titulo="Rol" valor="selected.rol" datos="mn.roles" col="6" required="true" value="nombre" label="descripcion"></select-datos>
                            </fieldset>
                            <div class="col-xs-12 text-center">
                                <button class="btn btn-raised btn-success" ng-click="guardar(datosUsuario)">Editar</button>
                                <button class="btn btn-raised btn-info" type="submit" ng-click="cancel()"><strong>Cerrar</strong></button>
                            </div>
                        </form>
                    </div>
                </uib-tab>
            </uib-tabset>
        </div>
    </div>
</div>