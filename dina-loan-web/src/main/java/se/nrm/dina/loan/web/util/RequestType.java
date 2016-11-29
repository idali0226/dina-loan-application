/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package se.nrm.dina.loan.web.util;

/**
 *
 * @author idali
 */
public enum RequestType {
    
    Physical,
    Photo,
    Information;
    
    public String getText() {
        return this.name();
    }
    
    public boolean isPhysical(String type) {
        return Physical.name().toLowerCase().equals(type.toLowerCase());
    }

    public boolean isPhoto(String type) {
        return Photo.name().toLowerCase().equals(type.toLowerCase());
    }

    public boolean isInformation(String type) {
        return Information.name().toLowerCase().equals(type.toLowerCase());
    }
}
