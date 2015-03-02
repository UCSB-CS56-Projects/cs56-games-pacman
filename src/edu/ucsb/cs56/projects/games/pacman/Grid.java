package edu.ucsb.cs56.projects.games.pacman;

import java.awt.*;
import java.util.Arrays;

/**
 * Class representing the map layout
 *
 * @author Yuxiang Zhu
 * @version CS56, W15
 */

public class Grid {
    final int blockSize = 24;
    final int nrOfBlocks = 17;
    final int scrSize = nrOfBlocks * blockSize;

    //Real level data 15 x 15
    /*final short easytest[][] = new short[][]{
            {19, 26, 26, 26, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 22},
            {21, 0, 0, 0, 17, 0, 0, 0, 0, 0, 0, 0, 0, 0, 20},
            {21, 0, 0, 0, 17, 0, 0, 0, 0, 0, 0, 0, 0, 0, 20},
            {21, 0, 0, 0, 17, 0, 0, 24, 0, 0, 0, 0, 0, 0, 20},
            {17, 18, 18, 18, 0, 0, 20, 0, 17, 0, 0, 0, 0, 0, 20},
            {17, 0, 0, 0, 0, 0, 20, 0, 17, 0, 0, 0, 0, 24, 20},
            {25, 0, 0, 0, 24, 24, 28, 0, 25, 24, 24, 0, 20, 0, 21},
            {1, 17, 0, 20, 0, 0, 0, 0, 0, 0, 0, 17, 20, 0, 21},
            {1, 17, 0, 0, 18, 18, 22, 0, 19, 18, 18, 0, 20, 0, 21},
            {1, 17, 0, 0, 0, 0, 20, 0, 17, 0, 0, 0, 20, 0, 21},
            {1, 17, 0, 0, 0, 0, 20, 0, 17, 0, 0, 0, 20, 0, 21},
            {1, 17, 0, 0, 0, 0, 0, 18, 0, 0, 0, 0, 20, 0, 21},
            {1, 17, 0, 0, 0, 0, 0, 0, 16, 16, 16, 0, 20, 0, 21},
            {1, 25, 24, 24, 24, 24, 24, 24, 24, 24, 0, 0, 0, 18, 20},
            {9, 8, 8, 8, 8, 8, 8, 8, 8, 8, 25, 24, 24, 24, 28}
    };*/

