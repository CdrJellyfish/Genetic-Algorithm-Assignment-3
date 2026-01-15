import java.util.ArrayList;
import java.util.List;
import java.lang.Math;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
//System
public class Population {
    private Tree trees[];
    private int populationSize;
    private int seed;
    private int maxDepth;
    public int ite;
    public CSVReader reader;
    // Constructor
    public Population(int populationSize, int seed, int maxDepth, CSVReader reader) {
        this.populationSize = populationSize;
        this.seed = seed;
        this.maxDepth = maxDepth;
        this.trees = new Tree[populationSize];
        this.reader = reader;
        initializePopulation();
        ite = 0;
    }

    public Population(int populationSize, int seed, int maxDepth, CSVReader reader, Tree transferredTree,double copies, double mutants, double newTrees) {
        this.populationSize = populationSize;
        this.seed = seed;
        this.maxDepth = maxDepth;
        this.trees = new Tree[populationSize];
        this.reader = reader;
        initializeTransPopulation(transferredTree,copies,mutants,newTrees);
        ite = 0;
    }

    public void setTrees(Tree t[]){
        trees = t;
    }

    // Initialize the population with random trees
    private void initializePopulation() {

        int amountPerDepth = (int)Math.floor(populationSize/(maxDepth));
        int j = 0;
        int k = 2;
        int insertSeed = seed;
        //System.out.println("amountPerDepth:  " + amountPerDepth);
        for (int i = 0; i < populationSize; i++) {            
            //System.out.println("i:  " + i);
            //System.out.println("j:  " + j);
            //System.out.println("k:  " + k);
            if(j == amountPerDepth && k != (maxDepth)){
                j = 0;
                k++;
            }

            trees[i]= (new Tree(insertSeed++, k,reader.variable,maxDepth));
            
            j++;
        }
            //int count = 1;
        for (Tree tree : trees) {
        
            tree.populateTree();
            
            tree.fixLeaves();
           // System.out.println("number: " + count++ );
           // System.out.println("count: " + tree.countNodes());
        }


    }


    private void initializeTransPopulation(Tree parTree,double copies, double mutants, double newTrees) {
       parTree.resetVarNames();

        int amountNew = (int)Math.floor(populationSize*(newTrees));
        int amountPerDepth = (int)Math.floor(amountNew/(maxDepth));
        int j = 0;
        int k = 2;
        int insertSeed = seed;
        //System.out.println("amountPerDepth:  " + amountPerDepth);
        for (int i = 0; i < populationSize; i++) {            
            //System.out.println("i:  " + i);
            //System.out.println("j:  " + j);
            //System.out.println("k:  " + k);
            if(j == amountPerDepth && k != (maxDepth)){
                j = 0;
                k++;
            }

            trees[i]= (new Tree(insertSeed++, k,reader.variable,maxDepth));
            
            j++;
        }
            //int count = 1;
        for (int i = 0; i < amountNew;i++) {
            trees[i].populateTree();
            trees[i].fixLeaves();
           // System.out.println(trees[i].getRoot().varNameList.length);
           // System.out.println("number: " + count++ );
           // System.out.println("count: " + tree.countNodes());
        }
        int copiesAmount = (int)Math.floor(populationSize*(newTrees));
        int upto = amountNew+copiesAmount;
        for(int i = amountNew; i < upto;i++){
            trees[i] = parTree.copyTree();
            //System.out.println(trees[i].getRoot().varNameList.length);

        }

        this.seed = this.seed+ 1;
            Random random = new Random(this.seed);       
        for(int i = upto; i < populationSize;i++){
            Tree newTree = parTree.copyTree();
            for(int d = 0; d <= newTree.countNodes()/2;d++){
                Node rNode = newTree.getRandomNode(random);
                rNode.mutateNode(random);
                //System.out.println(rNode.varNameList.length);
            }
            trees[i] =newTree;

        }


    }


    public void updateValues(String[] values, String target) {
        for (Tree tree : trees) {
            tree.updateValues(values,target);
        }
    }

    // Evaluate all trees by calculating their scores
    public void evaluate() {
        int i = 0;
        for (Tree tree : trees) {
            
            tree.calculateFitness();
            //System.out.println("Ite : "+ ite + "-- Tree: " + i + " Equation: "+ tree.toString());
            //System.out.println("Ite : "+ ite + "-- Tree: " + i + " -- Fitness: " +tree.getFitness() + " -- Avg: " + tree.avgFitness);
            i++;
        }
        //System.out.println("--------------Ite : "+ ite);
        ite++;
    }

    // Print all trees in the population
    public void displayPopulation() {
        for (int i = 0; i < populationSize; i++) {
            System.out.println("Tree " + i + ":\n" + trees[i].toString());
        }
    }


    public Tree getBestTree(){

        Tree bestCurrTree = trees[0];

        for (Tree tree : trees) {
            if(tree.avgFitness < bestCurrTree.avgFitness){bestCurrTree = tree;}
        }

        Tree best = bestCurrTree.copyTree();
        best.trainCounts = bestCurrTree.trainCounts;
        best.totalFitness = bestCurrTree.totalFitness;
        best.avgFitness= bestCurrTree.avgFitness;

        return best;
    }

    // Getter for the trees list
    public Tree[] getTrees() {
        return trees;
    }

    
}
