'use strict';

/**
 * @ngdoc function
 * @name yapp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of yapp
 */
angular.module('yapp')
	.controller('ConnectSchemaCtrl', function($scope, $location, $stateParams) {
		var clientId = $stateParams.id;
		var webSocket = new WebSocket("ws://localhost:9002/ws");
		webSocket.onopen = function(event) {
			webSocket.send('{"requestAction":"getAllRunSchemas"}');
		}
		webSocket.onmessage = function(event) {
			var response = JSON.parse(event.data);
			console.log(response);
			if (response.responseAction == 'getAllRunSchemas') {
				$scope.data = response;
			}
			if (response.responseAction == 'assignRunSchemaToClient') {
				alert('Schema aan client gekoppeld!');
				$scope.back();
			}
			$scope.$apply();
		}
		$scope.back = function() {
			window.history.back();
		}
		$scope.connectSchema = function(runSchemaId) {
			console.log(runSchemaId);
			console.log(clientId);
			var json = '{"requestAction": "assignRunSchemaToClient", "clientId" : "' + clientId + '", "runSchemaId" : "' + runSchemaId + '"}';
			webSocket.send(json);
		}
	});