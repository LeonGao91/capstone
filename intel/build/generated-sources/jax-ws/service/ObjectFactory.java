
package service;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the service package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _GetSummaryResponse_QNAME = new QName("http://service/", "getSummaryResponse");
    private final static QName _GetSummary_QNAME = new QName("http://service/", "getSummary");
    private final static QName _GetDetail_QNAME = new QName("http://service/", "getDetail");
    private final static QName _GetDetailResponse_QNAME = new QName("http://service/", "getDetailResponse");
    private final static QName _UploadFile_QNAME = new QName("http://service/", "uploadFile");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: service
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link UploadFile }
     * 
     */
    public UploadFile createUploadFile() {
        return new UploadFile();
    }

    /**
     * Create an instance of {@link GetDetailResponse }
     * 
     */
    public GetDetailResponse createGetDetailResponse() {
        return new GetDetailResponse();
    }

    /**
     * Create an instance of {@link GetDetail }
     * 
     */
    public GetDetail createGetDetail() {
        return new GetDetail();
    }

    /**
     * Create an instance of {@link GetSummary }
     * 
     */
    public GetSummary createGetSummary() {
        return new GetSummary();
    }

    /**
     * Create an instance of {@link GetSummaryResponse }
     * 
     */
    public GetSummaryResponse createGetSummaryResponse() {
        return new GetSummaryResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetSummaryResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service/", name = "getSummaryResponse")
    public JAXBElement<GetSummaryResponse> createGetSummaryResponse(GetSummaryResponse value) {
        return new JAXBElement<GetSummaryResponse>(_GetSummaryResponse_QNAME, GetSummaryResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetSummary }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service/", name = "getSummary")
    public JAXBElement<GetSummary> createGetSummary(GetSummary value) {
        return new JAXBElement<GetSummary>(_GetSummary_QNAME, GetSummary.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetDetail }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service/", name = "getDetail")
    public JAXBElement<GetDetail> createGetDetail(GetDetail value) {
        return new JAXBElement<GetDetail>(_GetDetail_QNAME, GetDetail.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetDetailResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service/", name = "getDetailResponse")
    public JAXBElement<GetDetailResponse> createGetDetailResponse(GetDetailResponse value) {
        return new JAXBElement<GetDetailResponse>(_GetDetailResponse_QNAME, GetDetailResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UploadFile }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service/", name = "uploadFile")
    public JAXBElement<UploadFile> createUploadFile(UploadFile value) {
        return new JAXBElement<UploadFile>(_UploadFile_QNAME, UploadFile.class, null, value);
    }

}
