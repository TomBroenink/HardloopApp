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
		var clientId = $stateParams.id;
		var webSocket = new WebSocket("ws://localhost:9002/ws");
		var accessLevel = localStorage.getItem('accessLevel');
		webSocket.onopen = function(event) {
			webSocket.send('{"requestAction": "getClientById", "clientId": "'+ clientId + '"}');
		}
		webSocket.onmessage = function(event) {
			var response = JSON.parse(event.data);
			if (response.responseAction == 'getClientById') {
				var data = response.client;
				$scope.username = data.username;
				$scope.firstName = data.firstName;
				$scope.lastName = data.lastName;
				$scope.phoneNumber = data.phoneNumber;
				$scope.$apply();
			}
			/*if (response.responseAction == 'updateClientById') {

			}*/
		}
		$scope.back = function() {
			window.history.back();
		}
		$scope.editClient = function() {
			var data = {"username": $scope.username, "firstName": $scope.firstName, "lastName": $scope.lastName, "phoneNumber": $scope.phoneNumber}
			console.log(data);
			// websocket send edit monitor details
		}
	});