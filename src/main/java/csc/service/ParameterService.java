package csc.service;

import org.springframework.cache.annotation.Cacheable;

import csc.models.Parameter;

public interface ParameterService {
	
	@Cacheable("users")
	Parameter findById(Integer id);
	
	@Cacheable("users")
	Parameter findByEmail(String email);
	
	void saveParameter(Parameter parameter);
	
	void updateParameter(Parameter parameter);
}
