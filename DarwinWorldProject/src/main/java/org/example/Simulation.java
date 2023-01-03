package org.example;

import gui.WriteToCSV;

import java.io.IOException;
import java.util.*;

public class Simulation implements Runnable {

    public final int STARTING_NUMBER_OF_ANIMALS;
    public final int STARTING_ENERGY_OF_ANIMAL;
    public final int STARTING_NUMBER_OF_PLANTS;
    public final int PLANT_SELECTION;
    private InfernalPortal map;
    private int numberOfAnimals;
    private int numberOfPlants;

    private ArrayList<Genotype> listOfGenotypes;

    private int moveDelay;
    private List<IAppObserver> appObserverList;
    private DataSet data;
    private int dayOfSimulation;
    WriteToCSV csvFile = new WriteToCSV("Map Statistics" );

    public int getNumberOfAnimals() {
        return numberOfAnimals;
    }

    public int getNumberOfPlants() {
        return this.map.getPlants().size();
    }

    public int getNumberOfFreeFields() {
        int numOfFreeFields=0;
        for(int i = 0; i<this.map.getWidth(); i++){
            for(int j = 0; j<this.map.getHeight();j++) {
                Vector2d position = new Vector2d(i,j);
                if (!this.map.isOccupiedByPlant(position) && !this.map.isOccupied(position)) {
                    numOfFreeFields++;
                }
            }
        }
        return numOfFreeFields;
    }

