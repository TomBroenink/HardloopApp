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
		//var user = localStorage.getItem('user');
		var data = {"username":"TomBroenink", "firstName":"Tom","lastName":"Broenink","phoneNumber":"0629277096"};
		$scope.username = data.username;
		$scope.firstName = data.firstName;
		$scope.lastName = data.lastName;
		$scope.phoneNumber = data.phoneNumber;
		var webSocket = new WebSocket("ws://localhost:9002/ws");
		webSocket.onmessage = function(event) {
			var response = JSON.parse(event.data);
			$scope.data = response;
			console.log($scope.data);
		}

		$scope.back = function() {
			window.history.back();
		}
		$scope.editDetails = function() {
			var data = {"username": $scope.username, "firstName": $scope.firstName, "lastName": $scope.lastName, "phoneNumber": $scope.phoneNumber}
			// websocket send edit monitor details
		}
	});