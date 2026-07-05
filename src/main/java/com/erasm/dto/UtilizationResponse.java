package com.erasm.dto;

public class UtilizationResponse {

    private Integer employeeId;
    private String employeeName;
    private Integer billablePercentage;
    private Integer benchPercentage;

    public UtilizationResponse(Integer employeeId, String employeeName,
            Integer billablePercentage, Integer benchPercentage) {
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.billablePercentage = billablePercentage;
        this.benchPercentage = benchPercentage;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public Integer getBillablePercentage() {
        return billablePercentage;
    }

    public void setBillablePercentage(Integer billablePercentage) {
        this.billablePercentage = billablePercentage;
    }

    public Integer getBenchPercentage() {
        return benchPercentage;
    }

    public void setBenchPercentage(Integer benchPercentage) {
        this.benchPercentage = benchPercentage;
    }
}