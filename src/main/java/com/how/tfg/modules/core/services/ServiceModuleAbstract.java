package com.how.tfg.modules.core.services;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.stereotype.Service;

import com.how.tfg.modules.core.domain.MeasureModuleAbstract;

@Service
public abstract class ServiceModuleAbstract<T,P extends MeasureModuleAbstract> {
	
	protected ConnectionRepository connectionRepository;
	protected MongoRepository<P, String> repository;
	protected String code;
	protected UsersConnectionRepository userConnectionRepository;
	
	
	public ServiceModuleAbstract(ConnectionRepository connectionRepository,
			MongoRepository<P, String> repository, UsersConnectionRepository userConnectionRepository, String code) {
		super();
		this.connectionRepository = connectionRepository;
		this.repository = repository;
		this.userConnectionRepository = userConnectionRepository;
		this.code = code;
	}

	public String getCode() {
		return code;
	}
	
	public void setCode(String code) {
		this.code = code;
	}
	
	@SuppressWarnings("unchecked")
	public T getApi() {
		return ((Connection<T>) connectionRepository.findConnections(code).get(0)).getApi();
	}
	
	
    public void deleteMeasureOfId(String measureId) {
    	repository.delete(measureId);
    }
    
	public boolean haveConnection() {
		return !connectionRepository.findConnections(code).isEmpty(); 
	}
	
    public P getMeasureById(String measureId){
    	return repository.findOne(measureId);
    }
    
    public void refreshAllMeasure() {
    	List<P> allMeasure = repository.findAll();
    	for (P measure : allMeasure) {
    		refreshMeasureOffline(measure);
    	}
    }
    
    public T getApiOfMeasureUser(P measure) {
    	return extracted(measure).getApi();
    }

	@SuppressWarnings("unchecked")
	private Connection<T> extracted(P measure) {
		return (Connection<T>) userConnectionRepository.createConnectionRepository(measure.getEmail()).findConnections(code).get(0);
	}
	
	public abstract void refreshMeasureOffline(P measure);

	public abstract List<P> getAllMeasure();
	
	public abstract void refreshMeasure(String measureId);
	
	public abstract void refreshMeasure(P measure);
	
}
