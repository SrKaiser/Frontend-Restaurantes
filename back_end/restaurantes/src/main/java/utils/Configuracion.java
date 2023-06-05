package utils;

import java.io.IOException;
import java.util.Properties;

import tests.PruebasBasicas;

/*
 * Como tenemos PropertiesReader, 
 * creamos la clase Configuracion para 
 * utilizarla en lugar de cargar las propiedades directamente.
 */

public class Configuracion {
    public static Properties cargarConfiguracion() {
        Properties properties = new Properties();
        try {
            PropertiesReader propertiesReader;
            if (PruebasBasicas.isTestEnvironment) {
            	propertiesReader = new PropertiesReader("test.properties");
            } else {
                propertiesReader = new PropertiesReader("aplicacion.properties");
            }
            properties = propertiesReader.getProperties();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }
}
