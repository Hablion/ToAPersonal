/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package byui.cit260.talesofarterra.control;

import byui.cit260.talesofarterra.exceptions.CharacterControlException;
import byui.cit260.talesofarterra.exceptions.GameControlException;
import byui.cit260.talesofarterra.model.Game;
import byui.cit260.talesofarterra.model.Location;
import byui.cit260.talesofarterra.model.Map;
import byui.cit260.talesofarterra.model.Player;
import byui.cit260.talesofarterra.model.Character;
import byui.cit260.talesofarterra.view.ErrorView;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import talesofarterra.TalesofArterra;

/**
 *
 * @author Dale
 */
public class GameControl {

    public static void saveGame(Game game, String fileName) throws GameControlException {
        String characterFileName = fileName + ".sav";
        FileOutputStream fileOutputStream = null;
        ObjectOutputStream objectOutputStream = null;
        
        try {
            fileOutputStream = new FileOutputStream(characterFileName);
            objectOutputStream = new ObjectOutputStream(fileOutputStream);
            //The object is being persisted here
            objectOutputStream.writeObject(game);
            objectOutputStream.close();
        } catch(Exception ioe) {
            throw new GameControlException(ioe.getMessage());
        }
    }

    public static void loadGame(String saveFile) throws GameControlException {
        FileInputStream fileInputStream = null;
        ObjectInputStream objectInputStream = null;
        String serializedFileName = saveFile;
        Game game = null;
        try {
            fileInputStream = new FileInputStream(serializedFileName);
            objectInputStream = new ObjectInputStream(fileInputStream);
            game = (Game) objectInputStream.readObject();
            objectInputStream.close();
        } catch (Exception ex) {
            throw new GameControlException(ex.getMessage());
        }
        TalesofArterra.setGame(game);
    }
    
    public void advanceHours (Game game, int hours) {
        game.setTime(game.getTime() + hours);
        if (game.getTime() > 23) {
            game.setTime(game.getTime() - 24);
            game.setDays(game.getDays() + 1);
        }
    }
    /**
     *
     * @param player
     * @throws java.io.IOException
     * @throws java.io.FileNotFoundException
     * @throws java.lang.ClassNotFoundException
     */
    public static void createNewGame(Player player) throws CharacterControlException{
        Game game = new Game();
        ErrorView ev = new ErrorView();
        CharacterControl cc = new CharacterControl();
        Character playerChar;
        playerChar = cc.loadCharacter("playerChar.ser");
        player.setPlayerChar(playerChar);
        
        game.setPlayer(player);
        
        QuestControl qc = new QuestControl();
        game.setJournal(qc.loadQuests());
        game.getJournal()[0].setAccepted(true);
        
        Map map = Map.Outside;
        game.setCurrentMap(map);
        
        Location location = Location.OutsideStart;
        LocationControl lc = new LocationControl();
        lc.setDialog();
        lc.changeLocation(game,location);
        
        TalesofArterra.setGame(game);
    }
}