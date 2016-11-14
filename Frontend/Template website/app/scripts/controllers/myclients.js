'use strict';

/**
 * @ngdoc function
 * @name yapp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of yapp
 */
angular.module('yapp')
	.controller('MyClientsCtrl', function($scope, $location, $state) {
		//var monitorId = localStorage.getItem('monitorId');
		var monitorId = "2";
		$scope.data = 'Data wordt opgehaald...'
		var webSocket = new WebSocket("ws://localhost:9002/ws");
		webSocket.onopen = function(event) {
			webSocket.send('{"requestAction":"getClientsForMonitor","monitorId": "2"}');
		}
		webSocket.onmessage = function(event) {
			var response = JSON.parse(event.data);
			if (response.responseAction == 'deleteClientFromMonitor') {
				$state.reload();
			}
			if (response.responseAction == 'getClientsForMonitor') {
				getClients(response);
			}
			function getClients(response) {
				if (response.clients.length == 0) {
					$scope.noClients = 'Je hebt nog geen clienten!';
				} else {
					$scope.data = response;
				}
				apply();
			}
		}
		function apply() {
			$scope.$apply();
		}
		$scope.disconnectClient = function(id, client) {
			if (confirm('Weet je zeker dat je ' + client + ' wilt ontkoppelen?') === true) {
				webSocket.send('{"requestAction":"deleteClientFromMonitor","monitorId": "2","clientId" : "' + id + '"}');
			} else {
				return;
			}
		}
	});