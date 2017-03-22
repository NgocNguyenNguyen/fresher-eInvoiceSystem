package csc.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import csc.models.Parameter;
import csc.repository.ParameterRepository;

@Service("parameterService")
public class ParameterServiceImpl implements ParameterService{

	@Autowired
	ParameterRepository parameterRepository;

	@Override
	public Parameter findById(Integer id) {
		// TODO Auto-generated method stub
		return parameterRepository.findById(id);
	}

	@Override
	public void saveParameter(Parameter parameter) {
		// TODO Auto-generated method stub
		parameterRepository.save(parameter);
	}

	@Override
	public void updateParameter(Parameter parameter) {
		// TODO Auto-generated method stub
		parameterRepository.save(parameter);
	}

	@Override
	public Parameter findByEmail(String email) {
		// TODO Auto-generated method stub
		return parameterRepository.findByEmail(email);
	}
			

}
