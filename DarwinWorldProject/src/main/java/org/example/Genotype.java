package org.example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Genotype {
    //nazewnictwo
    private static final int LENGTH_OF_GENOTYPE = 7;
    private static final int MIN_NUMBER_OF_MUTATED_GENS=0;
    private static final int MAX_NUMBER_OF_MUTATED_GENS=3;
    private Gen[] arrayOfGens;

    public Genotype() {
        this.arrayOfGens = new Gen[LENGTH_OF_GENOTYPE];
        for (int i=0; i < LENGTH_OF_GENOTYPE; i++) {
            this.arrayOfGens[i] = new Gen();
        }
    }

    public Genotype(Animal firstParent, Animal secondParent) {
        this.arrayOfGens = this.createGenotypeBasedOnParents(firstParent, secondParent);
        this.mutateGens();
    }

    private Gen[] createGenotypeBasedOnParents(Animal firstParent, Animal secondParent) {
        Animal strongerParent = Animal.strongerAnimal(firstParent,secondParent);
        Animal weakerParent = Animal.weakerAnimal(firstParent,secondParent);

        float fractionOfGensFromStrongerParent = (float)strongerParent.getEnergy() / (strongerParent.getEnergy() + weakerParent.getEnergy());
        int numOfGensFromStrongerParent = (int) (LENGTH_OF_GENOTYPE * fractionOfGensFromStrongerParent);
        int numOfGensFromWeakerParent = LENGTH_OF_GENOTYPE - numOfGensFromStrongerParent;
        Gen[] newGenomtype = new Gen[LENGTH_OF_GENOTYPE];

        Random rand = new Random();
        int leftOrRight = rand.nextInt(2);

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
            System.out.println( "right from stronger");
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
        int numOfGensToBeMutated = new Random().nextInt((MAX_NUMBER_OF_MUTATED_GENS - MIN_NUMBER_OF_MUTATED_GENS + 1) + MIN_NUMBER_OF_MUTATED_GENS);
        System.out.println( "num of gens to be mutated " + numOfGensToBeMutated);
        ArrayList<Integer> gensModified = new ArrayList<>();
        for (int i = 0; i < numOfGensToBeMutated; i++) {
            while (true) {
                int genMutated = new Random().nextInt(LENGTH_OF_GENOTYPE);
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
