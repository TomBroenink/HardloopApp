'use strict';

/**
 * @ngdoc function
 * @name yapp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of yapp
 */

angular.module('yapp')
	.controller('RegisterCtrl', function($scope) {
		var response;

		var webSocket = new WebSocket("ws://localhost:9002/ws");
		webSocket.onopen = function(event) {
			webSocket.send('{"requestAction": "getAllCareProperties"}');
		}
		webSocket.onmessage = function(event) {
			response = JSON.parse(event.data);
			console.log(response.careProperties);
			$scope.data = response.careProperties;
			$scope.$apply();
		}

		//$scope.json = jsonFile;
		$scope.submit = function() {
			$scope.createJson();
			console.log(jsonFile);
		}
		var jsonFile;
		$scope.createJson = function() {
			jsonFile = {
				"firstName" : $scope.firstName,
				"lastName" : $scope.lastName,
				"phoneNumber" : $scope.phoneNumber,
				"username" : $scope.userName,
				"password" : $scope.passWord,
				"profile" : $scope.profile
			}
		}
	
	});