package org.example;

public class ForestedEquatoria extends Plant{


    public ForestedEquatoria(Vector2d position) {
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
