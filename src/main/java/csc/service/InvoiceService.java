package csc.service;

import java.util.Date;
import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import csc.models.Customer;
import csc.models.Invoice;
import csc.models.TypeInvoice;

public interface InvoiceService {

	@Cacheable("invoice")
	Invoice findById(long id);
	
	@CacheEvict(value = {"report", "invoice"}, allEntries = true)
	void saveInvoice(Invoice invoice);

	@CacheEvict(value = {"report", "invoice"}, allEntries = true)
	void updateInvoice(Invoice invoice);

	@CacheEvict(value = {"report", "invoice"}, allEntries = true)
	void deleteInvoiceById(long id);

	@Cacheable("report")
	Page<Invoice> findAllInvoice(Customer idcustomer, String contractnumber, Pageable pageable);	
	@Cacheable("report")
	Page<Invoice> findByIdCustomer(Customer idcustomer, Pageable pageable);

	@Cacheable("report")
	List<Invoice> getListReport(Customer idCus, Date dateStart, Date dateEnd);
	
	@Cacheable("report")
	List<Invoice> getExpensesReport(Customer idCus, Date dateStart, Date dateEnd, TypeInvoice type);
	
	@Cacheable("invoice")
	Invoice getInvoice(String contractNum);
	
	@Cacheable("invoice")
	Invoice findByContractNumberAndIdCustomer(String contractNum, Customer customer);

}