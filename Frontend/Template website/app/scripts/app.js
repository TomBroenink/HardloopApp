'use strict';

/**
 * @ngdoc overview
 * @name yapp
 * @description
 * # yapp
 *
 * Main module of the application.
 */
angular
	.module('yapp', [
		'ui.router',
		'ngAnimate'
	])
	.config(function($stateProvider, $urlRouterProvider) {
		var userLoggedIn;
		if (localStorage.length == 0) {
			userLoggedIn = false;
		} else {
			userLoggedIn = true;
		}
 		$urlRouterProvider.when('/dashboard', '/dashboard/overview');
		$urlRouterProvider.otherwise('/login');
		$stateProvider
			.state('base', {
				abstract: true,
				url: '',
				templateUrl: 'views/base.html'
			})
			.state('login', {
				url: '/login',
				parent: 'base',
				templateUrl: 'views/login.html',
				controller: 'LoginCtrl'
			})
			.state('dashboard', {
				url: '/dashboard',
				parent: 'base',
				templateUrl: 'views/dashboard.html',
				controller: 'DashboardCtrl'
			})
			.state('overview', {
				url: '/overview',
				parent: 'dashboard',
				templateUrl: 'views/dashboard/overview.html'
			})
			.state('mijngegevens', {
				url: '/mijngegevens',
				parent: 'dashboard',
				templateUrl: 'views/dashboard/mijngegevens.html'
			})
			.state('reports', {
				url: '/reports',
				parent: 'dashboard',
				templateUrl: 'views/dashboard/reports.html'
			})
			.state('register', {
				url: '/register',
				parent: 'dashboard',
				templateUrl: 'views/dashboard/register.html'
			})
			.state('404', {
				url: '/404',
				parent: 'base',
				templateUrl: '/404.html'
			})
			.state('logout', {
				url: '/logout',
				template: '',
				controller: 'LogoutCtrl'
			});
	});