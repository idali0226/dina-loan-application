package se.nrm.dina.loan.web.controllers;

import java.io.Serializable; 
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author idali
 */
@Named(value = "departments")
@SessionScoped
@Slf4j
public class OtherDepartments implements Serializable {
    
    private String url;
    private String departmentName;
    private String departmentCollectionName;
    
    public OtherDepartments() {
        
    }

    public String getUrl() { 
        return url;
    }

    public void setUrl(String url) { 
        this.url = url;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    } 

    public String getDepartmentCollectionName() {
        return departmentCollectionName;
    }

    public void setDepartmentCollectionName(String departmentCollectionName) {
        this.departmentCollectionName = departmentCollectionName;
    } 
}
