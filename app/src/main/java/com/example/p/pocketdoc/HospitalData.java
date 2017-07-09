package com.example.p.pocketdoc;

import java.io.Serializable;

/**
 * Created by P on 11-04-2017.
 */

public class HospitalData implements Serializable {

    int hid;
    String hname;
    String address;
    String harea;
    String hcontact;
    String hlat;
    String hlong;
    String himage;

    public HospitalData(String address, String harea, String hcontact, int hid, String himage, String hlat, String hlong, String hname) {
        this.address = address;
        this.harea = harea;
        this.hcontact = hcontact;
        this.hid = hid;
        this.himage = himage;
        this.hlat = hlat;
        this.hlong = hlong;
        this.hname = hname;
    }
}
