package ga.gussio.ld38.earthinvaders.math;

public class Circle {
    private float radius;

    private float x;
    private float y;

    public Circle(float x, float y, float radius) {
        this.x = x;
        this.y = y;
        this.radius = radius;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getXCenter() {
        return x + radius;
    }

    public float getYCenter() {
        return y + radius;
    }

    public boolean hasCollision(Circle circle) {
        double xDiff = getXCenter() - circle.getXCenter();
        double yDiff = getYCenter() - circle.getYCenter();

        double distance = Math.sqrt((Math.pow(xDiff, 2) + Math.pow(yDiff, 2)));

        return distance < (radius + circle.getRadius());
    }
}
