package com.sayeedul.easy_find_3.Pojo;

public class SmsHistoryPojo {

   public String number_in, number_out, name_out , date;

    public SmsHistoryPojo(String number_in, String number_out, String name_out, String date) {
        this.number_in = number_in;
        this.number_out = number_out;
        this.name_out = name_out;
        this.date = date;

    }

    public String getNumber_in() {
        return number_in;
    }

    public void setNumber_in(String number_in) {
        this.number_in = number_in;
    }

    public String getNumber_out() {
        return number_out;
    }

    public void setNumber_out(String number_out) {
        this.number_out = number_out;
    }

    public String getName_out() {
        return name_out;
    }

    public void setName_out(String name_out) {
        this.name_out = name_out;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
