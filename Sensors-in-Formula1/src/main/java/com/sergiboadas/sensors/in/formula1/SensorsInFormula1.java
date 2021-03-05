/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sergiboadas.sensors.in.formula1;

import java.io.File;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author Sergi
 */
public class SensorsInFormula1 {

    private static List<String> getFilesNamesOfTheFolder(String resourcesPath) {
        List<String> listNames = new LinkedList<>();
        File folder = new File(resourcesPath);
        File[] regularFiles = folder.listFiles(f -> f.isFile() && f.canRead() && f.getName().contains("."));
        listNames = Arrays.asList(regularFiles)
                .stream()
                .map(File::getName)
                .collect(Collectors.toList());
        return listNames;
    }

    public static void main(String[] args) {
        // Read all files of resources folder
        String resourcesPath = "./resources/";
        List<String> filesNames = getFilesNamesOfTheFolder(resourcesPath);
         filesNames.forEach(name -> System.out.println("Name: " + name));
    }
    
}
