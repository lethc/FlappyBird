package com.mycompany.app;

import java.awt.*;
import java.awt.event.*;
// This is gonna store all the pipes in the game
// This will be used to place the pipes at random positions
import java.util.ArrayList;
import java.util.Random;

import javax.swing.*;

// Add another interface called KeyListener: Use code action to autogenerate the missing methods
// with these interfaces
public class FlappyBird extends JPanel implements ActionListener, KeyListener {
    // FlappyBird class is inherited from JPanel class
    // inheritance is going to allow us to define a new class with all the functionalities of JPanel
    // Keep the JPanel features and add variables and functions needed for FlappyBird game
    int boardWidth = 360;
    int boardHeight = 640;

    // Images: These four variables are going to store all the images (Load the images in the
    // constructor)
    Image backgroundImg;
    Image birdImg;
    Image topPipeImg;
    Image bottomPipeImg;

    // Bird
    int birdX = boardWidth / 8;
    int birdY =
            boardHeight
                    / 2; // from thet top of the screen, we are going to move the bird down half way
    int birdWidth = 34;
    int birdHeight = 24;

    class Bird {
        int x = birdX;
        int y = birdY;
        int width = birdWidth;
        int height = birdHeight;
        Image img;

        // Define the constructor
        Bird(Image img) {
            this.img = img;
        }
    }
    ;

    // Pipes
    int pipeX = boardWidth;
    int pipeY = 0;
    int pipewidth = 64; // scaled by 1/6
    int pipeHeight = 512;

    class Pipe {
        int x = pipeX;
        int y = pipeY;
        int width = pipewidth;
        int height = pipeHeight;
        Image img;
        // assign the img in the constructor
        boolean passed = false;

        Pipe(Image img) {
            this.img = img;
        }
    }

    // Game Logic
    int velocityX = -4; // move pipes to the left speed (simulates bird moving to the right)
    Bird bird;
    // defining variables to move the bird
    // if I move the bird up, it will move in the Y direction, but it's going towards 0, so it has
    // to be negative
    // if I move the bird down, also in the Y direction, but it has to be positive
    // moving backwards in the X direction, means it has to be negative
    // moving forwards in the X direction, means it has to be positive
    // When the bird is moved, it requires a velocity (velocity is a changing position over time)
    // The bird only moves up and down whereas the pipes move towards the left, thefore we only need
    // velocityY
    int velocityY = 0; // move bird up/down speed
    // gravity: without this the bird will go up forever (positive)
    int gravity = 1;
    ArrayList<Pipe> pipes; // because we have many pipes in the game we need to store them in a list
    // Change the position of the pipes randomly
    Random random = new Random();

    Timer gameLoop;
    Timer placePipestimer;

    // Take care of the GAME OVER situation
    // it becomes true when the bird falls off the screen or when the bird collides with one of the
    // pipes
    boolean gameOver = false;
    // Keep track of the score
    double score = 0;

