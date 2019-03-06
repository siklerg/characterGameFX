package game;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.ResourceBundle;

public class ViewController implements Initializable {

    private final String RULES = "S Z A B Á L Y O K\n \n" +
            "Három betűs szavakkal játszunk.\n " +
            "A következő szó mindig csak egy betűben térhet el az előzőtől!\n " +
            "Az x betű nem játszik: box, fax...\n" +
            "A dupla magánhangzós szavak sem: így, úgy... ezek állásfoglalásunk \n " +
            "szerint 2 betűsek, noha három karakterből állnak.\n\n" +
            "Egy szótárral nagyobb a kihívás, több szótárral nagyobb a szabadság!";

    @FXML
    Pane startPane;
    @FXML
    Label startLabel;
    @FXML
    Button startButton;

    @FXML
    Pane dictionaryPane;
    @FXML
    VBox selectDictionaries;
    @FXML
    Button goToPlayButton;
    @FXML
    CheckBox cBox1;
    @FXML
    CheckBox cBox2;
    @FXML
    CheckBox cBox3;
    @FXML
    CheckBox cBox4;

    @FXML
    BorderPane gamePane;
    @FXML
    TextField playerWordInput;
    @FXML
    Label computerWordLabel;
    @FXML
    Button checkPlayerWordButton;
    @FXML
    Button helpButton;
    @FXML
    Label playedWordsLabel;
    @FXML
    Label namesDictLabel;
    @FXML
    Label shortsDictLabel;
    @FXML
    Label suffixsDictLabel;
    @FXML
    Label suffixedWordsDictLabel;
    @FXML
    Label errorMessageLabel;
    @FXML
    Label solutionCounterLabel;
    @FXML
    Label playedWordsCounterLabel;
    @FXML
    Button exitButton;

    @FXML
    Pane messagePane;
    @FXML
    Label messageLabel01;
    @FXML
    Label helpWordLabel02;
    @FXML
    Button messagePaneOkButton;


    GamePlay gamePlay = new GamePlay();
    int helpCounter = 1;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        startPane.setVisible(true);
        startLabel.setText("Köszöntünk a játékban! \n \n " + RULES);
    }

    @FXML
    public void clickOnStartButton() {
        startPane.setVisible(false);
        dictionaryPane.setVisible(true);
    }

    @FXML
    public void initGame() {

        setupDictionaries();

        dictionaryPane.setVisible(false);
        gamePane.setVisible(true);

        gamePlay.computerWord = gamePlay.generateFirstComputerWord();
        gamePlay.basicDictionary.remove(gamePlay.computerWord);
        gamePlay.gameDictionary.add(gamePlay.computerWord);
        computerWordLabel.setText(gamePlay.computerWord);
        playedWordsCounterLabel.setText(String.valueOf(gamePlay.gameDictionary.size()));
        gamePlay.generateSolutionDictionary(gamePlay.computerWord);
        solutionCounterLabel.setText(String.valueOf(gamePlay.solutionDictionary.size()));
        playedWordsLabel.setText(gamePlay.gameDictionary.toString());
    }

    @FXML
    public void checkPlayerWord() {
        gamePlay.playerWord = playerWordInput.getText();
        gamePlay.generateSolutionDictionary(gamePlay.computerWord);
        if (gamePlay.solutionDictionary.isEmpty()) {
            endOfGamePane("S Z Á M Í T Ó G É P");
        } else if (gamePlay.checkWord().equals("")) {
            errorMessageLabel.setText("");
            gamePlay.basicDictionary.remove(gamePlay.playerWord);
            gamePlay.gameDictionary.add(gamePlay.playerWord);
            gamePlay.generateSolutionDictionary(gamePlay.playerWord);
            if (gamePlay.solutionDictionary.isEmpty()) {
                endOfGamePane("J Á T É K O S");
            } else {
                gamePlay.computerTurn();
                computerWordLabel.setText(gamePlay.computerWord);
                playerWordInput.clear();
                solutionCounterLabel.setText(String.valueOf(gamePlay.solutionDictionary.size()));
                playedWordsCounterLabel.setText(String.valueOf(gamePlay.gameDictionary.size()));
                playedWordsLabel.setText(gamePlay.gameDictionary.toString());
            }
        } else {
            errorMessageLabel.setText(gamePlay.checkWord());
        }
    }

    /**
     * Ellenőrzi a kiválasztott szótárakat, és betölti a szükségeseket
     */
    protected void setupDictionaries() {
        gamePlay.gameDictionary.clear();
        gamePlay.solutionDictionary.clear();
        gamePlay.basicDictionary = Dictionary.getBasicDictionary();

        if (cBox1.isSelected()) {
            gamePlay.basicDictionary.addAll(Dictionary.getSuffixedWordsDictionary());
            suffixedWordsDictLabel.setTextFill(Color.GREEN);
        }
        if (cBox2.isSelected()) {
            gamePlay.basicDictionary.addAll(Dictionary.getSuffixsDictionary());
            suffixsDictLabel.setTextFill(Color.GREEN);
        }
        if (cBox3.isSelected()) {
            gamePlay.basicDictionary.addAll(Dictionary.getNamesDictionary());
            namesDictLabel.setTextFill(Color.GREEN);
        }
        if (cBox4.isSelected()) {
            gamePlay.basicDictionary.addAll(Dictionary.getShortsDictionary());
            shortsDictLabel.setTextFill(Color.GREEN);
        }
    }

    @FXML
    protected void helpRequest() {
        messagePane.setVisible(true);
        messagePane.setOpacity(1.0);
        gamePane.setOpacity(0.3);
        gamePane.setDisable(true);
        gamePlay.generateSolutionDictionary(gamePlay.computerWord);
        if (helpCounter == 6) {
            messageLabel01.setText("Sajnos nincs több segítég!");
            helpWordLabel02.setText(":(");
        } else if (gamePlay.solutionDictionary.size() == 0) {
            messageLabel01.setText("Sajnos nincs több megoldás!");
            helpWordLabel02.setText("Győztes: S Z Á M Í T Ó G É P");
        } else {
            messageLabel01.setText(String.valueOf(helpCounter) + ". segítség az 5-ből!");
            helpWordLabel02.setText("Ajánlott szó: " + gamePlay.help());
            helpCounter++;
        }
    }

    protected void endOfGamePane(String winner) {
        messagePane.setVisible(true);
        messagePane.setOpacity(1.0);
        gamePane.setOpacity(0.3);
        gamePane.setDisable(true);
        messageLabel01.setText("A játék véget ért.");
        helpWordLabel02.setText("Győztes: " + winner);
    }

    @FXML
    protected void messagePaneClickOkButton() {
        gamePane.setOpacity(1);
        gamePane.setDisable(false);
        messagePane.setVisible(false);
    }

    @FXML
    protected void mouseOnExit() {
        exitButton.setTextFill(Color.RED);
    }

    @FXML
    protected void mouseOnExitOff() {
        exitButton.setTextFill(Color.BLACK);
    }


    @FXML
    protected void clickExit() {
        System.exit(0);
    }


}