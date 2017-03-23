package csc.service;

import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

import csc.models.Customer;
import csc.models.Users;

public interface CustomerService {
	void saveCustomer(Customer customer);
	
//	@Cacheable("users")
	Customer findByUser(Users user);
	
	@Cacheable("users")
	Customer findByEmail(String email);
	
	@Cacheable("users")
	Customer findById(long id);
	
	@CacheEvict(value="users", allEntries=true)
	void updateCustomer(Customer currentCustomer);
	
	@Cacheable("users")
	List<Customer> findAllUser();
}
