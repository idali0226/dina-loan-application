package se.nrm.dina.loan.web.pdf;

//import com.itextpdf.text.Anchor;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.Serializable;
import lombok.extern.slf4j.Slf4j;
import se.nrm.dina.loan.web.util.CommonString;
import se.nrm.dina.loan.web.util.RequestType;
import se.nrm.dina.mongodb.loan.vo.Loan;
import se.nrm.dina.mongodb.loan.vo.Sample;
import se.nrm.dina.mongodb.loan.vo.User;

/**
 *
 * @author idali
 */
@Slf4j
public class PDFCreator implements Serializable {
    
    private final String newLine = "\n";
    private final String adminPdf = "_admin.pdf";
    private final String pdf = ".pdf";
    private final String loanrequest = "/loanrequest_";
    private final String na = "N/A";
    private final String dash = " - ";
    
    private Paragraph paragraph;
    private BaseColor color = BaseColor.WHITE;
    
    private boolean isSwedish;
    
    private final Font titleFont = new Font(Font.FontFamily.TIMES_ROMAN, 22, Font.BOLD);
    private final Font subTitleFont = new Font(Font.FontFamily.TIMES_ROMAN, 16, Font.BOLD);
    private final Font sectionTitleFont = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);
    private final Font textFont = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL);
    private final Font lableFont = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD);
    
    private Document document;
    private Document adminDocument;
    
    public void createInformationLoanPDF(Loan loan, boolean isEducationLoan,
            boolean isSwedish, String path)
            throws FileNotFoundException, DocumentException {
        log.info("createInformationLoanPDF : {} -- {}", path, isSwedish);
        
        this.isSwedish = isSwedish;        
        
        openDocuments(loan, path);
        
        addNonSecondaryBorrow(loan);
        addCommercialInformation(loan, isEducationLoan);
        addTermsOfLoanAgreement();
        
        document.close();
        adminDocument.close();
    }
    
    public void createScientificLoanPDF(Loan loan, String requestType, boolean isSwedish, String path)
            throws FileNotFoundException, DocumentException {
        log.info("createScientificLoanPDF : {} -- {}", path, isSwedish);
        
        this.isSwedish = isSwedish;
        
        openDocuments(loan, path);
        if (loan.getSecondaryUser() != null) {
            addContact(loan);
        } else {
            addNonSecondaryBorrow(loan);            
        }
        
        addLoanRequestForScientificPurpose(loan, requestType);
        addLoanSampleList(loan, requestType);
        
        if (!RequestType.Information.isInformation(requestType)) {
            addDestructiveInformaionOrPhotoInformation(loan, requestType);
            addCITESInformation(loan);            
            addTermsOfLoanAgreement();            
        }
        
        document.close();
        adminDocument.close();
    }
    
    private void addDestructiveInformaionOrPhotoInformation(Loan loan, String requestType) throws DocumentException {
        log.info("addDestructiveInformaionOrPhotoInformation : {}", requestType);
        if (RequestType.Physical.isPhysical(requestType)) {
            addDestructiveInformation(loan);
        } else {
            addPhotoInformation(loan);
        }        
    }
    
    private void openDocuments(Loan loan, String path) throws FileNotFoundException, DocumentException {
        log.info("openDocuments");
        
        StringBuilder sb = new StringBuilder();
        sb.append(path);
        sb.append(loanrequest);
        sb.append(loan.getId());
        
        File summaryAdminFile = new File(sb.toString() + adminPdf);                 // loan request summary admin pdf file (request_id_admin.pdf)

        sb.append(pdf);
        File summaryFile = new File(sb.toString().trim());                      // loan request summary pdf file (request_id.pdf)

        document = new Document(PageSize.LETTER);
        adminDocument = new Document(PageSize.LETTER);
        PdfWriter.getInstance(document, new FileOutputStream(summaryFile));
        PdfWriter.getInstance(adminDocument, new FileOutputStream(summaryAdminFile));
        document.open();
        adminDocument.open();
        
        addTitle(loan);
    }
    
    private void addTitle(Loan loan) throws DocumentException {
        log.info("addTitle");
        paragraph = new Paragraph(CommonString.getLoanRequest(isSwedish), titleFont);
        paragraph.setAlignment(Element.ALIGN_CENTER);
        addEmptyLine(paragraph, 1);
        paragraph.add(new Paragraph(loan.getSubmitDate(), textFont));
        paragraph.add(new Paragraph(loan.getId(), subTitleFont));
        
        addEmptyLine(paragraph, 1);
        document.add(paragraph);
        adminDocument.add(paragraph);
    }
    
    private void addNonSecondaryBorrow(Loan loan) throws DocumentException {
        log.info("addNonSecondaryBorrow");
        paragraph = new Paragraph(CommonString.getContactDetails(isSwedish), subTitleFont);
        addEmptyLine(paragraph, 2);
        document.add(paragraph);
        adminDocument.add(paragraph);
        
        User primaryUser = loan.getPrimaryUser();
        log.info("primaryUser : {}", primaryUser);
        paragraph = new Paragraph(CommonString.getBowrrow(isSwedish), sectionTitleFont);
        
        document.add(paragraph);
        adminDocument.add(paragraph);
        
        paragraph = new Paragraph(primaryUser.getName() + newLine, textFont);
        paragraph.add(new Chunk(primaryUser.getEmail() + newLine, textFont));
        paragraph.add(new Chunk(primaryUser.getPhone() + newLine, textFont));
        paragraph.add(new Chunk(primaryUser.getInstitution() + newLine, textFont));
        if (primaryUser.getEoricode() != null && !primaryUser.getEoricode().isEmpty()) {
            paragraph.add(new Chunk(primaryUser.getEoricode() + newLine, textFont));
        }
        if (primaryUser.getDepartment() != null && !primaryUser.getDepartment().isEmpty()) {
            paragraph.add(new Chunk(primaryUser.getDepartment() + newLine, textFont));
        }
        paragraph.add(new Chunk(primaryUser.getAddress().getCompleteAddress() + newLine, textFont));
        addEmptyLine(paragraph, 1);
        document.add(paragraph);
        adminDocument.add(paragraph);
        log.info("addNonSecondaryBorrow added");
    }
    
    private void addCommercialInformation(Loan loan, boolean isEducationLoan) throws DocumentException {
        log.info("addCommercialInformation : {}", isEducationLoan);
        String paragraphTitl = isEducationLoan ? CommonString.getEducation(isSwedish)
                : CommonString.getCommercial(isSwedish);
        paragraph = new Paragraph(paragraphTitl, sectionTitleFont);
        addEmptyLine(paragraph, 1);
        document.add(paragraph);
        adminDocument.add(paragraph);
        
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(50);
        table.setHorizontalAlignment(Element.ALIGN_LEFT);
        
        PdfPCell cell = null;
        if (isEducationLoan) {
            addCells(table, cell, 2, color, CommonString.getPurpose(isSwedish),
                    loan.getEduPurpose(), false, false);
        } else {
            addCells(table, cell, 2, color, CommonString.getLoanType(isSwedish),
                    loan.getType(), false, false);
        }
        document.add(table);
        adminDocument.add(table);
        
        paragraph = new Paragraph();
        addEmptyLine(paragraph, 1);
        paragraph.add(new Chunk(CommonString.getDescription(isSwedish), sectionTitleFont));
        addEmptyLine(paragraph, 1);
        
        String loanDesc = loan.getLoanDescription();
        if (loanDesc == null || loanDesc.isEmpty()) {
            loanDesc = na;
        }
        paragraph.add(new Chunk(loanDesc, textFont));
        addEmptyLine(paragraph, 1);
        
        if (loan.getEdPurposeFile() != null && !loan.getEdPurposeFile().isEmpty()) {
            paragraph.add(new Chunk(CommonString.getAttachment(isSwedish), lableFont));
            paragraph.add(new Chunk(loan.getEdPurposeFile(), textFont));
            addEmptyLine(paragraph, 2);
        }
        paragraph.add(new Chunk(CommonString.getLoanPeriod(isSwedish), lableFont));
        paragraph.add(new Chunk(buildTimePeriod(loan), textFont));
        addEmptyLine(paragraph, 2);
        
        document.add(paragraph);
        adminDocument.add(paragraph);
        
        if (isEducationLoan) {
            paragraph = new Paragraph(CommonString.getExhibitionDescription(isSwedish), sectionTitleFont);
            addEmptyLine(paragraph, 1);
            
            String exhPurposeDesc = loan.getExhPorpuseDesc();
            log.info("exhPurposeDesc : {}", exhPurposeDesc);
            if (exhPurposeDesc == null || exhPurposeDesc.isEmpty()) {
                exhPurposeDesc = na;
            }
            paragraph.add(new Chunk(exhPurposeDesc, textFont));
            addEmptyLine(paragraph, 2);
            document.add(paragraph);
            adminDocument.add(paragraph);
        }
    }
    
    private String buildTimePeriod(Loan loan) {
        StringBuilder sb = new StringBuilder();
        sb.append(loan.getFromDate());
        sb.append(dash);
        sb.append(loan.getToDate());
        return sb.toString();
    }

    /**
     * Terms of Loan agreement for borrower and primary borrower
     *
     * @throws DocumentException
     */
    private void addTermsOfLoanAgreement() throws DocumentException {
        log.info("addTermsOfLoanAgreement");
        paragraph = new Paragraph(CommonString.getTermsOfAgreement(isSwedish), sectionTitleFont);
        
        addEmptyLine(paragraph, 1);
        paragraph.add(new Paragraph(CommonString.getLoanPolicy(isSwedish), textFont));
        paragraph.add(new Paragraph(CommonString.getPrimaryBorrowerAgreement(isSwedish), textFont));
        addEmptyLine(paragraph, 1);
        
        document.add(paragraph);
        adminDocument.add(paragraph);
        log.info("addTermsOfLoanAgreement added");
    }
    
    private void addContact(Loan loan) throws DocumentException {
        log.info("addContact : {}", loan);
        paragraph = new Paragraph(CommonString.getContactDetails(isSwedish), subTitleFont);
        addEmptyLine(paragraph, 2);
        document.add(paragraph);
        adminDocument.add(paragraph);
        
        User primaryUser = loan.getPrimaryUser();
        log.info("primeary user : {}", primaryUser);
        
        User secondaryUser = loan.getSecondaryUser();
        log.info("secondaryUser user : {}", secondaryUser);
        if (secondaryUser != null) {
            paragraph = new Paragraph(CommonString.getPrimaryContact(isSwedish), sectionTitleFont);
        } else {
            paragraph = new Paragraph(CommonString.getBowrrow(isSwedish), sectionTitleFont);
        }
        document.add(paragraph);
        adminDocument.add(paragraph);
        
        paragraph = new Paragraph(primaryUser.getName() + newLine, textFont);
        paragraph.add(new Chunk(primaryUser.getEmail() + newLine, textFont));
        paragraph.add(new Chunk(primaryUser.getPhone() + newLine, textFont));
        paragraph.add(new Chunk(primaryUser.getInstitution() + newLine, textFont));
        if (primaryUser.getEoricode() != null && !primaryUser.getEoricode().isEmpty()) {
            paragraph.add(new Chunk(primaryUser.getEoricode() + newLine, textFont));
        }
        if (primaryUser.getDepartment() != null && !primaryUser.getDepartment().isEmpty()) {
            paragraph.add(new Chunk(primaryUser.getDepartment() + newLine, textFont));
        }
        paragraph.add(new Chunk(primaryUser.getAddress().getCompleteAddress() + newLine, textFont));
        addEmptyLine(paragraph, 1);
        document.add(paragraph);
        adminDocument.add(paragraph);
        
        if (secondaryUser != null) {
            paragraph = new Paragraph(CommonString.getSecondaryContact(isSwedish), sectionTitleFont);
            document.add(paragraph);
            adminDocument.add(paragraph);
            
            paragraph = new Paragraph(secondaryUser.getName() + newLine, textFont);
            paragraph.add(new Chunk(secondaryUser.getEmail() + newLine, textFont));
            paragraph.add(new Chunk(secondaryUser.getPhone() + newLine, textFont));
            paragraph.add(new Chunk(secondaryUser.getInstitution() + newLine, textFont));
            
            if (secondaryUser.getEoricode() != null && !secondaryUser.getEoricode().isEmpty()) {
                paragraph.add(new Chunk(secondaryUser.getEoricode() + newLine, textFont));
            }
            
            if (secondaryUser.getDepartment() != null && !secondaryUser.getDepartment().isEmpty()) {
                paragraph.add(new Chunk(secondaryUser.getDepartment() + newLine, textFont));
            }
            paragraph.add(new Chunk(secondaryUser.getAddress().getCompleteAddress() + newLine, textFont));
            addEmptyLine(paragraph, 2);
            document.add(paragraph);
            adminDocument.add(paragraph);
        }
        log.info("Contacts are added" );
    }
    
    private void addCITESInformation(Loan loan) throws DocumentException {
        log.info("addCITESInformation");
        paragraph = new Paragraph(CommonString.getCITE(isSwedish), sectionTitleFont);
        addEmptyLine(paragraph, 1);
        
        paragraph.add(new Chunk(CommonString.getCITENumber(isSwedish), lableFont));
        
        String citeNum = loan.getCitesNumber();
        if (citeNum == null || citeNum.isEmpty()) {
            citeNum = " - ";
        }
        paragraph.add(new Chunk(citeNum, textFont));
        
        addEmptyLine(paragraph, 2);
        document.add(paragraph);
        adminDocument.add(paragraph);
        log.info("addCITESInformation is added");
    }
    
    private void addPhotoInformation(Loan loan) throws DocumentException {
        log.info("addPhotoInformation");
        paragraph = new Paragraph(CommonString.getPhotoMethod(isSwedish), sectionTitleFont);
        addEmptyLine(paragraph, 1);
        
        String photoInst = loan.getPhotoInstraction();
        if (photoInst == null || photoInst.isEmpty()) {
            photoInst = " - ";
        }
        paragraph.add(new Chunk(photoInst, textFont));
        addEmptyLine(paragraph, 1);
        
        if (loan.getPhotoInstractionFile() != null && !loan.getPhotoInstractionFile().isEmpty()) {
            paragraph.add(new Chunk(CommonString.getAttachment(isSwedish), lableFont));
            paragraph.add(new Chunk(loan.getPhotoInstractionFile(), textFont));
            addEmptyLine(paragraph, 2);
        }
        document.add(paragraph);
        adminDocument.add(paragraph);
        log.info("addPhotoInformation added");
    }
    
    private void addDestructiveInformation(Loan loan) throws DocumentException {
        log.info("addDestructiveInformation");
        paragraph = new Paragraph(CommonString.getDestructiveMethod(isSwedish), sectionTitleFont);
        addEmptyLine(paragraph, 1);
        
        String destractive = loan.getIsDestructive();
        if (Boolean.parseBoolean(destractive)) {
            paragraph.add(new Chunk(CommonString.getDestructive(isSwedish), textFont));
            addEmptyLine(paragraph, 1);
        } else {
            paragraph.add(new Chunk(CommonString.getNoDestructive(isSwedish), textFont));
            addEmptyLine(paragraph, 1);
        }
        
        String destructiveMeth = loan.getDestructiveMethod();
        if (destructiveMeth == null || destructiveMeth.isEmpty()) {
            destructiveMeth = " - ";
        }
        paragraph.add(new Chunk(destructiveMeth, textFont));
        addEmptyLine(paragraph, 1);
        
        if (loan.getDestructiveFile() != null && !loan.getDestructiveFile().isEmpty()) {
            paragraph.add(new Chunk(CommonString.getAttachment(isSwedish), lableFont));
            paragraph.add(new Chunk(loan.getDestructiveFile(), textFont));
            addEmptyLine(paragraph, 2);
        }
        log.info("DestructiveInformation is added");
        document.add(paragraph);
        adminDocument.add(paragraph);
    }
    
    private void addCells(PdfPTable table, PdfPCell cell, int index, BaseColor color, String label, String value, boolean isTop, boolean isBottom) {
        
        color = BaseColor.WHITE;
        if (index % 2 != 0) {
            color = new BaseColor(237, 246, 250);
        }
        
        cell = new PdfPCell(new Phrase(label, lableFont));
        cell.setPaddingLeft(15);
        if (isTop) {
            cell.setPaddingTop(5);
        } else if (isBottom) {
            cell.setPaddingBottom(5);
        }
        
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setBackgroundColor(color);
        table.addCell(cell);
        
        cell = new PdfPCell(new Phrase(value, textFont));
        if (isTop) {
            cell.setPaddingTop(5);
        } else if (isBottom) {
            cell.setPaddingBottom(5);
        }
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setBackgroundColor(color);
        table.addCell(cell);
    }
    
    private void addTable(Sample sample, int index) throws DocumentException {
        
        color = BaseColor.WHITE;
        PdfPTable table = new PdfPTable(2);
        table.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.setTotalWidth(new float[]{72, 144});
        
        PdfPCell cell = null;
        addCells(table, cell, index, color, CommonString.getCatelogNumber(isSwedish), sample.getCatalogNumber(), true, false);
        addCells(table, cell, index, color, CommonString.getFamily(isSwedish), sample.getFamily(), false, false);
        addCells(table, cell, index, color, CommonString.getGenus(isSwedish), sample.getGenus(), false, false);
        addCells(table, cell, index, color, CommonString.getSpecies(isSwedish), sample.getSpecies(), false, false);
        addCells(table, cell, index, color, CommonString.getAuthor(isSwedish), sample.getAuctor(), false, false);
        addCells(table, cell, index, color, CommonString.getTypeStatus(isSwedish), sample.getTypeStatus(), false, false);
        
        addCells(table, cell, index, color, CommonString.getCountry(isSwedish), sample.getCountry(), false, false);
        addCells(table, cell, index, color, CommonString.getLocality(isSwedish), sample.getLocality(), false, false);
        
        addCells(table, cell, index, color, CommonString.getCollectedYear(isSwedish), sample.getCollectedYear(), false, false);
        addCells(table, cell, index, color, CommonString.getCollector(isSwedish), sample.getCollector(), false, false);
        addCells(table, cell, index, color, CommonString.getPreservationType(isSwedish), sample.getType(), false, false);
        addCells(table, cell, index, color, CommonString.getLocation(isSwedish), sample.getLocation(), false, false);
        addCells(table, cell, index, color, CommonString.getCommens(isSwedish), sample.getOther(), false, true);
        
        adminDocument.add(table);
        
        table = new PdfPTable(2);
        table.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.setTotalWidth(new float[]{72, 144});
        
        cell = null;
        addCells(table, cell, index, color, CommonString.getCatelogNumber(isSwedish), sample.getCatalogNumber(), true, false);
        addCells(table, cell, index, color, CommonString.getFamily(isSwedish), sample.getFamily(), false, false);
        addCells(table, cell, index, color, CommonString.getGenus(isSwedish), sample.getGenus(), false, false);
        addCells(table, cell, index, color, CommonString.getSpecies(isSwedish), sample.getSpecies(), false, false);
        addCells(table, cell, index, color, CommonString.getAuthor(isSwedish), sample.getAuctor(), false, false);
        addCells(table, cell, index, color, CommonString.getTypeStatus(isSwedish), sample.getTypeStatus(), false, false);
        
        addCells(table, cell, index, color, CommonString.getCountry(isSwedish), sample.getCountry(), false, false);
        addCells(table, cell, index, color, CommonString.getLocality(isSwedish), sample.getLocality(), false, false);
        
        addCells(table, cell, index, color, CommonString.getCollectedYear(isSwedish), sample.getCollectedYear(), false, false);
        addCells(table, cell, index, color, CommonString.getCollector(isSwedish), sample.getCollector(), false, false);
        
        addCells(table, cell, index, color, CommonString.getPreservationType(isSwedish), sample.getType(), false, false);
        addCells(table, cell, index, color, CommonString.getCommens(isSwedish), sample.getOther(), false, true);
        
        document.add(table);
    }
    
    private void addLoanSampleList(Loan loan, String requestType) throws DocumentException {
        log.info("addLoanSampleList");
        
        paragraph = new Paragraph(CommonString.getSampleDetails(isSwedish), sectionTitleFont);
        
        addEmptyLine(paragraph, 2);
        document.add(paragraph);
        adminDocument.add(paragraph);
        
        if (loan.getSamples() != null && !loan.getSamples().isEmpty()) {
            int index = 0;
            for (Sample sample : loan.getSamples()) {
                index++;
                addTable(sample, index);
            }
        } else {
            paragraph = new Paragraph();
            paragraph.add(new Chunk(na, textFont));
            document.add(paragraph);
            adminDocument.add(paragraph);
        }
        
        paragraph = new Paragraph();
        addEmptyLine(paragraph, 1);
        
        if (RequestType.Information.isInformation(requestType)) {
            paragraph.add(new Chunk(CommonString.getAdditionalInfoInfo(isSwedish), sectionTitleFont));
        } else {
            paragraph.add(new Chunk(CommonString.getAdditionalInfo(isSwedish), sectionTitleFont));
        }
        addEmptyLine(paragraph, 1);
        String additionalInfo = loan.getSampleSetAdditionalInfo();
        if (additionalInfo == null || additionalInfo.isEmpty()) {
            additionalInfo = na;
        }
        paragraph.add(new Chunk(additionalInfo, textFont));
        addEmptyLine(paragraph, 2);
        document.add(paragraph);
        adminDocument.add(paragraph);
        log.info("addLoanSampleList added");
    }
    
    private void addLoanRequestForScientificPurpose(Loan loan, String requestType) throws DocumentException {
        log.info("addLoanRequestForScientificPurpose : {}", requestType);
        paragraph = new Paragraph(CommonString.getLoanDetail(isSwedish), sectionTitleFont);
        addEmptyLine(paragraph, 2);
        document.add(paragraph);
        adminDocument.add(paragraph);
        
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(50);
        table.setHorizontalAlignment(Element.ALIGN_LEFT);
        
        PdfPCell cell = null;
        addCells(table, cell, 2, color,
                CommonString.getDepartment(isSwedish),
                loan.getDepartment(), false, false);
        addCells(table, cell, 2, color,
                CommonString.getLoanPurpose(isSwedish),
                loan.getPurpose(), false, false);
        addCells(table, cell, 2, color,
                CommonString.getLoanType(isSwedish),
                loan.getType(), false, false);
        addCells(table, cell, 2, color,
                CommonString.getRelevantCollection(isSwedish),
                loan.getReleventCollection(), false, false);
        
        document.add(table);
        adminDocument.add(table);
        
        if (!RequestType.Information.isInformation(requestType)) {
            paragraph = new Paragraph();
            addEmptyLine(paragraph, 1);
            paragraph.add(new Chunk(CommonString.getLoanDescription(isSwedish), sectionTitleFont));           // Description of loan
            addEmptyLine(paragraph, 1);
            
            String loanDescription = loan.getLoanDescription();
            if (loanDescription == null || loanDescription.isEmpty()) {
                loanDescription = na;
            }
            paragraph.add(new Chunk(loanDescription, textFont));
            addEmptyLine(paragraph, 2);
            
            if (loan.getDestructiveFile() != null && !loan.getDestructiveFile().isEmpty()) {
                paragraph.add(new Chunk(CommonString.getAttachment(isSwedish), lableFont));
                paragraph.add(new Chunk(loan.getDestructiveFile(), textFont));
                addEmptyLine(paragraph, 2);
            }

            // attached loan description file
            
            
            
            if (loan.getLoanDescriptionFile() != null && !loan.getLoanDescriptionFile().isEmpty()) {

//                Anchor anchor = new Anchor(loan.getLoanDescriptionFile());
//                anchor.setReference(buildBasePath(loan.getUuid(), loan.getLoanDescriptionFile()));
//                paragraph.add(anchor);
                paragraph.add(new Chunk(CommonString.getAttachment(isSwedish), lableFont));                
                paragraph.add(new Chunk(loan.getLoanDescriptionFile(), textFont));                
            }

            // attached loan type material file
            if (loan.getTypeMaterialFile() != null && !loan.getTypeMaterialFile().isEmpty()) {

//                Anchor anchor = new Anchor(loan.getTypeMaterialFile());
//                anchor.setReference(buildBasePath(loan.getUuid(), loan.getTypeMaterialFile()));
//                paragraph.add(anchor);
                paragraph.add(new Chunk(loan.getTypeMaterialFile(), textFont));

//                addEmptyLine(paragraph, 2);
            }
            addEmptyLine(paragraph, 2);
            
            document.add(paragraph);
            adminDocument.add(paragraph);
            log.info("addLoanRequestForScientificPurpose added");
        }
    }
    
    private String buildBasePath(String uuid, String fileName) {
        StringBuilder sb = new StringBuilder();
        sb.append("localhost");
        sb.append(uuid);
        sb.append("-");
        sb.append(fileName.trim());
        return sb.toString();
    }

//    private void addLoanReference(Loan loan) throws DocumentException {  
//        paragraph = new Paragraph(CommonString.getLoanReference(isSwedish) + loan.getId() + NEW_LINE, SUB_TITLE_FONT);  
//        document.add(paragraph); 
//        adminDocument.add(paragraph);
//    }
    private void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }
}
