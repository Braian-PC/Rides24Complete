package businessLogic;

public class BLFactory {
	public BLFacade getBusinessLogicFactory(boolean b) {
		return new BLFacadeImplementation();
		
		/*Provisional, pruébalo
		 * 
		 * package businessLogic;

import configuration.ConfigXML;
import data_access.DataAccess;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import java.net.URL;

public class BLFactory implements BLFacadeFactory {

    @Override
    public BLFacade createBLFacade() {
        ConfigXML config = ConfigXML.getInstance();
        
        if (config.isBusinessLogicLocal()) {
            // Crear y devolver la implementación local
            DataAccess da = new DataAccess(config.getDataBaseOpenMode().equals("initialize"));
            return new BLFacadeImplementation(da);
        } else {
            // Crear y devolver la implementación remota
            try {
                String serviceName = "http://" + config.getBusinessLogicNode() + ":" 
                                     + config.getBusinessLogicPort() + "/ws/" 
                                     + config.getBusinessLogicName() + "?wsdl";

                URL url = new URL(serviceName);
                QName qname = new QName("http://businessLogic/", "BLFacadeImplementationService");
                Service service = Service.create(url, qname);
                
                return service.getPort(BLFacade.class);
            } catch (Exception e) {
                System.out.println("Error creating remote BLFacade: " + e);
                return null; // O lanzar una excepción
            }
        }
    }
}

		 */
	}
}
