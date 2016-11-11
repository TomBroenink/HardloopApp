'use strict';

/**
 * @ngdoc function
 * @name yapp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of yapp
 */
angular.module('yapp')
	.controller('ClientCtrl', function($scope, $stateParams) {
		$scope.client = $stateParams.client;
		$scope.accessLevel = localStorage.getItem('accessLevel');
	});