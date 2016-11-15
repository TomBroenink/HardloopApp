'use strict';

/**
 * @ngdoc function
 * @name yapp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of yapp
 */
angular.module('yapp')
	.controller('NewClientCtrl', function($scope, $location) {
		var response;
		var clientId;
		var monitorId = "2";
		var i = 1;
		$scope.data = 'Data wordt opgehaald...'
		var webSocket = new WebSocket("ws://localhost:9002/ws");
		webSocket.onopen = function(event) {
			webSocket.send('{"requestAction": "getAllClients"}');
		}
		webSocket.onmessage = function(event) {
			response = JSON.parse(event.data);
			if(response.responseAction == 'assignClientToMonitor') {
				if (response.responseStatusCode == 0) {
					alert('Je kunt deze client niet koppelen!');
				} else {
					alert('Client is succesvol gekoppeld!');
					$scope.back();
				}
			}
			if (response.responseAction == 'getAllClients') {
				$scope.data = response;
				$scope.$apply();
			}
		}
		$scope.back = function() {
			window.history.back();
		}
		$scope.connectClient = function(clientId, name) {
			webSocket.send('{"requestAction": "assignClientToMonitor","monitorId": "2","clientId": "' + clientId + '", "monitorNumber": "1"}');

		}
	});