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
    private static final int ENERGY_NEEDED_TO_REPRODUCE = 6;
    private static final int ENERGY_USED_BY_REPRODUCTION = 5;
    private static final int ENERGY_GAINED_BY_EATING = 1;
    private static final int ENERGY_USED_BY_MOVING = 1;
    private static final int ENERGY_GAINED_BY_EATING_PLANT=2;

    public Vector2d getPosition() {
        return position;
    }

    public int getEnergy() {
        return energy;
    }

    public Genotype getGenotype() {
        return genotype;
    }

    public Animal(Vector2d position, Gen orientation, int energy, Genotype genotype, InfernalPortal map) {
        this.position = position;
        this.orientation = orientation;
        this.energy = energy;
        this.genotype = genotype;
        this.map = map;
        this.age=0;
        this.numOfChildren=0;
        this.observerList = new ArrayList<>();
        this.indexOfActiveGen = 0;
        //to mozna dodac do funkcji place w mapie lepiej
        this.addObserver(this.map);
    }



    public int getNumOfChildren() {
        return numOfChildren;
    }

    public void eat() {
        this.energy+=ENERGY_GAINED_BY_EATING;
    }

    public void reduceEnergy(int usedEnergy) {
        this.energy -= usedEnergy;
    }


    public String toString2() {
        return "Animal{" +
                "position=" + position +
                ", orientation=" + orientation +
                ", energy=" + energy +
                ", genotype=" + genotype +
                ", age=" + age +
                ", numOfChildren=" + numOfChildren +
                '}';
    }

    // HELPER FUNCTION
    @Override
    public String toString() {
        return this.orientation.toString();
    }

    private boolean canReproduce(Animal otherAnimal) {
        return (this.position.equals(otherAnimal.getPosition()) && this.energy >= ENERGY_NEEDED_TO_REPRODUCE &&
                otherAnimal.getEnergy() >= ENERGY_NEEDED_TO_REPRODUCE);
    }

    public int getAge() {
        return age;
    }

    public Animal reproduce(Animal otherAnimal) {
        if (this.canReproduce(otherAnimal)) {
            this.reduceEnergy(ENERGY_USED_BY_REPRODUCTION);
            otherAnimal.reduceEnergy(ENERGY_USED_BY_REPRODUCTION);
            int energyOfBabyAnimal = ENERGY_USED_BY_REPRODUCTION * 2;
            Genotype genotypeOfBaby = new Genotype(this, otherAnimal);
            Animal babyAnimal = new Animal(this.position, new Gen(0), energyOfBabyAnimal, genotypeOfBaby, this.map);
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
            System.out.println("magic portal");
            this.teleportToMagicPortal();
        }
        else{
            this.energy-= ENERGY_USED_BY_MOVING;
            this.informObserversAboutChangedPosition(this.position, newVector);
            this.position = newVector;
        }
        this.setIndexOfActiveGen();

    }

    private void teleportToMagicPortal() {
        this.energy-=ENERGY_USED_BY_REPRODUCTION;
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



    @Override
    public String getNameOfPathElement() {
        return "src/main/resources/animal.png";
    }
}

