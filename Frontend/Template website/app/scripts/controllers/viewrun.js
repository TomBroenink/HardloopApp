'use strict';

/**
 * @ngdoc function
 * @name yapp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of yapp
 */
angular.module('yapp')
	.controller('ViewRunCtrl', function($scope, $location, $stateParams) {
		var runId = $stateParams.id;
		var webSocket = new WebSocket("ws://localhost:9002/ws");
		webSocket.onopen = function(event) {
			webSocket.send('{"requestAction": "getRunById", "runId": "'+ runId + '"}');
		}
		webSocket.onmessage = function(event) {
			var response = JSON.parse(event.data);
			$scope.data = response.run;
			var run = response.run;
			console.log($scope.data);
			$scope.$apply();

			generateMap();
			function generateMap() {
		        var map;
				var groningen = {lat: 53.2216999, lng: 6.5649233};
				var jsonFile;
				$scope.coords = [];
				var center = {lat: Number(run.route[0].lat), lng: Number(run.route[0].long)};
			    map = new google.maps.Map(document.getElementById('map'), {
			      	center: center,
			      	zoom: 13
			    });
			    for (var i = 0; i < run.route.length; i++) {
			    	var location = {lat: Number(run.route[i].lat), lng: Number(run.route[i].long)};
			    	//console.log(location);
					var marker = new google.maps.Marker({
						position: location,
						map: map
					});
			    }
			}
		}

		$scope.back = function() {
			window.history.back();
		}

	    

/*	    for (var i = 0; i < data.route.length; i++) {
	    	console.log('i');
	    }*/
/*	    var marker = new google.maps.Marker({
	        position: location, 
	        map: map
	    });
	    $scope.lat = '' + marker.getPosition().lat();
	    $scope.lng = '' + marker.getPosition().lng();
	    var location = [$scope.lat, $scope.lng];
	    $scope.coords.push(location);
	    $scope.$apply();
	    console.log($scope.coords);*/

	});