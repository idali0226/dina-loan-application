package se.nrm.dina.loan.web.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import javax.activation.MimetypesFileTypeMap;
import javax.inject.Inject;
import javax.servlet.annotation.WebServlet;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import se.nrm.dina.loan.web.config.InitialProperties;

/**
 *
 * @author idali
 */
@WebServlet("/pdf")
@Slf4j
public class PolicyFilesServlet extends HttpServlet {

    @Inject
    private InitialProperties properities;

    private final String pdf = "pdf";
    private final String id = "id"; 
    private final String scientific = "scientific";
    private final String dash = "-";
    private final String slash = "/";
    private final String attachment = "att";
    private final String pdfMimitype = "application/pdf";
    
    private String mimetype;
    private MimetypesFileTypeMap mimeTypesMap;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        log.info("doGet");

        boolean isAttachmentFile = isAttechmentFile(request);
        File file;
        String fileName = null;
        if (request.getParameter(pdf) != null 
                && !request.getParameter(pdf).isEmpty()) {
            
            mimetype = pdfMimitype;
            file = new File(getPolicyPdfFileName(request.getParameter(pdf)));
            log.info("is file exit : {}", file.exists());
            
        } else {
            String loanFile = isAttachmentFile ? request.getParameter(attachment)
                    : request.getParameter(id);
             
            if (loanFile != null) {
                fileName = loanFile.replace(dash, slash);
            }
            
            log.info("fileName : {}", fileName);
            file = new File(buildFilePath(fileName)); 
        } 
        if (file.exists()) {
            mimetype = isAttachmentFile ? getFileType(file) : pdfMimitype; 
            log.info("mimetype : {}", mimetype);
            
            byte[] content = FileUtils.readFileToByteArray(file);

            response.setContentType(mimetype);
            response.setContentLength(content.length);
            response.getOutputStream().write(content); 
        }
    }

    private boolean isAttechmentFile(HttpServletRequest request) {
        return request.getParameter(attachment) != null
                && !request.getParameter(attachment).isEmpty();
    }

    private String getPolicyPdfFileName(String paramter) {
        return paramter.equals(scientific)
                ? properities.getScientificPolicyPath() : properities.getEducationalPolicyPath();
    }
    
    private String getFileType(File file) {
        mimeTypesMap = new MimetypesFileTypeMap();

        return mimeTypesMap.getContentType(file); 
    }
 
    private String buildFilePath(String fileName) {
        return properities.getLoanFilePath() + fileName;
    } 
}
