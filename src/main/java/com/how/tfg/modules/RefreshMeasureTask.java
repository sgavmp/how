package com.how.tfg.modules;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.how.tfg.modules.core.services.ServiceModuleAbstract;

@EnableScheduling
public class RefreshMeasureTask {
	
	List<ServiceModuleAbstract<?,?>> allService;
	
	@Autowired
	public void setAllService(List<ServiceModuleAbstract<?, ?>> allService) {
		this.allService = allService;
	}
	
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Scheduled(cron="0 0 0/12 * * ?")
    public void reportCurrentTime() {
    	System.out.println("Start all refresh " + dateFormat.format(new Date()));
        for (ServiceModuleAbstract<?,?> service : allService) {
        	System.out.println("Start refresh of " + service.getCode() + ": " + dateFormat.format(new Date()));
        	service.refreshAllMeasure();
        	System.out.println("End refresh of " + service.getCode() + ": " + dateFormat.format(new Date()));
        }
        System.out.println("End all refresh " + dateFormat.format(new Date()));
    }

}
