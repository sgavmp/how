package com.how.tfg.data.domain.trello;

import org.joda.time.DateTime;

public class CardsOfDay {
	private DateTime time;
	private Integer num;
	
	public CardsOfDay() {
		super();
	}
	
	public CardsOfDay(DateTime time, Integer num) {
		super();
		this.time = time;
		this.num = num;
	}

	public DateTime getTime() {
		return time;
	}
	public void setTime(DateTime time) {
		this.time = time;
	}
	public Integer getNum() {
		return num;
	}
	public void setNum(Integer num) {
		this.num = num;
	}
	
	
}
