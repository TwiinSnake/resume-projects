import java.util.Random;

public class SkipList {

    // Initialize the head and tail to be used for the skip list.
    private Event head;
    private Event tail;

    // Create a negative and positive infinity integer to use for the head and tail of the skip list.
    private final int NEGINF = Integer.MIN_VALUE;
    private final int POSINF = Integer.MAX_VALUE;

    // Keep track of the skip list's height.
    private int height = 0;

    public Random random = new Random();


    public SkipList() {
        head = new Event(NEGINF, null);
        tail = new Event(POSINF, null);
        head.next = tail;
    }

    /*
    // Search for the bottom node of the time we are looking for.
    // If it doesn't exist, returns the node right before where it should exist.
    */
    public Event search(int time) {
        Event n = head;

        // For each node in each linked list, check if it is equal to the specified time.
        while (n.below != null) {
            n = n.below;
            while (time >= n.next.time) {
                n = n.next;
            }
        }
        return n;
    }


    public Event insert(int time, String name, FakeRandHeight randomHeight) {

        // If the time given is already in the skip list, it cannot be added.
        Event p = search(time);
        if (p.time == time) {
            return p;
        }

        // Flip a coin to determine how many levels the new event takes up. (REAL Random)
        /*
        int heads = -1;
        do {
            heads++;
            canIncreaseLevel(heads);
        } while (random.nextBoolean() == true);
        */

        // FAKE RANDOM *******
        int heads = randomHeight.get();
        canIncreaseLevel(heads);


        // Vertical will represent the position at the beginning of each linked list.
        Event vertical = head;
        // Horizontal will be how we move through each linked list.
        Event horizontal;
        /*
        // previousLevel will act as a pointer for each instance of the new event in each
        // linked list so that it can be properly linked to itself on other linked lists.
        */
        Event previousLevel = new Event(0,null);
        // Navigate to the highest linked list the event should be added to.
        for (int i = 0; i < (height - heads); i++) {
            vertical = vertical.below;
        }
        // For each linked list the event should be added to...
        for (int i = 0; i <= heads; i++) {
            // Start horizontal at the beginning of the current linked list to iterate through.
            horizontal = vertical;
            // Move horizontal until it is the node before where the event should be added.
            while (time > horizontal.next.time) {
                horizontal = horizontal.next;
            }
            // Create a new event, and link it in the current linked list.
            Event pNewLevel = new Event(time,name);
            /*
            // At the same time, if the previousLevel of the new event was already inserted,
            // link it to the new level.
            */
            previousLevel.below = pNewLevel;
            pNewLevel.next = horizontal.next;
            horizontal.next = pNewLevel;
            /*
            // Set the previousLevel to the event we just added, and move the linked-list-head
            // pointer down.
            */
            previousLevel = pNewLevel;
            vertical = vertical.below;
        }
        return previousLevel;
    }


    // Remove the event with the given time.
    public Event delete(int time) {
        Event vertical = head.below;
        Event horizontal = vertical;
        Event thingToReturn = new Event(0, null);
        // For each linked list in the skip list
        for (int i = 0; i < height; i++) {
            // While we haven't moved to the end of the linked list.
            if (horizontal == null) {
                break;
            }
            while (horizontal.next.time != POSINF) {
                // If we found the event we're looking to delete, stop.
                if (horizontal.next.time == time) {
                    break;
                }
                horizontal = horizontal.next;
            }
            // If the previous while loop ended right before the node we want to delete
            if (horizontal.next.time == time) {
                // Delete it.
                thingToReturn = horizontal.next;
                horizontal.next = horizontal.next.next;
            }
            // Move down to the next linked list.
            vertical = vertical.below;
            horizontal = vertical;
        }
        // If a linked list has been exhausted, then it should be deleted as well.
        Event newHeadPlace = new Event(0, null);
        for (int i = 0; i < height; i++) {
            vertical = head.below;
            if (vertical.next.time == POSINF) {
                head = vertical;
                height--;
            }
        }
        return thingToReturn;
    }


    // Prints the skip list.
    public void printSkipList() {
        Event vertical = head;
        Event horizontal = vertical;
        // For every linked list in the skip list
        for (int i = 0; i <= height; i++) {
            // Print its level
            System.out.print("(S" + (height - i) + ") ");
            // If the linked list is empty, then print it so.
            if (horizontal.next.time == POSINF && horizontal == head) {
                System.out.print("empty");
            }
            // While we haven't reached the end of the linked list, print each element
            while (horizontal.time != POSINF) {
                // ... that isn't at the very beginning at negative infinity.
                if (horizontal.time != NEGINF) {
                    System.out.print(horizontal.time + ":" + horizontal.name + " ");
                }
                horizontal = horizontal.next;
            }
            vertical = vertical.below;
            horizontal = vertical;
            System.out.println();
        }
    }


    // If the amount of heads warrants the creation of a new linked list, do so.
    private void canIncreaseLevel(int level) {
        if (level >= height) {
            height++;
            newList();
        }
    }


    // Create a new skip list, assuming the first has already been created.
    private void newList() {
        Event newHead = new Event(NEGINF, null);
        Event newTail = new Event(POSINF, null);

        newHead.next = newTail;
        newHead.below = head;
        newTail.below = tail;

        head = newHead;
        tail = newTail;
    }
}