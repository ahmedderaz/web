package com.afaqy.avl.webnotifier.model;

import java.util.Set;

/**
 * Name : DeviceConnectionStatus
 * <br>
 * Description : Describe the device status, used to be sent to web
 * <br>
 * Date : 05/12/2018
 * <br>
 * Create by : Mohamed Elkady
 * <br>
 * Mail : elkady@afaqy.com
 */
public class DeviceConnectionStatus {
    private String unitId;
    private long date;
    private String status;
    private String imei;
    private Set<String> users; // associated user

    public DeviceConnectionStatus(String unitId, long date, String status, String imei, Set<String> users) {
        this.unitId = unitId;
        this.date = date;
        this.status = status;
        this.imei = imei;
        this.users = users;
    }

    public DeviceConnectionStatus(){

    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public Set<String> getUsers() {
        return users;
    }

    public void setUsers(Set<String> users) {
        this.users = users;
    }
}
