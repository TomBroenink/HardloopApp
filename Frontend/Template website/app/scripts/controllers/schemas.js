'use strict';

/**
 * @ngdoc function
 * @name yapp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of yapp
 */
angular.module('yapp')
	.controller('SchemasCtrl', function($scope, $location) {
		var webSocket = new WebSocket("ws://localhost:9002/ws");
	});