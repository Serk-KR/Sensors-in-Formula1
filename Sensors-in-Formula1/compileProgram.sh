#!/bin/sh

###########################
## Shell script which remove tha target directory and then compile the program. Once has finished, put the .jar file at the same directory where it is executed
###########################


rm -rf target/				# Delete the target folder
mvn clean compile assembly:single	# Compile the program



rm -rf SENSORS-IN-FORMULA1/		# Delete the current folder where the jar and resources will be 
mkdir SENSORS-IN-FORMULA1/		# Create the folder again

cp -r resources/ SENSORS-IN-FORMULA1/	# Copy the resources folder where there are the sensors files to the new folder created
mv target/*.jar SENSORS-IN-FORMULA1/ 	# Moved the .jar to the new folder 

#rm -rf target/ 	# Delete the target folder


