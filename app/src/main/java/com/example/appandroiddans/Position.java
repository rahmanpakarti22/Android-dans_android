package com.example.appandroiddans;

public class Position
{
    private String title;
    private String company;
    private String location;
    private String type;
    private String company_logo;
    private String description;
    private String url;

    public Position(String title, String company, String location, String type, String company_logo, String description, String url){
        this.title = title;
        this.company = company;
        this.location = location;
        this.type = type;
        this.company_logo = company_logo;
        this.description = description;
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public String getCompany() {
        return company;
    }

    public String getLocation() {
        return location;
    }

    public String getType() {
        return type;
    }

    public String getCompany_logo() {
        return company_logo;
    }

    public String getDescription() {
        return description;
    }

    public String getUrl() {
        return url;
    }
}
