
package test;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the test package. 
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

    private final static QName _GetFilePath_QNAME = new QName("http://test/", "getFilePath");
    private final static QName _HelloResponse_QNAME = new QName("http://test/", "helloResponse");
    private final static QName _Hello_QNAME = new QName("http://test/", "hello");
    private final static QName _UploadFile_QNAME = new QName("http://test/", "uploadFile");
    private final static QName _UploadFileFileContent_QNAME = new QName("", "fileContent");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: test
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Hello }
     * 
     */
    public Hello createHello() {
        return new Hello();
    }

    /**
     * Create an instance of {@link UploadFile }
     * 
     */
    public UploadFile createUploadFile() {
        return new UploadFile();
    }

    /**
     * Create an instance of {@link HelloResponse }
     * 
     */
    public HelloResponse createHelloResponse() {
        return new HelloResponse();
    }

    /**
     * Create an instance of {@link GetFilePath }
     * 
     */
    public GetFilePath createGetFilePath() {
        return new GetFilePath();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetFilePath }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://test/", name = "getFilePath")
    public JAXBElement<GetFilePath> createGetFilePath(GetFilePath value) {
        return new JAXBElement<GetFilePath>(_GetFilePath_QNAME, GetFilePath.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link HelloResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://test/", name = "helloResponse")
    public JAXBElement<HelloResponse> createHelloResponse(HelloResponse value) {
        return new JAXBElement<HelloResponse>(_HelloResponse_QNAME, HelloResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Hello }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://test/", name = "hello")
    public JAXBElement<Hello> createHello(Hello value) {
        return new JAXBElement<Hello>(_Hello_QNAME, Hello.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UploadFile }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://test/", name = "uploadFile")
    public JAXBElement<UploadFile> createUploadFile(UploadFile value) {
        return new JAXBElement<UploadFile>(_UploadFile_QNAME, UploadFile.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link byte[]}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "fileContent", scope = UploadFile.class)
    public JAXBElement<byte[]> createUploadFileFileContent(byte[] value) {
        return new JAXBElement<byte[]>(_UploadFileFileContent_QNAME, byte[].class, UploadFile.class, ((byte[]) value));
    }

}
