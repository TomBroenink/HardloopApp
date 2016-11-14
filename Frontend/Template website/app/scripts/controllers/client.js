'use strict';

/**
 * @ngdoc function
 * @name yapp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of yapp
 */
angular.module('yapp')
	.controller('ClientCtrl', function($scope, $location, $stateParams) {
		var clientId = $stateParams.id;
		var accessLevel = localStorage.getItem('accessLevel');
		var webSocket = new WebSocket("ws://localhost:9002/ws");
		webSocket.onopen = function(event) {
			webSocket.send('{"requestAction": "getClientById", "clientId": "'+ clientId + '"}');
			webSocket.send('{"requestAction": "getRunSchemasForClient", "clientId": "'+ clientId + '"}');
		}
		webSocket.onmessage = function(event) {
			var response = JSON.parse(event.data);
			if (response.responseAction == 'getRunSchemasForClient') {
				$scope.schemas = response.runSchemas;
				console.log($scope.schemas);
				$scope.$apply();
			}
			if (response.responseAction == 'getClientById') {
				console.log(response.client);
				$scope.data = response.client;
				$scope.$apply();
			}
		}
		$scope.back = function() {
			window.history.back();
		}
		$scope.dropdownChange = function() {
			$location.path('/dashboard/viewschema/' + $scope.selectedSchema.id)
		}
	});