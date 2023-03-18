package com.example.musixBE.models;

public class StatusList {
    public static final StatusData errorService = new StatusData(500, "Error while save data");
    public static final StatusData errorUsernameExisted = new StatusData(452, "Username is existed");
    public static final StatusData errorUsernameNotFound = new StatusData(453, "Username Not Found");
    public static final StatusData errorPasswordNotCorrect = new StatusData(454, "Password Not Correct");
}
