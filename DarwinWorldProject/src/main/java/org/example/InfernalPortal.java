package org.example;

import java.util.*;
import java.util.stream.Collectors;

public class InfernalPortal implements IPositionChangeObserver {
    private int width;
    private int height;
    private ArrayList<Animal> tombs = new ArrayList();

    public Map<Vector2d, ArrayList<Animal>> getAnimals() {
        return animals;
    }

    protected Map<Vector2d, ArrayList <Animal>> animals = new HashMap<>();
    protected Map<Vector2d, Plant> plants = new HashMap<>();

    public InfernalPortal(int width, int height) {
        this.width = width;
        this.height = height;

    }
    public Object objectAt(Vector2d position) {
        return this.animals.get(position);
    }

    public boolean checkIfMagicPortal(Vector2d position) {

        if (!position.precedes(new Vector2d(this.width, this.height)) || !position.follows(new Vector2d(0, 0))) {
            return true;
        }
        return false;
    }

    public boolean checkIfPlantIsAtPosition(Vector2d position) {
        return this.plants.get(position)!= null;
    }

    public boolean checkIfDrawWithEating(Vector2d position) {
        return this.animals.get(position).size() > 1;
    }

    public Animal solveDrawWithEatingOrReproducing(ArrayList<Animal> animals) {
        animals = this.theStrongestAnimalsFromList(animals);
        if (animals.size() >1) {
            animals = this.theOldestAnimalsFromList(animals);
            if (animals.size() > 1) {
                animals = this.animalsWithMostKidsFromList(animals);
            }
        }
        return animals.get(0);
    }

    public ArrayList<Animal> theStrongestAnimalsFromList(ArrayList<Animal> animals) {
        Animal animalWithMaxEnergy = animals.stream().max(Comparator.comparing(Animal::getEnergy)).orElse(null);
        ArrayList<Animal> strongestAnimals =  animals.stream().
                filter(animal -> animal.getEnergy() == animalWithMaxEnergy.getEnergy()).
                collect(Collectors.toCollection(ArrayList::new));
        return strongestAnimals;
    }

    public ArrayList<Animal> theOldestAnimalsFromList(ArrayList<Animal> animals) {
        Animal oldestAnimal = animals.stream().max(Comparator.comparing(Animal::getAge)).orElse(null);
        ArrayList<Animal> oldestAnimals =  animals.stream().
                filter(animal -> animal.getAge() == oldestAnimal.getAge()).
                collect(Collectors.toCollection(ArrayList::new));
        return oldestAnimals;
    }

    public ArrayList<Animal> animalsWithMostKidsFromList(ArrayList<Animal> animals) {
        Animal animalWithMostKids = animals.stream().max(Comparator.comparing(Animal::getNumOfChildren)).orElse(null);
        ArrayList<Animal> animalsWithMostKids =  animals.stream().
                filter(animal -> animal.getNumOfChildren() == animalWithMostKids.getNumOfChildren()).
                collect(Collectors.toCollection(ArrayList::new));
        return animalsWithMostKids;
    }

    public void placeAnimalOnTheMap(Animal animal, Simulation simulation) {
        if (animals.containsKey(animal.getPosition())) {
            animals.get(animal.getPosition()).add(animal);
        }
        else {
            ArrayList<Animal> animalsList = new ArrayList<>();
            animalsList.add(animal);
            animals.put(animal.getPosition(), animalsList);
        }
        simulation.addNewGenotype(animal.getGenotype());
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    @Override
    public void positionChanged(Vector2d oldPosition, Vector2d newPosition, Animal animal) {
        ArrayList<Animal> listOfAnimalsAtThisPosition = this.animals.get(oldPosition);
        if (listOfAnimalsAtThisPosition.size() > 1) {
            if (listOfAnimalsAtThisPosition.contains(animal)) {
                listOfAnimalsAtThisPosition.remove(animal);
            }

        }
        else {
                this.animals.remove(oldPosition);
        }
        if (animals.containsKey(newPosition)) {
            animals.get(newPosition).add(animal);
        }
        else {
            ArrayList<Animal> animalsList = new ArrayList<>();
            animalsList.add(animal);
            animals.put(newPosition, animalsList);
        }

        }


    public Vector2d generateRandomPositionOnTheMap() {
        int randomX = new Random().nextInt(this.width+1);
        int randomY = new Random().nextInt(this.height+1);
        return new Vector2d(randomX, randomY);
    }

    public ArrayList<Animal> getTombs() {
        return tombs;
    }

    public void deleteDeadAnimalsFromTheMap() {
        // czy mozna jakos lepiej po tym iterowac
        for (ArrayList<Animal> listOfAnimals : animals.values()) {
            ArrayList<Animal> listOfAnimalsCopy = new ArrayList<>();
            listOfAnimalsCopy.addAll(listOfAnimals);
            for (Animal animal : listOfAnimalsCopy) {
                if(animal.getEnergy()<=0) {
                    this.tombs.add(animal);
                    listOfAnimals.remove(animal);
                }
            }
            }
        animals.values().removeIf(value -> value.size() == 0);
//                this.animals.values().stream().
//                forEach(listOfAnimals -> listOfAnimals.
//                removeIf(animal -> animal.getEnergy() <=0));

    }

}

