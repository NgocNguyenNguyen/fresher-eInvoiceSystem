var app = angular.module('dbApp', [ 'ngRoute','zingchart-angularjs', 'oitozero.ngSweetAlert', 'uiSwitch', 'bw.paging', 'ngMessages' ]);
var BASE_URL = "http://localhost:8080/EInvoice/";
app.config(function($routeProvider) {

	$routeProvider.when("/", {
		templateUrl : "/EInvoice/views/welcome.html",
		controller : 'UserController',
	}).when("/user-manage", {
		templateUrl : "/EInvoice/views/admin/userManagement.html",
		controller : 'UserController as ctrl',
	}).when("/config-email", {
		templateUrl : "/EInvoice/views/admin/config-email.html",
		controller : 'UserController as ctrl',
	}).when("/customer-manage", {
		templateUrl : "/EInvoice/views/customer/customerManagement.html",
		controller: 'CustomerController as ctrl',
	}).when("/invoice-manage", {
		templateUrl : "/EInvoice/views/customer/invoiceManagement.html",
		controller: 'InvoiceController as ctrl',
	}).when("/report-manage", {
		templateUrl : "/EInvoice/views/customer/reportManagement.html",
		controller: 'ReportController as ctrl',
	}).when("/chart-manage", {
		templateUrl : "/EInvoice/views/customer/chartManagement.html",
		controller: 'ChartController as ctrl',
	}).when("/password-manage", {
		templateUrl : "/EInvoice/views/customer/changePassword.html",
		controller: 'UserController as ctrl',



	})
	
	.otherwise({
		redirectTo : '/'
	});

});
