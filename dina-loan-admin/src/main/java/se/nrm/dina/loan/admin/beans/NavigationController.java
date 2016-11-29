/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package se.nrm.dina.loan.admin.beans;

import java.io.Serializable;
import javax.faces.bean.ManagedBean; 
import javax.faces.bean.RequestScoped;

/**
 *
 * @author idali
 */
@ManagedBean(name = "navigationController", eager = true)
@RequestScoped
public class NavigationController implements Serializable  {
    
    
    public String processHome() {
        return "/secure/home.xhtml";
    }
}
