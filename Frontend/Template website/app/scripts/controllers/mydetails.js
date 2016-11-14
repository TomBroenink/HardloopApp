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
		var accessLevel = localStorage.getItem('accessLevel');
		var webSocket = new WebSocket("ws://localhost:9002/ws");
		var data;
		webSocket.onmessage = function(event) {
			var response = JSON.parse(event.data);
			data = response.client;
			console.log(data);
			$scope.firstName = data.firstName;
			$scope.lastName = data.lastName;
			$scope.username = data.username;
			$scope.phoneNumber = data.phoneNumber;
			$scope.$apply();
		}
		webSocket.onopen = function(event) {
			webSocket.send('{"requestAction": "getMonitorById", "monitorId":"2"}');
		}
		//$scope.message = db.rij1;

		if (accessLevel == 1) {
			$scope.accessLevel = 'Locatiemanager';
		} else {
			$scope.accessLevel = 'Begeleider';
		}
	});