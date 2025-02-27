package org.example;

import java.util.Date;

public class Author {
    String name;
    String penName;
    String nationality;
    Date birthdate;
    Date deathDate;


    public String getName(){ return this.name; }
    public void setName(String name){ this.name = name;}

    public String getPenName(){ return this.penName; }
    public void setPenName(String penName){ this.penName = penName;}


    public String getNationality(){ return this.nationality; }
    public void setNationality(String nationality){ this.nationality = nationality;}

    public Date getBirthdate(){ return this.birthdate; }
    public void setBirthdate(Date birthdate){ this.birthdate = birthdate; }

    public Date getDeathDate(){ return this.deathDate; }
    public void setDeathDate(Date deathDate){ this.deathDate = deathDate; }

}