    /*
     check this link to implement the ghost AI movement at intersection.
     Revise the level 1 data to classic pacman for intersection detection
     http://gameinternals.com/post/2072558330/understanding-pac-man-ghost-behavior
     */
    final short leveldata1[][] = new short[][]{
            {19, 26, 26, 18, 26, 26, 26, 22, 0, 19, 26, 26, 26, 18, 26, 26, 22},
            {21, 0, 0, 21, 0, 0, 0, 21, 0, 21, 0, 0, 0, 21, 0, 0, 21},
            {17, 26, 26, 16, 26, 18, 26, 24, 26, 24, 26, 18, 26, 16, 26, 26, 20},
            {25, 26, 26, 20, 0, 25, 26, 22, 0, 19, 26, 28, 0, 17, 26, 26, 28},
            {0, 0, 0, 21, 0, 0, 0, 21, 0, 21, 0, 0, 0, 21, 0, 0, 0},
            {0, 0, 0, 21, 0, 19, 26, 24, 26, 24, 26, 22, 0, 21, 0, 0, 0},
            {27, 26, 26, 16, 26, 20, 0, 0, 0, 0, 0, 17, 26, 16, 26, 26, 30},
//    change line above to  {26, 26, 26, 16, 26, 20, 0 , 0 , 0 , 0 , 0 ,17, 26, 16, 26, 26, 26},
//    we can implement the feature of transport from left to right or from right to left
            {0, 0, 0, 21, 0, 17, 26, 26, 26, 26, 26, 20, 0, 21, 0, 0, 0},
            {0, 0, 0, 21, 0, 21, 0, 0, 0, 0, 0, 21, 0, 21, 0, 0, 0},
            {19, 26, 26, 16, 26, 24, 26, 22, 0, 19, 26, 24, 26, 16, 26, 26, 22},
            {21, 0, 0, 21, 0, 0, 0, 21, 0, 21, 0, 0, 0, 21, 0, 0, 21},
            {25, 22, 0, 21, 0, 0, 0, 17, 2, 20, 0, 0, 0, 21, 0, 19, 28}, // "2" in this line stands for where the pacman spawn
            {0, 21, 0, 17, 26, 26, 18, 24, 24, 24, 18, 26, 26, 20, 0, 21, 0},
            {19, 24, 26, 28, 0, 0, 25, 18, 26, 18, 28, 0, 0, 25, 26, 24, 22},
            {21, 0, 0, 0, 0, 0, 0, 21, 0, 21, 0, 0, 0, 0, 0, 0, 21},
            {25, 26, 26, 26, 26, 26, 26, 24, 26, 24, 26, 26, 26, 26, 26,26, 28},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
    };


//    {19, 26, 26, 26, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 22},
//    {21, 0 , 0 , 0 , 17, 16, 16, 16, 16, 16, 16, 16, 16, 16, 20},
//    {21, 0 , 0 , 0 , 17, 16, 16, 16, 16, 16, 16, 16, 16, 16, 20},
//    {21, 0 , 0 , 0 , 17, 16, 16, 24, 16, 16, 16, 16, 16, 16, 20},
//    {17, 18, 18, 18, 16, 16, 20, 0 , 17, 16, 16, 16, 16, 16, 20},
//    {17, 16, 16, 16, 16, 16, 20, 0 , 17, 16, 16, 16, 16, 24, 20},
//    {25, 16, 16, 16, 24, 24, 28, 0 , 25, 24, 24, 16, 20, 0 , 21},
//    {1 , 17, 16, 20, 0 , 0 , 0 , 0 , 0 , 0 , 0 , 17, 20, 0 , 21},
//    {1 , 17, 16, 16, 18, 18, 22, 0 , 19, 18, 18, 16, 20, 0 , 21},
//    {1 , 17, 16, 16, 16, 16, 20, 0 , 17, 16, 16, 16, 20, 0 , 21},
//    {1 , 17, 16, 16, 16, 16, 20, 0 , 17, 16, 16, 16, 20, 0 , 21},
//    {1 , 17, 16, 16, 16, 16, 16, 18, 16, 16, 16, 16, 20, 0 , 21},
//    {1 , 17, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 20, 0 , 21},
//    {1 , 25, 24, 24, 24, 24, 24, 24, 24, 24, 16, 16, 16, 18, 20},
//    {9 , 8 , 8 , 8 , 8 , 8 , 8 , 8 , 8 , 8 , 25, 24, 24, 24, 28}

