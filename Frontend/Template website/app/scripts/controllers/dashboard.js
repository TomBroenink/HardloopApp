'use strict';

/**
 * @ngdoc function
 * @name yapp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of yapp
 */
angular.module('yapp')
	.controller('DashboardCtrl', function($scope, $state) {
		$scope.$state = $state;
	});
	function DashboardCtrl($scope) {
		$scope.currentNavItem = 'page1';
	}