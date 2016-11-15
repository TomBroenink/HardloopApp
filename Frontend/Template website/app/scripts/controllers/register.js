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
		var jsonFile;
		$scope.name = "Naam";
		$scope.desc = "Beschrijving"

		var webSocket = new WebSocket("ws://localhost:9002/ws");
		webSocket.onopen = function(event) {
			webSocket.send('{"requestAction": "getAllCareProperties"}');
		}
		webSocket.onmessage = function(event) {
			response = JSON.parse(event.data);
			console.log(response);
			if (response.responseAction == "getAllCareProperties") {
				console.log(response.careProperties);
				$scope.data = response.careProperties;
			}
			if (response.responseAction == "registerClient") {
				$scope.message = "Register complete!";
			}
			$scope.$apply();
		}

		$scope.selectProfile = function(name, desc, id) {
			console.log(name, desc, id);
			$scope.name = name;
			$scope.desc = desc;
			$scope.id = id;

		}

		$scope.submit = function() {
			$scope.createJson();
			console.log(jsonFile);
		}
		
		$scope.createJson = function() {
			jsonFile = {
				"requestAction": "registerClient",
				"firstName": $scope.firstName,
				"lastName": $scope.lastName,
				"phoneNumber": $scope.phoneNumber,
				"username": $scope.username,
				"password": $scope.password,
				"careProfileId": $scope.id
			}
			webSocket.send(JSON.stringify(jsonFile));
		}
	
	});