package com.example.gymmanager;

public class Member {

    public int id;
    public String name;
    public String phone;
    public String plan;
    public String startDate;
    public int months;
    public double monthlyFee;
    public boolean trainer;
    public int discount;
    public double totalFee;
    public  String expiryDate;

    public Member(int id, String name, String phone, String plan, String startDate, int months, double monthlyFee, boolean trainer,
                  int discount, double totalFee, String expiryDate) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.plan = plan;
        this.startDate = startDate;
        this.months = months;
        this.monthlyFee = monthlyFee;
        this.trainer = trainer;
        this.discount = discount;
        this.totalFee = totalFee;
        this.expiryDate = expiryDate;
    }
}
