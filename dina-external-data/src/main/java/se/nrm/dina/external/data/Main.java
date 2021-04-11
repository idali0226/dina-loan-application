/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.nrm.dina.external.data;

import java.util.Calendar;

/**
 *
 * @author idali
 */
public class Main {

    public static void main(String[] args) {
        Calendar c = Calendar.getInstance();

        c.setTime (
        new java.util.Date());
        int dayOfWeek  = c.get(Calendar.DAY_OF_WEEK);
        System.out.println("day of the week : " + dayOfWeek);
    }
    
}
