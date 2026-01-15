import java.util.Scanner; // Import the Scanner class to read text files
// Import the File class
// Import this class to handle errors
import java.io.*;

public class CSVReader{

    private File CSvFile;
    public int dataAmount;
    public String data[][];
    public String variable[];
    public String target[];


    public CSVReader(){
     
      
}

public void extractFile(){

  try {
    this.CSvFile = new File("hepatitis.tsv");

    if (!CSvFile.exists()) {
        throw new FileNotFoundException("File not found: " + CSvFile.getAbsolutePath());
    }

    //System.out.println("File found. Length: " + CSvFile.length() + " bytes");

    FileInputStream fileInputStream = new FileInputStream(this.CSvFile);
    InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

    // Example: Read first line
    int lineCount = 0;
    String firstLine = bufferedReader.readLine();
    String[] namesL = firstLine.split("\t");
    String line;
    while ((line = bufferedReader.readLine()) != null) { 
          lineCount += 1; 
    }
    dataAmount = lineCount;
    //System.out.println(lineCount);
    data = new String[lineCount][namesL.length-1];
    variable = new String[namesL.length-1];
    target = new String[lineCount]; 


   

    bufferedReader.close();

    fileInputStream = new FileInputStream(this.CSvFile);
    inputStreamReader = new InputStreamReader(fileInputStream);
    bufferedReader = new BufferedReader(inputStreamReader);
    firstLine = bufferedReader.readLine();


      String[] names = firstLine.split("\t"); 
      for(int j = 0; j < names.length-1;j++){
      variable[j] = names[j];
      }
    
    int i = 0;
    while ((line = bufferedReader.readLine()) != null) {
      String[] w = line.split("\t"); 
      for(int j = 0; j < w.length-1;j++){
      data[i][j] = w[j];
      }
      target[i] = w[w.length-1];

      //System.out.println("1989: " + data[i] + " 1990: "+ variable[i] + " target: "+ target[i]);
      i++;
    }


} catch (FileNotFoundException e) {
    System.err.println("Error: " + e.getMessage());
} catch (IOException e) {
    System.err.println("Error reading the file: " + e.getMessage());
}

}


}