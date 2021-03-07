/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sergiboadas.sensors.in.formula1;

import com.sergiboadas.sensors.in.formula1.controller.SensorsController;
import com.sergiboadas.sensors.in.formula1.exceptions.SensorFileNotFoundException;
import com.sergiboadas.sensors.in.formula1.util.Extension;
import java.io.File;
import java.io.FileNotFoundException;
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

    /**
     * Returns a list of strings which represents the path of all the regular files
     * with a name stared by "sensor" and with a csv extension
     *
     * @param resourcesPath the location of the resources folder
     * @return path list of the good files
     */
    private static List<String> getFilesPathsOfTheFolder(String resourcesPath) {
        List<String> listPaths = new LinkedList<>();
        File folder = new File(resourcesPath);
        File[] regularFiles = folder.listFiles(f -> f.isFile() && f.canRead() && f.getName().startsWith("sensor") && f.getName().contains(Extension.CSV.getExtension()));
        listPaths = Arrays.asList(regularFiles)
                .stream()
                .map(File::getAbsolutePath)
                .collect(Collectors.toList());
        return listPaths;
    }

    public static void main(String[] args) throws InterruptedException, FileNotFoundException, SensorFileNotFoundException {
        System.out.println("------ INITIALIZING THE PROGRAM... ------");

        // Read all files of resources folder
        String resourcesPath = "resources/";
        List<String> filesPaths = getFilesPathsOfTheFolder(resourcesPath);
        if (filesPaths.isEmpty()) {
            throw new SensorFileNotFoundException("There aren't any regular file with extension");
        }

        // Create the sensors controller
        SensorsController sensorsController = new SensorsController(filesPaths);

        // Calling to the main method
        sensorsController.readIndefinitelySensorsFiles();

        // Interaction with the user in order to stop the main execution
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
