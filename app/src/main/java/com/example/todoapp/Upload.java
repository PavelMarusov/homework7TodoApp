package com.example.todoapp;

public class Upload {
    private String name;
    private  String imagaUrl;

    public Upload(){}

    public Upload(String name, String imagaUrl) {
        if (name.trim().equals("")){
            name = "Нет имени";
        }
        this.name = name;
        this.imagaUrl = imagaUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImagaUrl() {
        return imagaUrl;
    }

    public void setImagaUrl(String imagaUrl) {
        this.imagaUrl = imagaUrl;
    }
}
