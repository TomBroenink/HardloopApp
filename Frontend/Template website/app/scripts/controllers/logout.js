'use strict';

/**
 * @ngdoc function
 * @name yapp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of yapp
 */

angular.module('yapp')
	.controller('LogoutCtrl', function($location) {
		localStorage.clear();
		$location.path('/');
	});