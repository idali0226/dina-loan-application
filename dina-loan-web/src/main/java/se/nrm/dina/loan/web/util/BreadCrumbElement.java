package se.nrm.dina.loan.web.util;



/**
 *
 * @author idali
 */
public enum BreadCrumbElement {
    
    Home,
//    OnlineForm,
    Department,
//    Enhet,
    Botany,
    ESB,
    Geology,
    Palaeobiology, 
    Zoology,  
    Purpose, 
    RequestType,
    Project,
    Collection,
    Speciments,
    Desctructive,
    Photo,
    
    Cites,
    Contact,
    Review,
    NoImplement,
    
    LoanInformation,
    PurposeOfUse,
    Storage,
    LoanType,
    LoanDetail;
      
    public String getText() {
        return this.name().toLowerCase();
    }  
    
    public String getMainMenuTextByLocale(boolean isSwedish) {
      return isSwedish ? NameMapping.getMainMenuSvByKey(this) : NameMapping.getMainMenuEnByKey(this);
    }
    
    public String getEduMenuTextByLocale(boolean isSwedish) {
      return isSwedish ? NameMapping.getEduMenuSvByKey(this) : NameMapping.getEduMenuEnByKey(this);
    }
    
    public String getArtMenuTextByLocale(boolean isSwedish) {
      return isSwedish ? NameMapping.getArtMenuSvByKey(this) : NameMapping.getArtMenuEnByKey(this);
    }
}
