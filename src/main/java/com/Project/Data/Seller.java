package com.Project.Data;

public class Seller {
    public String cif;
    public String name;
    public String businessName;
    public String phone;
    public String email;
    public String password;
    public String url;
    public boolean isPro;

    public Seller() {
    }

    public Seller(String cif, String name, String businessName, String phone, String email, String password, String url, boolean isPro) {
        this.cif = cif;
        this.name = name;
        this.businessName = businessName;
        this.phone = phone;
        this.email = email;
        this.password = password;
        this.url = url;
        this.isPro = isPro;
    }
}
