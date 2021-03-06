# Sensors-in-Formula1

 We have access to a system that allows to record the speed of a F1 car. The measurements are collected by two high-precision sensors that sample the speed every second. Each sensor stores their measurements in its own separate CSV file. This program reads and processes this data (in parallel) to output a summary of it.
 The sensors files with the data for testing are at resources folder. You can create some other sensors files but they must begin with **sensor** and must have a csv extension like **.csv**.


## Description of the sensor system 📖

**1 -** The CSV files generated by the sensors contain, for each line, the following information, delimited by commas:
* **1. Field index** - The timestamp of the sample, represented as the number of seconds that have elapsed since 00:00:00 Thursday 1 January 1970
* **2. Field description** - A fractional number representing the speed of the car in km/h 

**2 -** The program will run while the sensors are actively collecting data. Thus, in any moment, new measurements can appear at the end of each file. Keep in mind, however, that the CSV files are append-only, so the pre-existing content will never be modified. 

**3 -** Due to clock readjustments in the sensor controller, multiple samples can appear for the same timestamp in any of the two sensors. Also, take into account that both sensors can contain information for different sets of timestamp


## Program files 🛠️

* **SensorsInFormula1.java (Main class)** - Contain the logic of the program
* **SensorDataAggregator.java** - Class that contains a queue where the information of the sensors (files) that are read in parallel is stored
* **SensorController.java** - Class which controlls everything related with the sensors. The beggining and finish fo reading sensors files and where is decided if create a csv with the results or not
* **SensorDataFormatException.java** - Exception related with the format data of the sensors files
* **SensorFilNotFoundException.java** - Exception related with a non exsisting path of the sensors files
* **SensorData.java** - Model of sensors data where the time and the speed is stored
* **Extension.java** - Enum which represents the extension files that the program could admit (if the program is continued in a future)
* **SensorFileReader.java** - Class where is implemented the method of read a file indefenedly until the user finish the executation of the program and where is passed the read lines of the file to the common queue of the SensorDataAggregator class
* **SensorFileWriter.java** - Class which creates a new csv with all the sensors results


## Program developer ✒️

* **Sergi Boadas Vilagran** - *Code and documentation* - [Sergi's github profile](https://github.com/Serk-KR)
 
## How to run the code 📄
Using an IDE like Netbeans and executing it. If you add some new data in some sensors files located in resources folder, then the program will create a new output file of the results

## Future work 

First of all, improve the code, make it more generic and finally make that work using the jar file.
For executing the jar file, you have to download or clone this repository and then execute the script "compileProgram". Then will appear the jar at a new folder called SENSORS-IN-FORMULA1. Once it appears, go inside it and then you have to execute using the following sentence:
 * **java -jar Sensors-in-Formula1-1.0-jar-with-dependencies.jar**
(Executing the program using the jar doesn't work as expected because never detect that the sensors files changes)

**So right now, in order to see that the code produces the expected output, you need t run it using an IDE like Netbeans**
