<b>Required Softwares:</b>
  1. Java 1.8 or later
  2. Maven 3.5.2 or later
  3. git 2.16 or later


<b>Project Setup:</b>
  1. Clone project https://github.com/ariyaram/barcode_project.git
  2. got to barcode_project folder
  3. run mvn package
  4. execute java -cp <jar_path>/<jar_name>.jar <className> <file_path>
  	ex: java -cp target/barcode-1.0-SNAPSHOT-jar-with-dependencies.jar com.sandeep.app.barcode.BarCodeApplication C:\Users\ramarao.ariyaram\Desktop\Barcode_Input.csv


<b>Sample_File:</b><font color="red"> (Barcode_Input.csv)</font><br/>

barcode_length, no_of_barcodes,Min GC %,Max GC %,Min TM %,Max TM%, Similarity,Complementarity,parsec species type,userName<br/>
27,200,,,,,,,Homo_sapiens,Sandeep

<b>Output Dir:</b>
	<user.home>/Generated_Barcodes/ <br/>
	{color:red}Ex:{color}</font> Windows: C:\Users\Sandeep\Generated_Barcodes <br/>
	    Linux:  /home/Sandeep/Generated_Barcodes
 
<b>Parsec_Species_Types:</b><br/>
	  	Danio_rerio<br/>
	  	Rattus_norvegicus<br/>
	 	 Homo_sapiens<br/>
	 	 Gallus_gallus<br/>
	  	 Mus_musculus<br/>
	 	 Drosophila_melanogaster<br/>
	 	 Saccharomyces_cerevisiae<br/>
	 	 Caenorhabditis_elegans
	
