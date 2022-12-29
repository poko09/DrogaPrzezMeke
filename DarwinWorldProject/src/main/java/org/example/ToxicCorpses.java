package org.example;

public class ToxicCorpses extends Plant {


    public ToxicCorpses(Vector2d position) {
        super(position);
    }

    @Override
    public String toString() {
        return "*";
    }

    @Override
    public String getNameOfPathElement() {
        return "src/main/resources/toxicCorpses.png";
    }
}
