'use strict';

/**
 * @ngdoc function
 * @name yapp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of yapp
 */
angular.module('yapp')
	.controller('ClientCtrl', function($scope, $stateParams) {
		var clientId = $stateParams.id;
		var accessLevel = localStorage.getItem('accessLevel');
		var webSocket = new WebSocket("ws://localhost:9002/ws");
		webSocket.onmessage = function(event) {
			var response = JSON.parse(event.data);
			$scope.data = response;
			console.log($scope.data);
		}
		$scope.init = function () {
			// Doe hier de call om de gegevens van de client op te halen
			// webSocket.send('{"requestAction": "getAllClients"}');
		}

		$scope.back = function() {
			window.history.back();
		}

		console.log($stateParams);
	});