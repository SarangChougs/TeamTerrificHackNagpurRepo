package net.tafri.trackmylocation;

public class Request {

    String Username, UserId, UserMobile;
    String DriverName, DriverId, DriverMobileNo;
    String Status;

    public Request(String username, String userId, String userMobile, String driverName, String driverId, String driverMobileNo, String status) {
        Username = username;
        UserId = userId;
        UserMobile = userMobile;
        DriverName = driverName;
        DriverId = driverId;
        DriverMobileNo = driverMobileNo;
        Status = status;
    }

    public Request(){
        //empty constructor
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getUserMobile() {
        return UserMobile;
    }

    public void setUserMobile(String userMobile) {
        UserMobile = userMobile;
    }

    public String getDriverName() {
        return DriverName;
    }

    public void setDriverName(String driverName) {
        DriverName = driverName;
    }

    public String getDriverId() {
        return DriverId;
    }

    public void setDriverId(String driverId) {
        DriverId = driverId;
    }

    public String getDriverMobileNo() {
        return DriverMobileNo;
    }

    public void setDriverMobileNo(String driverMobileNo) {
        DriverMobileNo = driverMobileNo;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }
}
