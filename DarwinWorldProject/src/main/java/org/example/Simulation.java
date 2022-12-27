package org.example;

import java.util.ArrayList;
import java.util.HashMap;

public class Simulation {

    public final int STARTING_NUMBER_OF_ANIMALS = 8;
    public final int STARTING_ENERGY_OF_ANIMAL = 100;
    public final int STARTING_NUMBER_OF_PLANTS = 20;
    private InfernalPortal map;
    private int numberOfAnimals;
    private int numberOfPlants;
    //uzupelnic logike do tego
    private int numberOfFreeFields;
    private ArrayList<Genotype> listOfGenotypes;
    private int avarageEnergyOfAnimals;

    public Simulation(InfernalPortal map) {
        this.map = map;
        this.listOfGenotypes = new ArrayList<>();
        this.createAndPlaceAnimalsOnTheMap();
        this.numberOfAnimals = STARTING_NUMBER_OF_ANIMALS;
        this.numberOfPlants = STARTING_NUMBER_OF_PLANTS;



    }

    public void createAndPlaceAnimalsOnTheMap() {
        for (int i = 0; i < STARTING_NUMBER_OF_ANIMALS; i ++) {
            Animal a = new Animal(this.map.generateRandomPositionOnTheMap(), new Gen(0), STARTING_ENERGY_OF_ANIMAL,
                    new Genotype(),this.map);
            map.placeAnimalOnTheMap(a,this);
        }
    }


    public void animalsEatPlants() {

    }

    public void growthOfNewPlants() {

    }
    public void addNewGenotype(Genotype genotype) {
        this.listOfGenotypes.add(genotype);
    }
    public void calculateAverageEnergy() {
        int energy = 0;
        // faknie byloby zmienic na streama
        for (ArrayList<Animal> listOfAnimals : this.map.getAnimals().values()) {
            for (Animal animal : listOfAnimals) {
                energy+=animal.getEnergy();
            }
        }
        this.avarageEnergyOfAnimals=energy/this.numberOfAnimals;
    }
    public void moveAllAnimals() {
        HashMap<Vector2d, ArrayList <Animal>> animalsCopy = new HashMap<Vector2d, ArrayList <Animal>>(this.map.getAnimals());
        for (ArrayList<Animal> listOfAnimals : animalsCopy.values()) {
            for (Animal animal : listOfAnimals) {
                animal.move();
            }
        }
    }

    public void reproductionOfAnimal() {
        HashMap<Vector2d, ArrayList <Animal>> animalsCopy = new HashMap<Vector2d, ArrayList <Animal>>(this.map.animals);
        for (ArrayList<Animal> listOfAnimals : animalsCopy.values()) {
            if (listOfAnimals.size() > 1) {
                if (listOfAnimals.size() > 2) {
                    Animal parent1 = this.map.solveDrawWithEatingOrReproducing(listOfAnimals);
                    ArrayList<Animal> listOfAnimalsCopy = new ArrayList<>();
                    listOfAnimalsCopy.addAll(listOfAnimals);
                    listOfAnimalsCopy.remove(parent1);
                    Animal parent2 = this.map.solveDrawWithEatingOrReproducing(listOfAnimalsCopy);
                    Animal babyAnimal = parent1.reproduce(parent2);
                    if (babyAnimal!=null) {
                        this.map.placeAnimalOnTheMap(babyAnimal,this);
                        this.numberOfAnimals+=1;
                    }
                }
                else {
                    Animal babyAnimal = listOfAnimals.get(0).reproduce(listOfAnimals.get(1));
                    if (babyAnimal!=null) {
                        this.map.placeAnimalOnTheMap(babyAnimal,this);
                        this.numberOfAnimals+=1;
                    }
                }



            }
        }
    }
    public void animalsGetsOlder() {
            this.map.getAnimals().values().stream().
            forEach(listOfAnimals -> listOfAnimals.
                    forEach(animal -> animal.getOlder()));
    }

    public void simulationOfOneDay() {
        this.map.deleteDeadAnimalsFromTheMap();
        this.moveAllAnimals();
        this.animalsEatPlants();
        this.reproductionOfAnimal();
        this.growthOfNewPlants();
        this.animalsGetsOlder();

    }

    public void wholeSimulation() {
        while(true) {
            this.simulationOfOneDay();
        }
    }

}

