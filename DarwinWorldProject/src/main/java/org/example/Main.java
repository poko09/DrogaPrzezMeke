package org.example;

import gui.Menu;
import javafx.application.Application;

public class Main {
    public static void main(String[] args) {


        try {
            Application.launch(Menu.class, args);
        }
        catch (IllegalArgumentException ex) {
            System.exit(0);

        }

//        DataSet ds = new DataSet("parametry.txt");
//        InfernalPortal mapka = new InfernalPortal(ds);
//        Simulation engine = new Simulation(mapka, ds);
//        engine.simulationOfOneDay();
////        System.out.println(mapka);
//        engine.simulationOfOneDay();
////        System.out.println(mapka);
//        engine.simulationOfOneDay();
////        System.out.println(mapka);
//        engine.simulationOfOneDay();
////        System.out.println(mapka);
//        engine.simulationOfOneDay();
////        System.out.println(mapka);
//        engine.simulationOfOneDay();
////        System.out.println(mapka);
////        engine.run();
//        engine.simulationOfOneDay();
//
//        System.out.println(mapka.getAnimals().toString());














// symulacja
//        ToxicCorpses toxic = new ToxicCorpses(new Vector2d(0,0));
//        ToxicCorpses toxic1 = new ToxicCorpses(new Vector2d(6,5));
//        ToxicCorpses toxic2 = new ToxicCorpses(new Vector2d(7,8));
//
//        System.out.println(toxic.getPosition());
//        ForestedEquatoria equatoria = new ForestedEquatoria(new Vector2d(3,3));
//        System.out.println(equatoria.isAt(new Vector2d(3,4)));
//
//        Map<Vector2d, Plant> plants = new HashMap<>();
//        //asdasdpoaskdpokaspdok
//        plants.put(toxic.getPosition(), toxic);
//        plants.put(toxic1.getPosition(), toxic1);
//        plants.put(toxic2.getPosition(), toxic2);
//        System.out.println("=============");
//
//        plants.forEach(
//                (key, value)
//                -> System.out.println(key));
//
//

//        InfernalPortal map = new InfernalPortal(ds);
//
//        InfernalPortal mapka = new InfernalPortal(ds);
//        //System.out.println(mapka);
//
//        Simulation engine = new Simulation(mapka, ds);
//        //engine.run();
//        System.out.println(mapka);


//        DataSet ds = new DataSet("parametry.txt");
//        InfernalPortal mapka = new InfernalPortal(ds);
//        Simulation engine = new Simulation(mapka, ds);
//        System.out.println(mapka);






////        toDO toksyczne trupy
//        int height = 10;
//        int width = 10;
//        Vector2d v1 = new Vector2d(0,0);
//        ArrayList<Vector2d> cementary = new ArrayList<>();
//        cementary.add(new Vector2d(0,0));
//        cementary.add(new Vector2d(2,2));
//        cementary.add(new Vector2d(2,2));
//        cementary.add(new Vector2d(1,1));
//        cementary.add(new Vector2d(0,0));
//
//        Map<Vector2d, ArrayList<Vector2d>> listOfDeadbodies = new HashMap<>();
//
//        for(int i = 0; i<=height; i++){
//            for(int j = 0; j<=width;j++) {
//                listOfDeadbodies.put(new Vector2d(j,i), new ArrayList<Vector2d>());
//            }
//        }
//        System.out.println(v1);
//        ArrayList<Vector2d> deadAnimals = new ArrayList<>();
//
//        for(Vector2d positionOfDead : cementary) {
//            if(listOfDeadbodies.containsKey(positionOfDead)) {
//                deadAnimals.add(positionOfDead);
//
//            }
//
//        }
//        deadAnimals.forEach(
//                (element)
//                -> System.out.println(element));

//        listOfDeadbodies.forEach(
//                (key, value)
//                        -> System.out.println(key));

//       Comparator<Vector2d> bigger = (o1, o2) -> o1.getX().compareTo(o2.getX());

//        deadAnimals.sort(Comparator.comparing(Vector2d::getX));
//        deadAnimals.sort(Comparator.comparing(Vector2d::getY));
//        deadAnimals.forEach(
//                (element)
//                        -> System.out.println(element));
//
//        int x = 8;
//        int y = 2;
//        if(deadAnimals.size() >= y) {
//            System.out.println("no siemka, tu se wyrosne");
//        }
//        ArrayList<Vector2d> dupa = new ArrayList();







//        Forested Equatoria - test
//        System.out.println("==========='='='='='='='='");
//
//        int numberOfPlants = 20;
////
//        int insideEquatoria = (int) (0.8 * numberOfPlants);
//        int outsideEquatoria = numberOfPlants - insideEquatoria;
//
//        int upperEquatoria = (int) (0.6 * 10);
//        int lowerEqatoria = (int)(0.4 * 10);
//
//        Random rand = new Random();
//
//        //        toDo zdekomponuj to byczku!
//
//        for(int i = 0; i < insideEquatoria; i++) {
//
//            int x = rand.nextInt(10);
//            int y = rand.nextInt((upperEquatoria - lowerEqatoria) + 1) + lowerEqatoria;
//            ForestedEquatoria fe = new ForestedEquatoria(new Vector2d(x, y));
////            map.placeForestedEquatoria(fe);
//            System.out.println(fe.getPosition());
//
//
//        }
//        System.out.println("==");
//
//        for(int i = 0; i < outsideEquatoria; i++) {
//            if(i%2==0) {
//                int x = rand.nextInt(10);
//                int y = rand.nextInt((10 - upperEquatoria)+1) + upperEquatoria;
//                ForestedEquatoria fe = new ForestedEquatoria(new Vector2d(x, y));
////                map.placeForestedEquatoria(fe);
//                System.out.println(fe.getPosition());
//
//            }
//            else {
//                int x = rand.nextInt(10);
//                int y = rand.nextInt((lowerEqatoria) + 1);
//                ForestedEquatoria fe = new ForestedEquatoria(new Vector2d(x, y));
////                map.placeForestedEquatoria(fe);
//                System.out.println(fe.getPosition());
//            }
//        }












          //Application.launch(App.class, args);
//        Genotype genotype = new Genotype();
//        Genotype genotype2 = new Genotype();
//        InfernalPortal map = new InfernalPortal(50,50);
//        Animal a1 = new Animal(new Vector2d(2,2), new Gen(0), -40, genotype,map);
//        Animal a2 = new Animal(new Vector2d(2,2), new Gen(1), -45, genotype2,map);
//        Animal a5 = new Animal(new Vector2d(2,2), new Gen(2), 50, genotype2,map);
//        Animal a3 = new Animal(new Vector2d(2,4), new Gen(3), 40, genotype,map);
//        Animal a4 = new Animal(new Vector2d(2,4), new Gen(4), 40, genotype2,map);
//        Simulation simulation = new Simulation(map);
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