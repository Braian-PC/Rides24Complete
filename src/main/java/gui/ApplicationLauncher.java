package gui;

import java.net.URL;
import java.util.Locale;

import javax.swing.UIManager;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import configuration.ConfigXML;
import data_access.DataAccess;
import businessLogic.BLFacade;
import businessLogic.BLFacadeFactory;
import businessLogic.BLFacadeImplementation;

public class ApplicationLauncher {

    public static void main(String[] args) {
        ConfigXML c = ConfigXML.getInstance();
        System.out.println(c.getLocale());
        Locale.setDefault(new Locale(c.getLocale()));
        System.out.println("Locale: " + Locale.getDefault());

        try {
            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");

            // Usamos el Factory Method para crear la instancia adecuada de BLFacade
            BLFacade appFacadeInterface = BLFacadeFactory.createBLFacade();

            if (appFacadeInterface != null) {
                MainGUI.setBussinessLogic(appFacadeInterface);
                MainGUI a = new MainGUI();
                a.setVisible(true);
            } else {
                System.out.println("Error: No se pudo crear la instancia de BLFacade.");
            }

        } catch (Exception e) {
            System.out.println("Error in ApplicationLauncher: " + e.toString());
        }
    }
}


/**
 * package gui;

import java.util.Locale;
import javax.swing.UIManager;
import configuration.ConfigXML;
import businessLogic.BLFacade;
import businessLogic.BLFactory;

public class ApplicationLauncher {

    public static void main(String[] args) {

        ConfigXML config = ConfigXML.getInstance();
        System.out.println(config.getLocale());
        Locale.setDefault(new Locale(config.getLocale()));
        System.out.println("Locale: " + Locale.getDefault());

        try {
            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");

            // Utilizar la factoría para obtener la implementación de BLFacade
            BLFactory factory = new BLFactory();
            BLFacade appFacadeInterface = factory.createBLFacade();
            
            if (appFacadeInterface == null) {
                throw new Exception("Error: No se pudo crear la instancia de BLFacade.");
            }

            MainGUI.setBussinessLogic(appFacadeInterface);
            MainGUI mainGui = new MainGUI();
            mainGui.setVisible(true);

        } catch (Exception e) {
            System.out.println("Error in ApplicationLauncher: " + e);
        }
    }
}

 */
