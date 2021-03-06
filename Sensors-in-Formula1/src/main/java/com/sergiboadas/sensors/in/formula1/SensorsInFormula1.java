/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sergiboadas.sensors.in.formula1;

import com.sergiboadas.sensors.in.formula1.controller.SensorsController;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 *
 * @author Sergi
 */
public class SensorsInFormula1 {
    
    private static List<String> getFilesPathsOfTheFolder(String resourcesPath) {
        List<String> listPaths = new LinkedList<>();
        File folder = new File(resourcesPath);
        File[] regularFiles = folder.listFiles(f -> f.isFile() && f.canRead() && f.getName().contains("."));
        listPaths = Arrays.asList(regularFiles)
                .stream()
                .map(File::getAbsolutePath)
                .collect(Collectors.toList());
        return listPaths;
    }

    public static void main(String[] args) throws InterruptedException, FileNotFoundException {
        System.out.println("------ INITIALIZING THE PROGRAM... ------");
        
        // Read all files of resources folder
        String resourcesPath = "resources/";
        List<String> filesPaths = getFilesPathsOfTheFolder(resourcesPath);
    
        // Create the sensors controller
        SensorsController sensorsController = new SensorsController(filesPaths);
        
        // Calling to the main method
        sensorsController.readIndefinitelySensorsFiles();
        
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the word \"FINISH\" and then press \"Enter\" to stop the execution");
        String userInput;
        boolean stopExecution = false;
        while (!stopExecution) {
            userInput = scanner.nextLine();
            stopExecution = ("FINISH".equals(userInput));
        }
        
        // Stop the program
        System.out.println("------ STOPPING THE PROGRAM... ------");
        sensorsController.stopReadingSensorsFiles();
    }
}
 