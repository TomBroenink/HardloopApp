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
		webSocket.onopen = function(event) {
			webSocket.send('{"requestAction":"getAllRunSchemas"}');
		}
		webSocket.onmessage = function(event) {
			var response = JSON.parse(event.data);
			$scope.data = response;
			$scope.$apply();
		}
	});