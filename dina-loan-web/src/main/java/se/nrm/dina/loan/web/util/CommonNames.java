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
public enum CommonNames {
    
    NoResults,
    MissingCatNum,
    MissingFamily,
    MissingGenus,
    MissingSpecies,
    DuplicatValue,
    DestructivePolicy,
    ScientificLoanPolicy,
    CommonLoanPolicy,
    PreservationTypeNotSpecified,
    UploadFileFailed,
    Idle,
    IdleMsg,
    DataSourceConnectionError,
    RequestFailed, 
    SendingEmailsFailed;
    
    
    public String getText() {
        return this.name();
    }
}
