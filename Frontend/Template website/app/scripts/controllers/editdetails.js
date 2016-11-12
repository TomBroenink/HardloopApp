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
		var accessLevel = localStorage.getItem('accessLevel');
		var webSocket = new WebSocket("ws://localhost:9002/ws");
		webSocket.onmessage = function(event) {
			var response = JSON.parse(event.data);
			$scope.data = response;
			console.log($scope.data);
		}
/*		$scope.init = function () {
			webSocket.send('{"requestAction": "getAllClients"}');
		}*/

		$scope.back = function() {
			window.history.back();
		}
	});