package util;
import java.awt.Color;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import participant.*;
import participant.ExamplePlayer2;
import participant.OldExamplePlayer;
import participant.PlayerLogic;
public class Main {

    public static void main(String[] args){
        System.out.println("test");

        ExamplePlayer1 sl1 = new ExamplePlayer1();
        ExamplePlayer1 sl2 = new ExamplePlayer1();
        //ExamplePlayer1 sl3 = new ExamplePlayer1();
        //ExamplePlayer1 sl4 = new ExamplePlayer1();

        //ExamplePlayer2 sl1 = new ExamplePlayer2();
        //ExamplePlayer2 sl2 = new ExamplePlayer2();
        ExamplePlayer2 sl3 = new ExamplePlayer2();
        ExamplePlayer2 sl4 = new ExamplePlayer2();

        PlayerLogic[] playerLogics = {sl1, sl2, sl3, sl4};
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

        String result = game.startGame(4*25, test);
        System.out.println("Result: " + result);
        
    }

}
