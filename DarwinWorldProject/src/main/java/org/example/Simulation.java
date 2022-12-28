package org.example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Simulation implements Runnable {

    public final int STARTING_NUMBER_OF_ANIMALS;
    public final int STARTING_ENERGY_OF_ANIMAL;
    public final int STARTING_NUMBER_OF_PLANTS;
    private InfernalPortal map;
    private int numberOfAnimals;

    public int getNumberOfAnimals() {
        return numberOfAnimals;
    }

    private int numberOfPlants;
    // ToDo: uzupelnic logike do tego
    private int numberOfFreeFields;
    private ArrayList<Genotype> listOfGenotypes;
    private int avarageEnergyOfAnimals;
    private int moveDelay;
    private List<IAppObserver> appObserverList;
    private DataSet data;
    private int dayOfSimulation;
    public Simulation(InfernalPortal map, DataSet data) {
        this.STARTING_NUMBER_OF_ANIMALS = data.getNumberOfAnimals();
        this.STARTING_ENERGY_OF_ANIMAL = data.getInitialEnergyOfAnimals();
        this.STARTING_NUMBER_OF_PLANTS = data.getNumberOfPlants();
        this.data = data;
        this.map = map;
        this.listOfGenotypes = new ArrayList<>();
        this.createAndPlaceAnimalsOnTheMap();
        this.numberOfAnimals = STARTING_NUMBER_OF_ANIMALS;
        this.numberOfPlants = STARTING_NUMBER_OF_PLANTS;
        this.appObserverList = new ArrayList<>();
        this.dayOfSimulation=0;

    }

    public void createAndPlaceAnimalsOnTheMap() {
        for (int i = 0; i < STARTING_NUMBER_OF_ANIMALS; i ++) {
            Animal a = new Animal(this.map.generateRandomPositionOnTheMap(), new Gen(0), STARTING_ENERGY_OF_ANIMAL,
                    new Genotype(this.data),this.map, this.data);
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
            // created copy to avoid concurrent modification exception
            ArrayList<Animal> listOfAnimalsCopy = new ArrayList<>(listOfAnimals);
            for (Animal animal : listOfAnimalsCopy) {
                animal.move();
            }
        }
    }
    public void reduceNumberOfAnimals() {
        this.numberOfAnimals-=1;
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

    public int getDayOfSimulation() {
        return dayOfSimulation;
    }

    public void simulationOfOneDay() {
        this.dayOfSimulation+=1;
        // te funkcje moze jednak lepiej wrzucic do symulacji
        this.map.deleteDeadAnimalsFromTheMap(this);
        this.moveAllAnimals();
        this.animalsEatPlants();
        this.reproductionOfAnimal();
        this.growthOfNewPlants();
        this.animalsGetsOlder();

    }

    public void run() {

        while(true) {
            this.simulationOfOneDay();
            this.informObservers();

            try {
                Thread.sleep(this.moveDelay);
            } catch (InterruptedException e) {
            }

        }
    }

    public void setMoveDelay(int moveDelay) {
        this.moveDelay = moveDelay;
    }

    public void addAppObserver(IAppObserver observer) {
        this.appObserverList.add(observer);
    }

    public void informObservers() {
        for (IAppObserver observer: this.appObserverList){
            observer.positionChangedApp();
        }
    }

}

