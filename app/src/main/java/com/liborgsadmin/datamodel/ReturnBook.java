package com.liborgsadmin.datamodel;

import java.util.ArrayList;

/**
 * Created by sakshiagarwal on 12/01/16.
 */
public class ReturnBook {

    private String bookName, bookIssueId, imageUrl, userName, userEmail;
    private ArrayList<String> authorName;
    private long returnDate;

    public String getName() { return bookName; }
    public void setName(String bookName) {this.bookName = bookName;}

    public ArrayList<String> getAuthor() { return authorName;}
    public void setAuthor(ArrayList<String> authorName) { this.authorName = authorName;}

    public String getUserName() {return userName;}
    public void setUserName(String userName) {this.userName = userName;}

    public String getEmail() { return userEmail;}
    public void setEmail(String userEmail) {this.userEmail = userEmail;}

    public String getIssueId() {return bookIssueId;}
    public void setIssueId(String bookIssueId) {this.bookIssueId = bookIssueId; }

    public long getReturnDate() {return returnDate;}
    public void setReturnDate(long returnDate) {this.returnDate = returnDate;}

    public String getUrl() {return imageUrl; }
    public void setUrl(String imageUrl) {this.imageUrl = imageUrl; }
}
