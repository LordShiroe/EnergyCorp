<div class="row">
    <div class="col-lg-12">
        <div class="tabs-container">
            <uib-tabset>
                <uib-tab>
                    <uib-tab-heading>
                        Datos del Medidor
                    </uib-tab-heading>
                    <div class="panel-body">
                        <form name="datosTransformador">
                            <fieldset>
                                <input-text titulo="Marca" valor="info.MARMED" col="6" readonly="true"  ></input-text>
                                <input-text titulo="Medidor" valor="info.NUMMED" col="6" readonly="true"  ></input-text>
                                <input-text titulo="Transformador" valor="info.CODTRA" col="6" readonly="true"  ></input-text>
                                <input-text titulo="Estado Medidor" valor="info.ESTMED" col="6" readonly="true"  ></input-text>
                                <input-text titulo="Lectura Inicial" valor="info.LECINI" col="6" readonly="true"  ></input-text>
                                <input-text titulo="Fecha Instalaci�n" valor="info.FECINS" col="6" readonly="true"  ></input-text>
                                <input-text titulo="Latitud" valor="info.LATITU" col="6" readonly="true"  ></input-text>
                                <input-text titulo="Longitud" valor="info.LONGIT" col="6" readonly="true"  ></input-text>
                            </fieldset>
                            <div class="col-xs-12 text-center">
                                <!--<button class="btn btn-raised btn-info" ng-click="guardar(datosTransformador)">guardar</button>-->
                                <button class="btn btn-raised btn-info" type="submit" ng-click="cancel()"><strong>Cerrar</strong></button>
                            </div>
                        </form>
                    </div>
                </uib-tab>
            </uib-tabset>
        </div>
    </div>
</div>