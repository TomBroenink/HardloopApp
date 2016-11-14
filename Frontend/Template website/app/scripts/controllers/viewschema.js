'use strict';

/**
 * @ngdoc function
 * @name yapp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of yapp
 */
angular.module('yapp')
	.controller('ViewSchemaCtrl', function($scope, $location, $stateParams) {
		var schemaId = $stateParams.id;
		var responseAction = 'getRunSchemaById';
		var responseStatusCode = 0;
		var webSocket = new WebSocket("ws://localhost:9002/ws");
/*		webSocket.onopen = function(event) {
			webSocket.send('{"requestAction": "getSchemaById", "clientId": "'+ clientId + '"}');
		}*/
		webSocket.onmessage = function(event) {
			var response = JSON.parse(event.data);
			if (/*response.*/responseAction === 'getRunSchemaById') {
				if (/*response.*/responseStatusCode == 0) {
					$scope.checkIfExists = 'Dit schema bestaat niet!';
					$scope.$apply();
				}
				$scope.schemas = response.runSchemas;
				console.log($scope.schemas);
				$scope.$apply();
			}
			if (response.responseAction == 'getClientById') {
				console.log(response.client);
				$scope.data = response.client;
				$scope.$apply();
			}
		}
	});