package csc.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import csc.models.Customer;
import csc.models.Users;
import csc.repository.CustomerRepository;

@Service("customerService")
public class CustomerServiceImpl implements CustomerService{

	@Autowired
	CustomerRepository customerRepository;
	
	@Override
	public void saveCustomer(Customer customer) {
		customerRepository.save(customer);
	}

	@Override
	public Customer findByUser(Users user) {
		return customerRepository.findByUser(user);
	}

	@Override
	public Customer findByEmail(String email) {
		return customerRepository.findByEmail(email);
	}

	@Override
	public Customer findById(long id) {
		return customerRepository.findById(id);
	}

	@Override
	public void updateCustomer(Customer currentCustomer) {
		customerRepository.save(currentCustomer);
	}

	@Override
	public List<Customer> findAllUser() {
		return customerRepository.findAll();
	}
	
	

}
