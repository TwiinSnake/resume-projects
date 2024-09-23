import java.lang.Character;

public class MapPiece {
    char map;
    int x;
    int y;
    boolean discovery;
    boolean isDot;
    // double weight;
    int distance;
    MapPiece up = null;
    MapPiece right = null;
    MapPiece down = null;
    MapPiece left = null;

    public MapPiece (char map, int x, int y) {
        this.map = map;
        this.x = y;
        this.y = x;
        this.distance = -1;
        if (this.map == '.') {
            isDot = true;
        }
        if (Character.isUpperCase(this.map) && this.map != 'P') {
            isDot = true;
        }
        /*
        if (map == '.') {
            weight = 0.5;
        }
        if (map != '.') {
            weight = 1.5;
        }
        */
    }
}