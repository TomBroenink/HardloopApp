'use strict';

/**
 * @ngdoc function
 * @name yapp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of yapp
 */
angular.module('yapp')
	.controller('ConnectRunsCtrl', function($scope, $location, $stateParams) {
		console.log($stateParams);
		var schemaId = $stateParams.id;
		var webSocket = new WebSocket("ws://localhost:9002/ws");
		webSocket.onopen = function(event) {
			webSocket.send('{"requestAction": "getAllRuns"}');
		}
		webSocket.onmessage = function(event) {
			var response = JSON.parse(event.data);
			console.log(response);
			if (response.responseAction == 'getAllRuns') {
				$scope.data = response;
				$scope.$apply();
			}
			if (response.responseAction == 'assignRunToRunSchema') {
				if (response.responseStatusCode == '0') {
					alert('Kan run niet koppelen aan schema!');
				} else {
					alert('Run gekoppeld aan schema!');
					$scope.back();
				}
				//console.log(response);
			}
			$scope.$apply();
		}
		$scope.back = function() {
			window.history.back();
		}
		$scope.connectRun = function(runId, name) {
			webSocket.send('{"requestAction": "assignRunToRunSchema","runSchemaId": "' + schemaId + '","runId": "' + runId + '"}');
		}
	});