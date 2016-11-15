'use strict';

/**
 * @ngdoc function
 * @name yapp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of yapp
 */
angular.module('yapp')
	.controller('EditDetailsCtrl', function($scope, $location, $stateParams) {
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
			if (response.responseAction == 'updateUser') {
				alert('Gegevens gewijzigd!');
				$scope.back();
			}
			if (response.responseAction == 'getMonitorById') {
				var data = response.client;
				console.log(data);
				$scope.username = data.username;
				$scope.firstName = data.firstName;
				$scope.lastName = data.lastName;
				$scope.phoneNumber = data.phoneNumber;
				$scope.pId = data.personalData_id;
			}
			$scope.$apply();
		}

		$scope.back = function() {
			window.history.back();
		}
		$scope.editDetails = function() {
			var data = {"requestAction" : "updateUser", "personalDataId" : $scope.pId, "username": $scope.username, "firstName": $scope.firstName, "lastName": $scope.lastName, "phoneNumber": $scope.phoneNumber}
			webSocket.send(JSON.stringify(data));
			// websocket send edit monitor details
		}
	});