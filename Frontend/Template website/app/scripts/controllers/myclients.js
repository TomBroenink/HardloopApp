'use strict';

/**
 * @ngdoc function
 * @name yapp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of yapp
 */
angular.module('yapp')
	.controller('MyClientsCtrl', function($scope, $location) {
		var webSocket = new WebSocket("ws://localhost:9002/ws");
		webSocket.onmessage = function(event) {
			var response = JSON.parse(event.data);
			$scope.data = response;
			console.log($scope.data);
		}
		// query naar server
		// json response komt terug en wordt opgeslagen in variable db
		$scope.init = function () {
			webSocket.send('{"requestAction": "getAllClients"}');
		}
	});