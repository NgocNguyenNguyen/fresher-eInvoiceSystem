package csc.controllers;

import java.text.SimpleDateFormat;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import csc.models.Customer;
import csc.models.Invoice;
import csc.models.TypeInvoice;
import csc.models.Users;
import csc.repository.TypeInvoiceRepository;
import csc.service.CompanyService;
import csc.service.CustomerService;
import csc.service.InvoiceService;
import csc.service.UserService;

/**
 * @author user
 */
@RestController
public class InvoiceController {

	// ------------------------
	// PUBLIC METHODS
	// ------------------------
	@Autowired
	InvoiceService invoiceService;

	@Autowired
	UserService userService;

	@Autowired
	CustomerService customerService;

	@Autowired
	CompanyService companyService;
	
	@Autowired
	private TypeInvoiceRepository typeInvoiceRepository;

	@RequestMapping(value = "/user/getReport/start={start}&end={end}", method = RequestMethod.GET)
	public ResponseEntity<List<Invoice>> getListReport(@PathVariable("start") String dateStart,
			@PathVariable("end") String dateEnd) {
		System.out.println("getListReport");
		System.out.println("start= " + dateStart + " -end= " +dateEnd );
		Customer idCus = this.getIdCustomer();
		
		SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");
		List<Invoice> invoices = null;
		try {
			invoices = invoiceService.getListReport(idCus, myFormat.parse(dateStart), myFormat.parse(dateEnd));
		} catch (Exception ex) {
			System.out.println("Exception=" + ex.getMessage());
		}
		
		if (invoices.size() == 0) {
			return new ResponseEntity<List<Invoice>>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<List<Invoice>>(invoices, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/user/getExpensesReport/start={start}&end={end}&type={type}", method = RequestMethod.GET)
	public ResponseEntity<List<Invoice>> getExpensesReport(@PathVariable("start") String dateStart,
			@PathVariable("end") String dateEnd, @PathVariable("type") int idType) {
		System.out.println("getExpensesReport");
		System.out.println("start= " + dateStart + " -end= " +dateEnd );
		//Get current id customer
		Customer idCus = this.getIdCustomer();
		List<Invoice> invoices = null;
		TypeInvoice type= null;
		// Format Datetime
		SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");

		try {
			type = typeInvoiceRepository.findById(idType);
			
			invoices = invoiceService.getExpensesReport(idCus, 
					myFormat.parse(dateStart), myFormat.parse(dateEnd), 
					type);
		} catch (Exception ex) {
			System.out.println("Exception=" + ex.getMessage());
		}
		
		if (invoices.size() == 0) {
			return new ResponseEntity<List<Invoice>>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<List<Invoice>>(invoices, HttpStatus.OK);
	}

	// -------------------Retrieve All Users--------------------------------------------------------

	@RequestMapping(value = "/invoice/getAll/search={search}", method = RequestMethod.GET)
	public ResponseEntity<Page<Invoice>> listAllInvoice(@PathVariable("search") String search, Pageable pageable) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String username = auth.getName();
		Users user = new Users();
		user = userService.findByName(username);

		Customer cus = new Customer();
		cus = customerService.findByUser(user);

		Page<Invoice> invoice = invoiceService.findAllInvoice(cus, search, pageable);
		if (invoice.getSize() == 0) {
			return new ResponseEntity<Page<Invoice>>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<Page<Invoice>>(invoice, HttpStatus.OK);
	}

	// -------------------Retrieve Single User--------------------------------------------------------

	@RequestMapping(value = "/invoice/get/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Invoice> getInvoice(@PathVariable("id") long id) {
		Invoice invoice = invoiceService.findById(id);
		if (invoice == null) {
			return new ResponseEntity<Invoice>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Invoice>(invoice, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/invoice/getName/{contractNum}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Invoice> getInvoiceByContract(@PathVariable("contractNum") String contractNum) {
		Invoice invoice = invoiceService.findByContractNumberAndIdCustomer(contractNum, this.getIdCustomer());
		if (invoice == null) {
			invoice =  new Invoice();
		}
		return new ResponseEntity<Invoice>(invoice, HttpStatus.OK);
	}

	// -------------------Create a User--------------------------------------------------------

	@RequestMapping(value = "/invoice/create", method = RequestMethod.POST)
	public ResponseEntity<Void> createInvoice(@RequestBody Invoice invoice, UriComponentsBuilder ucBuilder) {
		System.out.println("Creating Invoice " + invoice.getId());

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String username = auth.getName();
		Users user = new Users();
		user = userService.findByName(username);
		System.out.println("Type invoice" + invoice.getIdType());
		Customer cus = new Customer();
		cus = customerService.findByUser(user);
		
		invoice.setIdCustomer(cus);

		invoiceService.saveInvoice(invoice);

		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(ucBuilder.path("/invoice/{id}").buildAndExpand(invoice.getId()).toUri());
		return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
	}

	// ------------------- Update a User --------------------------------------------------------

	@RequestMapping(value = "/invoice/update/{id}", method = RequestMethod.POST)
    public ResponseEntity<Invoice> updateInvoice(@PathVariable("id") long id, @RequestBody Invoice invoice) {
        System.out.println("Updating Invoice " + id);
          
        Invoice currentInvoice = invoiceService.findById(id);
          
        if (currentInvoice==null) {
            System.out.println("Invoice with id " + id + " not found");
            return new ResponseEntity<Invoice>(HttpStatus.NOT_FOUND);
        }
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String username = auth.getName();
		Users user = new Users();
		user = userService.findByName(username);
		System.out.println("Type invoice" +invoice.getIdType()); 
		Customer cus = new Customer();
		cus = customerService.findByUser(user);
        
        currentInvoice.setDate(invoice.getDate());
        currentInvoice.setContractNumber(invoice.getContractNumber());
        currentInvoice.setNameService(invoice.getNameService());
        currentInvoice.setIndexConsumed(invoice.getIndexConsumed());
        currentInvoice.setTotal(invoice.getTotal());
        currentInvoice.setVat(invoice.getVat());
        currentInvoice.setPtef(invoice.getPtef());
        currentInvoice.setGrandTotal(invoice.getGrandTotal());
        currentInvoice.setIdCustomer(cus);
        currentInvoice.setIdType(invoice.getIdType());
        currentInvoice.setIdCpn(invoice.getIdCpn());
          
        invoiceService.updateInvoice(currentInvoice);
        return new ResponseEntity<Invoice>(currentInvoice, HttpStatus.OK);
    }

	// ------------------- Delete a User --------------------------------------------------------

	@RequestMapping(value = "/invoice/delete/{id}", method = RequestMethod.GET)
	public ResponseEntity<Invoice> deleteInvoice(@PathVariable("id") long id) {
		System.out.println("Fetching & Deleting Invoice with id " + id);

		Invoice invoice = invoiceService.findById(id);
		if (invoice == null) {
			System.out.println("Unable to delete. Invoice with id " + id + " not found");
			return new ResponseEntity<Invoice>(HttpStatus.NOT_FOUND);
		}

		invoiceService.deleteInvoiceById(id);
		return new ResponseEntity<Invoice>(HttpStatus.NO_CONTENT);
	}

	private Customer getIdCustomer() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String username = auth.getName();
		Users user = new Users();
		Customer cus = new Customer();
//		String idCus = null;
		
		user = userService.findByName(username);

		cus = customerService.findByUser(user);

//		idCus = cus.getIdCustomer();
		
		return cus;
	}

} // class UserController