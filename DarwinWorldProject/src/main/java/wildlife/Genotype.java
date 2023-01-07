package wildlife;

import file_support.DataSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Random;

public class Genotype {
    private final int LENGTH_OF_GENOTYPE;
    private final int MIN_NUMBER_OF_MUTATED_GENS;
    private final int MAX_NUMBER_OF_MUTATED_GENS;
    private final Gen[] arrayOfGens;

    // constructor to create random genotype
    public Genotype(DataSet data) {
        this.LENGTH_OF_GENOTYPE = data.getLengthOfGenotype();
        this.MIN_NUMBER_OF_MUTATED_GENS = data.getMinNumberOfMutations();
        this.MAX_NUMBER_OF_MUTATED_GENS = data.getMaxNumberOfMutations();
        this.arrayOfGens = new Gen[LENGTH_OF_GENOTYPE];
        for (int i = 0; i < LENGTH_OF_GENOTYPE; i++) {
            this.arrayOfGens[i] = new Gen();
        }
    }

    // constructor to create genotype based on two parents
    public Genotype(Animal firstParent, Animal secondParent, DataSet data) {
        this.LENGTH_OF_GENOTYPE = data.getLengthOfGenotype();
        this.MIN_NUMBER_OF_MUTATED_GENS = data.getMinNumberOfMutations();
        this.MAX_NUMBER_OF_MUTATED_GENS = data.getMaxNumberOfMutations();
        this.arrayOfGens = this.createGenotypeBasedOnParents(firstParent, secondParent);
        this.mutateGens();
    }

    public Gen[] getArrayOfGens() {
        return arrayOfGens;
    }

    private int getRotationFromSpecificGen(int index) {
        return this.arrayOfGens[index].getRotation();
    }

    private Gen[] createGenotypeBasedOnParents(Animal firstParent, Animal secondParent) {
        Animal strongerParent = Animal.strongerAnimal(firstParent,secondParent);
        Animal weakerParent = Animal.weakerAnimal(firstParent,secondParent);

        float fractionOfGensFromStrongerParent = (float)strongerParent.getEnergy() / (strongerParent.getEnergy() + weakerParent.getEnergy());
        int numOfGensFromStrongerParent = (int) (LENGTH_OF_GENOTYPE * fractionOfGensFromStrongerParent);
        int numOfGensFromWeakerParent = LENGTH_OF_GENOTYPE - numOfGensFromStrongerParent;
        Gen[] newGenotype = new Gen[LENGTH_OF_GENOTYPE];

        Random rand = new Random();
        int leftOrRight = rand.nextInt(2);

        if (leftOrRight==0) {
            for (int i = 0; i < numOfGensFromStrongerParent; i++) {
                newGenotype[i] = new Gen(strongerParent.getGenotype().getRotationFromSpecificGen(i));
            }
            for (int j= newGenotype.length -1; j > newGenotype.length-numOfGensFromWeakerParent -1; j--) {
                newGenotype[j] = new Gen (weakerParent.getGenotype().getRotationFromSpecificGen(j));
            }
        }
        else {
            for (int i = 0; i < numOfGensFromWeakerParent; i++) {
                newGenotype[i] = new Gen (weakerParent.getGenotype().getRotationFromSpecificGen(i));
            }
            for (int j= newGenotype.length -1; j > newGenotype.length-numOfGensFromStrongerParent -1; j--) {
                newGenotype[j] = new Gen(strongerParent.getGenotype().getRotationFromSpecificGen(j));
            }
        }

        return newGenotype;
    }

    public void mutateGens() {
        int numOfGensToBeMutated = new Random().nextInt((MAX_NUMBER_OF_MUTATED_GENS - MIN_NUMBER_OF_MUTATED_GENS + 1) + MIN_NUMBER_OF_MUTATED_GENS);
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Genotype genotype = (Genotype) o;
        return LENGTH_OF_GENOTYPE == genotype.LENGTH_OF_GENOTYPE && Arrays.equals(arrayOfGens, genotype.arrayOfGens);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(LENGTH_OF_GENOTYPE);
        result = 31 * result + Arrays.hashCode(arrayOfGens);
        return result;
    }

    @Override
    public String toString() {
        String array = "[";
        for (Gen gen : this.getArrayOfGens()) {
            array = array + gen.getRotation() + " ";
        }
        array+="]";
        return array;
    }
}
