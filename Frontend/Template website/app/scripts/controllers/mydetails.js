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
		webSocket.onmessage = function(event) {
			var response = JSON.parse(event.data);
			$scope.data = response;
			console.log($scope.data);
		}
		$scope.init = function () {
			webSocket.send('{"requestAction": "getAllClients"}');
		}
		//$scope.message = db.rij1;
		$scope.firstName = 'Tom';
		$scope.lastName = 'Broenink';
		$scope.username = 'tom_broenink@hotmail.com';
		$scope.phoneNumber = '06-29277096';
		if (accessLevel == 1) {
			$scope.accessLevel = 'Locatiemanager';
		} else {
			$scope.accessLevel = 'Begeleider';
		}
		$scope.client1 = 'TomBroenink';
	});