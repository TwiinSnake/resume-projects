import java.util.Queue;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.Stack;

public class Graph {
    // Initialize the queue and stack to use for BFS later.
    Queue<MapPiece> q = new LinkedList<>();
    Stack<String> s = new Stack<>();

    // An X and Y variable to keep track of the size of the map.
    int x;
    int y;

    // Keep a pointer to the very beginning of the map.
    MapPiece head;


    public Graph (int x, int y) {
        this.x = x;
        this.y = y;
    }


    public char BFS(MapPiece mp, char ghost) {
        resetDistovery();
        // Add the starting map piece to the queue.
        q.add(mp);
        mp.distance = 0;
        // While the queue isn't empty...
        while (!q.isEmpty()) {
            mp = q.remove();
            // If the spot has already been discovered, skip.
            if (mp.discovery || mp.map == '#') {
                continue;
            }
            // Otherwise, discover it.
            mp.discovery = true;
            // If the up piece is not empty, and it is not a wall, add it to the queue.
            if (mp.up != null && mp.up.map != '#') {
                q.add(mp.up);
                // Set the distance from the starting position of the next node being added to the queue.
                if (mp.up.distance > mp.distance + 1 || mp.up.distance == -1) {
                    mp.up.distance = mp.distance + 1;
                }
            }
            // Same with the right...
            if (mp.right != null && mp.right.map != '#') {
                q.add(mp.right);
                if (mp.right.distance > mp.distance + 1 || mp.right.distance == -1) {
                    mp.right.distance = mp.distance + 1;
                }
            }
            // ... down...
            if (mp.down != null && mp.down.map != '#') {
                q.add(mp.down);
                if (mp.down.distance > mp.distance + 1 || mp.down.distance == -1) {
                    mp.down.distance = mp.distance + 1;
                }
            }
            // ... and left.
            if (mp.left != null && mp.left.map != '#') {
                q.add(mp.left);
                if (mp.left.distance > mp.distance + 1 || mp.left.distance == -1) {
                    mp.left.distance = mp.distance + 1;
                }
            }
        }
        // Next we'll print out the path the ghost found to the Pac Man.
        mp = search('P');
        char direction = ' ';
        int distance = mp.distance;
        int actualDistance = mp.distance;
        // Start with pushing Pac Man's position onto the stack.
        s.push("(" + mp.x + "," + mp.y + ") ");
        // While the stack hasn't pushed the ghost onto it...
        while (mp.map != ghost) {
            // Find the direction with the shortest distance to the Pac Man.
            // Up
            if (mp.up.distance < distance && mp.up.map != '#') {
                direction = 'd';
                distance = mp.up.distance;
            }
            // Right
            if (mp.right.distance < distance && mp.right.map != '#') {
                direction = 'l';
                distance = mp.right.distance;
            }
            // Down
            if (mp.down.distance < distance && mp.down.map != '#') {
                direction = 'u';
                distance = mp.down.distance;
            }
            // Left
            if (mp.left.distance < distance && mp.left.map != '#') {
                direction = 'r';
                distance = mp.left.distance;
            }
            // Based on the shortest direction found, push that position onto the stack.
            switch (direction) {
                // Down
                case 'd':
                    mp = mp.up;
                    s.push("(" + mp.x + "," + mp.y + ") ");
                    break;
                // Left
                case 'l':
                    mp = mp.right;
                    s.push("(" + mp.x + "," + mp.y + ") ");
                    break;
                // Up
                case 'u':
                    mp = mp.down;
                    s.push("(" + mp.x + "," + mp.y + ") ");
                    break;
                // Right
                case 'r':
                    mp = mp.left;
                    s.push("(" + mp.x + "," + mp.y + ") ");
                    break;
                default:
                    break;
            }
        }
        // Then, print out the ghost's chosen direction to move, the distance to the Pac Man, and...
        System.out.print("Ghost " + Character.toLowerCase(mp.map) + ": " + direction + " " + actualDistance + " ");
        // ... each position along the path.
        while (!s.isEmpty()) {
            System.out.print(s.pop());
        }
        System.out.println();
        return direction;
    }

