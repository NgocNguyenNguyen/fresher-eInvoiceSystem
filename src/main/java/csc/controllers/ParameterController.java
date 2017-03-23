package csc.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import csc.models.Parameter;
import csc.service.ParameterService;

@RestController
public class ParameterController {
	private static final Logger log = LoggerFactory.getLogger(ParameterController.class);

	@Autowired
	ParameterService parameterService;
		
	@Autowired
	PasswordEncoder passwordEncoder;

		   
    //-------------------Retrieve Single Parameter--------------------------------------------------------
      
    @RequestMapping(value = "/parameter/get/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Parameter> getParameterByKey(@PathVariable("id") Integer id) {
    			
		Parameter para = parameterService.findById(id);
				
        log.info("Fetching Customer with id " + para.getId());
        
        return new ResponseEntity<Parameter>(para, HttpStatus.OK);
    } 
    
    //------------------- Update a Customer--------------------------------------------------------
    
    @RequestMapping(value = "/parameter/update/{id}", method = RequestMethod.POST)
    public ResponseEntity<Parameter> updateParameter(@PathVariable("id") Integer id, @RequestBody Parameter parameter) {
          
    	Parameter currentParameter = parameterService.findById(id);
          
        if (currentParameter==null) {
            return new ResponseEntity<Parameter>(HttpStatus.NOT_FOUND);
        }
               
    	currentParameter.setTimeEmail(parameter.getTimeEmail());      
    	currentParameter.setEmail(parameter.getEmail());
    	currentParameter.setPwdEmail(parameter.getPwdEmail());
                                  
        parameterService.updateParameter(currentParameter);
        return new ResponseEntity<Parameter>(currentParameter, HttpStatus.OK);
    }
    
}
