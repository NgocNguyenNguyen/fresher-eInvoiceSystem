app.controller('ChartController', ['$scope','$filter','ReportService' , 'TypeInvoiceService',
	function($scope, $filter, ReportService, TypeInvoiceService) {
			var self = this;
			var reports = [];
			var eb = [];
			var pb = [];
			var ib = [];
			var wb = [];
			var dt = [];
			var day;
			var totalpb = 0;
			var totaleb = 0;
			var totalib = 0;
			var totalwb = 0;
			var tpb = [];
			var teb = [];
			var tib = [];
			var twb = [];

		    function defaultValue() {
		        $scope.dateStart = new Date('01-10-2016');
		        $scope.dateEnd = new Date('04-10-2016');
		        $scope.totalElements = 0;
		    }
		    defaultValue();

			/* Action when click button */
			function fetchAllReport() {
				dateStart = new Date($scope.dateStart).toISOString().slice(0,
						10);
				dateEnd = new Date($scope.dateEnd).toISOString().slice(0, 10);

				// Call service get All Type
				TypeInvoiceService.fetchAll().then(getAllTypeSuccess,
						getAllTypeError);

			}
			// Event get All Type Success
			var getAllTypeSuccess = function(rs) {
				$scope.getAllType = rs;
				// Call service get report
				ReportService.fetchAllReport(dateStart, dateEnd).then(getReportSuccess,
						getReportError);
			};
			// Event get All Type Error
			var getAllTypeError = function(error) {
				console.error('Error while fetching All Type');
			}

			// Event get report Success
			var getReportSuccess = function(rs) {
				self.reports = rs.content;
				$scope.totalElements = rs.length;

				// If having report then display chart
				if (rs.length > 0) {
					$scope.isReport = true;
				}
				reports = rs;
				var a = new Date(reports[0].date);

				x = new Date(dateStart);
				y = new Date(dateEnd);

				// add in bar chart
				dt.push(a.getTime());

				if (x.getFullYear() == y.getFullYear()) {
					for (i = x.getMonth(); i <= y.getMonth(); i++) {
						for ( var temp in reports) {
							day = new Date(reports[temp].date);
							if (day.getMonth() == i) {
								switch (reports[temp].idType.code) {
								case "PB":
									pb.push(reports[temp].grandTotal);
									break;
								case "EB":
									eb.push(reports[temp].grandTotal);
									break;
								case "IB":
									ib.push(reports[temp].grandTotal);
									break;
								case "WB":
									wb.push(reports[temp].grandTotal);
									break;
								default:
									console.log("Nothing");
									break;
								}
							}
						}
					}
				} else {
					for (i = x.getMonth(); i <= (y.getMonth() + 12); i++) {
						for ( var temp in reports) {
							day = new Date(reports[temp].date);
							// console.log('day ' + day.getMonth());
							if (day.getMonth() == i) {
								switch (reports[temp].idType.code) {
								case "PB":
									pb.push(reports[temp].grandTotal);
									break;
								case "EB":
									eb.push(reports[temp].grandTotal);
									break;
								case "IB":
									ib.push(reports[temp].grandTotal);
									break;
								case "WB":
									wb.push(reports[temp].grandTotal);
									break;
								default:
									console.log("Nothing");
									break;
								}
							}
						}
					}
				}

				// add in pie chart
				for (i = 0; i < pb.length; i++) {
					totalpb += pb[i];
				}
				for (i = 0; i < eb.length; i++) {
					totaleb += eb[i];
				}
				for (i = 0; i < ib.length; i++) {
					totalib += ib[i];
				}
				for (i = 0; i < wb.length; i++) {
					totalwb += wb[i];
				}
				tpb.push(totalpb);
				teb.push(totaleb);
				tib.push(totalib);
				twb.push(totalwb);

				// refresh chart
				eb = [];
				pb = [];
				ib = [];
				wb = [];
				dt = [];
				totalpb = 0;
				totaleb = 0;
				totalib = 0;
				totalwb = 0;
				tpb = [];
				teb = [];
				tib = [];
				twb = [];
			};
			// Event get report Error
			var getReportError = function(errResponse) {
				console.error('Error while fetching Invoice');
			};

			$scope.barChart = {
				"type" : "bar",
				"scale-x" : {
					"min-value" : dt,
					"step" : 2629743000,
					"transform" : {
						"type" : "date",
						"all" : "%m.%Y"
					}
				},
				"series" : [ {
					'values' : pb,
					backgroundColor : "#FAEE00",
				}, {
					'values' : eb,
					backgroundColor : "#A0FFEE"
				},

				{
					'values' : wb,
					backgroundColor : "green"
				},

				{
					'values' : ib,
					backgroundColor : "red"
				}, ],
				"labels" : [ {
					"text" : "Phone Bill",
					"x" : "350px",
					"y" : "10px",
					"background-color" : "#FAEE00",
					"width" : "100px",
					"height" : "30px"
				}, {
					"text" : "Electric Bill",
					"x" : "450px",
					"y" : "10px",
					"background-color" : "#A0FFEE",
					"width" : "100px",
					"height" : "30px"
				}, {
					"text" : "Water Bill",
					"x" : "550px",
					"y" : "10px",
					"background-color" : "green",
					"width" : "100px",
					"height" : "30px"
				}, {
					"text" : "Internet Bill",
					"x" : "650px",
					"y" : "10px",
					"background-color" : "red",
					"width" : "100px",
					"height" : "30px"
				} ]
			};
			$scope.pieChart = {
				type : "pie",
				plot : {
					slice : 50
				// to make a donut
				},
				series : [ {
					values : tpb,
					text : "Phone Bill",
					"background-color" : "#FAEE00"
				}, {
					values : teb,
					text : "Electric Bill",
					"background-color" : "#A0FFEE"

				}, {
					values : tib,
					text : "Iternet Bill",
					"background-color" : "red"
				}, {
					values : twb,
					text : "Water Bill",
					"background-color" : "green"
				} ]
			};
		// Trigger when click button REPORT
		$scope.btnReport = function() {

			fetchAllReport();
		}
}]);

