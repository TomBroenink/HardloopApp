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
		var webSocket = new WebSocket("ws://localhost:9002/ws");
		webSocket.onopen = function(event) {
			webSocket.send('{"requestAction": "getRunSchemaById", "runSchemaId": "'+ schemaId + '"}');
		}
		webSocket.onmessage = function(event) {
			var response = JSON.parse(event.data);
			var data = response.runSchema;
			$scope.name = data.name;
			$scope.description = data.description;
			$scope.$apply();
		}
		$scope.back = function() {
			window.history.back();
		}
	});