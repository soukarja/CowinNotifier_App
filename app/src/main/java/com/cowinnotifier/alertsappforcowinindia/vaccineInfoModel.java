package com.cowinnotifier.alertsappforcowinindia;

public class vaccineInfoModel {
    private String vaccinationDate;
    private String vaccinationCenter;
    private String vaccinationAddress;
    private String vaccinationName;
    private String vaccinationAge;
    private String vaccinationPrice;
    private String vaccinationDose1;
    private String vaccinationDose2;

    vaccineInfoModel(String vaccinationDate, String vaccinationCenter, String vaccinationAddress, String vaccinationName, String vaccinationAge, String vaccinationPrice, String vaccinationDose1, String vaccinationDose2){
        this.vaccinationDate=vaccinationDate;
        this.vaccinationCenter=vaccinationCenter;
        this.vaccinationAddress=vaccinationAddress;
        this.vaccinationName=vaccinationName;
        this.vaccinationAge=vaccinationAge;
        this.vaccinationPrice=vaccinationPrice;
        this.vaccinationDose1=vaccinationDose1;
        this.vaccinationDose2=vaccinationDose2;
    }



    public String getVaccinationDate() {
        return vaccinationDate;
    }

    public String getVaccinationCenter() {
        return vaccinationCenter;
    }

    public String getVaccinationAddress() {
        return vaccinationAddress;
    }

    public String getVaccinationName() {
        return vaccinationName;
    }

    public String getVaccinationAge() {
        return vaccinationAge;
    }

    public String getVaccinationPrice() {
        return vaccinationPrice;
    }

    public String getVaccinationDose1() {
        return vaccinationDose1;
    }

    public String getVaccinationDose2() {
        return vaccinationDose2;
    }
}
