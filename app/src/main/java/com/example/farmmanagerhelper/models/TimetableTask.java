package com.example.farmmanagerhelper.models;

public class TimetableTask {


    private String taskID;
    private String taskName;
    private String taskAssignedTo;
    private String taskDate;
    private String taskDescription;
    private String taskStartTime;
    private String taskEndTime;
    private boolean taskCompleteStatus;
    private String taskColor;

    public TimetableTask(String taskID, String taskName, String taskAssignedTo, String taskDate, String taskDescription, String taskStartTime, String taskEndTime, boolean taskCompleteStatus, String taskColor) {
        this.taskID = taskID;
        this.taskName = taskName;
        this.taskAssignedTo = taskAssignedTo;
        this.taskDate = taskDate;
        this.taskDescription = taskDescription;
        this.taskStartTime = taskStartTime;
        this.taskEndTime = taskEndTime;
        this.taskCompleteStatus = taskCompleteStatus;
        this.taskColor = taskColor;
    }

    // no argument constructor
    public TimetableTask()
    {

    }

    public String getTaskID() {
        return taskID;
    }

    public void setTaskID(String taskID) {
        this.taskID = taskID;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskDate() {
        return taskDate;
    }

    public void setTaskDate(String taskDate) {
        this.taskDate = taskDate;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }


    public String getTaskStartTime() {
        return taskStartTime;
    }

    public void setTaskStartTime(String taskStartTime) {
        this.taskStartTime = taskStartTime;
    }

    public String getTaskEndTime() {
        return taskEndTime;
    }

    public void setTaskEndTime(String taskEndTime) {
        this.taskEndTime = taskEndTime;
    }

    public boolean isTaskCompleteStatus() {
        return taskCompleteStatus;
    }

    public void setTaskCompleteStatus(boolean taskCompleteStatus) {
        this.taskCompleteStatus = taskCompleteStatus;
    }

    public String getTaskColor() {
        return taskColor;
    }

    public void setTaskColor(String taskColor) {
        this.taskColor = taskColor;
    }

    public String getTaskAssignedTo() {
        return taskAssignedTo;
    }

    public void setTaskAssignedTo(String taskAssignedTo) {
        this.taskAssignedTo = taskAssignedTo;
    }
}
