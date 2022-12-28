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

    public InfernalPortal( DataSet data) {
        this.width = data.getWidthOfMap();
        this.height = data.getHeightOfMap();

    }
    // dodac rosliny
    public ArrayList<Animal> objectAt(Vector2d position) {
        return this.animals.get(position);
    }


    public boolean isOccupied(Vector2d position) {
        return objectAt(position) != null;
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

    public void deleteDeadAnimalsFromTheMap(Simulation simulation) {
        // czy mozna jakos lepiej po tym iterowac
        for (ArrayList<Animal> listOfAnimals : animals.values()) {
            ArrayList<Animal> listOfAnimalsCopy = new ArrayList<>();
            listOfAnimalsCopy.addAll(listOfAnimals);
            for (Animal animal : listOfAnimalsCopy) {
                if(animal.getEnergy()<=0) {
                    animal.die(simulation);
                    this.tombs.add(animal);
                    listOfAnimals.remove(animal);
                    simulation.reduceNumberOfAnimals();
                }
            }
            }
        animals.values().removeIf(value -> value.size() == 0);
//                this.animals.values().stream().
//                forEach(listOfAnimals -> listOfAnimals.
//                removeIf(animal -> animal.getEnergy() <=0));

    }



//    public ArrayList<ArrayList<Animal>> mostPopularGenotype() {
//        HashMap<Genotype, ArrayList<Animal>> allGenotypes = new HashMap<>();
//        for (ArrayList<Animal> listOfAnimals : animals.values()) {
//            for (Animal animal : listOfAnimals) {
//                if (allGenotypes.containsKey(animal.getGenotype())) {
//                    allGenotypes.get(animal.getGenotype()).add(animal);
//                } else {
//                    ArrayList<Animal> listOfAnimalsWithThatGenotype = new ArrayList<>();
//                    listOfAnimalsWithThatGenotype.add(animal);
//                    allGenotypes.put(animal.getGenotype(), listOfAnimalsWithThatGenotype);
//                }
//            }
//        }
//        // Todo: poprawic to zeby było prosciej
//        Collection<ArrayList<Animal>> arrayListOfAnimals =  allGenotypes.values();
//        listComparator comparator = new listComparator();
//        Collections.sort(arrayListOfAnimals, comparator);
//        return arrayListOfAnimals;
//    }

    public class listComparator implements Comparator<ArrayList<Animal>> {

        @Override
        public int compare(ArrayList<Animal> o1, ArrayList<Animal> o2) {
            return o1.size() - o2.size();
        }
    }



    //HELPER FUNCTION-> DELTE LATER
    public void listAllAnimals() {
        for (Map.Entry<Vector2d, ArrayList<Animal>> set : animals.entrySet()) {
            System.out.println(set.getKey());
            for (Animal animal : set.getValue()) {
                System.out.println(animal.toString());
            }
        }
    }

    public String toString() {
        MapVisualizer mapVisualizer = new MapVisualizer(this);
        return mapVisualizer.draw(new Vector2d(0,0),new Vector2d(this.width, this.height));
    }

}


