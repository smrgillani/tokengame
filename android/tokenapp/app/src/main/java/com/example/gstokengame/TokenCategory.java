package com.example.gstokengame;

public class TokenCategory {
    private String Id;
    private String ImageIco;
    private String Name;
    private String GreetingCount;
    private String Modified;


    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getImageIco() {
        return ImageIco;
    }

    public void setImageIco(String imageIco) {
        ImageIco = imageIco;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getGreetingCount() {
        return GreetingCount;
    }

    public void setGreetingCount(String greetingCount) {
        GreetingCount = greetingCount;
    }

    public String getModified() {
        return Modified;
    }

    public void setModified(String modified) {
        Modified = modified;
    }
}
