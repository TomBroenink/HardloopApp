'use strict';

/**
 * @ngdoc function
 * @name yapp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of yapp
 */
angular.module('yapp')
	.controller('NewSchemaCtrl', function($scope, $location, $window) {
		var webSocket = new WebSocket("ws://localhost:9002/ws");
		webSocket.onopen = function(event) {
			//webSocket.send('{"requestAction": "createRunSchema"}');
		}
		webSocket.onmessage = function(event) {
			var response = JSON.parse(event.data);
			console.log(response);
			if (response.responseStatusDescription == 'Succes') {
				alert('Schema toegevoegd!');
				$scope.back();
			}
		}
		$scope.back = function() {
			window.history.back();
		}

		$scope.saveSchema = function() {
			var json = '{"requestAction": "createRunSchema", "name" : "' + $scope.name + '", "description" : "' + $scope.description + '"}';
			webSocket.send(json);
		}
	});