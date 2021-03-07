/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sergiboadas.sensors.in.formula1.util;

import com.sergiboadas.sensors.in.formula1.model.SensorData;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;

/**
 *
 * @author Sergi
 */
public class SensorFileWriter {

    private final String outputFileName = "AverageSpeed_";
    private int outputFilesGeneraded = 0;

    public SensorFileWriter() {
    }
    
    /**
     * Print the list of sensors data to a csv file with the output filename concatenated
     * with the number of output files generated
     *
     * @param dataToBePrinted data to be printed
     */
    public void printOutputFileCsv(List<SensorData> dataToBePrinted) throws FileNotFoundException {
        String fileName = this.outputFileName + this.outputFilesGeneraded + Extension.CSV.getExtension();
        try (PrintWriter writer = new PrintWriter(new File(fileName))) {
            dataToBePrinted
                    .forEach(data -> writer.println(data.getTime() + "," + data.getSpeed()));
        }
        this.outputFilesGeneraded++;
    }
}
