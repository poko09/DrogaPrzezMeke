package org.example;

import java.util.Objects;

public abstract class Plant implements IElement{

    protected Vector2d position;

    public Plant(Vector2d position){

        this.position = position;

    }

    public Vector2d getPosition() {
        return position;
    }

    public boolean isAt(Vector2d position) {
        return Objects.equals(this.position, position);
    }


}
