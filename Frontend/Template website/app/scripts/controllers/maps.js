'use strict';

/**
 * @ngdoc function
 * @name yapp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of yapp
 */
angular.module('yapp')
	.controller('MapsCtrl', function($scope) {
		function onGoogleReady() {
		    angular.bootstrap(document.getElementById("map"), ['app.ui-map']);
		}
		$scope.mapOptions = {
	      	center: new google.maps.LatLng(35.784, -78.670),
	      	zoom: 15,
	      	mapTypeId: google.maps.MapTypeId.ROADMAP
	    };
	});