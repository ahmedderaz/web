package com.afaqy.avl.webnotifier.model;


import com.google.gson.annotations.SerializedName;

/**
 * Name : WebSocketsEvent
 * Description :
 * Date : 7/24/2017
 * Create by : Mahmoud Kelany
 * E-Mail : kelany@afaqy.com
 */
public class WebSocketsEvent {
    @SerializedName("ei")
    private String eventId;
    @SerializedName("edi")
    private String eventDataId;
    @SerializedName("et")
    private String eventType;
    @SerializedName("ed")
    private String eventDesc;
    private String imei;
    @SerializedName("dts")
    private long dtServer;
    @SerializedName("dtt")
    private long dtTracker;
    @SerializedName("s")
    private double speed;
    @SerializedName("a")
    private double angle;
    @SerializedName("lt")
    private double lat;
    @SerializedName("ln")
    private double lng;
    @SerializedName("al")
    private double altitude;
    @SerializedName("eui")
    private String eventUserId;
    @SerializedName("uui")
    private String unitId;
    @SerializedName("ns")
    private EventNotify eventNotifySystem;
    @SerializedName("ss")
    private String systemSound;

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public String getEventUserId() {
        return eventUserId;
    }

    public void setEventUserId(String eventUserId) {
        this.eventUserId = eventUserId;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getEventDesc() {
        return eventDesc;
    }

    public void setEventDesc(String eventDesc) {
        this.eventDesc = eventDesc;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public long getDtServer() {
        return dtServer;
    }

    public void setDtServer(long dtServer) {
        this.dtServer = dtServer;
    }

    public long getDtTracker() {
        return dtTracker;
    }

    public void setDtTracker(long dtTracker) {
        this.dtTracker = dtTracker;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getEventDataId() {
        return eventDataId;
    }

    public void setEventDataId(String eventDataId) {
        this.eventDataId = eventDataId;
    }

    public double getAltitude() {
        return altitude;
    }

    public void setAltitude(double altitude) {
        this.altitude = altitude;
    }

    public String getSystemSound() {
        return systemSound;
    }

    public void setSystemSound(String systemSound) {
        this.systemSound = systemSound;
    }

    public EventNotify getEventNotifySystem() {
        return eventNotifySystem;
    }

    public void setEventNotifySystem(EventNotify eventNotifySystem) {
        this.eventNotifySystem = eventNotifySystem;
    }
}
