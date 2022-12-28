package org.example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Genotype {

    private final int length_of_genotype;
    private final int min_number_of_mutated_gens;
    private final int max_number_of_mutated_gens;

    private Gen[] arrayOfGens;
    public Genotype(DataSet data) {
        this.length_of_genotype = data.getLengthOfGenotype();
        this.min_number_of_mutated_gens = data.getMinNumberOfMutations();
        this.max_number_of_mutated_gens = data.getMaxNumberOfMutations();
        this.arrayOfGens = new Gen[length_of_genotype];
        for (int i = 0; i < length_of_genotype; i++) {
            this.arrayOfGens[i] = new Gen();
        }
    }

    public Genotype(Animal firstParent, Animal secondParent, DataSet data) {
        this.length_of_genotype = data.getLengthOfGenotype();
        this.min_number_of_mutated_gens = data.getMinNumberOfMutations();
        this.max_number_of_mutated_gens = data.getMaxNumberOfMutations();
        this.arrayOfGens = this.createGenotypeBasedOnParents(firstParent, secondParent);
        this.mutateGens();
    }

    public Gen[] getArrayOfGens() {
        return arrayOfGens;
    }

    private Gen[] createGenotypeBasedOnParents(Animal firstParent, Animal secondParent) {
        Animal strongerParent = Animal.strongerAnimal(firstParent,secondParent);
        Animal weakerParent = Animal.weakerAnimal(firstParent,secondParent);

        float fractionOfGensFromStrongerParent = (float)strongerParent.getEnergy() / (strongerParent.getEnergy() + weakerParent.getEnergy());
        int numOfGensFromStrongerParent = (int) (length_of_genotype * fractionOfGensFromStrongerParent);
        int numOfGensFromWeakerParent = length_of_genotype - numOfGensFromStrongerParent;
        Gen[] newGenomtype = new Gen[length_of_genotype];

        Random rand = new Random();
        int leftOrRight = rand.nextInt(2);

        // ToDo: Podzielic na funkcje
        //jesli 0 to lewa czesc genotypu zostanie wzieta z osobnika silenijeszego
        if (leftOrRight==0) {
            System.out.println( "left from stronger");
            for (int i = 0; i < numOfGensFromStrongerParent; i++) {
                //animal moze miec metode genrate i zeby nie bylo tych funkcji
                newGenomtype[i] = new Gen(strongerParent.getGenotype().getRotationFromSpecificGen(i));
            }
            for (int j= newGenomtype.length -1; j > newGenomtype.length-numOfGensFromWeakerParent -1; j--) {
                newGenomtype[j] = new Gen (weakerParent.getGenotype().getRotationFromSpecificGen(j));
            }

        }

        //jesli 1 to prawa czesc genotypu zostanie wzieta z osobnika silenijeszego
        else {
            //System.out.println( "right from stronger");
            for (int i = 0; i < numOfGensFromWeakerParent; i++) {
                newGenomtype[i] = new Gen (weakerParent.getGenotype().getRotationFromSpecificGen(i));
            }
            for (int j= newGenomtype.length -1; j > newGenomtype.length-numOfGensFromStrongerParent -1; j--) {
                newGenomtype[j] = new Gen(strongerParent.getGenotype().getRotationFromSpecificGen(j));
            }

        }

        return newGenomtype;
    }

//    public Gen getGenFromGenotype (int index) {
//        // dodac walidacje czy nie za duzy
//        return this.arrayOfGens[index];
//    }

    private int getRotationFromSpecificGen(int index) {
        return this.arrayOfGens[index].getRotation();
    }

    public void mutateGens() {
        int numOfGensToBeMutated = new Random().nextInt((max_number_of_mutated_gens - min_number_of_mutated_gens + 1) + min_number_of_mutated_gens);
       // System.out.println( "num of gens to be mutated " + numOfGensToBeMutated);
        ArrayList<Integer> gensModified = new ArrayList<>();
        for (int i = 0; i < numOfGensToBeMutated; i++) {
            while (true) {
                int genMutated = new Random().nextInt(length_of_genotype);
                if (!gensModified.contains(genMutated)) {
                    this.arrayOfGens[genMutated].randomlyChangeGene();
                    gensModified.add(genMutated);
                    break;
                }
            }


        }

    }

    @Override
    public String toString() {
        return "Genotype: " + Arrays.toString(arrayOfGens);
    }
}
