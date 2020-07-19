package com.a1611.riya.wifidirectmessaging;

public class ClientData {

    private String name;
    private String ip;
    private String status;

    public ClientData() {
    }

    public ClientData(String name, String ip, String status) {
        this.name = name;
        this.ip = ip;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
