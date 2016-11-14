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
			console.log(response);
			if (response.responseAction == 'getAllClients') {
				$scope.data = response;
				$scope.$apply();
			} else {

			}
		}
		$scope.back = function() {
			window.history.back();
		}
		$scope.connectClient = function(id, name) {
			var clientId = id.toString();
			webSocket.send('{"requestAction": "assignClientToMonitor","monitorId": "8","clientId": clientId, "monitorNumber": "1"}');
			alert('Client ' + name + ' is succesvol gekoppeld!')
		}
	});