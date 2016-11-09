'use strict';

/**
 * @ngdoc function
 * @name yapp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of yapp
 */
angular.module('yapp')
	.controller('MyDetailsCtrl', function($scope, $location) {
		var db = {rij1: 'Dit is rij 1'};
		$scope.message = db.rij1;
	});
