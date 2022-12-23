package org.example;

import java.util.ArrayList;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        Genotype genotype = new Genotype();
        Genotype genotype2 = new Genotype();
        InfernalPortal map = new InfernalPortal(5,5);
        Animal a1 = new Animal(new Vector2d(2,2), new Gen(0), 40, genotype,map);
        Animal a2 = new Animal(new Vector2d(2,2), new Gen(0), 40, genotype2,map);
        Animal a3 = new Animal(new Vector2d(2,3), new Gen(0), 40, genotype,map);
        Animal a4 = new Animal(new Vector2d(2,4), new Gen(0), 40, genotype2,map);
        map.placeAnimalOnTheMap(a1);
        map.placeAnimalOnTheMap(a2);
        map.placeAnimalOnTheMap(a3);
        map.placeAnimalOnTheMap(a4);
        a1.move();
        for (Map.Entry<Vector2d, ArrayList<Animal>> set : map.getAnimals().entrySet()) {
            System.out.println(set.getKey());
            for (Animal animal : set.getValue()) {
                System.out.println( animal);
            }
        }

    }
}