/*
 * Copyright (c) 2019. Afaqy Company
 * Mohammed ElAdly (mohammed.eladly@afaqy.com)
 * All rights reserved.
 */

package com.afaqy.avl.webnotifier.model;

public class WebSocketBindEvent {

    private String unitId;
    private String id;
    private String rfid;
    private String ibtn;
    private String type; // bind_trailer or bind_driver
    private long date;

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRfid() {
        return rfid;
    }

    public void setRfid(String rfid) {
        this.rfid = rfid;
    }

    public String getIbtn() {
        return ibtn;
    }

    public void setIbtn(String ibtn) {
        this.ibtn = ibtn;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "WebSocketBindEvent{" +
                "unitId='" + unitId + '\'' +
                ", id='" + id + '\'' +
                ", rfid='" + rfid + '\'' +
                ", ibtn='" + ibtn + '\'' +
                ", type='" + type + '\'' +
                ", date=" + date +
                '}';
    }
}
