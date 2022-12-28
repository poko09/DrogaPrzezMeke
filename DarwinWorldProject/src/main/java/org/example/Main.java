package org.example;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {



        //Application.launch(App.class, args);
//        Genotype genotype = new Genotype();
//        Genotype genotype2 = new Genotype();
        DataSet data = new DataSet("parametry.txt");
        InfernalPortal map = new InfernalPortal(data);
//        Animal a1 = new Animal(new Vector2d(2,2), new Gen(0), -40, genotype,map);
//        Animal a2 = new Animal(new Vector2d(2,2), new Gen(1), -45, genotype2,map);
//        Animal a5 = new Animal(new Vector2d(2,2), new Gen(2), 50, genotype2,map);
//        Animal a3 = new Animal(new Vector2d(2,4), new Gen(3), 40, genotype,map);
//        Animal a4 = new Animal(new Vector2d(2,4), new Gen(4), 40, genotype2,map);
        Simulation simulation = new Simulation(map,data);
        ArrayList<ArrayList<Animal>> listOfList =map.mostPopularGenotype();
        for (ArrayList<Animal> list : listOfList) {
            System.out.println(list.size());
        }
//        map.placeAnimalOnTheMap(a1, simulation);
//        map.placeAnimalOnTheMap(a2,simulation);
//        map.placeAnimalOnTheMap(a3,simulation);
//        map.placeAnimalOnTheMap(a4,simulation);
//        map.placeAnimalOnTheMap(a5,simulation);


//        for (Map.Entry<Vector2d, ArrayList<Animal>> set : map.getAnimals().entrySet()) {
//            System.out.println(set.getKey());
//            for (Animal animal : set.getValue()) {
//                System.out.println( animal);
//            }
//        }
//        map.deleteDeadAnimalsFromTheMap();
//
//        for (Map.Entry<Vector2d, ArrayList<Animal>> set : map.getAnimals().entrySet()) {
//            System.out.println(set.getKey());
//            for (Animal animal : set.getValue()) {
//                System.out.println( animal);
//            }
//        }
//        System.out.println("tombs");
//        for (Animal a : map.getTombs()) {
//            System.out.println( a);
//        }
//        simulation.reproductionOfAnimal();
//        System.out.println( "reproduction animals");
//        for (Map.Entry<Vector2d, ArrayList<Animal>> set : map.getAnimals().entrySet()) {
//            System.out.println(set.getKey());
//            for (Animal animal : set.getValue()) {
//                System.out.println( animal);
//            }
//        }
//        a1.move();
//        for (Map.Entry<Vector2d, ArrayList<Animal>> set : map.getAnimals().entrySet()) {
//            System.out.println(set.getKey());
//            for (Animal animal : set.getValue()) {
//                System.out.println( animal);
//            }
//        }





    }
}