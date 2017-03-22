package csc.controllers;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import csc.service.ParameterService;

@Component
public class ScheduledTasks {
	private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);

	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
	
	private static String CRON ="0 10 23 22 * ?";
	@Autowired
	ParameterService parameterService;
	

	@Scheduled(fixedRate = 5000)
	public void reportCurrentTime() {
		log.info("The time is now {}", dateFormat.format(new Date()));
		log.info("The cron is {}", CRON);
	}
	
	@Scheduled(cron = "0 10 23 22 * ?") //"0 10 23 22 * ?"
	public void reportByDate() {
		log.info("The time is now {} 0 59 10 22 * ?");
	}
	
//	public ScheduledTasks() {
//		Parameter para = parameterService.findById(1);
//		
//		Date d = para.getTimeEmail();
//		
//		int day = d.getDay();
//		
//		int minute = d.getMinutes();
//		
//		int hour = d.getHours();
//		
//		CRON = "0 " + minute + " " + hour + " " + day + " * ?" ;
//		
//		log.info("The cron {}", CRON);
//	}
}
