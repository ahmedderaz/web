package com.afaqy.avl.webnotifier.model;

import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Property;

import java.util.List;

/**
 * Name : EventNotify
 * <br>
 * Description :
 * <br>
 * Date : 08/07/2018
 * <br>
 * Create by : Mohamed Elkady
 * <br>
 * Mail : elkady@afaqy.com
 */
@Embedded
public class EventNotify {
    @Property("email")
    private boolean notifyEmail;

    @Property
    private List<String> emails;

    @Property("sms")
    private boolean notifySms;

    @Property("sms_numbers")
    private List<String> smsNumbers;

    @Property("mobile")
    private boolean notifyMobile;

    @Property("system")
    private boolean notifySystem;

    @Property("system_sound")
    private String systemSound;

    public boolean isNotifyEmail() {
        return notifyEmail;
    }

    public void setNotifyEmail(boolean notifyEmail) {
        this.notifyEmail = notifyEmail;
    }

    public List<String> getEmails() {
        return emails;
    }

    public void setEmails(List<String> emails) {
        this.emails = emails;
    }

    public boolean isNotifySms() {
        return notifySms;
    }

    public void setNotifySms(boolean notifySms) {
        this.notifySms = notifySms;
    }

    public List<String> getSmsNumbers() {
        return smsNumbers;
    }

    public void setSmsNumbers(List<String> smsNumbers) {
        this.smsNumbers = smsNumbers;
    }

    public boolean isNotifyMobile() {
        return notifyMobile;
    }

    public void setNotifyMobile(boolean notifyMobile) {
        this.notifyMobile = notifyMobile;
    }

    public boolean isNotifySystem() {
        return notifySystem;
    }

    public void setNotifySystem(boolean notifySystem) {
        this.notifySystem = notifySystem;
    }

    public String getSystemSound() {
        return systemSound;
    }

    public void setSystemSound(String systemSound) {
        this.systemSound = systemSound;
    }

    @Override
    public String toString() {
        return "EventNotify{" +
                "notifyEmail=" + notifyEmail +
                ", emails=" + emails +
                ", notifySms=" + notifySms +
                ", smsNumbers=" + smsNumbers +
                ", notifyMobile=" + notifyMobile +
                ", notifySystem=" + notifySystem +
                '}';
    }
}
