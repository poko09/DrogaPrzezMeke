package darwin_world;

import file_support.DataSet;
import file_support.Counter;
import file_support.WriteToCSV;
import wildlife.*;

import java.io.IOException;
import java.util.*;

public class Simulation implements Runnable {

    public final int STARTING_NUMBER_OF_ANIMALS;
    public final int STARTING_ENERGY_OF_ANIMAL;
    public final int STARTING_NUMBER_OF_PLANTS;
    public final int PLANT_SELECTION;
    private int numberOfAnimals;
    private int moveDelay;
    private int dayOfSimulation;
    private ArrayList<Genotype> listOfGenotypes;
    private InfernalPortal map;
    private List<IAppObserver> appObserverList;
    private DataSet data;
    WriteToCSV csvFile;
    private Counter counter;

    public Simulation(InfernalPortal map, DataSet data, Counter counter) throws IOException {
        this.STARTING_NUMBER_OF_ANIMALS = data.getNumberOfAnimals();
        this.STARTING_ENERGY_OF_ANIMAL = data.getInitialEnergyOfAnimals();
        this.STARTING_NUMBER_OF_PLANTS = data.getNumberOfPlants();
        this.PLANT_SELECTION = data.getPlantSelection();
        this.csvFile= new WriteToCSV("Map Statistics" ,counter.getCount());
        this.data = data;
        this.map = map;
        this.listOfGenotypes = new ArrayList<>();
        this.createAndPlaceAnimalsOnTheMap();
        this.growthOfNewPlants(this.STARTING_NUMBER_OF_PLANTS);
        this.numberOfAnimals = STARTING_NUMBER_OF_ANIMALS;
        this.appObserverList = new ArrayList<>();
        this.dayOfSimulation=0;
        this.counter = counter;
    }


    // getters
    public int getNumberOfAnimals() {
        return numberOfAnimals;
    }

    public int getNumberOfPlants() {
        return this.map.getPlants().size();
    }

    public int getDayOfSimulation() {
        return dayOfSimulation;
    }


