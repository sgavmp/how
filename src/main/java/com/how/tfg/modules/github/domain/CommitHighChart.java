package com.how.tfg.modules.github.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;

public class CommitHighChart {
	
	private String name;
	
	private String color;
	
	private static String[] colors = {"#2f7ed8", "#0d233a", "#8bbc21", "#910000", "#1aadce", 
	         "#492970", "#f28f43", "#77a1e5", "#c42525", "#a6c96a"};
	
	private static Integer aColor = 0;
	
	private static Integer maxColor = 10;
	
	private List<List<String>> data;

	public CommitHighChart() {
		super();
	}
	
	public CommitHighChart(Map<Long,Integer> commitPerDay) {
		this.name = "Commits";
		data = new ArrayList<>();
		for (Long day : commitPerDay.keySet()) {
			List<String> temp = new ArrayList<String>();
			DateTime date = new DateTime(day);
			temp.add(date.getYear()+","+(date.getMonthOfYear()-1)+","+date.getDayOfMonth());
			temp.add(commitPerDay.get(day).toString());
			data.add(temp);
		}
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
