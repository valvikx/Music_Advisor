package by.valvik.musicadvisor.tuple;

public record Tuple<X, Y>(X x, Y y) {

    public X getX() {

        return x;

    }

    public Y getY() {

        return y;

    }

}