    // methods connected to life of animals
    public void createAndPlaceAnimalsOnTheMap() {
        for (int i = 0; i < STARTING_NUMBER_OF_ANIMALS; i ++) {
            Animal a = new Animal(this.map.generateRandomPositionOnTheMap(), new Gen(0), STARTING_ENERGY_OF_ANIMAL,
                    new Genotype(this.data),this.map, this.data);
            map.placeAnimalOnTheMap(a,this);
        }
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

    public void animalsEatPlants() {

        Map<Vector2d, ArrayList<Animal>> animalsCopy = this.map.getAnimals();
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

    // methods connected to growth of new plants
    public void growthOfNewPlants(int numberOfPlants) {
        switch (this.PLANT_SELECTION) {
            case 1 -> forestedEquatoriaGrowth(numberOfPlants);
            case 2 -> toxicCorpsesGrowth(numberOfPlants);
            default -> System.out.println("Podano nieprawidlowa wartosc rosliny");
        }
    }
    public void forestedEquatoriaGrowth(int numberOfPlants) {
        Random rand = new Random();

        int upperEquatoria = (int) (0.6 * map.getHEIGHT());
        int lowerEqatoria = (int)(0.4 * map.getHEIGHT());

        int counterInside = 0;
        int counterOutside = 0;

        ArrayList<Vector2d> plantFreeListInsideEquatoria = this.freePositionsForestedEquatoria(lowerEqatoria, upperEquatoria);
        ArrayList<Vector2d> plantFreeListOutsideEquatoria = this.freePositionsOutsideEquatoria(lowerEqatoria, upperEquatoria);
        for (int i=0; i < numberOfPlants;i++) {
            int randomNum = rand.nextInt(5);
            if (randomNum < 4) {
                if (counterInside < plantFreeListInsideEquatoria.size()) {
                    ForestedEquator fe = new ForestedEquator(plantFreeListInsideEquatoria.get(counterInside));
                    map.placeForestedEquator(fe);
                    counterInside++;
                }
            } else {
                if (counterOutside < plantFreeListOutsideEquatoria.size()) {
                    ForestedEquator fe = new ForestedEquator(plantFreeListOutsideEquatoria.get(counterOutside));
                    map.placeForestedEquator(fe);
                    counterOutside++;
                }
                }
            }
    }

    public ArrayList<Vector2d> freePositionsForestedEquatoria(int lowerEquatoria, int upperEquatoria) {
        ArrayList<Vector2d> plantFreeList = new ArrayList<>();
        for(int i = 0; i<this.map.getWIDTH(); i++){
            for(int j = lowerEquatoria; j<=upperEquatoria;j++) {
                Vector2d currentPosition = new Vector2d(i,j);
                if (!this.map.isOccupiedByPlant(currentPosition)) {
                    plantFreeList.add(currentPosition);
                }
            }
        }
        Collections.shuffle(plantFreeList);
        return plantFreeList;
    }

    public ArrayList<Vector2d> freePositionsOutsideEquatoria(int upperEquatoria, int lowerEquatoria) {
        ArrayList<Vector2d> plantFreeList = new ArrayList<>();
        for(int i = 0; i<this.map.getWIDTH(); i++){
            for(int j = 0; j<lowerEquatoria;j++) {
                Vector2d currentPosition = new Vector2d(i,j);
                if (!this.map.isOccupiedByPlant(currentPosition)) {
                    plantFreeList.add(currentPosition);
                }
            }
            for(int j = upperEquatoria+1; j<map.getHEIGHT();j++) {
                Vector2d currentPosition = new Vector2d(i,j);
                if (!this.map.isOccupiedByPlant(currentPosition)) {
                    plantFreeList.add(currentPosition);
                }
            }

        }
        Collections.shuffle(plantFreeList);
        return plantFreeList;
    }

    public void toxicCorpsesGrowth(int numberOfPlant) {
        int width = this.map.getWIDTH();
        int height = this.map.getHEIGHT();
        Random rand = new Random();

        int nonToxicPlaces = (int) (0.8 * numberOfPlant); // preferred place to grow
        int toxicPlaces = numberOfPlant - nonToxicPlaces;

        ArrayList<Animal> tombsCopy = this.map.getTombs();
        ArrayList<Vector2d> cementary = new ArrayList<>();

        for(Animal a : tombsCopy) {
            cementary.add(a.getPosition());
        }

        ArrayList<Vector2d> allPlacesOnMap = new ArrayList<>();
        for(int i = 0; i<height; i++){
            for(int j = 0; j<width;j++) {
                allPlacesOnMap.add(new Vector2d(j,i));
            }
        }

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

    // methods connected to statistics of the simulation
    public double calculateAverageEnergy() {
        int energy = 0;
        int count = 0;
        for (ArrayList<Animal> listOfAnimals : this.map.getAnimals().values()) {
            for (Animal animal : listOfAnimals) {
                energy+=animal.getEnergy();
                count++;
            }
        }
        return Math.round((double)(energy/count));
    }

    public int getNumberOfFreeFields() {
        int numOfFreeFields=0;
        for(int i = 0; i<this.map.getWIDTH(); i++){
            for(int j = 0; j<this.map.getHEIGHT(); j++) {
                Vector2d position = new Vector2d(i,j);
                if (!this.map.isOccupiedByPlant(position) && !this.map.isOccupiedByAnimal(position)) {
                    numOfFreeFields++;
                }
            }
        }
        return numOfFreeFields;
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

    public void addNewGenotype(Genotype genotype) {
        this.listOfGenotypes.add(genotype);
    }

    // methods to run simulation
    public void simulationOfOneDay() {
        this.dayOfSimulation+=1;
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
            } catch (Exception e) {
                break;
            }

        }
    }

    public void setMoveDelay(int moveDelay) {
        this.moveDelay = moveDelay;
    }

    // methods connected to model observer
    public void addAppObserver(IAppObserver observer) {
        this.appObserverList.add(observer);
    }

    public void informObservers() {
        for (IAppObserver observer: this.appObserverList){
            observer.positionChangedApp();
        }
    }

    //methods connected to managing files
    public void updateFile() {
        String[] dataArray = {
                String.valueOf(this.getDayOfSimulation()),
                String.valueOf(this.getNumberOfAnimals()),
                String.valueOf( this.getNumberOfPlants()),
                String.valueOf( this.getNumberOfFreeFields()),
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

