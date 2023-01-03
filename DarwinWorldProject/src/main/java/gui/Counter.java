package gui;

public class Counter {
    int count;

    public Counter() {
        this.count = 1;
    }

    public void increase() {
        this.count++;
    }

    public int getCount() {
        return count;
    }
}
