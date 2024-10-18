package util;

import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner; // Import the Scanner class to read text files
import java.util.Set;

//This file reads and interprets the data from a text file into a world object for games.
/*
 * How to create a map data file:
 * 
 * You can name your map by using the convention-
 * World{name: INSERT_NAME_HERE}
 * 
 * Similarly you can create a province by using-
 * Province{name: INSERT_NAME_HERE | adjacent: ADJPROVINCE_1, ADJPROVINCE_2, ADJPROVINCE_N}
 * There can be any number of adjacent provinces. The way the interpreter works is it does mutliple passes, 
 * so you can use a province name before actually initializing that province.
 * 
 * Then, for each province you must include a graphics object that tells the renderer where to put it on the map-
 * Graphics{name: INSERT_NAME_PROVINCE | coords: [X_COORD_1, Y_COORD_1, X_COORD_2, Y_COORD_2, X_COORD_N, Y_COORD_N]}
 * 
 * There must be at least 3 points in the array, there is no upper limit. 
 * IMPORTANT: The name given must match a name of an existing province.
 * 
 * If you don't want to deal with writing out each coord, neither do I! That's why I made a simple program in Example01.java to
 * do it for you! Simply run Example01.java, and it will pull up a beta version of the world map. All you have to do is click anywhere
 * on the screen for each point you want in your shape, then close the window. It will print out something like this in the terminal:
 * private int[] name = {410, 270, 570, 276, 558, 390, 401, 370};
 * You can then copy paste that array into the coords section of a Graphics (replacing {} with []), and you're good to go.
 * 
 * Finally, continents can be initialized as so-
 * Continent{name: INSERT_NAME | provs: PROVINCE_1, PROVINCE_2, PROVINCE_N | bonus: INSERT_INTEGER}
 * 
 */

public class MapReader {

    public static int find(Province[] p, String search){
        int index = -1;
        for(int i = 0; i < p.length; i++){
            if((p[i].getName()).equals(search)){
                index = i;
            }
        }
        return index;
    }

    /**
     * @param filename
     * @return This will read a file and return the world object associated with the data file. 
     * For instructions on how to create a file, read the comments in MapReader.java.
     */
    protected static World readFile(String filename){
        try {
            
            File myObj = new File(filename);
            //First Pass: get total provinces and continents to add, also create world object
            Scanner myReader = new Scanner(myObj);
            int numProvs = 0;
            int numContinents = 0;
            World world = new World("N/A");
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                if(data.length() > 0){
                    if(data.substring(0,8).equals("Province")){
                        numProvs++;
                    }
                    if(data.substring(0,9).equals("Continent")){
                        numContinents++;
                    }
                    if(data.substring(0,5).equals("World")){
                        if(data.indexOf("name: ") > -1){
                            String name = data.substring(data.indexOf("name: ")+6, data.indexOf("}"));
                            name = name.replaceAll(" ", "");
                            //System.out.println(name);
                            world = new World(name);
                        }
                    }
                }
            }
            myReader.close();

            //Second Pass: initialize provinces
            myReader = new Scanner(myObj);
            Province[] provs = new Province[numProvs];
            int index = 0;
            
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                if(data.length() > 0){
                    if(data.substring(0,8).equals("Province")){
                        if(data.indexOf("name: ") > -1){
                            String name = data.substring(data.indexOf("name: ")+6, data.indexOf("|"));
                            name = name.replaceAll(" ", "");
                            //System.out.println(name);
                            provs[index] = new Province(name);
                            index++;
                        }
                    }
                }
            }
            myReader.close();

