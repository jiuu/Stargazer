package com.example.alex.stargazer;

public class Constellation {
    String name;
    String abbr;
    String genitive;
    String eng;

    public Constellation(String name, String abbr, String genitive, String eng) {
        this.name = name;
        this.abbr = abbr;
        this.genitive = genitive;
        this.eng = eng;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAbbr() {
        return abbr;
    }

    public void setAbbr(String abbr) {
        this.abbr = abbr;
    }

    public String getGenitive() {
        return genitive;
    }

    public void setGenitive(String genitive) {
        this.genitive = genitive;
    }

    public String getEng() {
        return eng;
    }

    public void setEng(String eng) {
        this.eng = eng;
    }
}
