package com.mycompany.app;

import javax.swing.*;

/** Hello world! */
public class App {
    public static void main(String[] args) {
        int boardWidth = 360;
        int boardHeight = 640;

        JFrame frame = new JFrame("Flappy Bird");
        // frame.setVisible(true);
        frame.setSize(boardWidth, boardHeight); // set the window size
        frame.setLocationRelativeTo(null); // place the window at the center of the screen
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // When the user presses the x on the window it will terminate the program

        // Add the JPanel onto the Frame
        FlappyBird flappyBird = new FlappyBird();
        frame.add(flappyBird);
        frame.pack(); // Was added to prevent the width and height would have taken to account the dimentions of the title bar (windows thing)
        flappyBird.requestFocus();
        frame.setVisible(true); // Only after the JPanel is added to the JFrame, the visibility is set to true
    }
}
