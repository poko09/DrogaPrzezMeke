package org.example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class Simulation implements Runnable {

    public final int STARTING_NUMBER_OF_ANIMALS;
    public final int STARTING_ENERGY_OF_ANIMAL;
    public final int STARTING_NUMBER_OF_PLANTS;
    public final int PLANT_SELECTION;
    private InfernalPortal map;
    private int numberOfAnimals;
    private int numberOfPlants;
    // ToDo: uzupelnic logike do tego
    private int numberOfFreeFields;
    private ArrayList<Genotype> listOfGenotypes;
    private int avarageEnergyOfAnimals;
    private int moveDelay;
    private List<IAppObserver> appObserverList;
    private DataSet data;
    private int dayOfSimulation;
    private final int HEIGHT_OF_MAP;
    private final int WIDTH_OF_MAP;

    public Simulation(InfernalPortal map, DataSet data) {
        this.STARTING_NUMBER_OF_ANIMALS = data.getNumberOfAnimals();
        this.STARTING_ENERGY_OF_ANIMAL = data.getInitialEnergyOfAnimals();
        this.STARTING_NUMBER_OF_PLANTS = data.getNumberOfPlants();
        this.PLANT_SELECTION = data.getPlantSelection();
        this.data = data;
        this.map = map;
        this.listOfGenotypes = new ArrayList<>();
        this.createAndPlaceAnimalsOnTheMap();
        this.growthOfNewPlants();
        this.numberOfAnimals = STARTING_NUMBER_OF_ANIMALS;
        this.numberOfPlants = STARTING_NUMBER_OF_PLANTS;
        this.appObserverList = new ArrayList<>();
        this.dayOfSimulation=0;
        this.HEIGHT_OF_MAP = data.getHeightOfMap();
        this.WIDTH_OF_MAP = data.getWidthOfMap();


    }

    public void createAndPlaceAnimalsOnTheMap() {
        for (int i = 0; i < STARTING_NUMBER_OF_ANIMALS; i ++) {
            Animal a = new Animal(this.map.generateRandomPositionOnTheMap(), new Gen(0), STARTING_ENERGY_OF_ANIMAL,
                    new Genotype(this.data),this.map, this.data);
            map.placeAnimalOnTheMap(a,this);
        }
    }

    /**
     * Sprawdzić to - mam watpliwosci
     *
     */
    public void animalsEatPlants() { //  będzie bardzo podobne do reprodukcji

        HashMap<Vector2d, ArrayList<Animal>> animalsCopy = new HashMap<Vector2d, ArrayList<Animal>>(this.map.animals);
        for (ArrayList<Animal> listOfAnimals : animalsCopy.values()) {
            if (this.map.getPlants().containsKey(listOfAnimals.get(0))) {
                if (listOfAnimals.size() > 1) {
                    Animal eater = this.map.solveDrawWithEatingOrReproducing(listOfAnimals);
                    eater.eat();
                    map.getPlants().remove(eater.getPosition());
                }
                else {
                    listOfAnimals.get(0).eat();
                    map.getPlants().remove(listOfAnimals.get(0).getPosition());
                }
            }


        }
    }


    public void growthOfNewPlants() {

        switch (this.PLANT_SELECTION) {
            case 1 -> forestedEquatoriaGrowth(this.STARTING_NUMBER_OF_PLANTS);
            case 2 -> toxicCorpsesGrowth(this.STARTING_NUMBER_OF_PLANTS);
            default -> System.out.println("Podano nieprawidlowa wartosc rosliny");
        }
    }
    public void forestedEquatoriaGrowth(int numberOfPlants) {

        int width = data.getWidthOfMap();
        int height = data.getHeightOfMap();
        int insideEquatoria = (int) (0.8 * numberOfPlants);
        int outsideEquatoria = numberOfPlants - insideEquatoria;

        int upperEquatoria = (int) (0.6 * height);
        int lowerEqatoria = (int)(0.4 * height);

        Random rand = new Random();

        //        toDo zdekomponuj to byczku!

        for(int i = 0; i < insideEquatoria; i++) {

            int x = rand.nextInt(width);
            int y = rand.nextInt((upperEquatoria - lowerEqatoria) + 1) + lowerEqatoria;
            ForestedEquatoria fe = new ForestedEquatoria(new Vector2d(x, y));
            map.placeForestedEquatoria(fe);

        }

        for(int i = 0; i < outsideEquatoria; i++) {
            if(i%2==0) {
                int x = rand.nextInt(width);
                int y = rand.nextInt((height- upperEquatoria)+1) + upperEquatoria;
                ForestedEquatoria fe = new ForestedEquatoria(new Vector2d(x, y));
                map.placeForestedEquatoria(fe);

            }
            else {
                int x = rand.nextInt(width);
                int y = rand.nextInt((lowerEqatoria) + 1);
                ForestedEquatoria fe = new ForestedEquatoria(new Vector2d(x, y));
                map.placeForestedEquatoria(fe);
            }
        }


    }
    
    public void toxicCorpsesGrowth(int numberOfPlant) {

        // toDO
        int nonToxicPlaces = (int) (0.8 * numberOfPlant); // preferred place to grow
        int toxicPlaces = numberOfPlant - nonToxicPlaces;

        ArrayList<Animal> tombsCopy = this.map.getTombs();
        ToxicCorpses tx = new ToxicCorpses(new Vector2d(0,0));
        map.placeToxicCorpsesOnTheMap(tx);



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

