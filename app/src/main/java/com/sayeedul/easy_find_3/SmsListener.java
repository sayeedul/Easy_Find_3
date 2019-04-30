package com.sayeedul.easy_find_3;

public interface SmsListener {
    public void messageReceived(String messageText, String name, String number);
}
