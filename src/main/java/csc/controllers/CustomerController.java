package csc.controllers;

import java.util.HashSet;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import csc.models.Customer;
import csc.models.Role;
import csc.models.Users;
import csc.service.CustomerService;
import csc.service.RoleService;
import csc.service.UserService;

@RestController
public class CustomerController {
	private static final Logger log = LoggerFactory.getLogger(CustomerController.class);
	@Autowired
	UserService userService;

	@Autowired
	CustomerService customerService;

	@Resource
	@Qualifier("roleService")
	RoleService roleService;

	@Autowired
	PasswordEncoder passwordEncoder;

	@RequestMapping(value = "/customer/getEmail", method = RequestMethod.POST)
	public boolean getCustomer(@RequestBody String email) {
		try {
			Customer cus = customerService.findByEmail(email);
			log.info("Email " + cus.getEmail());
		} catch (Exception ex) {
			log.error("Email does not exist");
			return false;
		}
		log.info("Email " + email + " does exist");
		return true;
	}

	// -------------------Retrieve Single Customer--------------------------------------------------------

	@RequestMapping(value = "/customer/get", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Customer> getCustomer() {

		Customer cus = this.getCustomerAuthen();
		//cus.getUser().setPassword(null);
		//cus.getUser().setRoles(null);

		log.info("Fetching Customer with id " + cus.getId());

		if (cus == null) {
			log.error("User with id " + cus.getId() + " not found");
			return new ResponseEntity<Customer>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Customer>(cus, HttpStatus.OK);
	}

	// ------------------- Update a Customer--------------------------------------------------------

	@RequestMapping(value = "/customer/update", method = RequestMethod.POST)
	public ResponseEntity<Customer> updateCustomer(@RequestBody Customer customer) {

		Customer currentCustomer = this.getCustomerAuthen();

		if (currentCustomer == null) {
			return new ResponseEntity<Customer>(HttpStatus.NOT_FOUND);
		}

		currentCustomer.setNameCustomer(customer.getNameCustomer());
		currentCustomer.setAddress(customer.getAddress());
		currentCustomer.setEmail(customer.getEmail());
		currentCustomer.setPhone(customer.getPhone());
		currentCustomer.setTaxCode(customer.getTaxCode());
		currentCustomer.setLimitConsume(customer.getLimitConsume());

		customerService.updateCustomer(currentCustomer);
		return new ResponseEntity<Customer>(currentCustomer, HttpStatus.OK);
	}

	private Customer getCustomerAuthen() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String username = auth.getName();
		Users user = new Users();
		Customer cus = new Customer();
		user = userService.findByName(username);
		cus = customerService.findByUser(user);

		return cus;
	}

	@RequestMapping(value = "/customer/updatepwd", method = RequestMethod.POST)
	public ResponseEntity<Users> updatePassword(@RequestBody Users curUser) {

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String username = auth.getName();
		Users user = new Users();
		user = userService.findByName(username);
		user.setPassword(passwordEncoder.encode(curUser.getPassword()));
		HashSet<Role> roles = new HashSet<>();
		roles.add(roleService.findByName("ROLE_MEMBER"));
		user.setRoles(roles);
		user.setActive(curUser.getActive());
		userService.updateUser(user);
		return new ResponseEntity<Users>(user, HttpStatus.OK);

	}

	@RequestMapping(value = "/customer/getPwd", method = RequestMethod.POST)
	public boolean getPwd(@RequestBody String Pwd) {
		try {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			String username = auth.getName();
			Users user = new Users();
			user = userService.findByName(username);
			if (passwordEncoder.matches(Pwd, user.getPassword())) {
				log.info("true");
				return true;
			} else {
				log.info("false");
				return false;
			}
		} catch (Exception ex) {
			log.error("Pwd does not exist");
			return false;
		}
	}
}
