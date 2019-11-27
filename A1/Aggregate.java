
/**
 * Name: Sharon Umute
 * Student Number: V00852291
 *
 */
import java.io.*;
import java.util.*;
import java.lang.Object;
import java.util.regex.*;

public class Aggregate{

    public static void main(String[] args) {
  		if (args.length > 0) {
            //Set basic variables
            String command = args[0];
            String aggColumn = args[1];
            String filename = args[2];

            //Check if document is of the right format
            if(!filename.substring(filename.lastIndexOf(".") + 1, filename.length()).equalsIgnoreCase("csv")){
                System.err.printf("Error: wrong document format.\n");
                System.exit(1);
            }
            //Checks if command entered is valid
            List<String> validCommands=Arrays.asList("count", "sum", "avg");
            if(!validCommands.contains(command)){
                System.err.printf("Invalid command.\n");
                System.exit(1);
            }

            //Create list of grouping columns
            List<String> groupColumns = new ArrayList<String>();
            for (int i=3; i< args.length; i++){
                groupColumns.add(args[i]);
            }

            //check if any grouping column and aggregation column equal
            if(groupColumns.contains(aggColumn)){
                System.err.printf("Error: Aggregation column can not equal a grouping column.\n");
                System.exit(1);
            }

            //create final list of required columns
            groupColumns.add(aggColumn);

            //read filed into a BufferedReader
            BufferedReader br = null;
            try{
            	br = new BufferedReader(new FileReader(filename));
            }catch( IOException e ){
            	System.err.printf("Error: Unable to open file %s\n",filename);
            	System.exit(1);
            }

            //Create a list of the indexes of each required column
            List<Integer> indexes=new ArrayList<Integer>();

            //Split the input text provided into rows and format the rows to have only the required columns
            List<String> rows  = new ArrayList<String>();
            String row="";
            String header="";
            int rowCount=0;

            double errorCheck=0;//To check for number format errors
            try{
              while((row=br.readLine()) != null){
                String tempRow="";
                rowCount++;
                String[] splitRow = row.split(",");
                if (rowCount==1){
                    for(String title: groupColumns){
                      if(Arrays.asList(splitRow).contains(title)){
                          indexes.add(Arrays.asList(splitRow).indexOf(title));
                          header+=title +",";
                      }else{
                        System.err.printf("Error: Something is wrong with one of the columns specified.\n");
                        System.exit(1);
                      }
                    }
                }else{
                  for(int index: indexes){
                      tempRow+=splitRow[index] + ",";
                  }
                  tempRow=tempRow.substring(0, tempRow.length()-1);
                  try{
                      errorCheck=Double.valueOf(tempRow.substring(tempRow.lastIndexOf(",")+1, tempRow.length()));
                  }catch(NumberFormatException e){
                      System.err.printf("Error: Invalid aggregation column. Mathematical operations cannot be performed on non numerical column.\n");
                      System.exit(1);
                  }
                  rows.add(tempRow);
                }
              }
            }catch(IOException ex){}


            rows.add("end,");//To mark end of list
            header=header.substring(0, header.length()-1);//to erase any extra commas
            header=header.replace(aggColumn, String.format("%s(%s)", command, aggColumn));//Modify aggregation column header
            Collections.sort(rows);

            //Perform agggregation and print
            //Performs mathmatical operations on matching rows.
            int count=0;
            double sum=0;
            double avg=0;
            System.out.println(header);//Print header first
            for(int i=0; i<rows.size()-1; i++){
                count++;
                sum+=Double.valueOf(rows.get(i).substring(rows.get(i).lastIndexOf(",")+1, rows.get(i).length()));
                avg=sum/count;
                if(!(rows.get(i).substring(0, rows.get(i).lastIndexOf(",")).equals(rows.get(i+1).substring(0, rows.get(i+1).lastIndexOf(","))))){
                  if (command.equals("count")){
                    rows.set(i, rows.get(i).replace(rows.get(i).substring(rows.get(i).lastIndexOf(",")+1, rows.get(i).length()), String.valueOf(count)));
                  }else if(command.equals("sum")){
                    rows.set(i, rows.get(i).replace(rows.get(i).substring(rows.get(i).lastIndexOf(",")+1, rows.get(i).length()), String.format("%.2f", sum)));
                  }else{
                    rows.set(i, rows.get(i).replace(rows.get(i).substring(rows.get(i).lastIndexOf(",")+1, rows.get(i).length()), String.format("%.2f", avg)));
                  }
                  count=0;
                  sum=0;
                  avg=0;
                  System.out.println(rows.get(i));
                }
              }
		} else {
        //If no arguments specified
        System.out.printf("Invalid Input.\n");
		}
	}
}
