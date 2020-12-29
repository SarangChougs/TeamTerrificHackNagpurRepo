package net.tafri.trackmylocation;

public class User {

    private String Name, Email, Uid, Address, MobileNo;

    public User(String name, String email, String uid, String address, String mobileNo) {
        Name = name;
        Email = email;
        Uid = uid;
        Address = address;
        MobileNo = mobileNo;
    }

    public User(){
        //empty constructor
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getMobileNo() {
        return MobileNo;
    }

    public void setMobileNo(String mobileNo) {
        MobileNo = mobileNo;
    }
}
