package co.therealm.nothanks;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import co.therealm.framework.FileIO;
import co.therealm.framework.Screen;
import co.therealm.framework.implementation.AndroidFileIO;
import co.therealm.framework.implementation.AndroidGame;
import co.therealm.nothanks.players.GeoffFAI;
import co.therealm.nothanks.players.HumanPlayer;
import co.therealm.nothanks.players.NetValueLessThanXAI;
import co.therealm.nothanks.players.NeverTakeAI;
import co.therealm.nothanks.players.Player;


public class NoThanksGame extends AndroidGame {

    private static List<Player> playerList;

    private FileIO fileIO = new AndroidFileIO(this);

    public static List<Player> getPlayerList(){
        if (playerList == null){
            loadPlayers();
        }
        return playerList;
    }

    public static void setPlayerList(List<Player> players){
        savePlayers();
        playerList = players;
    }

    private void loadPlayers(){

        try {
            InputStream is = fileIO.readFile("players.txt");
        } catch (IOException e) {
            // Exception caught, hard coded default options

            playerList.add(new HumanPlayer("Player 1"));
            playerList.add(new GeoffFAI());
            playerList.add(new NetValueLessThanXAI(11));
            playerList.add(new NeverTakeAI());
        }
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
