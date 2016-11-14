package com.liborgsadmin.datamodel;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by sakshiagarwal on 13/01/16.
 */


public class AllBooks implements Parcelable {

    private String bookName, imageUrl, publisher, pageCount, desc, subtitle;
    private ArrayList<String> authorName, categories;
    private Integer available;
    private Long isbn;

    public String getName() { return bookName; }
    public void setName(String bookName) {this.bookName = bookName;}

    public Long getISBN() {return isbn; }
    public void setISBN(Long isbn){this.isbn = isbn;}

    public ArrayList<String> getAuthor() { return authorName;}
    public void setAuthor(ArrayList<String> authorName) { this.authorName = authorName;}

    public String getUrl2() {return imageUrl; }
    public void setUrl2(String imageUrl) {this.imageUrl = imageUrl; }

    public String getPublisher() {return publisher;}
    public void setPublisher(String publisher) {this.publisher = publisher;}

    public  String getPageCount() {return pageCount;}
    public void setPageCount(String pageCount) {this.pageCount = pageCount;}

    public int getAvailable() {return available;}
    public void setAvailable(int available) {this.available = available;}

    public  String getDesc() {return desc;}
    public void setDesc(String desc) {this.desc = desc;}

    public  String getSubtitle() {return subtitle;}
    public void setSubtitle(String subtitle) {this.subtitle = subtitle;}

    public ArrayList<String> getCategories() { return categories;}
    public void setCategories(ArrayList<String> categories) { this.categories = categories;}


    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<AllBooks> CREATOR = new Creator<AllBooks>() {
        @Override
        public AllBooks createFromParcel(Parcel in) {
            return new AllBooks(in);
        }

        @Override
        public AllBooks[] newArray(int size) {
            return new AllBooks[size];
        }
    };

    public AllBooks() {

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(bookName);
        dest.writeList(authorName);
        dest.writeString(imageUrl);
        dest.writeString(imageUrl);
        dest.writeString(publisher);
        dest.writeString(pageCount);
        dest.writeInt(available);
        dest.writeString(desc);
        dest.writeString(subtitle);
        dest.writeList(categories);
    }

    public AllBooks(Parcel in) {
        bookName = in.readString();
        imageUrl = in.readString();
        publisher = in.readString();
        pageCount = in.readString();
        desc = in.readString();
        subtitle = in.readString();
        authorName = in.createStringArrayList();
        categories = in.createStringArrayList();
        available = in.readInt();
    }

}
