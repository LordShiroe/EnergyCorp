
function MainController($scope, $q, $http, $uibModal, SweetAlert, Notificar) {
    'use strict';
    var vm = this;
    $scope.selected = {};
    vm.pagina = 10;
    vm.pages = [{value: 10}, {value: 20}, {value: 50}, {value: 100}];
    vm.respuesta = [];
    vm.roles = [];

    listarUsuarios().then(function (json) {
        vm.respuesta = json;
    });
    listarRoles().then(function (json) {
        vm.roles = json;
    });
    vm.editatElemento = editatElemento;
    function editatElemento(res) {
        $scope.selected = res;
        var modalInstance = $uibModal.open({
            templateUrl: "./modal_editar",
            controller: usuarioModalController,
            scope: $scope,
            size: 'lg',
            windowClass: "animated fadeIn"
        });
    }
    vm.insertarElemento = insertarElemento;
    function insertarElemento() {
        $scope.selected = {};
        $uibModal.open({
            templateUrl: "./modal_insertar",
            controller: usuarioModalController,
            scope: $scope,
            size: 'lg',
            windowClass: "animated fadeIn"
        });
    }
    vm.eliminar = eliminar;
    function eliminar(res) {
        SweetAlert.swal({
            title: "¡Atención!",
            text: "Se va a eliminar este usuario",
            type: "warning",
            showCancelButton: true,
            confirmButtonColor: "#DD6B55",
            confirmButtonText: "OK!",
            cancelButtonText: "Cancelar",
            closeOnConfirm: true},
        function (isConfirm) {
            if (isConfirm) {
                eliminarUsuario(res.username).then(function (json) {
                    if (json.MENSAJE === "HA OCURRIDO UN ERROR AL BORRAR AL USUARIO.") {
                        Notificar.error();
                    } else {
                        listarUsuarios().then(function (json) {
                            vm.respuesta = json;
                        });
                    }
                });
            }
        });
    }
    /**
     * Lista los usuarios de la plataforma
     * @returns {$q@call;defer.promise}
     */
    function listarUsuarios() {
        var defered = $q.defer(), promise = defered.promise;
        $http({
            cache: true,
            method: 'POST',
            url: '/su010_gu111/funciones/listar',
            headers: {'Content-Type': 'application/x-www-form-urlencoded'}
        }).success(function (data) {
            defered.resolve(data);
        }).error(function (err) {
            defered.reject(err);
        });
        return promise;
    }

    /**
     * LISTA TODOS LOS ROLES EXISTENTES
     * @returns {$q@call;defer.promise}
     */
    function listarRoles() {
        var defered = $q.defer();
        var promise = defered.promise;
        $http({
            cache: true,
            method: 'POST',
            url: '/su010_gu111/funciones/roles',
            headers: {'Content-Type': 'application/x-www-form-urlencoded'}
        }).success(function (data) {
            defered.resolve(data);
        }).error(function (err) {
            defered.reject(err);
        });
        return promise;
    }

    /**
     * Elimina a un usuario
     * @param {type} usuario
     * @returns {$q@call;defer.promise}
     */
    function eliminarUsuario(usuario) {
        var defered = $q.defer();
        var promise = defered.promise;
        $http({
            cache: true,
            method: 'POST',
            url: '/su010_gu111/funciones/borrar',
            data: $.param({USUARIO: usuario}),
            headers: {'Content-Type': 'application/x-www-form-urlencoded'}
        }).success(function (data) {
            defered.resolve(data);
        }).error(function (err) {
            defered.reject(err);
        });
        return promise;
    }


    function usuarioModalController($scope, $uibModalInstance, Notificar) {
        /**
         * Edita la información del usuario.
         * @param {type} nombre
         * @param {type} email
         * @param {type} rol
         * @param {type} usuario
         * @returns {$q@call;defer.promise}
         */
        function editarUsuario(nombre, email, rol, usuario) {
            var defered = $q.defer();
            var promise = defered.promise;
            $http({
                cache: true,
                method: 'POST',
                url: '/su010_gu111/funciones/editar',
                data: $.param({NOMBRE: nombre, EMAIL: email, ROL: rol, USUARIO: usuario}),
                headers: {'Content-Type': 'application/x-www-form-urlencoded'}
            }).success(function (data) {
                defered.resolve(data);
            }).error(function (err) {
                defered.reject(err);
            });
            return promise;
        }
        /**
         * Crea un nuevo usuario
         * @param {type} nombre
         * @param {type} email
         * @param {type} rol
         * @param {type} usuario
         * @param {type} clave
         * @returns {$q@call;defer.promise}
         */
        function agregarUsuario(nombre, email, rol, usuario, clave) {
            var defered = $q.defer();
            var promise = defered.promise;
            $http({
                cache: true,
                method: 'POST',
                url: '/su010_gu111/funciones/agregar',
                data: $.param({NOMBRE: nombre, EMAIL: email, ROL: rol, USUARIO: usuario, CLAVE: clave}),
                headers: {'Content-Type': 'application/x-www-form-urlencoded'}
            }).success(function (data) {
                defered.resolve(data);
            }).error(function (err) {
                defered.reject(err);
            });
            return promise;
        }
        $scope.guardar = function (form) {
            if (form.$valid) {
                editarUsuario($scope.selected.nombre, $scope.selected.email, $scope.selected.rol, $scope.selected.username).then(function (json) {
                    if (json.MENSAJE === "SE HA EDITADO AL USUARIO CORRECTAMENTE") {
                        listarUsuarios().then(function (json) {
                            vm.respuesta = json;
                        });
                    } else {
                        Notificar.error();
                    }
                }, function (error) {
                    console.log(error);
                });
                $uibModalInstance.close();
                $scope.selected = {};
            } else {
                Notificar.required(form.$error);
            }
        };
        $scope.registrar = function (form) {
            if (form.$valid) {
                agregarUsuario($scope.selected.nombre, $scope.selected.email,
                        $scope.selected.rol, $scope.selected.username,
                        $scope.selected.password)
                        .then(function (json) {
                            if (json.MENSAJE === "SE HA CREADO AL USUARIO CORRECTAMENTE") {
                                listarUsuarios().then(function (json) {
                                    vm.respuesta = json;
                                });
                            } else {
                                Notificar.error();
                            }
                        }, function (error) {
                            console.log(error);
                        });
                $uibModalInstance.close();
                $scope.selected = {};
            } else {
                Notificar.required(form.$error);
            }
        };
        $scope.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };
    }
}
angular.module('enelar', [
    'smart-table',
    'ngCsv',
    'oitozero.ngSweetAlert',
    'ngSanitize',
    'ui.bootstrap',
    'ngAnimate'
]).controller('MainController', MainController).factory('sessionInjector', function () {
    var sessionInjector = {
        request: function (config) {
            var cookie = getCookie("LOGIN").replace(new RegExp('"', 'g'), '');
            config.headers['Authorization'] = cookie;
            return config;
        }
    };
    return sessionInjector;
}).config(['$httpProvider', function ($httpProvider) {
        $httpProvider.interceptors.push('sessionInjector');
    }]);