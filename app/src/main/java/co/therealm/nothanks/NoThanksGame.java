package co.therealm.nothanks;

import android.os.Bundle;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import co.therealm.framework.FileIO;
import co.therealm.framework.Screen;
import co.therealm.framework.implementation.AndroidFileIO;
import co.therealm.framework.implementation.AndroidGame;
import co.therealm.nothanks.players.GeoffFAI;
import co.therealm.nothanks.players.HumanPlayer;
import co.therealm.nothanks.players.NetValueLessThanXAI;
import co.therealm.nothanks.players.NeverTakeAI;
import co.therealm.nothanks.players.Player;
import co.therealm.nothanks.players.RandomAI;


public class NoThanksGame extends AndroidGame {

    private List<Player> playerList;

    private FileIO fileIO;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        fileIO = new AndroidFileIO(this);

        playerList = new ArrayList<Player>();

    }

    public List<Player> getPlayerList(){
        if (playerList == null){
            loadPlayers();
        }
        return playerList;
    }

    public void setPlayerList(List<Player> players){
        savePlayers();
        playerList = players;
    }

    private void loadPlayers(){
        List<Player> players = new ArrayList<Player>();

        try {
            InputStream is = fileIO.readFile("players.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line = null;
            String[] input;

            Player newPlayer = new RandomAI();

            while ((line = reader.readLine()) != null) {

                if (!line.startsWith("#")) {
                    input = line.split(",");

                    Class playerClass = Class.forName("co.therealm.nothanks.players." + input[0]);

                    Constructor<Player>[] constructors = playerClass.getConstructors();

                    for (Constructor<Player> constructor : constructors){
                        if (constructor.getParameterTypes().length == input.length - 1){
                            for (int i = 1; i < input.length; i++){
                                //TODO Figure this out
                            }
                            newPlayer = constructor.newInstance(input[1]);
                        }
                    }
                    /*switch (input[0]) {
                        case "HumanPlayer":
                            if (input.length > 1){
                                newPlayer = new HumanPlayer(input[1]);
                            } else {
                                newPlayer = new HumanPlayer("Player " + players.size() + 1);
                            }
                            break;
                        case "GeoffFAI":
                            if
                            newPlayer = new GeoffFAI();
                            break;
                        default:
                            break;
                    }*/

                    players.add(newPlayer);
                }


            }


            is.close();

        } catch (Exception e) {
            // Exception caught, hard coded default options

            players.add(new HumanPlayer("Player 1"));
            players.add(new GeoffFAI());
            players.add(new NetValueLessThanXAI(11));
            players.add(new NeverTakeAI());
        }


        setPlayerList(players);
    }


    private static void savePlayers(){
        // Write out to a file somehow
    }

    @Override
    public Screen getInitScreen() {
        return new LoadingScreen(this);
    }

    @Override
    public void onBackPressed() {
        getCurrentScreen().backButton();
    }

}
