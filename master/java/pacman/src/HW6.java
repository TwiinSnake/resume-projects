/*
        This program plays the game of Pac Man, using an implementation of a Graph using a linked list,
  queue, and a stack. The user enters a direction they would like to move, gaining points if they move
  over a dot. Once the user moves, the ghosts each take a turn to move towards the player, taking the
  shortest path they can find using Breadth-First Search.
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class HW6 {

    public static int points = 0;

    public static void main(String[] args) {
        // Initialize the file.
        File file = new File(args[0]);
        try {
            // Initialize the scanner to read through the file.
            Scanner scanner = new Scanner(file);
            // Initialize the graph using the x and y at the start of the file and build it.
            Graph map = new Graph(scanner.nextInt(), scanner.nextInt());
            map.buildMap(scanner);
            // Begin the game with the Pac Man's move.
            pacManMove(map);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    public static void pacManMove(Graph map) {
        // Initialize a new scanner for the console input.
        Scanner scanner = new Scanner(System.in);
        // Find and save where each of the ghosts and the Pac Man are for later use.
        MapPiece pac = map.search('P');
        MapPiece g = map.search('G');
        MapPiece h = map.search('H');
        MapPiece o = map.search('O');
        MapPiece s = map.search('S');
        map.printMap();
        boolean going = true;
        // Prompt the user for their move.
        System.out.print("Please enter your move [(u)p, (d)own, (l)eft, or (r)ight]: ");
        char input = scanner.next().charAt(0);
        // No matter what, if the Pac Man is not there, the game is over.
        if (pac.map != 'P') {
            System.out.println("A ghost is not hungry any more!");
        }
        // Depending on the user's input, move the Pac Man.
        // Up
        while (going) {
            if ( input == 'u' ) {
                // The player cannot move into any of the ghosts or a wall.
                if ( pac.up.map == '#' || pac.up == g || pac.up == h || pac.up == o || pac.up == s ) {
                    System.out.println("You can't move there!");
                    map.printMap();
                    System.out.print("Please enter your move [(u)p, (d)own, (l)eft, or (r)ight]: ");
                    input = scanner.next().charAt(0);
                    continue;
                }
                // If the player moves over a dot, they score a point and the dot is removed.
                if ( pac.up.map == '.' ) {
                    points++;
                    pac.up.isDot = false;
                    // pac.up.weight = 1.5;
                }
                // Move the pac man.
                pac.map = ' ';
                pac.up.map = 'P';
                pac = pac.up;
                going = false;
            }

            // Down
            if ( input == 'd' ) {
                if ( pac.down.map == '#' || pac.down == g || pac.down == h || pac.down == o || pac.down == s ) {
                    System.out.println("You can't move there!");
                    // continue;
                    map.printMap();
                    System.out.print("Please enter your move [(u)p, (d)own, (l)eft, or (r)ight]: ");
                    input = scanner.next().charAt(0);
                    continue;
                }
                if ( pac.down.map == '.' ) {
                    points++;
                    pac.down.isDot = false;
                    // pac.down.weight = 1.5;
                }
                pac.map = ' ';
                pac.down.map = 'P';
                pac = pac.down;
                going = false;
            }

            // Left
            if ( input == 'l' ) {
                if ( pac.left.map == '#' || pac.left == g || pac.left == h || pac.left == o || pac.left == s ) {
                    System.out.println("You can't move there!");
                    // continue;
                    map.printMap();
                    System.out.print("Please enter your move [(u)p, (d)own, (l)eft, or (r)ight]: ");
                    input = scanner.next().charAt(0);
                    continue;
                }
                if ( pac.left.map == '.' ) {
                    points++;
                    pac.left.isDot = false;
                    // pac.left.weight = 1.5;
                }
                pac.map = ' ';
                pac.left.map = 'P';
                pac = pac.left;
                going = false;
            }

            // Right
            if ( input == 'r' ) {
                if ( pac.right.map == '#' || pac.right == g || pac.right == h || pac.right == o || pac.right == s ) {
                    System.out.println("You can't move there!");
                    // continue;
                    map.printMap();
                    System.out.print("Please enter your move [(u)p, (d)own, (l)eft, or (r)ight]: ");
                    input = scanner.next().charAt(0);
                    continue;
                }
                if ( pac.right.map == '.' ) {
                    points++;
                    pac.right.isDot = false;
                    // pac.right.weight = 1.5;
                }
                pac.map = ' ';
                pac.right.map = 'P';
                pac = pac.right;
                going = false;
            }
        }
        // Re-print the map with the new position and the player's points, then play out the ghosts' turns.
        map.printMap();
        System.out.println("Points: " + points);
        // Make sure the search method did not find a wall instead of a ghost.
        if (g.map == 'G' || g.map == 'g') {
            ghostMove(map, g);
        }
        if (h.map == 'H' || h.map == 'h') {
            ghostMove(map, h);
        }
        if (o.map == 'O' || o.map == 'o') {
            ghostMove(map, o);
        }
        if (s.map == 'S' || s.map == 's') {
            ghostMove(map, s);
        }
    }


    public static void ghostMove(Graph map, MapPiece ghost) {
        // The player might lose during the ghost's move.
        boolean pacLost = false;
        // Search for the player using Breadth-First Search, starting from the ghost's position.
        char direction = map.BFS(ghost, ghost.map);
        // Move the ghost depending on its chosen direction from BFS.
        switch (direction) {
            // Down
            case 'd':
                // If the ghost is moving onto the player, they have lost.
                if (ghost.down.map == 'P') {
                    pacLost = true;
                }
                // Overwrite the node the ghost is moving to.
                ghost.down.map = ghost.map;
                // If the ghost is moving from an empty space to a dot, make it uppercase.
                if (Character.isLowerCase(ghost.map) && ghost.down.isDot) {
                    ghost.down.map = Character.toUpperCase(ghost.down.map);
                }
                // Otherwise if the ghost is moving from a dot to an empty space, make it lowercase.
                else if (Character.isUpperCase(ghost.map) && !ghost.down.isDot) {
                    ghost.down.map = Character.toLowerCase(ghost.down.map);
                }
                // In any case, check if the ghost moved from a dot anyway and overwrite the ghost's
                // original position with a dot.
                if (ghost.isDot) {
                    ghost.map = '.';
                }
                // ... or a space if they moved from a space.
                else {
                    ghost.map = ' ';
                }
                break;
            // Left
            case 'l':
                if (ghost.left.map == 'P') {
                    pacLost = true;
                }
                ghost.left.map = ghost.map;
                if (Character.isLowerCase(ghost.map) && ghost.left.isDot) {
                    ghost.left.map = Character.toUpperCase(ghost.left.map);
                } else if (Character.isUpperCase(ghost.map) && !ghost.left.isDot) {
                    ghost.left.map = Character.toLowerCase(ghost.left.map);
                } if (ghost.isDot) {
                    ghost.map = '.';
                } else {
                    ghost.map = ' ';
                }
                break;
            // Up
            case 'u':
                if (ghost.up.map == 'P') {
                    pacLost = true;
                }
                ghost.up.map = ghost.map;
                if (Character.isLowerCase(ghost.map) && ghost.up.isDot) {
                    ghost.up.map = Character.toUpperCase(ghost.up.map);
                } else if (Character.isUpperCase(ghost.map) && !ghost.up.isDot) {
                    ghost.up.map = Character.toLowerCase(ghost.up.map);
                } if (ghost.isDot) {
                    ghost.map = '.';
                } else {
                    ghost.map = ' ';
                }
                break;
            // Right
            case 'r':
                if (ghost.right.map == 'P') {
                    pacLost = true;
                }
                ghost.right.map = ghost.map;
                if (Character.isLowerCase(ghost.map) && ghost.right.isDot) {
                    ghost.right.map = Character.toUpperCase(ghost.right.map);
                } else if (Character.isUpperCase(ghost.map) && !ghost.right.isDot) {
                    ghost.right.map = Character.toLowerCase(ghost.right.map);
                } if (ghost.isDot) {
                    ghost.map = '.';
                } else {
                    ghost.map = ' ';
                }
                break;
            default:
                break;
        }
        // Print the new map after the ghost's move.
        map.printMap();
        // If the player lost and the Pac Man has been eaten by a ghost, the game is over.
        if (pacLost) {
            System.out.println("A ghost is not hungry any more!");
        }
    }
}