/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sergiboadas.sensors.in.formula1.util;

/**
 *
 * @author Sergi
 */
public enum Extension {
    CSV(".csv"),
    XML(".xml"),
    NOT_SUPPORTED(null);
    
    private final String extension;

    private Extension(String extension) {
        this.extension = extension;
    }

    public static Extension getCSV() {
        return CSV;
    }

    public static Extension getXML() {
        return XML;
    }

    public String getExtension() {
        return extension;
    }

    public static Extension fromPropertyName(String type) {
        for (Extension currentType : Extension.values()) {
            if (currentType.getExtension().equals(type)) {
                return currentType;
            }
        }
        return NOT_SUPPORTED;
    }
}
