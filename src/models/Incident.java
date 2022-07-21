package models;

import java.time.LocalDate;
import java.util.Comparator;

public class Incident implements Comparable<Incident>{
	private BusStop busStopDisabled;
	private LocalDate beginDate,endDate;
	private String description;
	private Boolean concluded;
	
	public Integer getBusStopDisabledNumber() {
		return getBusStopDisabled().getStopNumber();
	}
	public BusStop getBusStopDisabled() {
		return busStopDisabled;
	}
	public Incident(BusStop busStopDisabled, LocalDate beginDate, LocalDate endDate, String description,
			Boolean concluded) {
		this.busStopDisabled = busStopDisabled;
		this.beginDate = beginDate;
		this.endDate = endDate;
		this.description = description;
		this.concluded = concluded;
	}
	@Override
	public int compareTo(Incident o) {
		LocalDate b1 = this.getBeginDate(),b2 = o.getBeginDate();
		LocalDate e1 = this.getEndDate(),e2 = o.getEndDate();		
		if(e1==null && e2==null) {
			if(b1.isBefore(b2))
				return -1;
			else if(b1.isEqual(b2))
				return 0;
			else
				return 1;
		}
		else if(e1==null && e2!=null) {
			return 1;
		}
		else if(e1!=null && e2==null) {
			return -1;
		}
		else {
			if(e1.isBefore(e2))
				return -1;
			else if(e1.isEqual(e2))
				return 0;
			else
				return 1;
		}
	}
	@Override
	public String toString() {
		return "Incident [busStopDisabled=" + busStopDisabled + ", beginDate=" + beginDate + ", endDate=" + endDate
				+ ", description=" + description + ", concluded=" + concluded + "]";
	}
	
	public void setBusStopDisabled(BusStop busStopDisabled) {
		this.busStopDisabled = busStopDisabled;
	}
	public LocalDate getBeginDate() {
		return beginDate;
	}
	public void setBeginDate(LocalDate beginDate) {
		this.beginDate = beginDate;
	}
	public LocalDate getEndDate() {
		return endDate;
	}
	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Boolean getConcluded() {
		return concluded;
	}
	public void setConcluded(Boolean concluded) {
		this.concluded = concluded;
	}
}
