public class Event {
    public Event next;
    public Event below;
    public int time;
    public String name;

    public Event (int time, String name) {
        this.next = null;
        this.below = null;
        this.time = time;
        this.name = name;
    }
}