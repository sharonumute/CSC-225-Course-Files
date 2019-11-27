
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

    /**
     * The following method was the first response to a question asked on stackoverflow.
     * Code copied from http://stackoverflow.com/questions/44009818/itemgetteri-for-java (May 16th, 2017)
     * The method gets each element of a particular index, from every sublist in the list passed.
     * In a sense, it returns a specified row of a table
     *
     *
     *
     * Method is parameterized in the list-type, thus achieving maximal type checking. If the
     * inner lists are of different types, the inner lists must be of type List<Object>,
     * otherwise would not work. Type-checking is out of the window at that point.
     *
     * @param lists the list of lists, non-null and not containing null's.
     * @param i     the index to pick in each list, must be >= 0.
     * @param <T>   generic parameter of inner lists, see above.
     * @return      a List<T>, containing the picked elements.
     */

    public static <T> List<T> getIthOfEach(List<List<T>> lists, int i) {
        List<T> result = new ArrayList<T>();

        for(List<T> list : lists) {
            try {
                result.add(list.get(i)); // get ith element, add it to the result-list
            // if some list does not have an ith element, an IndexOutOfBoundException is
            // thrown. Catch it and continue.
            } catch (IndexOutOfBoundsException e) { }
        }
        return (result);
    }

    //This method obtains the coloumns necessary for the aggregation
    public static List<List<String>> RequiredColumns(List<List<String>> table, String aColumn, List<String> gColumns, List<String> columnTitles){
        List<List<String>> reqColumns  = new ArrayList<>();
        List<Integer> gColumnsIndexes  = new ArrayList<>();

        //Gets the index of aggregation column and group columns with respect
        //To the original table
        for(String gColumn : gColumns){
            gColumnsIndexes.add(columnTitles.indexOf(gColumn));
        }
        gColumnsIndexes.add( columnTitles.indexOf(aColumn));


        //Creates new table out of necessary columns from old table
        for (int index: gColumnsIndexes){
            reqColumns.add(table.get(index));
        }

        return reqColumns;
    }

    //This method performs the aggregation
    public static List<List<String>> Agg(List<List<String>> rtable, int numbOfRows, List<String> cColumns, String command, List<String> gcolumnTitles){
        List<List<String>> aggList  = new ArrayList<>(); //Final aggregated table

        //Initializes Titles in the new aggregated table
        for(String header: gcolumnTitles){
            List<String> title  = new ArrayList<>();
            title.add(header);
            aggList.add(title);
        }
        for(String ctitle: cColumns){
            List<String> title  = new ArrayList<>();
            title.add(ctitle);
            aggList.add(title);
        }

        //Aggregates each row of the table of required columns
        for(int i=1; i<numbOfRows; i++){
            double aggSub=0;
            List<String> newRow  = new ArrayList<>();
            List<String> group = new ArrayList<>();
            List<List<String>> existGroupCol = new ArrayList<>();
            List<List<String>> existGroupRow = new ArrayList<>();

            //Get's next row of the table of required columns
            newRow = new ArrayList<>(getIthOfEach(rtable, i));

            //Split Row by group columns and aggregate columns
            //Throw error for non numerical aggregation columns
            try{
                aggSub=Double.valueOf(newRow.get(newRow.size()-1));
            }catch(NumberFormatException e){
                System.err.printf("Error: Invalid aggregation column. Mathematical operations cannot be performed on non numerical column.\n");
                System.exit(1);
            }
            group=newRow.subList(0, (newRow.size()-1));

            //Get's list of existing elements of the group name, specified in the aggregate table, by column
            existGroupCol=aggList.subList(0, aggList.size()-3);

            //Get's list of existing elements of the group name, specified in the aggregate table, by row
            for(int k=1; k< aggList.get(0).size(); k++){
                List<String> temp = getIthOfEach(existGroupCol, k);
                existGroupRow.add(temp);
            }

            //Check if entry already exists in aggregated table, if so;
            if (existGroupRow.contains(group)){
                //Index of entry row.
                int index=existGroupRow.indexOf(group)+1;

                //Increment current count value of entry
                int currCount= Integer.valueOf(aggList.get(aggList.size()-3).get(index));
                currCount++;
                aggList.get(aggList.size()-3).set(index, String.valueOf(currCount));

                //Updates current sum of entry
                Double currSum= Double.valueOf(aggList.get(aggList.size()-2).get(index));
                currSum+= aggSub;
                aggList.get(aggList.size()-2).set(index, String.format("%.2f", currSum));

                //Updates current Average of entry
                //Formats Average value to 2 decimal places
                double currAvg= Double.valueOf(aggList.get(aggList.size()-1).get(index));
                currAvg= ((double)currSum)/ ((double)currCount);
                aggList.get(aggList.size()-1).set(index,  String.format("%.2f", currAvg));
            }else{
                //If the entry does not exist, creating new entry using it's parts.

                //First add the elements of the entry into the aggregate table, column by column
                for(int k=0; k<group.size(); k++){
                    aggList.get(k).add(group.get(k));
                }

                //Then enter new values for the entry's count, sum and average'
                aggList.get(aggList.size()-3).add("1");
                aggList.get(aggList.size()-2).add(String.valueOf (aggSub));
                aggList.get(aggList.size()-1).add(String.valueOf (aggSub));
            }

            //At the end of this loop, the final project has been fully aggregated
        }

        //Now all that's left is to filter out non specified commands
        if(command.equals("count")){
            aggList.remove(aggList.size()-2);
            aggList.remove(aggList.size()-1);
        }else if (command.equals("sum")){
            aggList.remove(aggList.size()-3);
            aggList.remove(aggList.size()-1);
        }else if (command.equals("avg")){
            aggList.remove(aggList.size()-3);
            aggList.remove(aggList.size()-2);
        }

        return aggList;
    }


    public static void main(String[] args) {
  		if (args.length > 0) {
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

            //read filed into a BufferedReader
            BufferedReader br = null;
            try{
            	br = new BufferedReader(new FileReader(filename));
            }catch( IOException e ){
            	System.err.printf("Error: Unable to open file %s\n",filename);
            	System.exit(1);
            }

            //Split the input text provided into columns
            List<List<String>> columnList  = new ArrayList<>();
            String row="";
            try{
              while((row=br.readLine()) != null){
                String[] splitRow = row.split(",");
                for(int j=0; j<splitRow.length; j++){
                    if(columnList.size()<=j){
                        List<String> temp  = new ArrayList<>();
                        columnList.add(temp);
                    }
                    columnList.get(j).add(splitRow[j]);
                }
              }
            }catch(IOException ex){}


            //Get the List of all Titles in table
            List<String> columnTitles = new ArrayList<>();
            columnTitles= new ArrayList<>(getIthOfEach(columnList, 0));

            //Check  if aggregation or group columns specified are valid
            if(!(columnTitles.containsAll(groupColumns) && columnTitles.contains(aggColumn))){
                System.err.printf("Error: Something is wrong with one of the columns specified.\n");
                System.exit(1);
            }

            //Properyly set the title of the aggregate column in the table
            List<String> cCol  = Arrays.asList(String.format("count(%s)", aggColumn), String.format("sum(%s)", aggColumn), String.format("avg(%s)", aggColumn));

            int nor=columnList.get(0).size(); //Number of Rows

            //Begin to process the data
            List<List<String>> reqColumns  = new ArrayList<>(RequiredColumns(columnList, aggColumn, groupColumns, columnTitles));
            List<List<String>> outputList  = new ArrayList<>(Agg(reqColumns, nor, cCol, command, groupColumns));

            //Convert output list to CSV
            String outputCSV="";
            for(int i=0; i<outputList.get(0).size(); i++){
                for(List<String> column: outputList){
                    outputCSV+=column.get(i);
                    if(outputList.indexOf(column)!= outputList.size()-1){
                        outputCSV+=",";
                    }
                }
                outputCSV+="\n";
            }
            System.out.println(outputCSV);

		} else {
            //If no arguments specified
            System.out.printf("Invalid Input.\n");
		}
	}
}