    final short leveldata2[][] = new short[][]{
            {19,26,26,18,26,18,26,18,26,18,26,18,26,18,26,26,22},//1
            {25,26,18,28, 0,25,22,21, 0,21,19,28, 0,25,18,26,28},//2
            { 0, 0,17,22, 0, 0,21,21, 0,21,21, 0, 0,19,20, 0, 0},//3
            { 0, 0,21,25,26,26,24,24,26,24,24,26,26,28,21, 0, 0},//4
            {27,26,20, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,17,26,30},//5
            { 0, 0,21, 0, 0,19,26,26,26,26,26,22, 0, 0,21, 0, 0},//6
            { 0, 0,25,18,26,20, 0, 0, 0, 0, 0,17,26,18,28, 0, 0},//7
            { 0, 0, 0,21, 0,21, 0, 0, 0, 0, 0,21, 0,21, 0, 0, 0},//8
            { 0, 0, 0,21, 0,25,26,18,26,18,26,28, 0,21, 0, 0, 0},//9
            {27,22, 0,21, 0, 0, 0,21, 0,21, 0, 0, 0,21, 0,19,30},//10
            { 0,17,26,20, 0, 0,19,20, 0,17,22, 0, 0,17,26,20, 0},//11
            { 0,21, 0,21, 0, 0,21,25,26,28,21, 0, 0,21, 0,21, 0},//12
            { 0,21, 0,17,26,26,20, 0, 0, 0,17,26,26,20, 0,21, 0},//13
            {19,28, 0,21, 0, 0,17,22, 0,19,20, 0, 0,21, 0,25,22},//14
            {21, 0, 0,21, 0, 0,21,21, 0,21,21, 0, 0,21, 0, 0,21},//15
            {17,26,26,20, 0, 0,21,21, 0,21,21, 0, 0,17,26,26,20},//16
            {25,26,26,24,26,26,28,25,26,28,25,26,26,24,26,26,28}//17

    };
//            {0, 0, 19, 18, 18, 18, 18, 18, 18, 18, 18, 18, 22, 0, 0, 0, 0},
//            {0, 19, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 22, 0, 0, 0},
//            {19, 16, 16, 16, 16, 16, 24, 24, 24, 16, 16, 16, 16, 16, 22, 0, 0},
//            {17, 16, 16, 16, 16, 20, 0, 0, 0, 17, 16, 16, 16, 16, 20, 0, 0},
//            {17, 16, 16, 16, 16, 20, 0, 0, 0, 17, 16, 16, 16, 16, 20, 0, 0},
//            {17, 16, 16, 24, 24, 28, 0, 0, 0, 25, 24, 24, 16, 16, 20, 0, 0},
//            {17, 16, 20, 0, 0, 0, 0, 0, 0, 0, 0, 0, 17, 16, 20, 0, 0},
//            {17, 16, 20, 0, 0, 0, 0, 0, 0, 0, 0, 0, 17, 16, 20, 0, 0},
//            {17, 16, 20, 0, 0, 0, 0, 0, 0, 0, 0, 0, 17, 16, 20, 0, 0},
//            {17, 16, 16, 18, 18, 22, 0, 0, 0, 19, 18, 18, 16, 16, 20, 0, 0},
//            {17, 16, 16, 16, 16, 20, 0, 23, 0, 17, 16, 16, 16, 16, 20, 0, 0},
//            {17, 16, 16, 16, 16, 20, 0, 21, 0, 17, 16, 16, 16, 16, 20, 0, 0},
//            {25, 16, 16, 16, 16, 16, 18, 16, 18, 16, 16, 16, 16, 16, 28, 0, 0},
//            {0, 25, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 28, 0, 0, 0},
//            {0, 0, 25, 24, 24, 24, 24, 24, 24, 24, 24, 24, 28, 0, 0, 0, 0},
//            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
//            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}

    final short[][] leveldata3 = new short[][]{
            {19,26,26,18,26,22, 0,19,26,22, 0,19,26,18,26,26,22},//1
            {21, 0, 0,21, 0,17,26,20, 0,17,26,20, 0,21, 0, 0,21},//2
            {21, 0, 0,21, 0,21, 0,17,26,20, 0,21, 0,21, 0, 0,21},//3
            {25,26,26,20, 0,21, 0,21, 0,21, 0,21, 0,17,26,26,28},//4
            { 0, 0, 0,17,26,24,26,20, 0,17,26,24,26,20, 0, 0, 0},//5
            { 0,19,18,20, 0, 0, 0,21, 0,21, 0, 0, 0,17,18,22, 0},//6
            { 0,21,21,21, 0,19,26,24,26,24,26,22, 0,21,21,21, 0},//7
            {27,28,21,21, 0,21, 0, 0, 0, 0, 0,21, 0,21,21,25,30},//8
            { 0, 0,21,25,26,24,26,18,26,18,26,24,26,28,21, 0, 0},//9
            {27,26,20,19,26,26,26,20, 0,17,26,26,26,22,17,26,30},//10
            { 0, 0,21,21, 0, 0, 0,21, 0,21, 0, 0, 0,21,21, 0, 0},//11
            {19,22,21,21, 0,19,18,24,26,24,18,22, 0,21,21,19,22},//12
            {21,21,17,16,26,20,25,22, 0,19,28,17,26,16,20,21,21},//13
            {21,21,21,21, 0,21, 0,21, 0,21, 0,21, 0,21,21,21,21},//14
            {21,25,20,21, 0,17,22,21, 0,21,19,20, 0,21,17,28,21},//15
            {21, 0,21,25,22,21,17,28, 0,25,20,21,19,28,21, 0,21},//16
            {25,26,28, 0,25,28,25,26,26,26,28,25,28, 0,25,26,28}//17
    };
//            {19, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 22, 0, 0},
//            {17, 16, 24, 24, 16, 16, 16, 16, 16, 24, 24, 24, 24, 16, 20, 0, 0},
//            {17, 20, 0, 0, 17, 16, 16, 16, 20, 0, 0, 0, 0, 17, 20, 0, 0},
//            {17, 20, 0, 0, 17, 16, 16, 16, 20, 0, 0, 0, 0, 17, 20, 0, 0},
//            {17, 16, 18, 18, 16, 16, 16, 16, 16, 18, 22, 0, 0, 17, 20, 0, 0},
//            {17, 16, 16, 16, 16, 16, 24, 24, 24, 16, 20, 0, 0, 17, 20, 0, 0},
//            {17, 16, 16, 16, 16, 20, 0, 0, 0, 17, 16, 18, 18, 16, 20, 0, 0},
//            {17, 16, 16, 16, 16, 20, 0, 0, 0, 17, 16, 16, 16, 16, 20, 0, 0},
//            {17, 16, 24, 24, 16, 20, 0, 0, 0, 17, 16, 16, 16, 16, 20, 0, 0},
//            {17, 20, 0, 0, 17, 16, 18, 18, 18, 16, 16, 16, 16, 16, 20, 0, 0},
//            {17, 20, 0, 0, 25, 24, 16, 16, 16, 16, 16, 24, 24, 16, 20, 0, 0},
//            {17, 20, 0, 0, 0, 0, 17, 0, 16, 16, 20, 0, 0, 17, 20, 0, 0},
//            {17, 20, 0, 0, 0, 0, 17, 16, 16, 16, 20, 0, 0, 17, 20, 0, 0},
//            {17, 16, 18, 18, 18, 18, 16, 16, 16, 16, 16, 18, 18, 16, 20, 0, 0},
//            {25, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 28, 0, 0},
//            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
//            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}



