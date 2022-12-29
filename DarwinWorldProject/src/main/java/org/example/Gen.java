package org.example;

import java.util.Objects;
import java.util.Random;

public class Gen {
    private int rotation;

    public Gen(int rotation) {
        if (rotation >= 0 && rotation <8 ) {
            this.rotation = rotation;
        }
        else {
            throw new IllegalArgumentException("Wrong value!");
        }

    }

    public Gen () {
        this.rotation = new Random().nextInt(8);
    }

    public int getRotation() {
        return rotation;
    }

    public void next() {
        if (rotation >= 0 && rotation <7) {
            this.rotation+=1;
        }
        if (rotation==7) {
            this.rotation=0;
        }
    }

    public void previous() {
        if (rotation >= 1 && rotation <8) {
            this.rotation-=1;
        }
        if (rotation==0) {
            this.rotation=7;
        }
    }

    public void randomlyChangeGene() {
        int nextOrPrevious = new Random().nextInt(2);
        if (nextOrPrevious==0) {
            this.next();

        }
        else {
            this.previous();
        }


    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Gen gen = (Gen) o;
        return rotation == gen.rotation;
    }

    @Override
    public int hashCode() {
        return Objects.hash(rotation);
    }

    @Override
    public String toString() {
        return Integer.toString(rotation);
    }
}
