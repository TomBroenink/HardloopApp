'use strict';

/**
 * @ngdoc function
 * @name yapp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of yapp
 */
angular.module('yapp')
	.controller('MyDetailsCtrl', function($scope, $location, $stateParams) {
		var monitorId = localStorage.getItem('monitorId');
		var webSocket = new WebSocket("ws://localhost:9002/ws");
		var accessLevel = localStorage.getItem('accessLevel');
		var data;
		webSocket.onmessage = function(event) {
			var response = JSON.parse(event.data);
			data = response.client;
			$scope.firstName = data.firstName;
			$scope.lastName = data.lastName;
			$scope.username = data.username;
			$scope.phoneNumber = data.phoneNumber;
			if (accessLevel == 1) {
				$scope.accessLevel = 'Begeleider';
			}
			if (accessLevel == 2) {
				$scope.accessLevel = 'Locatiemanager';
			}
			$scope.$apply();
		}
		webSocket.onopen = function(event) {
			webSocket.send('{"requestAction": "getMonitorById", "monitorId":"' + monitorId + '"}');
		}
	});