package org.example;

import java.util.ArrayList;
import java.util.List;

public class Animal implements IElement {
    private Vector2d position;
    private Gen orientation;
    private int energy;
    private Genotype genotype;
    private List<IPositionChangeObserver> observerList;
    private InfernalPortal map;
    private int age;
    private int numOfChildren;
    private int indexOfActiveGen;
    //ToDO: zamienic na uppercasy
    private final int energyGainedFromPlant;
    private final int energyRequiredToReproduce;
    private final int energyUsedByReproduction;
    private DataSet data;
    private int numberOfEatedPlants;
    private boolean isAlive;
    private int dayOfDeath;

    public Vector2d getPosition() {
        return position;
    }

    public int getEnergy() {
        return energy;
    }

    public Genotype getGenotype() {
        return genotype;
    }

    public Animal(Vector2d position, Gen orientation, int energy, Genotype genotype, InfernalPortal map, DataSet data) {
        this.position = position;
        this.orientation = orientation;
        this.energy = energy;
        this.genotype = genotype;
        this.map = map;
        this.age=0;
        this.numOfChildren=0;
        this.observerList = new ArrayList<>();
        this.indexOfActiveGen = 0;
        this.numberOfEatedPlants=0;
        this.isAlive=true;
        //to mozna dodac do funkcji place w mapie lepiej
        this.addObserver(this.map);
        this.energyGainedFromPlant = data.getEnergyFromPlant();
        this.energyRequiredToReproduce = data.getEnergyRequiredToReproduce();
        this.energyUsedByReproduction = data.getEnergyUsedToMakeChild();
        this.data=data;


    }



    public int getNumOfChildren() {
        return numOfChildren;
    }

    public void eat() {
        this.energy+=energyGainedFromPlant;
        this.numberOfEatedPlants+=1;

    }

    public void reduceEnergy(int usedEnergy) {
        this.energy -= usedEnergy;
    }


    public String toString() {
        String textAnimal = "Animal: " +
                " active gen=" + orientation +
                ", energy =" + energy +
                ", genotype=" + genotype +
                ", age=" + age +
                ", number of kids = " + numOfChildren;
        if (this.isAlive) {
            return textAnimal;
        } else {
            return "Animal died on " + this.dayOfDeath + " day of simulation";
        }

    }

    // HELPER FUNCTION
//    @Override
//    public String toString() {
//        return this.orientation.toString();
//    }

    private boolean canReproduce(Animal otherAnimal) {
        return (this.position.equals(otherAnimal.getPosition()) && this.energy >= this.energyRequiredToReproduce &&
                otherAnimal.getEnergy() >= this.energyRequiredToReproduce);
    }

    public int getAge() {
        return age;
    }

    public Animal reproduce(Animal otherAnimal) {
        if (this.canReproduce(otherAnimal)) {
            this.reduceEnergy(this.energyUsedByReproduction);
            otherAnimal.reduceEnergy(this.energyUsedByReproduction);
            int energyOfBabyAnimal = this.energyUsedByReproduction * 2;
            Genotype genotypeOfBaby = new Genotype(this, otherAnimal, this.data);
            Animal babyAnimal = new Animal(this.position, new Gen(0), energyOfBabyAnimal, genotypeOfBaby, this.map, this.data);
            this.numOfChildren+=1;
            otherAnimal.numOfChildren+=1;

            return babyAnimal;
        }
        return null;
    }


    public void setOrientation() {
        this.orientation = this.genotype.getArrayOfGens()[this.indexOfActiveGen];
    }

    public void setIndexOfActiveGen() {
        if (this.indexOfActiveGen < this.genotype.getArrayOfGens().length -1) {
            this.indexOfActiveGen+=1;
        }
        else {
            this.indexOfActiveGen=0;
        }
    }

    public void move() {
        this.setOrientation();
        Vector2d newVector = this.position;
        switch (orientation.getRotation()) {
            case 0 -> newVector = this.position.add(new Vector2d(0,1));
            case 1 -> newVector = this.position.add(new Vector2d(1,1));
            case 2 -> newVector = this.position.add(new Vector2d(1,0));
            case 3 -> newVector = this.position.add(new Vector2d(1,-1));
            case 4 -> newVector = this.position.add(new Vector2d(0,-1));
            case 5 -> newVector = this.position.add(new Vector2d(-1,-1));
            case 6 -> newVector = this.position.add(new Vector2d(-1,0));
            case 7 -> newVector = this.position.add(new Vector2d(-1,1));
        }
        if (this.map.checkIfMagicPortal(newVector)) {
            //System.out.println("magic portal");
            this.teleportToMagicPortal();
        }
        else{
            this.energy-= 1;
            this.informObserversAboutChangedPosition(this.position, newVector);
            this.position = newVector;
        }
        this.setIndexOfActiveGen();

    }

    private void teleportToMagicPortal() {
        this.energy-=this.energyUsedByReproduction;
        Vector2d newPosition = this.map.generateRandomPositionOnTheMap();
        this.informObserversAboutChangedPosition(this.position, newPosition);
        this.position = newPosition;

    }


    public void getOlder() {
        this.age+=1;
    }
    private void informObserversAboutChangedPosition(Vector2d oldPosition, Vector2d newPosition) {
        for (IPositionChangeObserver observer : this.observerList) {
            observer.positionChanged(oldPosition, newPosition, this);
        }
    }

    public static Animal strongerAnimal(Animal firstAnimal, Animal secondAnimal) {
        if (firstAnimal.getEnergy() >= secondAnimal.getEnergy()) {
            return firstAnimal;
        }
        return secondAnimal;
    }

    public static Animal weakerAnimal(Animal firstAnimal, Animal secondAnimal) {
        if (firstAnimal.getEnergy() < secondAnimal.getEnergy()) {
            return firstAnimal;
        }
        return secondAnimal;
    }

    public void addObserver(IPositionChangeObserver observer) {
        this.observerList.add(observer);
    }

    public void die(Simulation simulation) {
        this.isAlive = false;
        this.dayOfDeath = simulation.getDayOfSimulation();
    }



    @Override
    public String getNameOfPathElement() {
        return "src/main/resources/animal.png";
    }
}