    public Simulation(InfernalPortal map, DataSet data) throws IOException {
        this.STARTING_NUMBER_OF_ANIMALS = data.getNumberOfAnimals();
        this.STARTING_ENERGY_OF_ANIMAL = data.getInitialEnergyOfAnimals();
        this.STARTING_NUMBER_OF_PLANTS = data.getNumberOfPlants();
        this.PLANT_SELECTION = data.getPlantSelection();
        this.data = data;
        this.map = map;
        this.listOfGenotypes = new ArrayList<>();
        this.createAndPlaceAnimalsOnTheMap();
        this.growthOfNewPlants(this.STARTING_NUMBER_OF_PLANTS);
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

    /**
     * Sprawdzić to - mam watpliwosci
     *
     */
    public void animalsEatPlants() { //  będzie bardzo podobne do reprodukcji

        Map<Vector2d, ArrayList<Animal>> animalsCopy = this.map.getAnimals();
        //HashMap<Vector2d, ArrayList<Animal>> animalsCopy = new HashMap<Vector2d, ArrayList<Animal>>(this.map.animals);
        for (ArrayList<Animal> listOfAnimals : animalsCopy.values()) {
            if (this.map.getPlants().containsKey(listOfAnimals.get(0).getPosition())) {
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


    public void growthOfNewPlants(int numberOfPlants) {

        switch (this.PLANT_SELECTION) {
            case 1 -> forestedEquatoriaGrowth(numberOfPlants);
            case 2 -> toxicCorpsesGrowth(numberOfPlants);
            default -> System.out.println("Podano nieprawidlowa wartosc rosliny");
        }
    }
    public void forestedEquatoriaGrowth(int numberOfPlants) {
        Random rand = new Random();
        int width = this.map.getWidth();
        int height = this.map.getHeight();

        //int insideEquatoria = (int) (0.8 * numberOfPlants); //preferred place to growth
       // int outsideEquatoria = numberOfPlants - insideEquatoria;

        int upperEquatoria = (int) (0.6 * height);
        int lowerEqatoria = (int)(0.4 * height);

        //        toDo zdekomponuj
        for (int i=0; i < numberOfPlants;i++) {
            int randomNum = rand.nextInt(5);
            System.out.println("random num " + randomNum);
            if (randomNum < 4) {

                int x = rand.nextInt(width);
                int y = rand.nextInt((upperEquatoria - lowerEqatoria) + 1) + lowerEqatoria;
                ForestedEquatoria fe = new ForestedEquatoria(new Vector2d(x, y));
                map.placeForestedEquatoria(fe);

            } else {
                //  Todo zmienic to zeby byl przedzial, bez ifa
                int randomNum2 = rand.nextInt(2);
                if (randomNum2 == 0) {
                    int x = rand.nextInt(width);
                    int y = rand.nextInt((height - upperEquatoria) + 1) + upperEquatoria;
                    ForestedEquatoria fe = new ForestedEquatoria(new Vector2d(x, y));
                    // ToDo to powinno byc abstrakcyjne
                    map.placeForestedEquatoria(fe);

                } else {
                    int x = rand.nextInt(width);
                    int y = rand.nextInt((lowerEqatoria) + 1);
                    ForestedEquatoria fe = new ForestedEquatoria(new Vector2d(x, y));
                    map.placeForestedEquatoria(fe);
                }
            }
        }
    }

    public void toxicCorpsesGrowth(int numberOfPlant) {
        int width = this.map.getWidth();
        int height = this.map.getHeight();
        Random rand = new Random();

        // toDO
        int nonToxicPlaces = (int) (0.8 * numberOfPlant); // preferred place to grow
        int toxicPlaces = numberOfPlant - nonToxicPlaces;

        ArrayList<Animal> tombsCopy = this.map.getTombs();
        ArrayList<Vector2d> cementary = new ArrayList<>();

        for(Animal a : tombsCopy) {
            cementary.add(a.getPosition());
        }

        ArrayList<Vector2d> allPlacesOnMap = new ArrayList<>();
        // to nie powinno byc chyba mniejsze lub rowne
        for(int i = 0; i<=height; i++){
            for(int j = 0; j<=width;j++) {
                allPlacesOnMap.add(new Vector2d(j,i));
            }
        }

        cementary.sort(Comparator.comparing(Vector2d::getX));
        cementary.sort(Comparator.comparing(Vector2d::getY));
        allPlacesOnMap.removeAll(cementary);

        for(int i = 0; i < nonToxicPlaces; i++) {
            while(true) {
                int x = rand.nextInt(width);
                int y = rand.nextInt(height);
                Vector2d vec = new Vector2d(x, y);
                if(allPlacesOnMap.contains(vec)) {
                    ToxicCorpses tx = new ToxicCorpses(vec);
                    map.placeToxicCorpsesOnTheMap(tx);
                    break;
                }
            }
        }
        for(int i = 0; i< toxicPlaces; i++) {
            int x = rand.nextInt(width);
            int y = rand.nextInt(height);
            Vector2d vec = new Vector2d(x, y);
            ToxicCorpses tx = new ToxicCorpses(vec);
            map.placeToxicCorpsesOnTheMap(tx);
        }

    }


    public void addNewGenotype(Genotype genotype) {
        this.listOfGenotypes.add(genotype);
    }
    public double calculateAverageEnergy() {
        int energy = 0;
        int count = 0;
        // faknie byloby zmienic na streama
        for (ArrayList<Animal> listOfAnimals : this.map.getAnimals().values()) {
            for (Animal animal : listOfAnimals) {
                energy+=animal.getEnergy();
                count++;
            }
        }
        return Math.round((double)(energy/count));
    }

    public double calculateAverageLifeLength () {
        int allLifeLength = 0;
        if (this.map.getTombs().size() == 0) {
            return 0;
        }
        for (Animal animal : this.map.getTombs()) {
                allLifeLength+=animal.getAge();

        }
        return Math.round((double)allLifeLength/this.map.getTombs().size());
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
        System.out.println("Day of simulation " + this.dayOfSimulation);
        // te funkcje moze jednak lepiej wrzucic do symulacji
        this.map.deleteDeadAnimalsFromTheMap(this);
        this.moveAllAnimals();
        this.animalsEatPlants();
        this.reproductionOfAnimal();
        if(this.dayOfSimulation >1){this.growthOfNewPlants(this.data.getDailyGrowthOfPlants());}
        this.animalsGetsOlder();
        this.updateFile();

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

    public void updateFile() {

        String[] dataArray = {
                String.valueOf(this.getDayOfSimulation()),
                String.valueOf(this.getNumberOfAnimals()),
                String.valueOf( this.getNumberOfPlants()),
                String.valueOf( this.getNumberOfFreeFields()),
                // ToDo tu chyba nie musi byc string
                String.valueOf(map.mostPopularGenotype()),
                String.valueOf(this.calculateAverageEnergy()),
                String.valueOf(this.calculateAverageLifeLength())
        };

        try {
            csvFile.appendDataToAFile(dataArray, true);
        }
        catch (IOException ex) {
            System.out.println("close the file");
        }




    }

}

