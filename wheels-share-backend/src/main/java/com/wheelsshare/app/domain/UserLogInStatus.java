package com.wheelsshare.app.domain;

public class UserLogInStatus {
    private Users user;
    private String status;

    public UserLogInStatus(Users user, String status) {
        this.user = user;
        this.status = status;
    }

    public UserLogInStatus(String status) {
        this.status = status;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
