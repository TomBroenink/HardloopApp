'use strict';

/**
 * @ngdoc function
 * @name yapp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of yapp
 */
angular.module('yapp')
	.controller('MyClientsCtrl', function($scope, $location) {
		var response;
		//var monitorId = localStorage.getItem('monitorId');
		var monitorId = "2";
		$scope.data = 'Data wordt opgehaald...'
		var webSocket = new WebSocket("ws://localhost:9002/ws");
		webSocket.onopen = function(event) {
			webSocket.send('{"requestAction":"getClientsForMonitor","monitorId": "2"}');
		}
		webSocket.onmessage = function(event) {
			response = JSON.parse(event.data);
			if (response.clients.length == 0) {
				$scope.noClients = 'Je hebt nog geen clienten!';
			} else {
				$scope.data = response;
			}
			$scope.$apply();
		}
	});