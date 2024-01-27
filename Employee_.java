
class Employee_ {
    private String positionID;
    private String timeInDay;
    private String timeOutDay;
    private String employeeName;
    private String fileNumber;
    private String timeHours;

    public Employee_(String positionID, String timeInDay, String timeOutDay, String employeeName, String fileNumber, String timeHours) {
        this.positionID = positionID;
        this.timeInDay = timeInDay;
        this.timeOutDay = timeOutDay;
        this.employeeName = employeeName;
        this.fileNumber = fileNumber;
        this.timeHours = timeHours;
    }

    public String getPositionID() {
        return positionID;
    }

    public String getTimeHours() {
        return timeHours;
    }

    public String getTimeInDay() {
        return timeInDay;
    }

    public String getTimeOutDay() {
        return timeOutDay;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public String getFileNumber() {
        return fileNumber;
    }
}

