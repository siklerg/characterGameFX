package game;

import java.util.ArrayList;
import java.util.Random;

public class GamePlay {

    protected Random random = new Random();

    protected ArrayList<String> basicDictionary = new ArrayList<>(); //játszható szavak szótára
    protected ArrayList<String> gameDictionary = new ArrayList<>(); //megjátszott szavak szótára
    protected ArrayList<String> solutionDictionary = new ArrayList<>(); //lehetséges szavak szótára

    protected String playerWord = "---";
    protected String computerWord = "";
    protected boolean gameOver = false;
    protected String winner = "";


    /**
     * Kezdő szó generálása.
     */
    protected String generateFirstComputerWord() {
        int x = random.nextInt(basicDictionary.size());
        return basicDictionary.get(x);
    }

    /**
     * Meghatározza a lehetséges megoldások számát, ehhez legenerálja a megoldás szótárt.
     */
    protected int numberOfSolutions(String word) {
        generateSolutionDictionary(word);
        int counter = solutionDictionary.size();
        return counter;
    }

    /**
     * Legenerálja a MEGOLDÁS SZÓTÁRT a megadott szó alapján
     */
    protected void generateSolutionDictionary(String word) {
        solutionDictionary.clear();
        for (int i = 0; i < basicDictionary.size(); i++) {
            if (validatingWord(basicDictionary.get(i), word) == 2) {
                solutionDictionary.add(basicDictionary.get(i));
            }
        }
    }

    /**
     * Segítség a játékosnak. Legenerálódik a lehetséges szavak szótára, és
     * véletlenszerűen választunk belőle.
     */
    protected String help() {
        generateSolutionDictionary(computerWord);
        int x = random.nextInt(solutionDictionary.size());
        return solutionDictionary.get(x);
    }

    /**
     * Lekéri a MEGOLDÁS SZÓTÁRT, és a gép véletlenszerűen kiválaszt egy szót,
     * majd megjeleníti.
     */
    protected void computerTurn() {
        generateSolutionDictionary(playerWord);
        int x = random.nextInt(solutionDictionary.size());
        computerWord = solutionDictionary.get(x);
        basicDictionary.remove(computerWord);
        gameDictionary.add(computerWord);
    }

    /**
     * A megadott szó ellenőrzése a szabályoknak megfelelően, ha van hiba,
     * visszadaja üzenetben.
     */
    protected String checkWord() {
        String message = "";
        if (gameDictionary.indexOf(playerWord) != -1) {
            message = "H I B A - Ez a szó már volt!";
        } else if (playerWord.length() != 3) {
            message = "H I B A - Három betűs szavakkal játszunk!";
        } else if (validatingWord(playerWord, computerWord) != 2) {
            message = "H I B A - Pontosan egy betűnek kell változnia!";
        } else if (basicDictionary.indexOf(playerWord) == -1) {
            message = "H I B A - Ez a szó nincs benne a beállított szótárban!";
        }
        return message;
    }

    /**
     * Ellenőrzi, hogy a megadott két szó csak egy betűben tér-e el egymástól.
     */
    protected int validatingWord(String word1, String word2) {
        int match = 0;
        if (word1.charAt(0) == word2.charAt(0)) {
            match++;
        }
        if (word1.charAt(1) == word2.charAt(1)) {
            match++;
        }
        if (word1.charAt(2) == word2.charAt(2)) {
            match++;
        }
        return match;
    }


    protected void checkPlayerWord() {

        // ellenőrizzük a kapott szót, ha van hiba üzenet, akkor kiírjuk
        if (!checkWord().equals("")) {
            System.out.println(checkWord());
        }
        basicDictionary.remove(playerWord);
        gameDictionary.add(playerWord);
        //ha nincs több megfelelő szó, azaz a megoldás szótár üresként generálódik le, akkor győzött a játékos
        if (numberOfSolutions(playerWord) == 0) {
            gameOver = true;
            winner = "Játékos";
        }

        computerTurn();

        //ha nincs több megfelelő szó, azaz a megoldás szótár üresként generálódik le, akkor győzött a gép
        if (numberOfSolutions(computerWord) == 0) {
            gameOver = true;
            winner = "Gép. Nincs több megfelelő szó a szótárban.";
        }


        // játék vége
        System.out.println();
        System.out.println("A játék végetért!");
        System.out.println();
        if (gameOver) {
            System.out.println("Győztes: " + winner);
            System.out.println();
        }
        System.out.println("Összesen " + gameDictionary.size() + " szó került a játékba.");
        System.out.println();
        System.out.println("Ezek a következők voltak: ");

    }

}