    final short leveldata4[][] = new short[][]{
            {19, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 22, 0, 0},
            {17, 16, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 16, 20, 0, 0},
            {17, 20, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 17, 20, 0, 0},
            {17, 16, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 16, 20, 0, 0},
            {17, 16, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 16, 20, 0, 0},
            {17, 20, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 17, 20, 0, 0},
            {17, 16, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 16, 20, 0, 0},
            {17, 16, 24, 24, 24, 24, 24, 16, 24, 24, 24, 24, 24, 16, 20, 0, 0},
            {17, 20, 0, 0, 0, 0, 0, 21, 0, 0, 0, 0, 0, 17, 20, 0, 0},
            {17, 20, 0, 19, 18, 18, 18, 16, 18, 18, 18, 22, 0, 17, 20, 0, 0},
            {17, 20, 0, 17, 16, 16, 16, 16, 16, 16, 16, 20, 0, 17, 20, 0, 0},
            {17, 20, 0, 25, 24, 24, 24, 24, 24, 24, 24, 28, 0, 17, 20, 0, 0},
            {17, 20, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 17, 20, 0, 0},
            {17, 16, 18, 18, 18, 18, 22, 0, 19, 18, 18, 18, 18, 16, 20, 0, 0},
            {25, 24, 24, 24, 24, 24, 28, 0, 25, 24, 24, 24, 24, 24, 28, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
    };

    final short leveldata5[][] = new short[][]{
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 19, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 22, 0, 0, 0},
            {0, 0, 17, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 20, 0, 0, 0},
            {0, 0, 17, 16, 16, 16, 24, 24, 24, 24, 24, 24, 16, 20, 0, 0, 0},
            {0, 0, 17, 16, 16, 20, 0, 0, 0, 0, 0, 0, 17, 20, 0, 0, 0},
            {0, 0, 17, 16, 16, 20, 0, 0, 0, 0, 0, 0, 25, 28, 0, 0, 0},
            {0, 0, 17, 16, 16, 16, 18, 18, 18, 18, 22, 0, 0, 0, 0, 0, 0},
            {0, 0, 17, 16, 16, 16, 16, 16, 16, 16, 20, 0, 0, 0, 0, 0, 0},
            {0, 0, 25, 24, 16, 16, 16, 16, 16, 16, 16, 18, 18, 18, 22, 0, 0},
            {0, 0, 0, 0, 17, 16, 16, 16, 16, 16, 24, 16, 16, 16, 20, 0, 0},
            {0, 0, 0, 0, 17, 16, 16, 16, 16, 20, 0, 17, 16, 16, 20, 0, 0},
            {19, 18, 18, 26, 16, 24, 24, 0, 24, 28, 0, 17, 16, 16, 20, 0, 0},
            {17, 16, 20, 0, 21, 0, 0, 21, 0, 0, 0, 17, 16, 16, 20, 0, 0},
            {17, 16, 20, 0, 21, 0, 19, 16, 22, 0, 0, 25, 24, 24, 28, 0, 0},
            {25, 24, 28, 0, 29, 0, 25, 24, 28, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
    };

    short[][] screenData;
    Color mazeColor, dotColor;

    /**
     * Constructor for Board object
     */
    public Grid() {
        screenData = new short[nrOfBlocks][nrOfBlocks];
        mazeColor = new Color(5, 100, 5);
        dotColor = new Color(192, 192, 0);
    }

    /**
     * Checks if there are any pellets left for Pacman to eat, and restarts the game on the next board in a
     * higher difficulty if finished
     *
     * @return A boolean indicating whether or not the maze is finished
     */
    public boolean checkMaze() {
        for (int i = 0; i < nrOfBlocks; i++) {
            for (int j = 0; j < nrOfBlocks; j++) {
                if ((screenData[i][j] & 48) != 0)
                    return false;
            }
        }
        return true;
    }

    /**
     * Count the number of pellets left for Pacman to eat
     *
     * @return An int indicating how many pellets are left
     */
    public int getPelletNum() {
        int numOfPellet = 0;
        for (int i = 0; i < nrOfBlocks; i++) {
            for (int j = 0; j < nrOfBlocks; j++) {
                if ((screenData[i][j] & 16) != 0)
                    numOfPellet+=1;
            }
        }
        return numOfPellet;
    }


    /**
     * Initialize level
     */
    public void levelInit(int numBoardsCleared) {
        for (int i = 0; i < nrOfBlocks; i++) {
            if (numBoardsCleared % 3 == 0)
                screenData[i] = Arrays.copyOf(leveldata1[i], nrOfBlocks);
            else if (numBoardsCleared % 3 == 1)
                screenData[i] = Arrays.copyOf(leveldata2[i], nrOfBlocks);
            else if (numBoardsCleared % 3 == 2)
                screenData[i] = Arrays.copyOf(leveldata3[i], nrOfBlocks);
            else if (numBoardsCleared % 3 == 3)
                screenData[i] = Arrays.copyOf(leveldata4[i], nrOfBlocks);
            else if (numBoardsCleared % 3 == 4)
                screenData[i] = Arrays.copyOf(leveldata5[i], nrOfBlocks);
        }
    }

    /**
     * Draws the maze that serves as a playing field.
     *
     * @param g2d a Graphics2D object
     */
    public void drawMaze(Graphics2D g2d) {
        int x, y;

        g2d.setStroke(new BasicStroke(2));
        for (int i = 0; i < nrOfBlocks; i++) {
            for (int j = 0; j < nrOfBlocks; j++) {
                y = i * blockSize;
                x = j * blockSize;

                g2d.setColor(mazeColor);
                
                /*
                screen draw explaination
                bit 0 not 0 -> draw left
                bit 1 not 0 -> draw top
                bit 2 not 0 -> draw right
                bit 3 not 0 -> draw bottom
                bit 4 not 0 -> draw point
                 */

                if ((screenData[i][j] & 1) != 0) // draws left
                    g2d.drawLine(x, y, x, y + blockSize - 1);
                if ((screenData[i][j] & 2) != 0) // draws top
                    g2d.drawLine(x, y, x + blockSize - 1, y);
                if ((screenData[i][j] & 4) != 0) // draws right
                    g2d.drawLine(x + blockSize - 1, y, x + blockSize - 1, y + blockSize - 1);
                if ((screenData[i][j] & 8) != 0) // draws bottom
                    g2d.drawLine(x, y + blockSize - 1, x + blockSize - 1, y + blockSize - 1);

                g2d.setColor(dotColor);
                if ((screenData[i][j] & 16) != 0) // draws point
                    g2d.fillRect(x + 11, y + 11, 2, 2);
            }
        }
    }
}
