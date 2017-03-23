angular.module('dbApp').controller('CustomerController', function($scope, CustomerService, SweetAlert) {
	 var self = this;
	 
	$scope.myEnable = function(){
	/*
	 * document.myForm.name.disabled = false; document.myForm.email.disabled =
	 * false; document.myForm.address.disabled = false;
	 * document.myForm.phone.disabled = false; document.myForm.taxcode.disabled =
	 * false; document.myForm.limit.disabled = false;
	 * document.myForm.btn.disabled = false;
	 */
		document.myForm.set.disabled = false;
		console.log('enabled form');
		
		$scope.isUpdate = true;
    };
    
    self.user={id:null,
    		username:'',
    		password:'',
    		active: ''
    };   
    self.cus={
    		id:null,
    		idCustomer:'',
    		nameCustomer:'',
    		address:'',
    		email:'',
    		phone:'',
    		taxCode:'',
    		limitConsume:''
    		}; 
    
   
    self.users=[];
       
    self.edit = edit;
    self.updateCustomer = updateCustomer;
    self.changePwd = changePwd;
    self.getPwd = getPwd;
    // seft.getCustomer = getCustomer;
    
    
    getCustomer();
   
    function getCustomer(){
    	CustomerService.getCustomer()
            .then(
            function(d) {

            	self.cus = d; 
            	
            	// console.log("d.totalElements" + d.totalElements);
            },
            function(errResponse){
                console.error('Error while fetching Customer');
            }
        );
    }
    
    function updateCustomer(){    	
    	// alert
        SweetAlert.swal({
        	title: "Are you sure?",
        	text: "You will update your information.",
        	type: "warning",
        	showCancelButton: true,
        	confirmButtonColor: "#DD6B55",
        	confirmButtonText: "Yes, update it!",
        	cancelButtonText: "No, cancel plx!",
        	closeOnConfirm: false,
        	closeOnCancel: true
        }, 
        function(isConfirm){ // Function that triggers on user action.
    		// var r = confirm("Are you sure!");
    		// console.log(self.cus);
			  if (isConfirm) {
				  CustomerService.updateCustomer(self.cus)
	                .then(
		                function(succes){
		                	SweetAlert.swal("Updated!", "Your information has been updated.", "success");
		                	document.myForm.set.disabled = true;
		                	$scope.isUpdate = false;
		                }),
	                	// console.log("Update success")
	                	// document.myForm.set.disabled = true;
		                function(errResponse){
		                    console.error('Error while updating Customer');
		                    SweetAlert.swal("Error!", "Error while updating Customer", "error");
		                }
			  } else {
				    //Do nothing.
			  }
            
        });
    }
    
    function edit(id){
        console.log('id to be edited', id);
        for(var i = 0; i < self.users.length; i++){
            if(self.users[i].id === id) {
                self.cus = angular.copy(self.users[i]);             
                break;
            }
        }
    }
    
    function changePwd(){
    	console.log('Enter!')
    	self.user.password = $scope.newpass;
    	self.user.active = "1";
    	
    	SweetAlert.swal({
        	title: "Are you sure?",
        	text: "You will change your password.",
        	type: "warning",
        	showCancelButton: true,
        	confirmButtonColor: "#DD6B55",
        	confirmButtonText: "Yes, update it!",
        	cancelButtonText: "No, cancel plx!",
        	closeOnConfirm: false,
        	closeOnCancel: true
        }, 
        function(isConfirm){ 
    		
			  if (isConfirm) {
				  CustomerService.changePwd(self.user)
	                .then(
		                function(succes){
		                	SweetAlert.swal("Changed!", "Your password has been updated.", "success");
		                	
		                }),
	                	// console.log("Update success")
	                	// document.myForm.set.disabled = true;
		                function(errResponse){
		                    console.error('Error while updating Customer');
		                    SweetAlert.swal("Error!", "Error while updating Customer", "error");
		                }
			  } else {
				    //Do nothing.
			  }
            
        });
    	
    }
	    function getPwd(){
	    	CustomerService.getPwd($scope.oldpass)
	        .then(
	        function(success) {	        	
	        	if(success == false)
	        	{
	        		$scope.isOldPass = true;
	        		$scope.pwdError = "Wrong password, Please check your password!"
	        			document.myFormChange.newpass.disabled = true;
	        			document.myFormChange.cfnewpass.disabled = true;
	        			document.myFormChange.btn.disabled = true;
	        	}
	        	else {
	        		$scope.isOldPass = false;
	        		document.myFormChange.newpass.disabled = false;
        			document.myFormChange.cfnewpass.disabled = false;
        			document.myFormChange.btn.disabled = false;
	        	}
	        },
	        function(errResponse){
	            console.error('Error while fetching User');
	        }
	    );
	    }	    
});


