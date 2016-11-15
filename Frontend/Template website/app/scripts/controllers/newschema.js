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
			webSocket.send('{"requestAction": "getAllRuns"}');
		}
		webSocket.onmessage = function(event) {
			var response = JSON.parse(event.data);
			if (response.responseAction == 'createRunSchema') {
				if (response.responseStatusDescription == 'Succes') {
					alert('Schema toegevoegd!');
					$scope.back();
				}
			}
			if (response.responseAction == 'getAllRuns') {
				console.log(response);
				$scope.runs = response.runs;
				$scope.$apply();
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