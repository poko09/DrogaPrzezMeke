package wildlife;

import darwin_world.Vector2d;

public abstract class Plant  {
    protected Vector2d position;

    public Plant(Vector2d position){
        this.position = position;
    }

    public Vector2d getPosition() {
        return position;
    }

}
