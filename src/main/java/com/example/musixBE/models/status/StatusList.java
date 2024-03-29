package com.example.musixBE.models.status;

public class StatusList {
    public static final StatusData successService = new StatusData(200, "Success");
    public static final StatusData errorService = new StatusData(500, "Error while save data");
    public static final StatusData errorUsernameExisted = new StatusData(452, "Username is existed");
    public static final StatusData errorUsernameNotFound = new StatusData(453, "Username Not Found");
    public static final StatusData errorPasswordNotCorrect = new StatusData(454, "Password Not Correct");
    public static final StatusData errorTokenNotFound = new StatusData(455, "Token Not Found");
    public static final StatusData errorTokenNotValid = new StatusData(456, "Token Not Valid");
    public static final StatusData errorUserIdNotFound = new StatusData(457, "User Id Not Found");
    public static final StatusData errorEmailNotFound = new StatusData(458, "No User Found For This Email Address");
    public static final StatusData errorTokenDoesNotMatch = new StatusData(459, "Token does not match with the email");
    public static final StatusData errorPlaylistNotFound = new StatusData(460, "Playlist Not Found");
    public static final StatusData errorPlaylistExisted = new StatusData(461, "Playlist Existed");
    public static final StatusData errorCommentNotFound = new StatusData(462, "Comment Not Found");
    public static final StatusData errorUsernameDoesNotMatch = new StatusData(463, "Username Does Not Match");
    public static final StatusData errorPostNotFound = new StatusData(464, "Post Not Found");
    public static final StatusData errorCommentNotFoundOnProvidedPost = new StatusData(465, "Comment Not Found On Provided Post");
    public static final StatusData errorReplyNotFoundOnProvidedComment = new StatusData(466, "Reply Not Found On Provided Comment");

    public static final StatusData errorAccountEnable = new StatusData(467, "Account is not valid");
}
