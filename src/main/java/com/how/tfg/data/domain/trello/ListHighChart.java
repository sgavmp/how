package com.how.tfg.data.domain.trello;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.joda.time.DateTime;

public class ListHighChart {
	
	private String name;
	
	private String color;
	
	private static String[] colors = {"#2f7ed8", "#0d233a", "#8bbc21", "#910000", "#1aadce", 
	         "#492970", "#f28f43", "#77a1e5", "#c42525", "#a6c96a"};
	
	private static Integer aColor = 0;
	
	private static Integer maxColor = 10;
	
	private List<List<String>> data;

	public ListHighChart() {
		super();
	}
	
	public ListHighChart(String name, List<CardsOfDay> cards) {
		this.name = name;
		data = new ArrayList<>();
		for (CardsOfDay card : cards) {
			List<String> temp = new ArrayList<String>();
			temp.add(card.getTime().getYear()+","+(card.getTime().getMonthOfYear()-1)+","+card.getTime().getDayOfMonth()+","+card.getTime().getHourOfDay()+","+card.getTime().getMinuteOfHour());
			temp.add(card.getNum().toString());
			data.add(temp);
		}
		DateTime now = DateTime.now();
		Integer numNow = cards.get(0).getNum();
		List<String> temp = new ArrayList<String>();
		temp.add(now.getYear()+","+(now.getMonthOfYear()-1)+","+now.getDayOfMonth()+","+now.getHourOfDay()+","+now.getMinuteOfHour());
		temp.add(numNow.toString());
		Collections.reverse(data);
		data.add(temp);
		color = colors[aColor++];
		if (aColor==maxColor)
			aColor=0;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<List<String>> getData() {
		return data;
	}

	public void setData(List<List<String>> data) {
		this.data = data;
	}
	
	
}
