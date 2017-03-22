package csc.service;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import csc.models.Users;

public interface UserService {
	
	@Cacheable("users")
	Users findById(long id);
	
	@Cacheable("users")
	Users findByName(String name);
	
	@Cacheable("users")
	void saveUser(Users user);
	
	@CacheEvict(value = "users", allEntries = true)
	void updateUser(Users user);
	
	@CacheEvict(value = "users", allEntries = true)
	void deleteUserById(long id);

	@Cacheable("users")
	Page<Users> findAllUsers(Pageable pageable); 
	
	@CacheEvict(value = "users", allEntries = true)
	void deleteAllUsers();
	
	@Cacheable("users")
	public boolean isUserExist(Users user);
	
	@Cacheable("users")
	Page<Users> findByActiveAndUsernameContaining(String active, String username, Pageable pageable);
	
	@Cacheable("users")
	Page<Users> findByUsernameContaining(String username, Pageable pageable);

			
}