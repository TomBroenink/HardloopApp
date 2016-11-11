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
		$scope.db = [
			{'username': 'TomBroenink'},
			{'username': 'test'},
			{'username': 'Hennie' }
		];
	});