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
    public Tree trees[];
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

public void computeGlobalSimularity() {
    int cutoffDepth = 3;
    int nodesChecked = 0;
    int simScore = 0;
    for (Tree tree : trees) {
            nodesChecked = countNodesUpToDepth(tree.getRoot(),cutoffDepth);
            simScore = 0;
            for (Tree popInd : trees) {
            simScore =+ computeGlobalSimHelper(tree.getRoot(), popInd.getRoot(), 0, cutoffDepth);
        }
        tree.avgFitness = tree.avgFitness*(1-(simScore/(nodesChecked*populationSize)));
    }
}

private int computeGlobalSimHelper(Node a, Node b, int currentDepth, int cutoffDepth ) {
    if (a == null || b == null || currentDepth >= cutoffDepth) return 0;

    int match = 0;

    if (a.getType() == 0 && b.getType() == 0 && a.getOperator().equals(b.getOperator())) {
        match = 1;
    }

    return match
         + computeGlobalSimHelper(a.getLeft(), b.getLeft(), currentDepth + 1, cutoffDepth)
         + computeGlobalSimHelper(a.getRight(), b.getRight(), currentDepth + 1, cutoffDepth);
}

public int countNodesUpToDepth(Node root, int maxDepth) {
    return countNodesUpToDepthHelper(root, 0, maxDepth);
}

private int countNodesUpToDepthHelper(Node node, int currentDepth, int maxDepth) {
    if (node == null || currentDepth > maxDepth) return 0;

    return 1 + countNodesUpToDepthHelper(node.getLeft(), currentDepth + 1, maxDepth)
             + countNodesUpToDepthHelper(node.getRight(), currentDepth + 1, maxDepth);
}


public void computeLocalSimularity() {
    int minDepth = 3;
    int nodesChecked = 0;
    int simScore = 0;
    for (Tree tree : trees) {
            nodesChecked = countNodesFromDepth(tree.getRoot(),minDepth);
            simScore = 0;
            for (Tree popInd : trees) {
            simScore =+ computeLocalSimHelper(tree.getRoot(), popInd.getRoot(), 0, minDepth);
        }
        if(nodesChecked != 0){
        tree.avgFitness = tree.avgFitness*(1-(simScore/(nodesChecked*populationSize)));
        }
    }
}

private int computeLocalSimHelper(Node a, Node b, int currentDepth, int cutoffDepth) {
    if (a == null || b == null) return 0;

    int match = 0;

    if (currentDepth >= cutoffDepth) {
        if (a.getType() == b.getType()) {
            if (a.getType() == 0 && a.getOperator().equals(b.getOperator())) {
                match = 1;
            } else if (a.getType() == 1 && a.getvarName().equals(b.getvarName()) &&
                       Double.compare(a.getValue(), b.getValue()) == 0) {
                match = 1;
            }
        }
    }

    return match
         + computeLocalSimHelper(a.getLeft(), b.getLeft(), currentDepth + 1, cutoffDepth)
         + computeLocalSimHelper(a.getRight(), b.getRight(), currentDepth + 1, cutoffDepth);
}

public int countNodesFromDepth(Node root, int minDepth) {
    return countNodesFromDepthHelper(root, 0, minDepth);
}

private int countNodesFromDepthHelper(Node node, int currentDepth, int minDepth) {
    if (node == null) return 0;

    int count = (currentDepth > minDepth) ? 1 : 0;

    return count + countNodesFromDepthHelper(node.getLeft(), currentDepth + 1, minDepth)
                 + countNodesFromDepthHelper(node.getRight(), currentDepth + 1, minDepth);
}

    
}
