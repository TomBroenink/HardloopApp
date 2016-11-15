'use strict';

/**
 * @ngdoc function
 * @name yapp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of yapp
 */
angular.module('yapp')
	.controller('CareProfilesCtrl', function($scope) {
		var response;

		var webSocket = new WebSocket("ws://localhost:9002/ws");
		webSocket.onopen = function(event) {
			webSocket.send('{"requestAction": "getAllCareProperties"}');
		}
		webSocket.onmessage = function(event) {
			response = JSON.parse(event.data);
			console.log(response);
			console.log(response.careProperties);
			$scope.data = response.careProperties;
			$scope.$apply();
		}

		$scope.deleteProfile = function(profileId) {
			console.log(profileId);
			var temp = '{"requestAction": "deleteCareProfile", "careProfileId": ' + profileId + '}';
			console.log(temp);
			webSocket.send(temp);
		}
		$scope.addProfile = function() {
			webSocket.send('{"requestAction": "createCareProperty","careProperty": {"name": "' + $scope.name + '","description": "' + $scope.description + '"}}');
		}
	});