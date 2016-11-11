'use strict';

/**
 * @ngdoc function
 * @name yapp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of yapp
 */
angular.module('yapp')
	.controller('MyClientsCtrl', function($scope, $location, $routeParams) {
		console.log($routeParams);
		$scope.client1 = 'TomBroenink';
	});