app.controller('ReportController', [
		'$scope',
		'$filter',
		'ReportService',
		'TypeInvoiceService',
		function($scope, $filter, ReportService, TypeInvoiceService) {

			var self = this;
			var reports = [];
			self.typeInvoices=[];
			
			function defaultValue() {
				self.titleReport = "Report: Expenses Report";
				self.currencyUnit = "VND";
				self.totalIndex = 0;
				self.totalGrand = 0;
				$scope.dateStart = new Date('01-10-2016');
				$scope.dateEnd = new Date('04-10-2016');
			}

			defaultValue();

			// Event get All Type Success
			var getAllTypeSuccess = function(rs) {
				self.typeInvoices = rs;
			};
			// Event get All Type Error
			var getAllTypeError = function(error) {
				console.error('Error while fetching All Type');
			}
			// Call service get All Type
			TypeInvoiceService.fetchAll().then(getAllTypeSuccess, getAllTypeError);
					
			// Event get report Success
			var getReportSuccess = function(rs) {
				self.reports = rs;

				// If having report then display chart
				if (self.reports.length > 0) {
					$scope.isReport = true;
					self.nameInvoice = self.reports[0].idType.nameInvoice;
					if (self.reports[0].indexConsumed == 1) {
						$scope.indexConsumed = false;
					} else {
						$scope.indexConsumed = true;
					}
					for (index in rs) {
						self.totalIndex = self.totalIndex + rs[index].indexConsumed;
						self.totalGrand = self.totalGrand + rs[index].grandTotal;
					}
				}
				reports = rs;
			};
			// Event get report Error
			var getReportError = function(errResponse) {
				console.error('Error while fetching Invoice');
			};			
			
			/* Action when click button */
			function fetchAllReport() {
				dateStart = new Date($scope.dateStart).toISOString().slice(0, 10);
				dateEnd = new Date($scope.dateEnd).toISOString().slice(0, 10);
				
				// Call service get report
				ReportService.fetchExpensesReport(dateStart, dateEnd, self.idType)
						.then(getReportSuccess, getReportError);

			}

			// Trigger when click button REPORT
			$scope.btnReport = function() {
				fetchAllReport();
				self.totalIndex = 0;
				self.totalGrand = 0;
			}
			
		} ]);