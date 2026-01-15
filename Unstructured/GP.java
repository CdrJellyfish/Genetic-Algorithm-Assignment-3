import java.util.Random;

public class GP {
//System
    private Population population;
    private Tree newPopulation[];
    private int size;
    private CSVReader reader;
    private double goSplit;
    private double testSplit;
    private int positionCounter;
    private int testStart;
    private int epochs;
    public Tree  bestTree;
    public int bestTreeCounter;
    private int seed;
    private Random random;

    public GP(int seed, int epochs,int size,int depth,double gSplit, double tSplit){
        this.seed = seed;
        this.size = size;
        reader = new CSVReader();
        reader.extractFile();
        population = new Population(size, this.seed, depth,reader );
        this.random = new Random(this.seed);
        goSplit = gSplit;
        testSplit = tSplit;
        positionCounter = 0;
        testStart = (int)Math.ceil(reader.dataAmount*tSplit);
        this.epochs = epochs;

    }


    public void populateVariables(){
        
        population.updateValues(reader.data[positionCounter],reader.target[positionCounter]);
        positionCounter++;

    }

    public void evaluateTrees(){
        population.evaluate();
    }

    public void genNewPopulation(){
        //System.out.println(bestTreeCounter);
        newPopulation = new Tree[size];
        Tree currBest = population.getBestTree();
        positionCounter = 0;
        population.ite =0;
        //System.out.println("CurrBestTree  :  " + currBest.avgFitness + "    " + currBest.toString());
        
        if(bestTree == null){
            bestTree = currBest;
            bestTreeCounter = 0;
            //System.out.println("OverallBestTree  :  " + bestTree.avgFitness + "    " + bestTree.toString());
        }
        else if(currBest.avgFitness > bestTree.avgFitness){
            bestTree = currBest;
            bestTreeCounter = 0;
            //System.out.println("OverallBestTree  :  " + bestTree.avgFitness + "    " + bestTree.toString());
        }
        else{
            bestTreeCounter++;
        }
        
        int newGenSplit = (int)Math.ceil(size*goSplit);
        Tree currTree;
        Tree currTree2;
        for(int i = 0;i < newGenSplit;i++){
            currTree = tournamentSelect().copyTree();
            currTree = mutate(currTree);
            newPopulation[i]=currTree;
            // System.out.println("maxDepth: " + currTree.getMaxDepth());
            // System.out.println("depth: " + currTree.getDepth());
        }

        for(int i = newGenSplit;i < size;i++){
            currTree = tournamentSelect().copyTree();
            currTree2 = tournamentSelect().copyTree();
            Tree crossedTrees[] = crossover(currTree, currTree2);
            newPopulation[i] = crossedTrees[0];
            i++;
            if(i<size){
                newPopulation[i]= crossedTrees[1];
            }
            // System.out.println("maxDepth: " + crossedTrees[0].getMaxDepth());
            // System.out.println("depth: " + crossedTrees[0].getDepth());
            // System.out.println("maxDepth: " + crossedTrees[1].getMaxDepth());
            // System.out.println("depth: " + crossedTrees[1].getDepth());
        }

        population.setTrees(newPopulation);

    }

    public Tree tournamentSelect(){
        this.seed += 1;
        this.random = new Random(this.seed);
        int count = 3;
        Tree[] selected = new Tree[count];
        for (int i = 0; i < count; i++) {
            int index = this.random.nextInt(size);
            selected[i] = population.getTrees()[index];
        }

        Tree best = selected[0];
        for (int i = 1; i < selected.length; i++) {
            if (selected[i].avgFitness > best.avgFitness) {
                best = selected[i];
            }
        }
        return best;
    }

    public Tree mutate(Tree t){
        this.seed = this.seed+ 1;
        this.random = new Random(this.seed);

        Node rNode = t.getRandomNode(random);
        rNode.mutateNode(random);
        return t;
    }

    public Tree[] crossover(Tree parOne, Tree parTwo){
        this.seed = this.seed+ 1;
        this.random = new Random(this.seed);
        Tree r[] = new Tree[2];

        Node rNodeMom = parOne.getRandomNode(random);
        Node rNodeDad = parTwo.getRandomNodeByType(rNodeMom.getType());
        Node temp = new Node(0,-1, "0", 0, "makeOperator",reader.variable);
        if(rNodeDad == null){
            r[0] = parOne;
            r[1] = parTwo;
            return r;
        }
        temp.setLeft(rNodeMom.getLeft());
        temp.setRight(rNodeMom.getRight());
        rNodeMom.setLeft(rNodeDad.getLeft());
        rNodeMom.setRight(rNodeDad.getRight());
        rNodeDad.setLeft(temp.getLeft());
        rNodeDad.setRight(temp.getRight());
        
        r[0] = parOne;
        r[1] = parTwo;

        return r;
    }


    public void train(){
        //population.displayPopulation();
        for(int i = 0; i < epochs;i++){
            // System.out.println("iteration: " + i);
            // if(bestTreeCounter  >= 100){
            //     break;
            // }
            
            positionCounter = 0;
            for(int j = 0; j < testStart; j++){
                populateVariables();
                evaluateTrees();
            }
            genNewPopulation();
            // System.out.println("Best fitness : " + bestTree.avgFitness);
            // System.out.println("Best tree : " + bestTree.toString());
        }
        //population.displayPopulation();
        // System.out.println("------------------");
        // System.out.println("Best fitness : " + bestTree.avgFitness);
        // System.out.println("Best tree : " + bestTree.toString());
    }

    public void test(){


            positionCounter = 0;
            bestTree.avgFitness=0;
            bestTree.totalFitness=0;
            bestTree.trainCounts =0;
            for(int j = testStart; j < reader.dataAmount; j++){
                bestTree.updateValues(reader.data[j],reader.target[j]);
               
                bestTree.calculateAccuracy();
                // System.out.println("Target score : " + bestTree.target + "  GetScore : " + bestTree.calculateScore() );
                // System.out.println("Calc fitness : " + bestTree.calculateAccuracy() + "  AvgFit : " + bestTree.avgFitness );
                // System.out.println("----------------");
            }
            // System.out.println("Best fitness : " + bestTree.avgFitness);
            // System.out.println("Best tree : " + bestTree.toString());

    }


    
}
