package com.example.droidmaps;

public class Locationclass {
    public String name;
    public String height;
    public String age;
    public String colorOfLeaves;
    public String seasonOfBearingFruits;
    public Double latitude;
    public Double longitude;


   public  Locationclass(String name,String height,String age,String colorOfLeaves,String seasonOfBearingFruits,Double latitude,Double longitude){
        this.name=name;
        this.height=height;
        this.age=age;
        this.colorOfLeaves=colorOfLeaves;
        this.seasonOfBearingFruits=seasonOfBearingFruits;
        this.latitude=latitude;
        this.longitude=longitude;
    }
}
