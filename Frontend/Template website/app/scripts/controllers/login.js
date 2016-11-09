'use strict';

/**
 * @ngdoc function
 * @name yapp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of yapp
 */
angular.module('yapp')
  .controller('LoginCtrl', function($scope, $location) {
    $scope.submit = function() {
		// Mock data voor test
		var db = {responseCode : 200, email : 'tom_broenink@hotmail.com', password : 'test'}
		
		var email = $scope.Email;
		var password = $scope.Password;
		var userObject = {};
		if (!email) {
			return alert('Emailadres is leeg!');
		}
		if (!password) {
			return alert('Password is leeg!');
		}
		userObject = {email : email, password : password};
		
		// Stuur userObject met AJAX call naar server
		// Server stuurt een responseCode en eventueel JSON object terug
		// Lees responseCode uit header uit
		// Als responseCode 200 is dan stuurt de server een JSON object terug met de gegevens van de gebruiker
		// Stringify dit object en sla deze string op in localStorage
		// Elders in de app kan de string uit localStorage gehaald worden en geparsed worden om gebruikt te kunnen worden.
		
		if (db.responseCode == 200) {
			// Sla hier de naam en accesslevel in sessionStorage op
			if (email === db.email) {
				if (password === db.password) {
					alert('Succesvol ingelogd!');
					localStorage.setItem('username', db.email);
					localStorage.setItem('accessLevel', 1);
					$location.path('/dashboard');
					return false;
				}
			} else {
				alert('Verkeerde emailadres');
			}
		} else {
			alert('Fout bij inloggen');
		}
    }
  });