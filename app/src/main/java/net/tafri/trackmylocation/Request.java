package net.tafri.trackmylocation;

public class Request {

    String Username, UserId, UserMobile;
    String Status;

    public Request(){
        //empty constructor
    }

    public Request(String username, String userId, String userMobile, String status) {
        Username = username;
        UserId = userId;
        UserMobile = userMobile;
        Status = status;
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

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }
}
