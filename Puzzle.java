package com.company;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Puzzle extends JPanel {

    private Cells cellsPanel;
    private JTextField date;
    private JTextArea acrossCluesText;
    private JTextArea downCluesText;
    private JTextField groupNickname;
    final int CELL_SIZE = 5;
    public String[] acrossClue = new String[CELL_SIZE];
    public String[] downClue = new String[CELL_SIZE];
    int[] acrossClueNumbers = new int[CELL_SIZE];
    int[] downClueNumbers = new int[CELL_SIZE];


    private JTextArea newAcrossCluesText;
    private JTextArea newDownCluesText;

    public Puzzle( Cells cell, String dates, String[] acrossClues, String[] downClues,
                   int[] acrossClueNumbers,int[] downClueNumbers ) throws InterruptedException {
        JPanel cluesPanel = new JPanel();
        JPanel newCluesPanel = new JPanel();
        JPanel dateAndNickPanel = new JPanel();
        cellsPanel = cell;
        this.acrossClueNumbers = acrossClueNumbers;
        this.downClueNumbers = downClueNumbers;
        this.acrossClue = acrossClues;
        this.downClue = downClues;

        acrossCluesText = new JTextArea(
                "ACROSS           \n" + "\n");

        downCluesText = new JTextArea(
                "DOWN            \n" + "\n");

        newAcrossCluesText = new JTextArea(
                "NEW CLUES (ACROSS)\n" + "\n");

        newDownCluesText = new JTextArea(
                "NEW CLUES (DOWN)\n" + "\n");

        writeClues();

        Calendar cal = new GregorianCalendar();
        date = new JTextField();


        new Timer(500, new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                Date d = new Date();
                SimpleDateFormat s = new SimpleDateFormat("hh:mm:ss a");
                date.setText(dates + "\n " + s.format(d));
            }
        }).start();

        date.setFont(new Font("default", Font.BOLD, 20));
        date.setEditable(false);
        date.setBackground(Color.WHITE);
        date.setForeground(Color.MAGENTA);

        groupNickname = new JTextField("AIPLUS");
        groupNickname.setFont(new Font("default", Font.BOLD, 20));
        groupNickname.setEditable(false);
        groupNickname.setBackground(Color.WHITE);
        groupNickname.setForeground(Color.MAGENTA);

        FlowLayout layout = new FlowLayout(FlowLayout.LEFT);
        layout.setHgap(50);
        dateAndNickPanel.setLayout(layout);
        dateAndNickPanel.setBackground(Color.WHITE);
        dateAndNickPanel.add(date);
        dateAndNickPanel.add(groupNickname);
        dateAndNickPanel.setPreferredSize(new Dimension(760, 50));

        FlowLayout layout2 = new FlowLayout();
        layout2.setVgap(200);
        layout2.setHgap(70);
        cluesPanel.setLayout(layout2);
        cluesPanel.setBackground(Color.WHITE);
        cluesPanel.add(acrossCluesText);
        cluesPanel.add(downCluesText);

        FlowLayout layout3 = new FlowLayout();
        layout3.setVgap(230);
        layout3.setHgap(70);
        //newCluesPanel.setPreferredSize(new Dimension(960, 500));
        newCluesPanel.setLayout( layout3);
        newCluesPanel.add( newAcrossCluesText);
        newCluesPanel.add( newDownCluesText);
        newCluesPanel.setBackground( Color.WHITE);


        cellsPanel.setBackground(Color.WHITE);
        JPanel leftPanel = new JPanel();
        cellsPanel.setPreferredSize(new Dimension(760, 360));
        leftPanel.add(cellsPanel);
        leftPanel.add(dateAndNickPanel);


        BoxLayout boxLayout = new BoxLayout(leftPanel, BoxLayout.Y_AXIS);
        leftPanel.setLayout(boxLayout);

        JPanel rightPanel = new JPanel();

        rightPanel.setBackground(Color.WHITE);
        //cluesPanel.setPreferredSize(new Dimension(960, 500));
        rightPanel.setLayout(new BorderLayout());
        rightPanel.add(cluesPanel, BorderLayout.NORTH);
        rightPanel.add( newCluesPanel, BorderLayout.SOUTH);
        BoxLayout bl2 = new BoxLayout(rightPanel, BoxLayout.Y_AXIS);
        //rightPanel.add(dateAndNickPanel, BorderLayout.SOUTH);

        leftPanel.setPreferredSize(new Dimension(760, 540));
        rightPanel.setPreferredSize(new Dimension(1160, 540));
        add(leftPanel);
        add(rightPanel);
        BoxLayout bl3 = new BoxLayout(this, BoxLayout.X_AXIS);
        setLayout(bl3);

    }

    public void writeClues() {
        String[] acrossClues = acrossClue;
        String[] downClues = downClue;
        int[] acrossCluesNumbers = acrossClueNumbers;
        int[] downCluesNumbers = downClueNumbers;

        for (int i = 0; i < acrossClues.length; i++) {
            acrossCluesText.append(acrossCluesNumbers[i] + "  " + acrossClues[i] + "\n");
        }

        for (int i = 0; i < downClues.length; i++) {
            downCluesText.append(downCluesNumbers[i] + "  " + downClues[i] + "\n");
        }

        acrossCluesText.setFont(new Font("default", Font.ROMAN_BASELINE, 17));
        acrossCluesText.setEditable(false);
        downCluesText.setFont(new Font("default", Font.ROMAN_BASELINE, 17));
        downCluesText.setEditable(false);

        String[] newAcrossClues = cellsPanel.clueFinder.getNewAcrossClues();
        String[] newDownClues = cellsPanel.clueFinder.getNewDownClues();

        for( int i = 0; i < newAcrossClues.length; i++)
            newAcrossCluesText.append( acrossCluesNumbers[i] + "  " + newAcrossClues[i] + "\n");

        for( int i = 0; i < newDownClues.length; i++)
            newDownCluesText.append( downCluesNumbers[i] + "  " + newDownClues[i] + "\n");

        newAcrossCluesText.setFont(new Font("default", Font.ROMAN_BASELINE, 17));
        newAcrossCluesText.setEditable( false);
        newDownCluesText.setFont(new Font("default", Font.ROMAN_BASELINE, 17));
        newDownCluesText.setEditable( false);
    }
}