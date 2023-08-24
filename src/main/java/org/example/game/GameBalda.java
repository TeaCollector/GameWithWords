package org.example.game;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class GameBalda {

    private static final Random RANDOMWORD = new Random();
    private final String PATHTOWORDS = "words.txt";
    private char[][] boardForGame = null;
    private String playerWord = "";
    private String centralWord;
    private String[] arrayWithWords;
    private List<String> savedWords;
    private List<Integer> busyColumn = new ArrayList<>();
    private List<Character> letterOfCentralWord = new ArrayList<>();

    public void gameBalda() {
        savedWords = new ArrayList<>();
        findingFirstWord();
        int number = RANDOMWORD.nextInt(arrayWithWords.length);
        centralWord = arrayWithWords[number];
        savedWords.add(centralWord);
        boardForGame = new char[centralWord.length()][centralWord.length()];


        letterOfCentralWord = centralWord.chars()
                .mapToObj(e -> (char) e)
                .collect(Collectors.toCollection(ArrayList::new));

        System.out.println("With this word we will play:");
        fillTheBordWithLetters(centralWord);
        int counterOfAttempt = 0;

        while (counterOfAttempt < 1000) {
            continueToPlayGame();
            counterOfAttempt++;
            System.out.println("Lost letter: " + letterOfCentralWord);
            System.out.print("\n");

        }
    }

    private void continueToPlayGame() {
        showTheBoard();
        playerWord = playerInputWord();
        checkCorrectWord();
        char letter = playerWord.charAt(playerWord.length() / 2);
        writingPlayerWordInBoard(letter);
    }

    private void writingPlayerWordInBoard(char letter) {
        int indexOfMainWord;
        int endOfRow = 0;
        for (indexOfMainWord = 0; indexOfMainWord < playerWord.length(); indexOfMainWord++) {
            char letterFromTheMainWord = boardForGame[(boardForGame.length) / 2][indexOfMainWord];
            if (letterFromTheMainWord != letter || boardForGame[endOfRow][indexOfMainWord] != 0) {
                continue;
            } else {
                while (endOfRow < boardForGame.length) {
                    boardForGame[endOfRow][indexOfMainWord] = playerWord.charAt(endOfRow);
                    endOfRow++;
                }
            }
            break;
        }
    }

    private void findingFirstWord() {
        try (BufferedReader br = new BufferedReader(new FileReader(PATHTOWORDS))) {
            arrayWithWords = br.readLine().split(", ");
        } catch (Exception e) {
            e.fillInStackTrace();
        }
    }

    private void fillTheBordWithLetters(String word) {
        for (int i = 0; i < boardForGame.length; i++) {
            boardForGame[boardForGame.length / 2][i] = word.charAt(i);
        }
    }

    private void showTheBoard() {
        for (char[] lines : boardForGame) {
            System.out.println(Arrays.toString(lines));
        }
    }

    public void checkCorrectWord() {
        while (playerWord.length() != boardForGame.length) {
            System.out.println("you input incorrect word try again.");
            playerWord = playerInputWord();
        }
    }

    public String playerInputWord() {
        System.out.print("Please Enter the word: ");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        try {
            playerWord = br.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        boolean contains = savedWords.stream().anyMatch(e -> e.equals(playerWord)) ||
                letterOfCentralWord
                        .stream()
                        .noneMatch(lettersFromBoard ->
                                lettersFromBoard.equals(playerWord.charAt(playerWord.length() / 2)));
        if (contains) {
            System.out.println("This word was already written, or letter was used");
            playerInputWord();
        }

        Character remove = playerWord.charAt(playerWord.length() / 2);
        System.out.println(remove);
        letterOfCentralWord.remove(remove);
        savedWords.add(playerWord);
        return playerWord;
    }
}
