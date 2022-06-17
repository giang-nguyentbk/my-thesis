package com.example.myapp.datalist;

import java.io.Serializable;

public class Data implements Serializable {

    private String indicator;
    private float value;
    private String unit;
    private float LOWLOW;
    private float LOW;
    private float HIGH;
    private float HIGHHIGH;
    private float DEAD_BAND;
    private int severity;
    private int pre_severity;
    private long startTime;
    private long duration;
    private long pre_duration;
    private String content;
    private boolean ACK;
    private String ackUser;
    private long ACKTime;
    private int id;

    public Data(String indicator, float value, String unit, float LOWLOW, float LOW, float HIGH, float HIGHHIGH, float DEAD_BAND) {
        this.indicator = indicator;
        this.value = value;
        this.unit = unit;
        this.severity = this.pre_severity = 0;
        this.startTime = 0;
        this.duration = this.pre_duration = 0;
        this.content = "";
        this.ACK = false;
        this.ackUser = "";
        this.ACKTime = 0;
        this.LOWLOW = LOWLOW;
        this.LOW = LOW;
        this.HIGH = HIGH;
        this.HIGHHIGH = HIGHHIGH;
        this.DEAD_BAND = DEAD_BAND;
        this.id = 0;
    }

    public Data() {
        this.indicator = "";
        this.value = 0f;
        this.unit = "";
        this.severity = this.pre_severity = 0;
        this.startTime = 0;
        this.duration = this.pre_duration = 0;
        this.content = "";
        this.ACK = false;
        this.ackUser = "";
        this.ACKTime = 0;
        this.LOWLOW = 0f;
        this.LOW = 0f;
        this.HIGH = 0f;
        this.HIGHHIGH = 0f;
        this.DEAD_BAND = 0f;
        this.id = 0;
    }

    public String getIndicator() {
        return indicator;
    }

    public void setIndicator(String indicator) {
        this.indicator = indicator;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public float getLOWLOW() {
        return LOWLOW;
    }

    public void setLOWLOW(float LOWLOW) {
        this.LOWLOW = LOWLOW;
    }

    public float getLOW() {
        return LOW;
    }

    public void setLOW(float LOW) {
        this.LOW = LOW;
    }

    public float getHIGH() {
        return HIGH;
    }

    public void setHIGH(float HIGH) {
        this.HIGH = HIGH;
    }

    public float getHIGHHIGH() {
        return HIGHHIGH;
    }

    public void setHIGHHIGH(float HIGHHIGH) {
        this.HIGHHIGH = HIGHHIGH;
    }

    public int getSeverity() {
        return severity;
    }

    public void setSeverity(int severity) {
        this.severity = severity;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean getACK() {
        return ACK;
    }

    public void setACK(boolean ACK) {
        this.ACK = ACK;
    }

    public String getAckUser() {
        return ackUser;
    }

    public void setAckUser(String ackUser) {
        this.ackUser = ackUser;
    }

    public long getACKTime() {
        return ACKTime;
    }

    public void setACKTime(long ACKTime) {
        this.ACKTime = ACKTime;
    }

    public float getDEAD_BAND() {
        return DEAD_BAND;
    }

    public void setDEAD_BAND(float DEAD_BAND) {
        this.DEAD_BAND = DEAD_BAND;
    }

    public int getPre_severity() {
        return pre_severity;
    }

    public void setPre_severity(int pre_severity) {
        this.pre_severity = pre_severity;
    }

    public long getPre_duration() {
        return pre_duration;
    }

    public void setPre_duration(long pre_duration) {
        this.pre_duration = pre_duration;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}