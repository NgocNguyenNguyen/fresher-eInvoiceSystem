package csc.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import csc.models.Service;
import csc.models.TypeInvoice;
import csc.service.TypeInvoiceService;

/**
 * A class to test interactions with the SQLSERVER database using the UserDao
 * class.
 *
 */
@RestController
public class TypeInvoiceController {
	private static final Logger log = LoggerFactory.getLogger(TypeInvoiceController.class);

	// ------------------------
	// PUBLIC METHODS
	// ------------------------
	@Autowired
	TypeInvoiceService typeInvoiceService;
		
		
	
	//-------------------Retrieve All Users--------------------------------------------------------
    
    @RequestMapping(value = "/invoice/getTypeAll", method = RequestMethod.GET)
    public ResponseEntity<List<TypeInvoice>> listAllTypeInvoice() {
    			
    	List<TypeInvoice> typeInvoice = typeInvoiceService.findAll();
    	
        if(typeInvoice == null){
            return new ResponseEntity<List<TypeInvoice>>(HttpStatus.NO_CONTENT);//You many decide to return HttpStatus.NOT_FOUND
        }
        return new ResponseEntity<List<TypeInvoice>>(typeInvoice, HttpStatus.OK);
    }
    
    @RequestMapping(value = "/invoice/getAllService/{id}", method = RequestMethod.GET)
    public ResponseEntity<List<Service>> listAllService(@PathVariable("id") TypeInvoice id) {
    			
    	List<Service> service = typeInvoiceService.findByIdType(id);
    	
        if(service == null){
            return new ResponseEntity<List<Service>>(HttpStatus.NO_CONTENT);//You many decide to return HttpStatus.NOT_FOUND
        }
        return new ResponseEntity<List<Service>>(service, HttpStatus.OK);
    }
    
    @RequestMapping(value = "/invoice/getService/name={name}&id={id}", method = RequestMethod.GET)
    public ResponseEntity<Service> getServiceByNameAndIdtype(@PathVariable("name") String name, @PathVariable("id") TypeInvoice id) {
    			
    	Service service = typeInvoiceService.findByNameServiceAndIdType(name, id);
    	
        if(service == null){
            return new ResponseEntity<Service>(HttpStatus.NO_CONTENT);//You many decide to return HttpStatus.NOT_FOUND
        }
        return new ResponseEntity<Service>(service, HttpStatus.OK);
    }
       

} // class UserController
