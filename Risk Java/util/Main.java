package util;
import java.awt.Color;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import participant.ExamplePlayer1;
import participant.ExamplePlayer2;
import participant.OldExamplePlayer;
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

        Player one = new Player("one", sl1);
        one.setColors(new Color(252, 186, 3), new Color(128, 252, 3));
        one.setProfilePictureLink("https://waapple.org/wp-content/uploads/2021/06/Variety_Cosmic-Crisp-transparent-658x677.png");
        Player two = new Player("two", sl2);
        two.setColors(new Color(15, 252, 56), new Color(100, 160, 30));
        two.setProfilePictureLink("https://i.guim.co.uk/img/media/b1c1caa029d6f186f9d6b3fabb7f02517eb9c33b/0_58_2528_1519/master/2528.jpg?width=1200&height=900&quality=85&auto=format&fit=crop&s=22aae08708a226561bcdc42856a2bb18");
        Player three = new Player("three", sl3);
        three.setColors(new Color(0, 40, 160), new Color(25, 65, 200));
        three.setProfilePictureLink("https://i.guim.co.uk/img/media/b1c1caa029d6f186f9d6b3fabb7f02517eb9c33b/0_58_2528_1519/master/2528.jpg?width=1200&height=900&quality=85&auto=format&fit=crop&s=22aae08708a226561bcdc42856a2bb18");
        Player four = new Player("four", sl4);
        four.setColors(new Color(38, 186, 252), new Color(252, 100, 48));
        four.setProfilePictureLink("https://i.guim.co.uk/img/media/b1c1caa029d6f186f9d6b3fabb7f02517eb9c33b/0_58_2528_1519/master/2528.jpg?width=1200&height=900&quality=85&auto=format&fit=crop&s=22aae08708a226561bcdc42856a2bb18");
        Player[] players = {one, two, three, four};

        World world = MapReader.readFile("Risk Java\\util\\BasicWorld.txt");
        
        Game game = new Game(players, world, world.getGraphicProvinces());

        RenderEarth test = new RenderEarth(world.getGraphicProvinces(), players, game);

        String result = game.startGame(4*25, test);
        System.out.println("Result: " + result);
        
    }

}
