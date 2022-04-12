/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 *
 * @author Mihael
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class Person implements Comparable<Person> {
    
    @XmlElement(name = "id")
    private int id;
    @XmlElement(name = "firstname")
    private String firstName;
    @XmlElement(name = "middlename")
    private String middleName;
    @XmlElement(name = "lastname")
    private String lastName;

    public Person(String firstName, String middleName, String lastName) {
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
    }

    public Person(int id, String firstName, String middleName, String lastName) {
        this(firstName, middleName, lastName);
        this.id = id;
    }
    
    public Person(String firstName) {
        this.firstName = firstName;
    }
   
    public Person(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @Override
    public String toString() {
        return (firstName == null ? "" : firstName + " ") + (middleName == null ? "" : middleName + " ") + (lastName == null ? "" : lastName + " ");
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public int compareTo(Person o) {
        return ((Integer)id).compareTo(o.id);
    }
    
}
