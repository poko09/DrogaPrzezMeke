package darwin_world;

import file_support.DataSet;
import wildlife.*;
import java.util.*;
import java.util.stream.Collectors;

public class InfernalPortal implements IPositionChangeObserver {
    private final int WIDTH;
    private final int HEIGHT;
    private ArrayList<Animal> tombs = new ArrayList<>();
    protected Map<Vector2d, ArrayList <Animal>> animals = new HashMap<>();
    protected Map<Vector2d, Plant> plants = new HashMap<>();

    public InfernalPortal(DataSet data) {
        this.WIDTH = data.getWidthOfMap();
        this.HEIGHT = data.getHeightOfMap();
    }

    // getters
    public Map<Vector2d, ArrayList<Animal>> getAnimals() {
        return animals;
    }

    public int getWIDTH() {
        return WIDTH;
    }

    public Map<Vector2d, Plant> getPlants() {
        return plants;
    }
    public int getHEIGHT() {
        return HEIGHT;
    }

    public ArrayList<Animal> getTombs() {
        return tombs;
    }

    // METHODS

    // methods connected to positions of elements on the map
    public ArrayList<Animal> animalObjectAt(Vector2d position) {
        return this.animals.get(position);
    }
    public boolean isOccupiedByAnimal(Vector2d position) {
        return animalObjectAt(position) != null;
    }
    public boolean isOccupiedByPlant(Vector2d position) {
        return this.plants.containsKey(position);
    }

    public boolean checkIfMagicPortal(Vector2d position) {

        if (!position.precedes(new Vector2d(this.WIDTH -1, this.HEIGHT -1)) || !position.follows(new Vector2d(0, 0))) {
            return true;
        }
        return false;
    }

    public Vector2d generateRandomPositionOnTheMap() {
        int randomX = new Random().nextInt(this.WIDTH);
        int randomY = new Random().nextInt(this.HEIGHT);
        return new Vector2d(randomX, randomY);
    }

    // methods connected to placing and removing elements from the map
    public void placeToxicCorpsesOnTheMap(ToxicCorpses toxicCorpse) {
        plants.put(toxicCorpse.getPosition(), toxicCorpse);

    }
    public void placeForestedEquator(ForestedEquator forestedEquator) {
            plants.put(forestedEquator.getPosition(), forestedEquator);

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

    public void deleteDeadAnimalsFromTheMap(Simulation simulation) {
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
    }

    // methods connected to solving draws

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

    // methods connected to model observer
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

    // helper method
    public Genotype mostPopularGenotype() {
        HashMap<Genotype, ArrayList<Animal>> allGenotypes = new HashMap<>();

        for (ArrayList<Animal> listOfAnimals : animals.values()) {
            for (Animal animal : listOfAnimals) {
                if (allGenotypes.containsKey(animal.getGenotype())) {
                    allGenotypes.get(animal.getGenotype()).add(animal);
                } else {
                    ArrayList<Animal> listOfAnimalsWithThatGenotype = new ArrayList<>();
                    listOfAnimalsWithThatGenotype.add(animal);
                    allGenotypes.put(animal.getGenotype(), listOfAnimalsWithThatGenotype);
                }

            }
        }
        Collection<ArrayList<Animal>> collectionOfAnimals =  allGenotypes.values();
        ArrayList<ArrayList<Animal>> arrayListOfAnimals = new ArrayList<>(collectionOfAnimals);
        listComparator comparator = new listComparator();
        Collections.sort(arrayListOfAnimals, comparator);
        return arrayListOfAnimals.get(0).get(0).getGenotype();
    }

    public class listComparator implements Comparator<ArrayList<Animal>> {

        @Override
        public int compare(ArrayList<Animal> o1, ArrayList<Animal> o2) {
            return o2.size() - o1.size();
        }
    }



}

