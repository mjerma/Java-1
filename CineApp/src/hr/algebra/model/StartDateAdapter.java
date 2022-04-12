/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 *
 * @author Mihael
 */
public class StartDateAdapter extends XmlAdapter<String, LocalDate>{

    private static final DateTimeFormatter START_DATE_FORMATTER =  DateTimeFormatter.ofPattern("d.M.yyyy");
    
    @Override
    public LocalDate unmarshal(String text) throws Exception {
        return LocalDate.parse(text, START_DATE_FORMATTER);
    }

    @Override
    public String marshal(LocalDate date) throws Exception {
        return date.format(DateTimeFormatter.ISO_DATE);
    }
    
}
