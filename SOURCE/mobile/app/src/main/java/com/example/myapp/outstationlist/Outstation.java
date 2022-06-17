package com.example.myapp.outstationlist;

import com.example.myapp.R;

import java.io.Serializable;

public class Outstation implements Serializable {

    private int id;
    private int OS_Type;
    private int resourceId;
    private String rcv_outstationNumber;
    private String rcv_location;

    public Outstation(int id, int OS_Type, int resourceId, String rcv_outstationNumber, String rcv_location) {
        this.id = id;
        this.OS_Type = OS_Type;
        this.resourceId = resourceId;
        this.rcv_outstationNumber = rcv_outstationNumber;
        this.rcv_location = rcv_location;
    }

    public Outstation() {
        this.id = 0;
        this.OS_Type = 0;
        this.resourceId = R.drawable.ic_khithai;
        this.rcv_outstationNumber = "";
        this.rcv_location = "";
    }

    public int getOS_Type() {
        return OS_Type;
    }

    public void setOS_Type(int OS_Type) {
        this.OS_Type = OS_Type;
    }

    public int getResourceId() {
        return resourceId;
    }

    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }

    public String getRcv_outstationNumber() {
        return rcv_outstationNumber;
    }

    public void setRcv_outstationNumber(String rcv_outstationNumber) {
        this.rcv_outstationNumber = rcv_outstationNumber;
    }

    public String getRcv_location() {
        return rcv_location;
    }

    public void setRcv_location(String rcv_location) {
        this.rcv_location = rcv_location;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
