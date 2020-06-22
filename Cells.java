package com.company;


import net.sf.extjwnl.JWNLException;

import javax.swing.*;
import java.awt.*;

public class Cells extends JPanel {

    private final int CELL_SIZE = 100;
    String[][] solution;
    boolean[][] blackCells;
    int[][] littleNumbers;


    ClueFinder clueFinder;

    public Cells( boolean[][] blackCells, int[][] littleNumbers, String[][] solution, String[] acrossClues, String[] downClues, int[] acrossClueNumbers,int[] downClueNumbers) throws InterruptedException, JWNLException {
        this.solution = solution;
        this.blackCells = blackCells;
        this.littleNumbers = littleNumbers;
        clueFinder = new ClueFinder(acrossClues, downClues, acrossClueNumbers, downClueNumbers, littleNumbers, solution);
    }

    public void draw(Graphics g, int x, int y, boolean isBlack) {
        if (isBlack) {
            g.setColor(Color.BLACK);
            g.fillRect(x + 50, y + 50, CELL_SIZE, CELL_SIZE);
            g.setColor(Color.GRAY);
            g.drawRect(x + 50, y + 50, CELL_SIZE, CELL_SIZE);
        } else {
            g.setColor(Color.WHITE);
            g.fillRect(x + 50, y + 50, CELL_SIZE, CELL_SIZE);
            g.setColor(Color.GRAY);
            g.drawRect(x + 50, y + 50, CELL_SIZE, CELL_SIZE);
        }
    }

    public void drawLetter(Graphics g, int x, int y, String letter) {
        g.setFont(new Font("default", Font.BOLD, 36));
        g.setColor(Color.BLUE);
        g.drawString(letter, x * CELL_SIZE + CELL_SIZE / 2 + 50 - 10, y * CELL_SIZE + CELL_SIZE / 2 + 50 + 10);
    }

    public void drawNumber(Graphics g, int x, int y, int number) {
        g.setFont(new Font("default", Font.BOLD, 20));
        g.setColor(Color.BLACK);
        g.drawString("" + number, x * CELL_SIZE + CELL_SIZE / 7 + 50, y * CELL_SIZE + CELL_SIZE / 5 + 50);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        int x = 0;
        int y = 0;

        boolean[][] blackCell = blackCells;
        String[][] solutions = solution;
        int[][] numbers = littleNumbers;

        for (int i = 0; i < 5; i++) {
            x = 0;
            for (int j = 0; j < 5; j++) {
                draw(g, x, y, blackCell[i][j]);
                if (solutions[i][j] != null) {
                    drawLetter(g, j, i, solutions[i][j]);
                    if (numbers[i][j] != 0) {
                        drawNumber(g, j, i, numbers[i][j]);
                    }
                }

                x = x + CELL_SIZE;
            }

            y = y + CELL_SIZE;
        }
    }

}