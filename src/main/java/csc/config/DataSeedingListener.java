package csc.config;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.terracotta.statistics.jsr166e.ThreadLocalRandom;

import csc.models.Company;
import csc.models.Customer;
import csc.models.Invoice;
import csc.models.Parameter;
import csc.models.Role;
import csc.models.Service;
import csc.models.TypeInvoice;
import csc.models.Users;
import csc.repository.CompanyRepository;
import csc.repository.CustomerRepository;
import csc.repository.InvoiceRepository;
import csc.repository.ParameterRepository;
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
	
	@Autowired
	private ParameterRepository parameterRepository;

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
		 for (int index = 0; index < 20; index++) {
		 if (userRepository.findByUsername("member" + index + "@gmail.com") ==
		 null) {
		 Users user = new Users();
		 user.setUsername("member" + index + "@gmail.com");
		 user.setPassword(passwordEncoder.encode("123456"));
		 user.setActive("1");
		 HashSet<Role> roles = new HashSet<>();
		 roles.add(roleRepository.findByName("ROLE_MEMBER"));
		 user.setRoles(roles);
		 tmp = new Users();
		 tmp = userRepository.save(user);
		 createCustomer(tmp, tmp.getUsername());
		 }
		 }
		// End Test


		// Create TypeInvoice record
		createTypeInvoice("Electric Bill", "EB", 10F);
		createTypeInvoice("Water Bill", "WB", 8F);
		createTypeInvoice("Internet Bill", "IB", 5F);
		createTypeInvoice("Phone Bill", "PB", 5F);

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
		createService("G", 1, 2, 3000);
		createService("G", 2, 2, 2500);
		createService("G", 3, 5, 50000);
		createService("G", 4, 5, 20000);

		// Create Invoice record
		// contractNumber - idCompany - idCustomer - idType - totalRecord - year
		createInvoiceRecord("AC123457", 1, 1L, 1, 10, 2016);
		createInvoiceRecord("IC123458", 1, 1L, 2, 10, 2016);
		createInvoiceRecord("UC123458", 1, 1L, 3, 10, 2016);
		createInvoiceRecord("KC123458", 1, 1L, 4, 10, 2016);


		createInvoiceRecord("KC121450", 1, 3L, 1, 10, 2016);
		createInvoiceRecord("KC122550", 1, 3L, 2, 10, 2016);
		createInvoiceRecord("KC123650", 1, 3L, 3, 10, 2016);
		createInvoiceRecord("KC124750", 1, 3L, 4, 10, 2016);
		

		createInvoiceRecord("KC124250", 1, 3L, 1, 3, 2017);
		createInvoiceRecord("RC123250", 1, 3L, 2, 3, 2017);
		createInvoiceRecord("FC122250", 1, 3L, 3, 3, 2017);
		createInvoiceRecord("AC121250", 1, 3L, 4, 3, 2017);


		createInvoiceRecord("UC123457", 1, 2L, 1, 10, 2016);

		// Create parameter
		String dateString = "23/04/2017 23:11";		 
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");		 
		Date date = new Date();
		try {
			date = df.parse(dateString);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		createParameter(date, "nguyenvanantest123@gmail.com","Nguyenvanantest123!@#");		

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

	private void createTypeInvoice(String name, String code, float vat) {
		TypeInvoice ti = new TypeInvoice();
		// Company cpn = null;
		// int unit;
		if (typeInvoiceRepository.findByNameInvoice(name) == null) {
			ti = new TypeInvoice();
			// unit = 3000;
			ti.setCode(code);
			ti.setNameInvoice(name);
			ti.setDescription(name);
			ti.setVat(vat);
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
			int totalRecord, int year) {
		try {
			int day = 10;
			int month = 0;
			System.out.println("contractNumber:" + contractNumber);
			Invoice invoice;
			List<Company> cpn;
			List<Service> s;
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
				Float indexConsumed = 0F;
				BigDecimal ptef = null;
				BigDecimal grandTotal;
				invoice.setContractNumber(contractNumber + index);

				if (invoiceRepository.findByContractNumber(invoice.getContractNumber()) == null) {
					calendar.set(Calendar.MONTH, month + index);
					Date date = calendar.getTime();
					invoice.setDate(date);
					invoice.setIndexConsumed(indexConsumed);

					// set Type Invoice
					TypeInvoice typeInvoice = new TypeInvoice();
					typeInvoice = typeInvoiceRepository.findById(idType);
					vat = typeInvoice.getVat();
					invoice.setIdType(typeInvoice);

					//get name service
					s = new ArrayList<Service>();
					s = serviceRepository.findByIdType(typeInvoice);
					
					int randomNum = ThreadLocalRandom.current().nextInt(0, s.size());
					invoice.setNameService(s.get(randomNum).getNameService());

					//set index consumed
					if (typeInvoice.getCode().equals("IB") || typeInvoice.getCode().equals("PB")) {
						indexConsumed = 1F;
					}else {
						indexConsumed = 100F + index;
					}
					invoice.setIndexConsumed(indexConsumed);
					
					BigDecimal total = s.get(randomNum).getUnit().multiply(BigDecimal.valueOf(indexConsumed));
					invoice.setTotal(total);
					
					//set ptef
					if (typeInvoice.getCode().equals("WB")) {
						ptef = new BigDecimal(10);
					}else {
						ptef = new BigDecimal(0);
					}
					invoice.setPtef(ptef);
					
					invoice.setVat(vat);
					grandTotal = total.add(total.multiply(BigDecimal.valueOf(vat)).divide(BigDecimal.valueOf(100))).add((total.multiply(ptef).divide(BigDecimal.valueOf(100))));
					invoice.setGrandTotal(grandTotal);

					//set id_cpn
					cpn = new ArrayList<Company>();
					cpn = companyRepository.findByIdType(typeInvoice);
					if (cpn.size() <= randomNum) {
						invoice.setIdCpn(cpn.get(0).getIdCpn());
					} else {
						invoice.setIdCpn(cpn.get(randomNum).getIdCpn());
					}
					
					
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
	
	private void createParameter(Date timeEmail, String email, String pwdEmail) {
		Parameter pa = new Parameter();
		if(parameterRepository.findByEmail(email) == null){
			pa.setTimeEmail(timeEmail);
			pa.setEmail(email);
			pa.setPwdEmail(pwdEmail);
			parameterRepository.save(pa);
		}
	}
}
