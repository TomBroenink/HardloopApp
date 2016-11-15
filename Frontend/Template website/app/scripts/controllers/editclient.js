'use strict';

/**
 * @ngdoc function
 * @name yapp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of yapp
 */
angular.module('yapp')
	.controller('EditClientCtrl', function($scope, $location, $stateParams) {
		var monitorId = localStorage.getItem('monitorId');
		var webSocket = new WebSocket("ws://localhost:9002/ws");
		var accessLevel = localStorage.getItem('accessLevel');
		if (accessLevel == 1) {
			$scope.accessLevel = 'Begeleider';
		} else {
			$scope.accessLevel = 'Locatiemanager';
		}
		webSocket.onopen = function(event) {
			webSocket.send('{"requestAction": "getMonitorById", "monitorId":"' + monitorId + '"}');
		}
		webSocket.onmessage = function(event) {
			var response = JSON.parse(event.data);
			var data = response.client;
			$scope.username = data.username;
			$scope.firstName = data.firstName;
			$scope.lastName = data.lastName;
			$scope.phoneNumber = data.phoneNumber;
			$scope.$apply();
		}

		$scope.back = function() {
			window.history.back();
		}
		$scope.editDetails = function() {
			var data = {"username": $scope.username, "firstName": $scope.firstName, "lastName": $scope.lastName, "phoneNumber": $scope.phoneNumber}
			// websocket send edit monitor details
		}
	});