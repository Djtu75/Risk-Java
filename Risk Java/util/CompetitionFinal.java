package util;
import java.awt.Color;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import participant.*;
import participant.Entries.CooperPlayer;
import participant.Entries.Genny;
import participant.Entries.GoalPlayer;
import participant.Entries.Jakefrozen;
import participant.Entries.MatthewS;
import participant.Entries.MyIllogic;
import participant.Entries.MyPlayer;
import participant.Entries.Zach_Player;
import participant.Entries.ZacharyLogic;
public class CompetitionFinal {

    public static void main(String[] args){
        //System.out.println("test");

        PlayerLogic[] competitors = {new CooperPlayer(), new Genny()};

        //Hard Code
        //PlayerLogic[] competitors = {new Jakefrozen(), new MyIllogic(), new MatthewS(), new CooperPlayer()};

        //Make games
        PlayerLogic[] group1 = chooseCompetitors(competitors, competitors.length);
        for(PlayerLogic pl: group1){
            for(int i = 0; i < competitors.length; i++){
                if(competitors[i] == pl){
                    competitors[i] = null;
                }
            }
        }
        
        Player[] placement1 = runGame(group1, 100);
        for(int i = 0; i < placement1.length; i++){
            System.out.println((i + 1) + " place: " + placement1[i].getName());
        }
    }

    public static Player[] runGame(PlayerLogic[] playerLogics, int startingtroops){
        Color[] defaultColors = {Color.BLACK, Color.BLUE, Color.CYAN, Color.GREEN, Color.LIGHT_GRAY, Color.MAGENTA, Color.ORANGE, Color.RED, Color.YELLOW, Color.PINK};
        Player[] players = new Player[playerLogics.length];
        for(int i = 0; i < playerLogics.length; i++){

            Player player = new Player(Integer.toString(i+1), playerLogics[i]);
            if(playerLogics[i].getCustomName() != null){
                player = new Player(playerLogics[i].getCustomName(), playerLogics[i]);
            }

            if(playerLogics[i].getCustomColor() != null && playerLogics[i].getCustomColor()[0] != null && playerLogics[i].getCustomColor()[1] != null){
                player.setColors(playerLogics[i].getCustomColor()[0], playerLogics[i].getCustomColor()[1]);
            }
            else{
                Random rand = new Random();
                Color c1 = null;
                Color c2 = null;
                int a = 0;
                while(c1 == null){
                    a = rand.nextInt(defaultColors.length);
                    c1 = defaultColors[a];
                }
                defaultColors[a] = null;
                while(c2 == null){
                    a = rand.nextInt(defaultColors.length);
                    c2 = defaultColors[a];
                }
                defaultColors[a] = null;
                player.setColors(c1, c2);
            }

            if(playerLogics[i].getCustomProfile() != null){
                player.setProfilePictureLink(playerLogics[i].getCustomProfile());
            }
            else{
                player.setProfilePictureLink("https://waapple.org/wp-content/uploads/2021/06/Variety_Cosmic-Crisp-transparent-658x677.png");
            }

            players[i] = player;
        }
        
        //In the tragic case that two players choose the same colors, one will have to be set to a different color.
        for(int i = 0; i < players.length; i++){
            for(int j = i+1; j < players.length; j++){
                if(((Math.abs(players[i].getColors()[0].getRed() - players[j].getColors()[0].getRed())) < 10) && ((Math.abs(players[i].getColors()[0].getBlue() - players[j].getColors()[0].getBlue())) < 10) && ((Math.abs(players[i].getColors()[0].getGreen() - players[j].getColors()[0].getGreen())) < 10)){
                    if(((Math.abs(players[i].getColors()[1].getRed() - players[j].getColors()[1].getRed())) < 10) && ((Math.abs(players[i].getColors()[1].getBlue() - players[j].getColors()[1].getBlue())) < 10) && ((Math.abs(players[i].getColors()[1].getGreen() - players[j].getColors()[1].getGreen())) < 10)){
                        Random rand = new Random();
                        Color c1 = null;
                        Color c2 = null;
                        int a = 0;
                        while(c1 == null){
                            a = rand.nextInt(defaultColors.length);
                            c1 = defaultColors[a];
                        }
                        defaultColors[a] = null;
                        while(c2 == null){
                            a = rand.nextInt(defaultColors.length);
                            c2 = defaultColors[a];
                        }
                        defaultColors[a] = null;
                        players[j].setColors(c1, c2);
                    }
                }
            }
        }
        

        World world = MapReader.readFile("Risk Java\\util\\BasicWorld.txt");
        
        Game game = new Game(players, world, world.getGraphicProvinces());
        String[] backgroundImages = {"Risk Java\\util\\top-view-of-sea-in-the-ocean-background-footage-for-traveling-4k-free-video.jpg", "Risk Java\\util\\worldimg.png"};

        RenderEarth test = new RenderEarth(world.getGraphicProvinces(), backgroundImages, players, game);

        Player[] result = game.startGame(startingtroops, test);
        
        return result;
    }

    public static PlayerLogic[] chooseCompetitors(PlayerLogic[] options, int numplayers){
        Random rand = new Random();
        PlayerLogic[] playerLogics = new PlayerLogic[numplayers];
        for(int i = 0; i < numplayers; i++){
            playerLogics[i] = null;
            while(playerLogics[i] == null){
                int num = rand.nextInt(options.length);
                if(options[num] != null){
                    playerLogics[i] = options[num];
                    options[num] = null;
                }
                
            }
            
        }
        return playerLogics;
    }

}
