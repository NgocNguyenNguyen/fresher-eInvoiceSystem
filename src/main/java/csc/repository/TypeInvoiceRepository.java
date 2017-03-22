package csc.repository;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import csc.models.TypeInvoice;

@Repository
public interface TypeInvoiceRepository extends CrudRepository<TypeInvoice, Integer> {
	
	@Cacheable("report")
	TypeInvoice findByNameInvoice(String nameinvoice);
	@Cacheable("report")
	TypeInvoice findById(Integer id);
}
