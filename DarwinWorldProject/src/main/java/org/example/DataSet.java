package org.example;

import java.util.Map;

public class DataSet {

    private int heightOfMap;
    private int widthOfMap;
    private int mapSelection;
    private int numberOfPlants;
    private int energyFromPlant;
    private int dailyGrowthOfPlants;
    private int plantSelection;
    private int numberOfAnimals;
    private int initialEnergyOfAnimals;
    private int energyRequiredToReproduce;
    private int energyUsedToMakeChild;
    private int minNumberOfMutations;
    private int maxNumberOfMutations;
    private int mutationSelection;
    private int lengthOfGenotype;
    private int behaviourSelection;

    public DataSet() {
        FileUtility file = new FileUtility();
        Map<String, Integer> allParameters = file.getHashMapFromTextFile();

        this.heightOfMap = allParameters.get("heightOfMap");
        this.widthOfMap = allParameters.get("widthOfMap");
        this.mapSelection = allParameters.get("mapSelection");
        this.numberOfPlants = allParameters.get("numberOfPlants");
        this.energyFromPlant = allParameters.get("energyFromPlant");
        this.dailyGrowthOfPlants = allParameters.get("dailyGrowthOfPlants");
        this.plantSelection = allParameters.get("plantSelection");
        this.numberOfAnimals = allParameters.get("numberOfAnimals");
        this.initialEnergyOfAnimals = allParameters.get("initialEnergyOfAnimals");
        this.energyRequiredToReproduce = allParameters.get("energyRequiredToReproduce");
        this.energyUsedToMakeChild = allParameters.get("energyUsedToMakeChild");
        this.minNumberOfMutations = allParameters.get("minNumberOfMutations");
        this.maxNumberOfMutations = allParameters.get("maxNumberOfMutations");
        this.mutationSelection = allParameters.get("mutationSelection");
        this.lengthOfGenotype = allParameters.get("lengthOfGenotype");
        this.behaviourSelection = allParameters.get("behaviourSelection");
    }

    public int getHeightOfMap() {
        return heightOfMap;
    }

    public int getWidthOfMap() {
        return widthOfMap;
    }

    public int getMapSelection() {
        return mapSelection;
    }

    public int getNumberOfPlants() {
        return numberOfPlants;
    }

    public int getEnergyFromPlant() {
        return energyFromPlant;
    }

    public int getDailyGrowthOfPlants() {
        return dailyGrowthOfPlants;
    }

    public int getPlantSelection() {
        return plantSelection;
    }

    public int getNumberOfAnimals() {
        return numberOfAnimals;
    }

    public int getInitialEnergyOfAnimals() {
        return initialEnergyOfAnimals;
    }

    public int getEnergyRequiredToReproduce() {
        return energyRequiredToReproduce;
    }

    public int getEnergyUsedToMakeChild() {
        return energyUsedToMakeChild;
    }

    public int getMinNumberOfMutations() {
        return minNumberOfMutations;
    }

    public int getMaxNumberOfMutations() {
        return maxNumberOfMutations;
    }

    public int getMutationSelection() {
        return mutationSelection;
    }

    public int getLengthOfGenotype() {
        return lengthOfGenotype;
    }

    public int getBehaviourSelection() {
        return behaviourSelection;
    }

    @Override
    public String toString() {
        return "DataSet{" +
                "heightOfMap=" + heightOfMap +
                ", widthOfMap=" + widthOfMap +
                ", mapSelection=" + mapSelection +
                ", numberOfPlants=" + numberOfPlants +
                ", energyFromPlant=" + energyFromPlant +
                ", dailyGrowthOfPlants=" + dailyGrowthOfPlants +
                ", plantSelection=" + plantSelection +
                ", numberOfAnimals=" + numberOfAnimals +
                ", initialEnergyOfAnimals=" + initialEnergyOfAnimals +
                ", energyRequiredToReproduce=" + energyRequiredToReproduce +
                ", energyUsedToMakeChild=" + energyUsedToMakeChild +
                ", minNumberOfMutations=" + minNumberOfMutations +
                ", maxNumberOfMutations=" + maxNumberOfMutations +
                ", mutationSelection=" + mutationSelection +
                ", lengthOfGenotype=" + lengthOfGenotype +
                ", behaviourSelection=" + behaviourSelection +
                '}';
    }
}