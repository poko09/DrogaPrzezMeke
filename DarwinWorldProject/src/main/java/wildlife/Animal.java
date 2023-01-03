package wildlife;
import darwin_world.IPositionChangeObserver;
import darwin_world.InfernalPortal;
import darwin_world.Simulation;
import darwin_world.Vector2d;
import file_support.DataSet;
import java.util.ArrayList;
import java.util.List;

public class Animal {

    // final fields
    private final int ENERGY_GAINED_FROM_PLANT;
    private final int ENERGY_REQUIRED_TO_REPRODUCE;
    private final int ENERGY_USED_BY_REPRODUCTION;
    private int energy;
    private int age;
    private int numOfChildren;
    private int indexOfActiveGen;
    private int numberOfEatedPlants;
    private int dayOfDeath;
    private Vector2d position;
    private Gen orientation;
    private Genotype genotype;
    private List<IPositionChangeObserver> observerList;
    private InfernalPortal map;
    private DataSet data;
    private boolean isAlive;

    public Animal(Vector2d position, Gen orientation, int energy, Genotype genotype, InfernalPortal map, DataSet data) {
        this.position = position;
        this.orientation = orientation;
        this.energy = energy;
        this.genotype = genotype;
        this.map = map;
        this.ENERGY_GAINED_FROM_PLANT = data.getEnergyFromPlant();
        this.ENERGY_REQUIRED_TO_REPRODUCE = data.getEnergyRequiredToReproduce();
        this.ENERGY_USED_BY_REPRODUCTION = data.getEnergyUsedToMakeChild();
        this.data=data;
        this.age=0;
        this.numOfChildren=0;
        this.observerList = new ArrayList<>();
        this.indexOfActiveGen = 0;
        this.numberOfEatedPlants=0;
        this.isAlive=true;
        this.addObserver(this.map);
    }

    // getters
    public Vector2d getPosition() {
        return position;
    }

    public int getEnergy() {
        return energy;
    }

    public Genotype getGenotype() {
        return genotype;
    }

    public int getNumOfChildren() {
        return numOfChildren;
    }

    public int getAge() {
        return age;
    }

    // setters
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

    // FUNCTIONS

    // functions connected with eating
    public void eat() {
        this.energy+= ENERGY_GAINED_FROM_PLANT;
        this.numberOfEatedPlants+=1;

    }

    // functions connected with reproduction
    public void reduceEnergy(int usedEnergy) {
        this.energy -= usedEnergy;
    }

    private boolean canReproduce(Animal otherAnimal) {
        return (this.position.equals(otherAnimal.getPosition()) && this.energy >= this.ENERGY_REQUIRED_TO_REPRODUCE &&
                otherAnimal.getEnergy() >= this.ENERGY_REQUIRED_TO_REPRODUCE);
    }

    public Animal reproduce(Animal otherAnimal) {
        if (this.canReproduce(otherAnimal)) {
            this.reduceEnergy(this.ENERGY_USED_BY_REPRODUCTION);
            otherAnimal.reduceEnergy(this.ENERGY_USED_BY_REPRODUCTION);
            int energyOfBabyAnimal = this.ENERGY_USED_BY_REPRODUCTION * 2;
            Genotype genotypeOfBaby = new Genotype(this, otherAnimal, this.data);
            Animal babyAnimal = new Animal(this.position, new Gen(0), energyOfBabyAnimal, genotypeOfBaby, this.map, this.data);
            this.numOfChildren+=1;
            otherAnimal.numOfChildren+=1;

            return babyAnimal;
        }
        return null;
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

    // functions connected with moving
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
        this.energy-=this.ENERGY_USED_BY_REPRODUCTION;
        Vector2d newPosition = this.map.generateRandomPositionOnTheMap();
        this.informObserversAboutChangedPosition(this.position, newPosition);
        this.position = newPosition;

    }

    // function connected with observer model
    private void informObserversAboutChangedPosition(Vector2d oldPosition, Vector2d newPosition) {
        for (IPositionChangeObserver observer : this.observerList) {
            observer.positionChanged(oldPosition, newPosition, this);
        }
    }

    public void addObserver(IPositionChangeObserver observer) {
        this.observerList.add(observer);
    }

    // function connected with living with dying
    public void die(Simulation simulation) {
        this.isAlive = false;
        this.dayOfDeath = simulation.getDayOfSimulation();
    }

    public void getOlder() {
        this.age+=1;
    }

    // toString function
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

}

