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

        playerList = null;

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
            String[] playerInput;
            String[] playerParameters = null;

            Player newPlayer = null;

            while ((line = reader.readLine()) != null) {

                if (!line.startsWith("#")) {
                    playerInput = line.split(":");
                    if (playerInput.length == 0){
                        // Can not load this line
                        continue;
                    } else {

                        Class playerClass = Class.forName("co.therealm.nothanks.players." + playerInput[0]);


                        if (playerInput.length == 1) {

                            newPlayer = (Player)playerClass.newInstance();

                        } else if (playerInput.length > 1) {
                            playerParameters = playerInput[1].split(",");

                            Constructor<Player>[] constructors = playerClass.getConstructors();

                            for (Constructor<Player> constructor : constructors){
                                if (constructor.getParameterTypes().length == 1 && constructor.getParameterTypes()[0] == playerParameters.getClass()){
                                    newPlayer = constructor.newInstance(playerParameters);
                                }
                            }
                        }
                    }

                    if(newPlayer == null){
                        continue;
                    } else {
                        players.add(newPlayer);
                    }
                }


            }


            is.close();

            if (players.size() < 2){
                throw new RuntimeException("Can't have a game with fewer than two players.");
            }

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
        // TODO Write out to a file somehow


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
