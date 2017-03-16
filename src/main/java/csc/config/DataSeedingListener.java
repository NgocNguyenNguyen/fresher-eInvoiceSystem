package csc.config;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import csc.models.Company;
import csc.models.Customer;
import csc.models.Invoice;
import csc.models.Role;
import csc.models.Service;
import csc.models.TypeInvoice;
import csc.models.Users;
import csc.repository.CompanyRepository;
import csc.repository.CustomerRepository;
import csc.repository.InvoiceRepository;
import csc.repository.RoleRepository;
import csc.repository.ServiceRepository;
import csc.repository.TypeInvoiceRepository;
import csc.repository.UserRepository;

@Component
public class DataSeedingListener implements ApplicationListener<ContextRefreshedEvent> {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private CompanyRepository companyRepository;

	@Autowired
	private TypeInvoiceRepository typeInvoiceRepository;

	@Autowired
	private InvoiceRepository invoiceRepository;

	@Autowired
	private ServiceRepository serviceRepository;

	@Override
	public void onApplicationEvent(ContextRefreshedEvent arg0) {
		// Roles
		if (roleRepository.findByName("ROLE_ADMIN") == null) {
			roleRepository.save(new Role("ROLE_ADMIN"));
		}

		if (roleRepository.findByName("ROLE_MEMBER") == null) {
			roleRepository.save(new Role("ROLE_MEMBER"));
		}
		Users tmp;
		// Admin account
		if (userRepository.findByUsername("admin02@gmail.com") == null) {
			tmp = new Users();
			Users admin = new Users();
			admin.setUsername("admin02@gmail.com");
			admin.setPassword(passwordEncoder.encode("123456"));
			admin.setActive("1");
			HashSet<Role> roles = new HashSet<>();
			roles.add(roleRepository.findByName("ROLE_ADMIN"));
			admin.setRoles(roles);
			tmp = userRepository.save(admin);
			createCustomer(tmp, "admin02@gmail.com");

		}

		// Admin account
		if (userRepository.findByUsername("admin01@gmail.com") == null) {
			tmp = new Users();
			Users admin = new Users();
			admin.setUsername("admin01@gmail.com");
			admin.setPassword(passwordEncoder.encode("123456"));
			admin.setActive("1");
			HashSet<Role> roles = new HashSet<>();
			roles.add(roleRepository.findByName("ROLE_ADMIN"));
			admin.setRoles(roles);
			tmp = userRepository.save(admin);
			createCustomer(tmp, "admin01@gmail.com");

		}

		// Member account
		if (userRepository.findByUsername("member@gmail.com") == null) {
			tmp = new Users();
			Users user = new Users();
			user.setUsername("member@gmail.com");
			user.setPassword(passwordEncoder.encode("123456"));
			user.setActive("1");
			HashSet<Role> roles = new HashSet<>();
			roles.add(roleRepository.findByName("ROLE_MEMBER"));
			user.setRoles(roles);
			tmp = new Users();
			tmp = userRepository.save(user);
			createCustomer(tmp, "member@gmail.com");
		}
		// Test create 100 record for function paging with role admin
		// for (int index = 0; index < 20; index++) {
		// if (userRepository.findByUsername("member" + index + "@gmail.com") ==
		// null) {
		// Users user = new Users();
		// user.setUsername("member" + index + "@gmail.com");
		// user.setPassword(passwordEncoder.encode("123456"));
		// user.setActive("1");
		// HashSet<Role> roles = new HashSet<>();
		// roles.add(roleRepository.findByName("ROLE_MEMBER"));
		// user.setRoles(roles);
		// tmp = new Users();
		// tmp = userRepository.save(user);
		// createCustomer(tmp);
		// }
		// }
		// End Test


		// Create TypeInvoice record
		createTypeInvoice();
		
		// Create Company record
		// name - idCpn - nameInvoice
		createCpnRecord("Thang Long", "1230000", "Electric Bill");
		createCpnRecord("Sai Gon TNHH MTV", "3210000", "Water Bill");
		createCpnRecord("Gia Dinh", "6660000", "Water Bill");
		createCpnRecord("Thang Long 2", "4440000", "Electric Bill");
		createCpnRecord("Viettel", "1110000", "Internet Bill");
		createCpnRecord("VPN", "1120000", "Internet Bill");
		createCpnRecord("Mobi", "1130000", "Phone Bill");
		createCpnRecord("Vina", "1140000", "Phone Bill");
		
		// Create Service
		// name - idType - count - unit
		createService("G", 1, 2, 1000);
		createService("G", 2, 2, 1000);
		createService("G", 3, 5, 5000);
		createService("G", 4, 5, 1000);

		// Create Invoice record
		// contractNumber - idCompany - idCustomer - idType - totalRecord
		createInvoiceRecord("AC123457", 1, 1L, 1, 10);
		createInvoiceRecord("IC123458", 1, 1L, 2, 10);
		createInvoiceRecord("UC123458", 1, 1L, 3, 10);
		createInvoiceRecord("KC123458", 1, 1L, 4, 10);


		createInvoiceRecord("KC121450", 1, 3L, 1, 10);
		createInvoiceRecord("KC122450", 1, 3L, 2, 10);
		createInvoiceRecord("KC123450", 1, 3L, 3, 10);
		createInvoiceRecord("KC124450", 1, 3L, 4, 10);


		createInvoiceRecord("UC123457", 1, 2L, 1, 10);



	}

