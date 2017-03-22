package csc.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import csc.models.Parameter;

@Repository
public interface ParameterRepository extends CrudRepository<Parameter, Integer> {
	Parameter findById(Integer id);
	
	Parameter findByEmail(String email);
}
