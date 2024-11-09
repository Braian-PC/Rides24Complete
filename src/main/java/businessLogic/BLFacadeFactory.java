package businessLogic;

import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import configuration.ConfigXML;
import data_access.DataAccess;

public class BLFacadeFactory {

    public static BLFacade createBLFacade() {
        ConfigXML c = ConfigXML.getInstance();
        
        if (c.isBusinessLogicLocal()) {
            // Lógica local
            DataAccess da= new DataAccess(c.isDatabaseInitialized());//c.getDataBaseOpenMode().equals("initialize")
            return new BLFacadeImplementation(da);
        } else {
            // Lógica remota
            try {
                String serviceName = "http://" + c.getBusinessLogicNode() + ":" + c.getBusinessLogicPort() + "/ws/"
                        + c.getBusinessLogicName() + "?wsdl";
                URL url = new URL(serviceName);
                QName qname = new QName("http://businessLogic/", "BLFacadeImplementationService");
                Service service = Service.create(url, qname);
                return service.getPort(BLFacade.class);
            } catch (Exception e) {
                System.out.println("Error creating remote BLFacade: " + e.toString());
                return null;
            }
        }
    }
}

