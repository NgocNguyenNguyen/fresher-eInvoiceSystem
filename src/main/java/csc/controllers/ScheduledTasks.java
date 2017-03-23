package csc.controllers;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import csc.config.SendMail;
import csc.models.Customer;
import csc.models.Parameter;
import csc.service.CustomerService;
import csc.service.InvoiceService;
import csc.service.ParameterService;

@Component
public class ScheduledTasks {
	private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	private static int DAY = 1;

	@Autowired
	ParameterService parameterService;

	@Autowired
	InvoiceService invoiceService;

	@Autowired
	CustomerService customerService;

	/**
	 * [Auto] Function call after 10s waiting initial 1s
	 */
	@Scheduled(fixedDelayString = "${invoice.delay.property}")
	public void reportCurrentTime() {
		// Parameter para = null;
		//
		// // get dateStart current Time
		// String dateStart = getCurrentTime(DAY);
		// // get dateEnd current Time
		// String dateEnd = getCurrentTime(30);
		//
		// // get Email of admin
		// para = this.getEmailFrom();
		//
		// calculation(dateStart, dateEnd);
		log.info("Time in server {}" + sdf.format(new Date()));
		//this.sendMailByTime();
	}

	/**
	 * [Auto] Function call when Date 22th in 23:10
	 */
	@Scheduled(cron = "${invoice.cron.property}")
	public void sendMailByTime() {
		// get dateStart current Time
		String dateStart = getCurrentTime(DAY);
		// get dateEnd current Time
		String dateEnd = getCurrentTime(30);

		calculation(dateStart, dateEnd);
	}

	/**
	 * [Private] Function calculation and equals number limit_consume
	 */
	private void calculation(String dateStart, String dateEnd) {
		List<String> to = new ArrayList<String>();
		Parameter para = null;

		// get Email of admin
		para = this.getEmailFrom();
		// get list customer
		List<Customer> lstCustomer = customerService.findAllUser();
		for (Customer cus : lstCustomer) {
			// get grandTotal of id customer
			log.info("dateStart= " + dateStart + " " + dateEnd);
			long grandTotal = invoiceService.sumGrandTotal(cus.getIdCustomer(), dateStart, dateEnd);
			if (grandTotal != 0) {
				// If limit consume small grand total in current month
				if (cus.getLimitConsume().compareTo(BigDecimal.valueOf((long) grandTotal)) <= 0) {
					log.info("grandTotal of " + cus.getIdCustomer() + "= " + grandTotal);
					// get email of customer
					String email = cus.getEmail();
					if (email != null) {
						to.add(email);
					}
				}
			}
		}
		// Check to have emailTo
		this.getEmailTo(to);
		try {
			sendMail(para, to);
		} catch (Exception ex) {
			log.error("SendMail " + ex.getMessage());
		}
	}

	/**
	 * [Private] Function calculation and equals number limit_consume
	 */
	private String getCurrentTime(int day) {
		SimpleDateFormat dateFormatReport = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.set(Calendar.DAY_OF_MONTH, day);
		calendar.set(Calendar.MONTH, new Date().getMonth());
		calendar.set(Calendar.YEAR, LocalDateTime.now().getYear());
		Date date = calendar.getTime();

		return dateFormatReport.format(date);
	}

	/**
	 * [Private] Function get Email from
	 */
	private Parameter getEmailFrom() {
		Parameter para = parameterService.findById(1);
		// Check account email
		log.info("username= " + para.getEmail() + "pwd= " + para.getPwdEmail());
		return para;
	}

	/**
	 * [Private] Function get Email to
	 */
	private List<String> getEmailTo(List<String> to) {
		log.info("getEmailTo= " + to.size());
		if (to.size() > 0) {
			return to;
		}
		return null;
	}

	/**
	 * [Private] Function do send Mail
	 */
	private void sendMail(Parameter para, List<String> to) {
		if (to != null) {
			SendMail.sendMailSMTP(para.getEmail(), para.getPwdEmail(), to);
		}
	}
}
