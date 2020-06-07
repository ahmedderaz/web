package com.afaqy.avl.webnotifier.model;


import com.afaqy.avl.core.model.vo.PositionVO;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class WebSocketMessage {
    @SerializedName("id")
    private String uid;
    @SerializedName("cv")
    private int connectionValid;
    @SerializedName("lv")
    private int locationValid;
    @SerializedName("o")
    private double odemeter;
    @SerializedName("eh")
    private double accTimeOn;
    @SerializedName("d")
    private List<Object> positionData;

    public WebSocketMessage(){
    }

    public WebSocketMessage(PositionVO positionVO){
        setUid(positionVO.getId());
        setLocationValid(positionVO.getValid() ? 1 : 0);
        setConnectionValid(calcConnectionValidity(positionVO));
        setAccTimeOn(positionVO.getAccTimeOn());
        setOdemeter(positionVO.getOdometer());

        List<Object> dtElements = new ArrayList<>();
        dtElements.add(positionVO.getServerDate().getMillis());
        dtElements.add(positionVO.getTrackerDate().getMillis());
        dtElements.add(positionVO.getLatitude());
        dtElements.add(positionVO.getLongitude());
        dtElements.add(positionVO.getAltitude());
        dtElements.add(positionVO.getAngle());
        dtElements.add(positionVO.getSpeed());
        dtElements.add(positionVO.getParams());
        dtElements.add(positionVO.getAcc());
        dtElements.add(positionVO.getAccChangeTime().getMillis());
        dtElements.add(positionVO.getParamsWithDate());

        setPositionData(dtElements);
    }

    public int getConnectionValid() {
        return connectionValid;
    }

    public void setConnectionValid(int connectionValid) {
        this.connectionValid = connectionValid;
    }

    public int getLocationValid() {
        return locationValid;
    }

    public void setLocationValid(int locationValid) {
        this.locationValid = locationValid;
    }

    public double getOdemeter() {
        return odemeter;
    }

    public void setOdemeter(double odemeter) {
        this.odemeter = odemeter;
    }

    public double getAccTimeOn() {
        return accTimeOn;
    }

    public void setAccTimeOn(double accTimeOn) {
        this.accTimeOn = accTimeOn;
    }

    public List<Object> getPositionData() {
        return positionData;
    }

    public void setPositionData(List<Object> positionData) {
        this.positionData = positionData;
    }

    public int calcConnectionValidity(PositionVO positionVO) {
        // Test
        long minutes = ((new Date().getTime() - positionVO.getServerDate().getMillis()) / 1000) / 60;
        return minutes <= 5 ? 1 : 0;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    @Override
    public String toString() {
        return "WebSocketsMessage{" +
                "connectionValid=" + connectionValid +
                ", locationValid=" + locationValid +
                ", odemeter=" + odemeter +
                ", accTimeOn=" + accTimeOn +
                ", positionData=" + positionData +
                '}';
    }
}
