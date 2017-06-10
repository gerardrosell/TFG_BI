package com.grupoactive.bi.rosellcardus.gerard.tfg_bi;

import static java.lang.Integer.parseInt;

/**
 * Created by gerard on 16/05/17.
 */

public class Entry {
    public int IDregistre;
    public String IDJob;
    public String IDTask;
    public String Description;
    public int Hours;
    public String IDPerson;

    public Entry(String IDP, String WS_input){
        IDPerson=IDP;
        //WS_input.substring(1,WS_input.length());
        String[] llista= WS_input.split("\\\"");
        IDJob=llista[5];
        //IDJob=llista[5].substring(llista[5].length()-4);//linia com la superior però que talla els noms de projecte
        IDTask=llista[9];
        Hours=parseInt(llista[12].substring(1, llista[12].length()-1));
        IDregistre=parseInt(llista[2].substring(1, llista[2].length()-1));
        Description=llista[15];

    }

    public String getIDJob() {
        return IDJob;
    }

    public void setIDJob(String IDJob) {
        this.IDJob = IDJob;
    }

    public String getIDTask() {
        return IDTask;
    }

    public void setIDTask(String IDTask) {
        this.IDTask = IDTask;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public int getHours() {
        return Hours;
    }

    public void setHours(int hours) {
        Hours = hours;
    }

    public String getIDPerson() {
        return IDPerson;
    }

    public void setIDPerson(String IDPerson) {
        this.IDPerson = IDPerson;
    }
}