            //Third pass: add adjacencies
            myReader = new Scanner(myObj);
            
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                if(data.length() > 0){
                    if(data.substring(0,8).equals("Province")){
                        if(data.indexOf("name: ") > -1){
                            String name = data.substring(data.indexOf("name: ")+6, data.indexOf("|"));
                            name = name.replaceAll(" ", "");
                            int provIndex = find(provs, name);
                            if(provIndex > -1 && data.indexOf("adjacent: ") > -1){
                                int split = data.indexOf("adjacent: ");
                                String reading = data.substring(split);
                                reading = reading.substring(9);
                                split = reading.indexOf(",");
                                while(split > -1 && (split < reading.indexOf("|") || split < reading.indexOf("}"))){
                                    String adjName = reading.substring(0,split);
                                    adjName = adjName.replaceAll(" ", "");
                                    int findIndex = find(provs, adjName);
                                    if(findIndex > -1 && findIndex != provIndex){
                                        provs[provIndex].addAdjacent(provs[findIndex]);
                                    }
                                    reading = reading.substring(split+1);
                                    split = reading.indexOf(",");
                                }
                                String adjName = reading.substring(0,reading.length()-1);
                                adjName = adjName.replaceAll(" ", "");
                                int findIndex = find(provs, adjName);
                                if(findIndex > -1 && findIndex != provIndex){
                                    provs[provIndex].addAdjacent(provs[findIndex]);
                                }
                            }
                        }
                        
                    }
                }
            }
            for(Province p: provs){
                //System.out.print(p.getName() + ": ");
                for(Province j: p.getAdjacent()){
                    //System.out.print(j.getName() + ", ");
                }
                //System.out.println(" ");
            }
            myReader.close();

            //Fourth Pass: initialize continents

            myReader = new Scanner(myObj);
            Continent[] continents = new Continent[numContinents];
            int index2 = 0;
            
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                if(data.length() > 0){
                    if(data.substring(0,9).equals("Continent")){
                        if(data.indexOf("name: ") > -1){
                            String name = data.substring(data.indexOf("name: ")+6, data.indexOf("|"));
                            name = name.replaceAll(" ", "");
                            //System.out.println(name);
                            if(data.indexOf("bonus: ") > -1){
                                String checkstr = data.substring(data.indexOf("bonus: "), data.length());
                                int split = checkstr.indexOf("}");
                                if(checkstr.indexOf("|") > -1 && checkstr.indexOf("|") < checkstr.indexOf("}")){
                                    split = checkstr.indexOf("|");
                                }
                                String bonus = checkstr.substring(checkstr.indexOf("bonus: ")+7, split);
                                bonus = bonus.replaceAll(" ", "");
                                //System.out.println(bonus);
                                continents[index2] = new Continent(name, Integer.parseInt(bonus));

                                if(data.indexOf("provs: ") > -1){
                                    int split2 = data.indexOf("provs: ");
                                    String reading = data.substring(split2);
                                    reading = reading.substring(6);
                                    split2 = reading.indexOf(",");
                                    //System.out.println("1:"+reading);
                                    while(split2 > -1 && (split2 < reading.indexOf("|") || split2 < reading.indexOf("}"))){
                                        String provName = reading.substring(0,split2);
                                        provName = provName.replaceAll(" ", "");
                                        int findIndex = find(provs, provName);
                                        //System.out.println("2:"+provName+findIndex);
                                        if(findIndex > -1){
                                            continents[index2].addProvince(provs[findIndex]);
                                        }
                                        reading = reading.substring(split2+1);
                                        split2 = reading.indexOf(",");
                                    }
                                    split2 = reading.indexOf("}");
                                    if(reading.indexOf("|") > -1 && reading.indexOf("|") < reading.indexOf("}")){
                                        split2 = reading.indexOf("|");
                                    }
                                    String provName = reading.substring(0,split2-1);
                                    provName = provName.replaceAll(" ", "");
                                    int findIndex = find(provs, provName);
                                    //System.out.println(provName+findIndex);
                                    if(findIndex > -1){
                                        continents[index2].addProvince(provs[findIndex]);
                                    }
                                }
                                index2++;
                            }
                        }
                        
                        
                    }
                }
            }
            myReader.close();

            for(Continent c: continents){
                //System.out.println(c);
            }

            //Fifth pass: initialize graphics

            myReader = new Scanner(myObj);
            GraphicProvince[] gps = new GraphicProvince[numProvs];
            int graphicsIndex = 0;
            
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                if(data.length() > 0){
                    if(data.substring(0,8).equals("Graphics")){
                        if(data.indexOf("name: ") > -1){
                            String name = data.substring(data.indexOf("name: ")+6, data.indexOf("|"));
                            name = name.replaceAll(" ", "");
                            int provIndex = find(provs, name);
                            ArrayList<Integer> array = new ArrayList<>();
                            if(provIndex > -1 && data.indexOf("coords: ") > -1){
                                int split = data.indexOf("coords: ");
                                String reading = data.substring(split);
                                reading = reading.substring(9);
                                split = reading.indexOf(",");
                                while(split > -1 && (split < reading.indexOf("|") || split < reading.indexOf("}"))){
                                    String integer = reading.substring(0,split);
                                    integer = integer.replaceAll(" ", "");
                                    array.add(Integer.valueOf(integer));
                                    reading = reading.substring(split+1);
                                    split = reading.indexOf(",");
                                }
                                reading = reading.replaceAll(" ", "");
                                String integer = reading.substring(0,reading.length()-2);
                                integer = integer.replaceAll(" ", "");
                                array.add(Integer.valueOf(integer));
                            }
                            gps[graphicsIndex] = new GraphicProvince(provs[provIndex], array.stream().mapToInt(Integer::intValue).toArray());
                            graphicsIndex++;
                        }
                        
                    }
                }
            }

            myReader.close();

            for(GraphicProvince gp: gps){
                //System.out.print(gp.getProv() + ": ");
                for(int j: gp.getDimensions()){
                    //System.out.print(j + ", ");
                }
                //System.out.println(" ");
            }

            //Wrap up all of the data into world object and graphprovs

            HashSet<Continent> conts = new HashSet<Continent>();
            for(Continent c: continents){
                conts.add(c);
            }
            world.setContinents(conts);
            HashSet<Province> pros = new HashSet<Province>();
            for(Province p: provs){
                pros.add(p);
            }
            world.setProvinces(pros);

            world.setGraphicProvinces(gps);
            
            return world;

          } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
          }


        return null;
    }

}