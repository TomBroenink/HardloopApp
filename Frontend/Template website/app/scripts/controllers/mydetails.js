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
		console.log(localStorage);
		var monitorId = localStorage.getItem('monitorId');
		console.log(monitorId);
		var accessLevel = localStorage.getItem('accessLevel');
		var webSocket = new WebSocket("ws://localhost:9002/ws");
		var data;
		webSocket.onmessage = function(event) {
			console.log(event.data);
			var response = JSON.parse(event.data);
			data = response.client;
			$scope.firstName = data.firstName;
			$scope.lastName = data.lastName;
			$scope.username = data.username;
			$scope.phoneNumber = data.phoneNumber;
			$scope.$apply();
		}
		webSocket.onopen = function(event) {
			webSocket.send('{"requestAction": "getMonitorById", "monitorId":"' + monitorId + '"}');
		}
		//$scope.message = db.rij1;

		if (accessLevel == 1) {
			$scope.accessLevel = 'Locatiemanager';
		} else {
			$scope.accessLevel = 'Begeleider';
		}
	});