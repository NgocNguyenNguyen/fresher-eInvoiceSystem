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

import csc.models.Company;
import csc.models.TypeInvoice;
import csc.service.CompanyService;
import csc.service.TypeInvoiceService;

@RestController
public class CompanyController {
	private static final Logger log = LoggerFactory.getLogger(CompanyController.class);
	// ------------------------
	// PUBLIC METHODS
	// ------------------------
	@Autowired
	CompanyService companyService;
	@Autowired
	TypeInvoiceService typeInvoiceService;
	
	//-------------------Retrieve All Users--------------------------------------------------------
    
    @RequestMapping(value = "/invoice/getCpn/{id}", method = RequestMethod.GET)
    public ResponseEntity<List<Company>> getCpn(@PathVariable("id") int id) {
    	
    	
    	TypeInvoice ti = typeInvoiceService.findById(id);
    	List<Company> lstCpn = companyService.findByIdType(ti);
    	
        if(lstCpn.size() == 0){
            return new ResponseEntity<List<Company>>(HttpStatus.NO_CONTENT);//You many decide to return HttpStatus.NOT_FOUND
        }
        return new ResponseEntity<List<Company>>(lstCpn, HttpStatus.OK);
    }
}
