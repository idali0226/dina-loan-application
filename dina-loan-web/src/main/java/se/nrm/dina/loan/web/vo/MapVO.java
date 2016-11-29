/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package se.nrm.dina.loan.web.vo;

/**
 *
 * @author idali
 */
public class MapVO {

    public MapVO(String country, String count) {
        super();
        this.country = country;
        this.count = count;
    }
    private String country;
    private String count;

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }
}
