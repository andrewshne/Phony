package com.phonybook.phony;

import java.util.Comparator;

public class ContactModel {

    private int id;
    private String name;
    private String phonenum;
    private String email;
    private String image;
    private boolean iswork;

    public static Comparator<ContactModel> contactModelComparatorAsc = new Comparator<ContactModel>() {
        @Override
        public int compare(ContactModel o1, ContactModel o2) {
            return o1.getName().compareTo(o2.getName());
        }
    };

    //Constructor
    public ContactModel(int id, String name, String phonenum, String email, String image, boolean iswork) {
        this.id = id;
        this.name = name;
        this.phonenum = phonenum;
        this.email = email;
        this.iswork = iswork;
        this.image = image;
    }

    //Constructor without image
    public ContactModel(int id, String name, String phonenum, String email, boolean iswork) {
        //this.id = id;
        this.name = name;
        this.phonenum = phonenum;
        this.email = email;
        this.iswork = iswork;
    }

    //Constructor without id
    public ContactModel(String name, String phonenum, String email, String image, boolean iswork) {
        this.name = name;
        this.phonenum = phonenum;
        this.email = email;
        this.iswork = iswork;
        this.image = image;
    }

    //Constructor without id and image
    public ContactModel(String name, String phonenum, String email, boolean iswork) {
        this.name = name;
        this.phonenum = phonenum;
        this.email = email;
        this.iswork = iswork;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhonenum() { return phonenum; }

    public void setPhonenum(String phonenum) {
        this.phonenum = phonenum;
    }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public String getImage() { return image; }

    public void setImage(String image) { this.image = image; }

    public boolean isWork() { return iswork; }

    public void setIsWork(boolean iswork) {
        this.iswork = iswork;
    }
}