    // Simple search, iterates through every single node in the graph.
    public MapPiece search(char search) {
        MapPiece temp = head;
        while (temp.down != null) {
            while (temp.right != null) {
                if (temp.map == search || Character.toUpperCase(temp.map) == search) {
                    return temp;
                }
                temp = temp.right;
            }
            while (temp.left != null) {
                temp = temp.left;
            }
            temp = temp.down;
        }
        return temp;
    }


    public void buildMap(Scanner scanner) {
        // The scanner is, at this time, on the line with the X and Y. Move it.
        scanner.nextLine();
        // We will read each character on each line one character at a time through a string.
        String tempString = scanner.nextLine();
        // We will need a temporary pointer to the node before the one we are making...
        MapPiece temp = head;
        // ... and the node that will be above the node we are making.
        MapPiece upTemp = head;
        // For every Y row...
        for (int i = 0; i < x; i++) {
            // For every X column...
            for (int j = 0; j < y; j++) {
                // If there is no head, make one and set both temp and upTemp to it.
                if (head == null) {
                    head = new MapPiece(tempString.charAt(j), j%10, i%10);
                    temp = head;
                    upTemp = head;
                    continue;
                }
                // If this is the first iteration on a 2nd or beyond Y...
                if (j == 0) {
                    MapPiece newPiece = new MapPiece(tempString.charAt(j), j%10, i%10);
                    // Make sure the node above is properly linked.
                    upTemp.down = newPiece;
                    newPiece.up = upTemp;
                    temp = newPiece;
                    // Move the upTemp pointer for the next node.
                    upTemp = upTemp.right;
                    continue;
                }
                MapPiece newPiece = new MapPiece(tempString.charAt(j), j%10, i%10);
                // Link the new node with the temporary, left one.
                newPiece.left = temp;
                temp.right = newPiece;
                temp = temp.right;
                // If we need to link the nodes we are making to the ones above them, do that.
                if (i > 0) {
                    upTemp.down = newPiece;
                    newPiece.up = upTemp;
                    upTemp = upTemp.right;
                }
            }
            // Move the temporary pointer all the way to the left of its row.
            while (temp.left != null) {
                temp = temp.left;
            }
            // If the upTemp pointer has been moved...
            if (i > 0) {
                // Set it back at the start...
                upTemp = head;
                // ... and make it meet with the temporary pointer.
                while (upTemp.down != null) {
                    upTemp = upTemp.down;
                }
            }
            // If the file has ended, stop entirely. Otherwise, move on to the next line and loop.
            if (!scanner.hasNext()) {
                break;
            }
            tempString = scanner.nextLine();
        }
    }


    public void printMap() {
        MapPiece temp = head;
        // Print out the coordinates on the top row.
        System.out.print(" ");
        for (int i = 0; i < y; i++) {
            System.out.print(i%10);
        }
        System.out.println();
        for (int i = 0; i < x; i++) {
            // Print the Y coordinate for each row.
            System.out.print(i%10);
            for (int j = 0; j < y; j++) {
                // Print each character in that row.
                System.out.print(temp.map);
                if (temp.right != null) {
                    temp = temp.right;
                }
            }
            // Move the pointer to the back of its row, then move it down one.
            while (temp.left != null) {
                temp = temp.left;
            }
            System.out.println();
            if (temp.down != null) {
                temp = temp.down;
            }
        }
    }

    // Similar iteration to the search method, but resets the Discovery and Distance for each node.
    private void resetDistovery() {
        MapPiece temp = head;
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                temp.distance = -1;
                temp.discovery = false;
                if (temp.right != null) {
                    temp = temp.right;
                }
            }
            while (temp.left != null) {
                temp = temp.left;
            }
            if (temp.down != null) {
                temp = temp.down;
            }
        }
    }
}