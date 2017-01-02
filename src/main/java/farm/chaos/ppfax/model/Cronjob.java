package farm.chaos.ppfax.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity
public class Cronjob {

	@Id 	private Long id;
	@Index	private String minute;
	@Index	private String hour;
			private String dayOfMonth;
			private String month;
			private String year;
			private String dayOfWeek;
			private CronjobStatus status;

	@Index	private String appId;
	@Index	private String vendorId;

			private String command;
			private Date lastRun;
			private Date nextRun;
			private int maxRunMinutes;

			private String notification;
			private Date expectResult;

	public Cronjob() {
		minute = "*";
		hour = "*";
		dayOfMonth = "*";
		month = "*";
		year = "*";
		dayOfWeek = "*";
		status = CronjobStatus.DISABLED;
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getMinute() {
		return minute;
	}
	public void setMinute(String minute) {
		this.minute = minute;
	}
	public String getHour() {
		return hour;
	}
	public void setHour(String hour) {
		this.hour = hour;
	}
	public String getDayOfMonth() {
		return dayOfMonth;
	}
	public void setDayOfMonth(String dayOfMonth) {
		this.dayOfMonth = dayOfMonth;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getDayOfWeek() {
		return dayOfWeek;
	}
	public void setDayOfWeek(String dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}
	public CronjobStatus getStatus() {
		return status;
	}
	public void setStatus(CronjobStatus status) {
		this.status = status;
	}
	@JsonIgnore
	public String getAppId() {
		return appId;
	}
	@JsonIgnore
	public void setAppId(String appId) {
		this.appId = appId;
	}
	@JsonIgnore
	public String getVendorId() {
		return vendorId;
	}
	@JsonIgnore
	public void setVendorId(String vendorId) {
		this.vendorId = vendorId;
	}
	public String getCommand() {
		return command;
	}
	public void setCommand(String command) {
		this.command = command;
	}
	@JsonIgnore
	public Date getLastRun() {
		return lastRun;
	}
	@JsonIgnore
	public void setLastRun(Date lastRun) {
		this.lastRun = lastRun;
	}
	@JsonIgnore
	public Date getNextRun() {
		return nextRun;
	}
	@JsonIgnore
	public void setNextRun(Date nextRun) {
		this.nextRun = nextRun;
	}
	public int getMaxRunMinutes() {
		return maxRunMinutes;
	}
	public void setMaxRunMinutes(int maxRunMinutes) {
		this.maxRunMinutes = maxRunMinutes;
	}
	public String getNotification() {
		return notification;
	}
	public void setNotification(String notification) {
		this.notification = notification;
	}
	@JsonIgnore
	public Date getExpectResult() {
		return expectResult;
	}
	@JsonIgnore
	public void setExpectResult(Date expectResult) {
		this.expectResult = expectResult;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Cronjob [id=");
		builder.append(id);
		builder.append(", schedule=\"");
		builder.append(minute);
		builder.append(" ");
		builder.append(hour);
		builder.append(" ");
		builder.append(dayOfMonth);
		builder.append(" ");
		builder.append(month);
		builder.append(" ");
		builder.append(year);
		builder.append(" ");
		builder.append(dayOfWeek);
		builder.append("\", status=");
		builder.append(status);
		builder.append("\", appId=");
		builder.append(appId);
		builder.append("\", vendorId=");
		builder.append(vendorId);
		builder.append(", command=");
		builder.append(command);
		builder.append(", lastRun=");
		builder.append(lastRun);
		builder.append(", nextRun=");
		builder.append(nextRun);
		builder.append(", maxRunMinutes=");
		builder.append(maxRunMinutes);
		builder.append(", notification=");
		builder.append(notification);
		builder.append(", expectResult=");
		builder.append(expectResult);
		builder.append("]");
		return builder.toString();
	}

}
