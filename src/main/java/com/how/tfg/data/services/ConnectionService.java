package com.how.tfg.data.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

@Service
public class ConnectionService {
	
	private ConnectionRepository connectionRepository;

	@Autowired
	public ConnectionService(ConnectionRepository connectionRepository) {
		this.connectionRepository = connectionRepository;
	}
	
	public boolean haveAnyConnection() {
		MultiValueMap<String, Connection<?>> conects = connectionRepository.findAllConnections();
		return (conects.containsKey("github") || conects.containsKey("trello"));
	}
	
	public String getFirstApp() {
		MultiValueMap<String, Connection<?>> conects = connectionRepository.findAllConnections();
		if (conects.containsKey("facebook"))
			conects.remove("facebook");
		if (conects.containsKey("google"))
			conects.remove("google");
		List<String> apps = new ArrayList<String>(conects.keySet());
		Collections.sort(apps);
		return apps.get(0);
	}
}
