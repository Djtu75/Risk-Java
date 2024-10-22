package util;

public class ReadList {
    public static void main(String[] args){
        String asv = "Adair County, Stilwell\r\n" + //
                        "Alfalfa County, Cherokee\r\n" + //
                        "Atoka County, Atoka\r\n" + //
                        "Beaver County, Beaver\r\n" + //
                        "Beckham County, Sayre\r\n" + //
                        "Blaine County, Watonga\r\n" + //
                        "Bryan County, Durant\r\n" + //
                        "Caddo County, Anadarko\r\n" + //
                        "Canadian County, El Reno\r\n" + //
                        "Carter County, Ardmore\r\n" + //
                        "Cherokee County, Tahlequah\r\n" + //
                        "Choctaw County, Hugo\r\n" + //
                        "Cimarron County, Boise City\r\n" + //
                        "Cleveland County, Norman\r\n" + //
                        "Coal County, Coalgate\r\n" + //
                        "Comanche County, Lawton\r\n" + //
                        "Cotton County, Walters\r\n" + //
                        "Craig County, Vinita\r\n" + //
                        "Creek County, Sapulpa\r\n" + //
                        "Custer County, Arapaho\r\n" + //
                        "Delaware County, Jay\r\n" + //
                        "Dewey County, Taloga\r\n" + //
                        "Ellis County, Arnett\r\n" + //
                        "Garfield County, Enid\r\n" + //
                        "Garvin County, Pauls Valley\r\n" + //
                        "Grady County, Chickasha\r\n" + //
                        "Grant County, Medford\r\n" + //
                        "Greer County, Mangum\r\n" + //
                        "Harmon County, Hollis\r\n" + //
                        "Harper County, Buffalo\r\n" + //
                        "Haskell County, Stigler\r\n" + //
                        "Hughes County, Holdenville\r\n" + //
                        "Jackson County, Altus\r\n" + //
                        "Jefferson County, Waurika\r\n" + //
                        "Johnston County, Tishomingo\r\n" + //
                        "Kay County, Newkirk\r\n" + //
                        "Kingfisher County, Kingfisher\r\n" + //
                        "Kiowa County, Hobart\r\n" + //
                        "Latimer County, Wilburton\r\n" + //
                        "Le Flore County, Poteau\r\n" + //
                        "Lincoln County, Chandler\r\n" + //
                        "Logan County, Guthrie\r\n" + //
                        "Love County, Marietta\r\n" + //
                        "Major County, Fairview\r\n" + //
                        "Marshall County, Madill\r\n" + //
                        "Mayes County, Pryor\r\n" + //
                        "McClain County, Purcell\r\n" + //
                        "McCurtain County, Idabel\r\n" + //
                        "McIntosh County, Eufaula\r\n" + //
                        "Murray County, Sulphur\r\n" + //
                        "Muskogee County, Muskogee\r\n" + //
                        "Noble County, Perry\r\n" + //
                        "Nowata County, Nowata\r\n" + //
                        "Okfuskee County, Okemah\r\n" + //
                        "Oklahoma County, Oklahoma City\r\n" + //
                        "Okmulgee County, Okmulgee\r\n" + //
                        "Osage County, Pawhuska\r\n" + //
                        "Ottawa County, Miami\r\n" + //
                        "Pawnee County, Pawnee\r\n" + //
                        "Payne County, Stillwater\r\n" + //
                        "Pittsburg County, McAlester\r\n" + //
                        "Pontotoc County, Ada\r\n" + //
                        "Pottawatomie County, Shawnee\r\n" + //
                        "Pushmataha County, Antlers\r\n" + //
                        "Roger Mills County, Cheyenne\r\n" + //
                        "Rogers County, Claremore\r\n" + //
                        "Seminole County, Wewoka\r\n" + //
                        "Sequoyah County, Sallisaw\r\n" + //
                        "Stephens County, Duncan\r\n" + //
                        "Texas County, Guymon\r\n" + //
                        "Tillman County, Frederick\r\n" + //
                        "Tulsa County, Tulsa\r\n" + //
                        "Wagoner County, Wagoner\r\n" + //
                        "Washington County, Bartlesville\r\n" + //
                        "Washita County, Cordell\r\n" + //
                        "Woods County, Alva\r\n" + //
                        "Woodward County, Woodward\r\n";

        String[] nfg = asv.split("\r\n");
        int index = 1;
        for(String s: nfg){
            System.out.println("Continent{name: " + s.substring(0, s.indexOf("County")-1) + " | provs: " + ("prov" + (index) + ", ") + ("prov" + (index+1) + ", ") + ("prov" + (index+2) + ", ") + ("prov" + (index+3)) + " | bonus: " + 2 + "}");
            index += 4;
        }

    }
}
