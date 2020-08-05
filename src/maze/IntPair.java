package maze;

import java.util.Objects;

public class IntPair{
    private final int first;
    private final int second;

    public IntPair(int first, int second) {
        this.first = first;
        this.second = second;
    }

    public int getFirst() {
        return first;
    }

    public int getSecond() {
        return second;
    }

    @Override
    public String toString() {
        return "(" + first + ", " + second + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof IntPair)) return false;
        IntPair intPair = (IntPair) o;
        return first == intPair.first &&
                second == intPair.second;
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, second);
    }
}
