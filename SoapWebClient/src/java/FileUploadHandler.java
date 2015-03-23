
import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.WebServiceRef;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import test.SoapService_Service;

/**
 * Servlet to handle File upload request from Client
 */
public class FileUploadHandler extends HttpServlet {

    //service address
    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/localhost_8080/SoapWebService/SoapService.wsdl")
    private SoapService_Service service;

    //root directory to store the file
    private static final String rootDir = "/Users/wangjerome/Desktop/";

    /**
     * this post method is used to upload file
     *
     * @param request request from client
     * @param response response to client
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        //process only if its multipart content, meaning containing a file
        if (ServletFileUpload.isMultipartContent(request)) {
            try {
                List<FileItem> multiparts = new ServletFileUpload(
                        new DiskFileItemFactory()).parseRequest(request);

                //go through each file
                for (FileItem item : multiparts) {
                    if (!item.isFormField()) {
                        String name = new File(item.getName()).getName();

                        item.write(new File(rootDir + name));

                        getFilePath(rootDir + name);
                    }
                }

                //File uploaded successfully
                request.setAttribute("message", "File Uploaded Successfully");
            } catch (Exception ex) {
                request.setAttribute("message", "File Upload Failed due to " + ex);
            }

        } else {
            request.setAttribute("message",
                    "Sorry this Servlet only handles file upload request");
        }

        //send result page back
        request.getRequestDispatcher("/result.jsp").forward(request, response);

    }

    /**
     * wrapper for remote server call
     * @param path 
     *          file path of the recent uploaded file
     */
    private void getFilePath(java.lang.String path) {
        test.SoapService port = service.getSoapServicePort();
        port.getFilePath(path);
    }

}
