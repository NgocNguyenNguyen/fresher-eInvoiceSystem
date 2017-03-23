package csc.controllers;

import java.util.HashSet;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import csc.models.Customer;
import csc.models.Register;
import csc.models.Role;
import csc.models.Users;
import csc.service.CustomerService;
import csc.service.RoleService;
import csc.service.UserService;

/**
 * A class to test interactions with the SQLSERVER database using the UserDao
 * class.
 *
 */
@RestController
public class UserController {
	private static final Logger log = LoggerFactory.getLogger(UserController.class);

	// ------------------------
	// PUBLIC METHODS
	// ------------------------
	@Autowired
	UserService userService;

	@Autowired
	CustomerService customerService;

	@Resource
	@Qualifier("roleService")
	RoleService roleService;

	@Autowired
	PasswordEncoder passwordEncoder;

	@RequestMapping(value = "/user/getEmail", method = RequestMethod.POST)
	public boolean getUser(@RequestBody String email) {
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


	@RequestMapping(value = "/user/getAll/active={active}&search={username}", method = RequestMethod.GET)
	public ResponseEntity<Page<Users>> listAllUsers(@PathVariable("active") String active,
			@PathVariable("username") String username, Pageable pageable) {

		if (active.contains("a")) {
			Page<Users> users = userService.findByUsernameContaining(username, pageable);
			if (users.getSize() == 0) {
				return new ResponseEntity<Page<Users>>(HttpStatus.NO_CONTENT);
			}
			return new ResponseEntity<Page<Users>>(users, HttpStatus.OK);
		} else {
			Page<Users> users = userService.findByActiveAndUsernameContaining(active, username, pageable);
			if (users.getSize() == 0) {
				return new ResponseEntity<Page<Users>>(HttpStatus.NO_CONTENT);
			}
			return new ResponseEntity<Page<Users>>(users, HttpStatus.OK);
		}

	}

	// -------------------Retrieve Single User--------------------------------------------------------

	@RequestMapping(value = "/user/get/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Users> getUser(@PathVariable("id") long id) {
		log.info("Fetching User with id " + id);
		Users user = userService.findById(id);
		if (user == null) {
			log.info("User with id " + id + " not found");
			return new ResponseEntity<Users>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Users>(user, HttpStatus.OK);
	}

	// -------------------Retrieve Single User--------------------------------------------------------

	// @RequestMapping(value = "/user/getByActive/{active}", method =
	// RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	// public ResponseEntity<Page<Users>>
	// getUserByActive(@PathVariable("active") String active, Pageable pageable)
	// {
	// log.info("Fetching User with id " + active);
	// Page<Users> user = userService.findByActive(active, pageable);
	// if (user.getSize() == 0) {
	// log.info("User with id " + active + " not found");
	// return new ResponseEntity<Page<Users>>(HttpStatus.NOT_FOUND);
	// }
	// return new ResponseEntity<Page<Users>>(user, HttpStatus.OK);
	// }

	// -------------------Create a User--------------------------------------------------------

	@RequestMapping(value = "/user/create", method = RequestMethod.POST)
	public ResponseEntity<Void> createUser(@RequestBody Users user, UriComponentsBuilder ucBuilder) {
		log.info("Creating User " + user.getUsername());

		if (userService.isUserExist(user)) {
			log.info("A User with name " + user.getUsername() + " already exist");
			return new ResponseEntity<Void>(HttpStatus.CONFLICT);
		}

		userService.saveUser(user);

		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(ucBuilder.path("/user/{id}").buildAndExpand(user.getId()).toUri());
		return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
	}

	// ------------------- Update a User --------------------------------------------------------

	@RequestMapping(value = "/user/update/{id}", method = RequestMethod.POST)
	public ResponseEntity<Users> updateUser(@PathVariable("id") long id, @RequestBody Users user) {
		log.info("Updating User " + id);

		Users currentUser = userService.findById(id);

		if (currentUser == null) {
			log.info("User with id " + id + " not found");
			return new ResponseEntity<Users>(HttpStatus.NOT_FOUND);
		}

		currentUser.setUsername(user.getUsername());
		currentUser.setPassword(user.getPassword());
		currentUser.setActive(user.getActive());

		userService.updateUser(currentUser);
		return new ResponseEntity<Users>(currentUser, HttpStatus.OK);
	}

	// ------------------- Delete a User --------------------------------------------------------

	@RequestMapping(value = "/user/delete/{id}", method = RequestMethod.GET)
	public ResponseEntity<Users> deleteUser(@PathVariable("id") long id) {
		log.info("Fetching & Deleting User with id " + id);

		Users user = userService.findById(id);
		if (user == null) {
			log.info("Unable to delete. User with id " + id + " not found");
			return new ResponseEntity<Users>(HttpStatus.NOT_FOUND);
		}

		userService.deleteUserById(id);
		return new ResponseEntity<Users>(HttpStatus.NO_CONTENT);
	}

	// ------------------- Register --------------------------------------------------------

	@RequestMapping(value = "/user/register", method = RequestMethod.POST)
	public ResponseEntity<Void> register(@RequestBody Register res, UriComponentsBuilder ucBuilder) {
		log.info("Register " + res.getUsername());

		Users user = new Users();
		user.setUsername(res.getUsername());
		user.setActive("1");
		user.setPassword(passwordEncoder.encode(res.getPassword()));
		HashSet<Role> roles = new HashSet<>();
		roles.add(roleService.findByName("ROLE_MEMBER"));
		user.setRoles(roles);
		userService.saveUser(user);

		Customer cus = new Customer();
		cus.setUser(user);
		// cus.setAddress("");
		cus.setEmail(res.getEmail());
		cus.setNameCustomer(res.getName());
		cus.setIdCustomer("CUS20170" + user.getId());
		// cus.setPhone(Integer.parseInt(res.getPhone()));
		// cus.setTaxCode(Integer.parseInt("0"));
		// cus.setLimitConsume(BigDecimal.valueOf(0));
		customerService.saveCustomer(cus);

		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(ucBuilder.path("/user/{id}").buildAndExpand(res.getId()).toUri());
		return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
	}

	// ------------------- Get Customer --------------------------------------------------------
	@RequestMapping(value = "/customer", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Customer> getCurrentCus() {
		Customer cus = this.getCustomer();
		cus.setUser(null);
		if (cus == null) {
			return new ResponseEntity<Customer>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Customer>(cus, HttpStatus.OK);
	}

	private Customer getCustomer() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String username = auth.getName();
		Users user = new Users();
		Customer cus = new Customer();
		user = userService.findByName(username);
		cus = customerService.findByUser(user);

		return cus;
	}

} // class UserController
