'use strict';

/**
 * @ngdoc function
 * @name yapp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of yapp
 */
angular.module('yapp')
	.controller('MapsCtrl', function($scope, $location) {
		var webSocket = new WebSocket("ws://localhost:9002/ws");
		webSocket.onmessage = function(event) {
			var response = JSON.parse(event.data);
			console.log(response);
			if (response.responseStatusCode == '1') {
				alert('Run succesvol toegevoegd!');
			} else {
				alert('Run toevoegen mislukt!');
			}
		}
        var map;
		var groningen = {lat: 53.2216999, lng: 6.5649233};
		var jsonFile;
		$scope.coords = [];

		//["lat", "long"],
		//["lat", "long"],
		//["lat", "long"]

	    map = new google.maps.Map(document.getElementById('map'), {
	      	center: groningen,
	      	zoom: 13
	    });

/*        var marker = new google.maps.Marker({
          	position: groningen,
          	map: map,
          	title: 'Test'
        });*/

        google.maps.event.addListener(map, 'click', function(event) {
		    placeMarker(event.latLng);
		});

        function placeMarker(location) {
		    var marker = new google.maps.Marker({
		        position: location, 
		        map: map
		    });
		    $scope.lat = '' + marker.getPosition().lat();
		    $scope.lng = '' + marker.getPosition().lng();
		    var location = [$scope.lat, $scope.lng];
		    $scope.coords.push(location);
		    $scope.$apply();
		    console.log($scope.coords);
		}

  		$scope.createJson = function() {
  			jsonFile = {
  				"requestAction": "createRun",
				"name": $scope.runName,
				"description": $scope.runDescription,
				"distance": "",
				"route": $scope.coords
  			}
  			send();
  		}

  		function send() {
  			console.log(jsonFile);
  			webSocket.send(JSON.stringify(jsonFile));
  		}

	    // functie die coords ophaalt na klik
	    // $scope.coord1 = response.lat
	    // $scope.coord2 = response.long
			
});