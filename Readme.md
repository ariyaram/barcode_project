Required Softwares:
  1. Java 1.8 or later
  2. Maven 3.5.2 or later
  3. git 2.16 or later


Project Setup:
  1. Clone project https://github.com/ariyaram/barcode_project.git
  2. got to barcode_project folder
  3. run mvn package
  4. execute java -cp <jar_path>/<jar_name>.jar <className> <file_path>
  	ex: java -cp target/barcode-1.0-SNAPSHOT-jar-with-dependencies.jar com.sandeep.app.barcode.BarCodeApplication C:\Users\ramarao.ariyaram\Desktop\Barcode_Input.csv


Sample_File:

barcode_length, no_of_barcodes,Min GC %,Max GC %,Min TM %,Max TM%, Similarity,Complementarity,parsec species type,userName
27,200,,,,,,,Homo_sapiens,Sandeep

Output Dir:
	<user.home>/Generated_Barcodes/ <br/>
	Ex: Windows: C:\Users\Sandeep\Generated_Barcodes <br/>
	    Linux:  /home/Sandeep/Generated_Barcodes
 
Parsec_Species_Types:<br/>
	  Danio_rerio<br/>
	  Rattus_norvegicus<br/>
	  Homo_sapiens<br/>
	  Gallus_gallus<br/>
	  Mus_musculus<br/>
	  Drosophila_melanogaster<br/>
	  Saccharomyces_cerevisiae<br/>
	  Caenorhabditis_elegans
	
