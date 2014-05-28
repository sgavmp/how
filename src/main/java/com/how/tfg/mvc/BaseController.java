package com.how.tfg.mvc;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.how.tfg.data.domain.UserDetails;

@Controller
public abstract class BaseController implements Comparable<BaseController> {
	
	private ConnectionRepository repository;
	
	protected String nameApp;
	protected String code;
	protected String description;
	protected String image;
	protected String url;
	private List<BaseController> allApps;
	
	@ModelAttribute("apps")
	public List<BaseController> getAllApps() {
		return allApps;
	}
	
	@ModelAttribute("appCode")
	public String getappCode() {
		return code;
	}
	
	public ConnectionRepository getRepository() {
		return repository;
	}

	@Autowired
	public void setRepository(ConnectionRepository repository) {
		this.repository = repository;
	}
	
	public boolean userIsConnect() {
		return repository.findConnections(code).size()>0;
	}

	@Autowired(required=false)
	public void setAllApps(List<BaseController> allApps) {
		this.allApps = allApps;
		this.allApps.add(this);
		Collections.sort(this.allApps);
	}

	public String getNameApp() {
		return nameApp;
	}

	public void setNameApp(String nameApp) {
		this.nameApp = nameApp;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@ModelAttribute("principal")
    public UserDetails getUser() {
    	return (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
	
	public int compareTo(BaseController o) {
		return this.code.compareTo(o.getCode());
	}
}
