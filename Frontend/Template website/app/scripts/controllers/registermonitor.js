'use strict';

/**
 * @ngdoc function
 * @name yapp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of yapp
 */

angular.module('yapp')
	.controller('RegisterMonitorCtrl', function($scope) {
		var response;
		var jsonFile;

		var webSocket = new WebSocket("ws://localhost:9002/ws");
		$scope.message = "hoi";

		$scope.submit = function() {
			$scope.createJson();
			console.log(jsonFile);
		}
		webSocket.onmessage = function(event) {
			response = JSON.parse(event.data);
			if (response.responseAction == "registerMonitor") {
				$scope.message = "Register complete!";
			}
			$scope.$apply();
		}
		$scope.createJson = function() {
			jsonFile = {
				"requestAction": "registerMonitor",
				"person": {
				"firstName": $scope.firstName,
				"lastName": $scope.lastName,
				"phoneNumber": $scope.phoneNumber,
				"username": $scope.username,
				"password": $scope.password,
				"accessLevel": "1"
				}
			}
			webSocket.send(JSON.stringify(jsonFile));
		}
	});