	private void createService(String name, int idType, int count, int unit) {
		Service s;
		TypeInvoice ti;
		for (int index = 1; index <= count; index++) {
			System.out.println("name + index" + (name + index));
			ti = new TypeInvoice();
			ti = typeInvoiceRepository.findById(idType);
			if (serviceRepository.findByNameServiceAndIdType(name + index, ti) == null) {
				s = new Service();
				s.setIdType(ti);
				s.setNameService(name + index);
				s.setUnit(BigDecimal.valueOf(unit * index));
				System.out.println("createService=" + ti);
				serviceRepository.save(s);
			}
		}
	}

	private void createCustomer(Users user, String email) {
		Customer cus = new Customer();
		cus.setUser(user);
		cus.setAddress("test");
		cus.setEmail(email);
		cus.setNameCustomer("test");
		cus.setIdCustomer("CUS201703" + user.getId());
		cus.setPhone(12345671);
		cus.setTaxCode(Integer.parseInt("123457"));
		cus.setLimitConsume(BigDecimal.valueOf(1234));
		customerRepository.save(cus);
	}

	private void createTypeInvoice() {
		TypeInvoice ti = new TypeInvoice();
		// Company cpn = null;
		// int unit;
		if (typeInvoiceRepository.findByNameInvoice("Electric Bill") == null) {
			ti = new TypeInvoice();
			// unit = 3000;
			ti.setCode("EB");
			ti.setNameInvoice("Electric Bill");
			ti.setDescription("Electric Bill");
			ti.setVat(Float.parseFloat("10"));
			typeInvoiceRepository.save(ti);
		}
		if (typeInvoiceRepository.findByNameInvoice("Water Bill") == null) {
			ti = new TypeInvoice();
			// unit = 2500;

			ti.setCode("WB");
			ti.setNameInvoice("Water Bill");
			ti.setDescription("Water Bill");
			ti.setVat(Float.parseFloat("8"));
			typeInvoiceRepository.save(ti);
		}
		if (typeInvoiceRepository.findByNameInvoice("Internet Bill") == null) {
			ti = new TypeInvoice();
			// unit = 150000;

			ti.setCode("IB");
			ti.setNameInvoice("Internet Bill");
			ti.setDescription("Internet Bill");
			ti.setVat(Float.parseFloat("5"));
			typeInvoiceRepository.save(ti);
		}
		if (typeInvoiceRepository.findByNameInvoice("Phone Bill") == null) {
			ti = new TypeInvoice();
			// unit = 1500;

			ti.setCode("PB");
			ti.setNameInvoice("Phone Bill");
			ti.setDescription("Phone Bill");
			ti.setVat(Float.parseFloat("5"));
			typeInvoiceRepository.save(ti);
		}
	}

	private void createCpnRecord(String name, String idCpn, String nameInvoice) {
		TypeInvoice ti;
		System.out.println(companyRepository.findByNameCpn(name));
		if (companyRepository.findByNameCpn(name) == null) {
			Company cpn = new Company();
			cpn.setAddress("123 Dong Khoi");
			cpn.setBankAccount("123456789");
			cpn.setFax(Integer.parseInt("085623149"));
			cpn.setNameCpn(name);
			cpn.setPhoneCpn(Integer.parseInt("0123456852"));
			cpn.setTaxCode(Integer.parseInt("105878523"));
			cpn.setIdCpn(idCpn);
			// set idType
			ti = new TypeInvoice();
			ti = typeInvoiceRepository.findByNameInvoice(nameInvoice);
			cpn.setIdType(ti);

			companyRepository.save(cpn);
		}

	}

	private void createInvoiceRecord(String contractNumber, int idCompany, Long idCustomer, int idType,
			int totalRecord) {
		try {
			int day = 10;
			int month = 0;
			int year = 2016;
			System.out.println("contractNumber:" + contractNumber);
			Invoice invoice;
			// TypeInvoice ti;
			// DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
			// LocalDateTime now = LocalDateTime.now();
			Calendar calendar = Calendar.getInstance();
			calendar.clear();
			// calendar.set(Calendar.MONTH, month);
			calendar.set(Calendar.DAY_OF_MONTH, day);
			calendar.set(Calendar.YEAR, year);
			// Date date = new Date();
			for (int index = 0; index < totalRecord; index++) {
				// ti = new TypeInvoice();
				invoice = new Invoice();
				Float vat;
				Float indexConsumed = 100F;
				BigDecimal total = new BigDecimal(indexConsumed * (index + 1));
				BigDecimal ptef = new BigDecimal(index);
				BigDecimal grandTotal;
				invoice.setContractNumber(contractNumber + index);

				if (invoiceRepository.findByContractNumber(invoice.getContractNumber()) == null) {
					calendar.set(Calendar.MONTH, month + index);
					Date date = calendar.getTime();
					invoice.setDate(date);
					invoice.setIndexConsumed(indexConsumed);
					invoice.setNameService("G20");
					invoice.setPtef(ptef);
					invoice.setTotal(total);

					// set Type Invoice
					TypeInvoice typeInvoice = new TypeInvoice();
					typeInvoice = typeInvoiceRepository.findById(idType);
					vat = typeInvoice.getVat();
					invoice.setIdType(typeInvoice);
					invoice.setVat(vat);
					grandTotal = total.add(total.multiply(BigDecimal.valueOf(vat)).divide(BigDecimal.valueOf(100)));
					invoice.setGrandTotal(grandTotal);

					// set Id Customer
					Customer tmpCus = new Customer();
					tmpCus = customerRepository.findById(idCustomer);
					invoice.setIdCustomer(tmpCus);

					invoiceRepository.save(invoice);
				} else {
					System.out.println(invoice.getContractNumber() + "is existed!!!!");
				}

			}
		} catch (Exception ex) {
			System.out.println("Exception:" + ex);
		}
	}
}
