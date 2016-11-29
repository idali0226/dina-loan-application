/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package se.nrm.dina.loan.web.vo;
 
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter; 
import javax.faces.convert.FacesConverter;

/**
 *
 * @author idali
 */
@FacesConverter(value="solrRecordConverter")
public class SolrRecordConverter implements Converter {
      
    @Override
    public Object getAsObject(FacesContext fc, UIComponent uic, String string) {
        
        if (string !=null && string.isEmpty()) {
            return null;
        }
            
        SolrRecord record = new SolrRecord();
        record.setCatalogNum(string);
        return record; 
    }

    @Override
    public String getAsString(FacesContext fc, UIComponent uic, Object o) {
       if (o == null || o.equals("")) {
            return "";
        } else {
            return String.valueOf(((SolrRecord) o).getId());
        }
    }
    
}
