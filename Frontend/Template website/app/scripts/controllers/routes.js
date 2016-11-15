'use strict';

/**
 * @ngdoc function
 * @name yapp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of yapp
 */
angular.module('yapp')
	.controller('RoutesCtrl', function($scope, $location) {
		var webSocket = new WebSocket("ws://localhost:9002/ws");
		webSocket.onopen = function(event) {
			webSocket.send('{"requestAction":"getAllRuns"}');
		}
		webSocket.onmessage = function(event) {
			var response = JSON.parse(event.data);
			$scope.data = response.runs;
			console.log($scope.data);
			$scope.$apply();
		}
	});