package com.example.farmmanagerhelper.models;

// create object of times containing time in half hour increments with name, description, start time end time, status, colour

public class TimeSlot {

    private String timeSlotTime;
    private String timeSlotName;
    private String timeSlotDescription;
    private String timeSlotStartTime;
    private String timeSlotEndTime;
    private boolean timeSlotCompleteStatus;
    private String timeSlotColor;

    public TimeSlot(String timeSlotTime, String timeSlotName, String timeSlotDescription,
                    String timeSlotStartTime, String timeSlotEndTime,
                    boolean timeSlotCompleteStatus, String timeSltColor) {
        this.timeSlotTime = timeSlotTime;
        this.timeSlotName = timeSlotName;
        this.timeSlotDescription = timeSlotDescription;
        this.timeSlotStartTime = timeSlotStartTime;
        this.timeSlotEndTime = timeSlotEndTime;
        this.timeSlotCompleteStatus = timeSlotCompleteStatus;
        this.timeSlotColor = timeSltColor;
    }

    public String getTimeSlotTime() {
        return timeSlotTime;
    }

    public void setTimeSlotTime(String timeSlotTime) {
        this.timeSlotTime = timeSlotTime;
    }

    public String getTimeSlotName() {
        return timeSlotName;
    }

    public void setTimeSlotName(String timeSlotName) {
        this.timeSlotName = timeSlotName;
    }

    public String getTimeSlotDescription() {
        return timeSlotDescription;
    }

    public void setTimeSlotDescription(String timeSlotDescription) {
        this.timeSlotDescription = timeSlotDescription;
    }

    public String getTimeSlotStartTime() {
        return timeSlotStartTime;
    }

    public void setTimeSlotStartTime(String timeSlotStartTime) {
        this.timeSlotStartTime = timeSlotStartTime;
    }

    public String getTimeSlotEndTime() {
        return timeSlotEndTime;
    }

    public void setTimeSlotEndTime(String timeSlotEndTime) {
        this.timeSlotEndTime = timeSlotEndTime;
    }

    public boolean isTimeSlotCompleteStatus() {
        return timeSlotCompleteStatus;
    }

    public void setTimeSlotCompleteStatus(boolean timeSlotCompleteStatus) {
        this.timeSlotCompleteStatus = timeSlotCompleteStatus;
    }

    public String getTimeSlotColor() {
        return timeSlotColor;
    }

    public void setTimeSlotColor(String timeSlotColor) {
        this.timeSlotColor = timeSlotColor;
    }
}
