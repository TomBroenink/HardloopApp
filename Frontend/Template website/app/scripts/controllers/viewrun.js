'use strict';

/**
 * @ngdoc function
 * @name yapp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of yapp
 */
angular.module('yapp')
	.controller('ViewRunCtrl', function($scope, $location, $stateParams) {
		var runId = $stateParams.id;
		var webSocket = new WebSocket("ws://localhost:9002/ws");
		webSocket.onopen = function(event) {
			webSocket.send('{"requestAction": "getRunById", "runId": "'+ runId + '"}');
		}
		webSocket.onmessage = function(event) {
			var response = JSON.parse(event.data);
			console.log(response);
			$scope.data = response.run;
			console.log($scope.data);
			$scope.$apply();
		}
		$scope.back = function() {
			window.history.back();
		}
	});