    // Creating the constructor
    FlappyBird() {
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        // setBackground(Color.blue);
        setFocusable(
                true); // it is going to make sure that the FlappyBird class (JPanel) it is the one
        // that takes the key events
        addKeyListener(this); // make sure to check the three functions required by KeyListener

        // Load the images
        backgroundImg =
                new ImageIcon(getClass().getResource("images/flappybirdbg.png"))
                        .getImage(); // Load the image onto the variable
        birdImg =
                new ImageIcon(getClass().getResource("images/flappybird.png"))
                        .getImage(); // images location:
        // my-app/src/main/resources/com/mycompany/app/images
        topPipeImg = new ImageIcon(getClass().getResource("images/toppipe.png")).getImage();
        bottomPipeImg = new ImageIcon(getClass().getResource("images/bottompipe.png")).getImage();

        // bird
        bird = new Bird(birdImg);
        pipes = new ArrayList<Pipe>();

        // Place pipes timer
        // every 1500 = 1.5 seconds do the following function
        placePipestimer =
                new Timer(
                        1500,
                        new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                placePipes();
                            }
                        });
        placePipestimer.start(); // start the timer

        // game timer
        gameLoop =
                new Timer(
                        1000 / 60,
                        this); // 1000 milliseconds = 1 second (60 frames per second), this refers
        // to the actionPerformed function
        gameLoop.start();
    }

    // create a function that will create the pipes and add them to the arraylist
    public void placePipes() {
        // Math.random() gives us a random number between 0 and 1 and then multiply it by the
        // pipeHeight devided by 2
        // (0-1) * pipeHeight/2 -> (0-256) The output number is a random number between 0 and 256
        // pipeHeight/4 -> 128
        // pipeY is 0
        // In total: 0 - 128  - (0-256) --> pipeHeight/4 -> 3/4 pipeHeight
        // The pipes will be shifted upwards by a random amount
        int randomPipeY = (int) (pipeY - pipeHeight / 4 - Math.random() * (pipeHeight / 2));
        // Bottom pipe
        // for this we need to alocate some space for opening so that the bird can fly through
        int openingSpace = boardHeight / 4;
        // this needs to be called in a loop like the timer for the frames, etc
        Pipe topPipe = new Pipe(topPipeImg);
        topPipe.y = randomPipeY;
        pipes.add(topPipe);
        // create a pipe for the bottom pipe
        Pipe bottomPipe = new Pipe(bottomPipeImg);
        // set the y position becuase by default the pipe y is 0 (the top of th screen)
        // the y position is going to be  where the top pipe starts (x and y start at top left
        // corner)
        // so we need to get to the bottom of the top pipe
        bottomPipe.y = topPipe.y + pipeHeight + openingSpace;
        // then add the bottom pipe to the arraylist
        // You take the bottom position of the top pipe (Y position) and add it the height
        // and from there you gotta add the opening space (1/4 of the board height)
        // so here you can get the y position for the y pipe
        pipes.add(bottomPipe);
    }

    // This is a function from the JPanel class
    public void paintComponent(Graphics g) {
        // super refers to the parent class (JPanel)
        super.paintComponent(g);
        draw(g);
    }
    ;

    // this only gets called only once, a loop is needed to keep the game going
    public void draw(Graphics g) {
        /* // related to gemeLoop.start(), without this it only draws once
        System.out.println("Drawing"); */
        // Draw the background image
        g.drawImage(
                backgroundImg,
                0,
                0,
                boardWidth,
                boardHeight,
                null); // 0, 0 = x, y coordinates (the starting position of the image, when drawing
        // it always start on the top left corner)
        // Drawing the bird
        g.drawImage(bird.img, bird.x, bird.y, bird.width, bird.height, null);

        // Draw de pipes
        for (int i = 0; i < pipes.size(); i++) {
            Pipe pipe = pipes.get(i);
            g.drawImage(pipe.img, pipe.x, pipe.y, pipe.width, pipe.height, null);
        }
        // Draw the score
        g.setColor(Color.white);
        g.setFont(new Font("Arial", Font.PLAIN, 32)); // set the size to 32 pixels
        if (gameOver) {
            g.drawString("Game Over: " + String.valueOf((int) score), 10, 35);
        } else {
            g.drawString(String.valueOf((int) score), 10, 35);
        }
    }

    public void move() {
        velocityY += gravity;
        // bird: Update all the x and y positions of all the objects
        bird.y += velocityY;
        bird.y =
                Math.max(
                        bird.y,
                        0); // zero is the top of the screen (now the bird can't beyond the top
        // ofthe screen)
        // Move the pipes
        for (int i = 0; i < pipes.size(); i++) {
            Pipe pipe = pipes.get(i);
            pipe.x +=
                    velocityX; // every frame, we are going to move each pipe over 5 -5 to the left
            // If the bird has not passed this pipe and the x position of the bird is passed the
            // right side of pipe.x...
            if (!pipe.passed && bird.x > pipe.x + pipe.width) {
                pipe.passed = true;
                score +=
                        0.5; // 0.5 because there are 2 pipes, so we split the points by half for
                             // each pipe (0.5*2=1)
            }

            if (collision(bird, pipe)) {
                gameOver = true;
            }
        }
        // First scenario: the bird falls off the screen
        if (bird.y > boardHeight) {
            gameOver = true;
        }
    }

    // create funtion to handle the Second scenario: the bird collides with a pipe
    public boolean collision(Bird a, Pipe b) {
        // Specific formula for detecting collision
        // trust me bro, it works (?)
        return (a.x < b.x + b.width // a's top left corner doesn't reach b's top right corner
                && a.x + a.width > b.x // a's top right corner passes b's top left corner
                && a.y < b.y + b.height // a's top left corner doesn't reach b's bottom left corner
                && a.y + a.height > b.y); // a's bottom left corner passes b's top left corner
    }

    // This is going to be the action performed every 60 times a second
    @Override
    public void actionPerformed(ActionEvent e) {
        // before repaint the screen, update the position of the bird
        move(); // This action will be performed every 60 times a second
        repaint(); // This will call paintComponent() function
        if (gameOver) {
            placePipestimer.stop();
            gameLoop.stop();
        }
    }

    // KeyPressed is similar to KeyTyped, but it can be any key that is pressed (space, character,
    // f11, etc)
    @Override
    public void keyPressed(KeyEvent e) {
        // if the space key is pressed, then
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            velocityY = -9;
            if (gameOver) {
                // restart the game by resetting the conditions
                bird.y = birdY;
                velocityY = 0;
                pipes.clear();
                score = 0;
                gameOver = false;
                gameLoop.start();
                placePipestimer.start();
            }
        }
    }

    // KyType is when you type on a key that has a character
    @Override
    public void keyTyped(KeyEvent e) {}

    // KeyReleased is when you release a key
    @Override
    public void keyReleased(KeyEvent e) {}
}
