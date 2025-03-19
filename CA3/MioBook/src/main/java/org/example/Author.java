package org.example;

import java.time.LocalDate;
import java.util.Date;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class Author {
    String name;
    String penName;
    String nationality;
    LocalDate birthDate;
    LocalDate deathDate;

    public Author(String name, String penName, String nationality, LocalDate birthDate, LocalDate deathDate){
        this.name = name;
        this.penName = penName;
        this.nationality = nationality;
        this.birthDate = birthDate;
        this.deathDate = deathDate;

    }

    public String getName(){ return this.name; }
    public void setName(String name){ this.name = name;}
    public String getPenName(){ return this.penName; }
    public void setPenName(String penName){ this.penName = penName;}


    public String getNationality(){ return this.nationality; }
    public void setNationality(String nationality){ this.nationality = nationality;}

    public LocalDate getBirthDate(){ return this.birthDate; }
    public void setBirthDate(LocalDate birthDate){ this.birthDate = birthDate; }

    public LocalDate getDeathDate(){ return this.deathDate; }
    public void setDeathDate(LocalDate deathDate){ this.deathDate = deathDate; }

}