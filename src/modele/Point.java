package modele;

public class Point {
    private int x;
    private int y;

    public Point(int _x, int _y) {
        x = _x; // En colonne
        y = _y; // En ligne
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
