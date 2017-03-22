package csc.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import csc.models.Customer;
import csc.models.Invoice;
import csc.models.TypeInvoice;

@Repository
public interface InvoiceRepository extends CrudRepository<Invoice, Long>, JpaRepository<Invoice, Long> {

	//@Query(value = "select * from Invoice u where u.id_customer = ?#{[0]} and u.date between ?#{[1]} and ?#{[2]} ORDER BY ?#{#pageable}", nativeQuery = true)
	//Page<Invoice> findDateByIdCus(String idCus, String dateStart, String dateEnd, Pageable pageable);
	
	Invoice findByContractNumber(String contractnumber);
	
	Invoice findByContractNumberAndIdCustomer(String contractnumber, Customer customer);

	List<Invoice> findByIdCustomer(Customer idcustomer);

	Page<Invoice> findAll(Pageable pageable);
	
	Page<Invoice> findByIdCustomerAndContractNumberContaining(Customer idcustomer, String contractnumber, Pageable pageable);

	Page<Invoice> findByIdCustomer(Customer idcustomer, Pageable pageable);
	
	List<Invoice> findByIdCustomerAndDateBetween(Customer idCus, Date dateStart, Date dateEnd);
	
	List<Invoice> findByIdCustomerAndDateBetweenAndIdType(Customer idCus, Date dateStart, Date dateEnd, TypeInvoice type);

}