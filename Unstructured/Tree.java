import java.util.ArrayList;
import java.util.List;
import java.util.Random;
public class Tree {
//System
    private Node root;
    private int maxDepth;
    private int seed;
    private Random random;
    public double target;
    private double score;
    private double fitness;
    public int trainCounts;
    public double totalFitness;
    public double avgFitness;
    public String[] varList;
    public int overallMaxDepth;

    // Constructor
    public Tree(int rSeed, int Depth, String[] varNameList, int overallMaxDepth) {

        this.seed = rSeed;
        this.maxDepth = Depth-1;
        this.random = new Random(seed);
        this.score = 0;
        this.target = 0;
        this.avgFitness = 0;
        this.totalFitness = 0;
        this.trainCounts = 0;
        this.varList = varNameList;
        this.overallMaxDepth = overallMaxDepth;

        this.root = new Node(0,-1, "0", 0, makeOperator(),varNameList);

    }//End of constructor


    //Copy
    public Tree copyTree(){

        Tree newTree = new Tree(this.seed, this.maxDepth+1,this.varList,this.overallMaxDepth);
        newTree.root = this.root.copy();
        makeTreeCopy(this.root, newTree.root);
        newTree.score = 0;
        newTree.target = 0;
        newTree.avgFitness = 0;
        newTree.totalFitness = 0;
        newTree.trainCounts = 0;
        return newTree;

    }

    public void makeTreeCopy(Node orgTree, Node newTree){

        if(childLess(orgTree)){
            return;
        }

        if(hasOnlyLeftChild(orgTree)){
            newTree.setLeft(orgTree.getLeft().copy());
            makeTreeCopy(orgTree.getLeft(), newTree.getLeft());
            return;
        }
        else if(hasOnlyRightChild(orgTree)){
            newTree.setRight(orgTree.getRight().copy());
            makeTreeCopy(orgTree.getRight(), newTree.getRight());
            return;
        }
        else if(hasFullChild(orgTree)){
            newTree.setLeft(orgTree.getLeft().copy());
            newTree.setRight(orgTree.getRight().copy());
            makeTreeCopy(orgTree.getLeft(), newTree.getLeft());
            makeTreeCopy(orgTree.getRight(), newTree.getRight());
            return;
        }
        return;

    }

    public Node getRoot() {
        return root;
    }

    // Getter for target
    public double getTarget() {
        return target;
    }

    public double getFitness() {
        return fitness;
    }

    // Setter for target
    public void setTarget(double target) {
        this.target = target;
    }

    // Getter for score
    public double getScore() {
        return score;
    }

    // Setter for score
    public void setScore(double score) {
        this.score = score;
    }

    public int getMaxDepth() {
        return maxDepth;
    }


    public boolean populateTree(){
        int amount =  (int)Math.pow(2, maxDepth) + (int)Math.ceil(maxDepth/4);
        for (int i = 0; i < 2*amount; i++) {
            boolean inserted = this.insertNode();
            if(!inserted){return false;}
        }

        return true;

    }

    public void updateValues(String[] values, String targetValue){
        this.target = Double.parseDouble(targetValue);
        updateValuesForVarRec(root, values );
    }

    public static void updateValuesForVarRec(Node node, String[] values) {
        if (node == null) return;

        if(node.getType() == 1 && node.getvarName() != "Constant"){
            node.setValue(Double.parseDouble(values[node.getvarNum()]));
        }

        updateValuesForVarRec(node.getLeft(),values);
        updateValuesForVarRec(node.getRight(),values);
    }

    public boolean insertNode(){

        // Find where to insert node.
        Node parNode = findInsertPlace(0, root);

        if(parNode == null || parNode.getType()==1){
            return false;
        }

        int depth = findDepth(parNode);


        //If the parNode is a terminal a function should be added
        if(depth == maxDepth-1){

            int varNum = makeVar();
            int val = makeConstant(varNum);

            if(hasOnlyRightChild(parNode)){
                parNode.setLeft(new Node(1, varNum, getName(varNum), val,"",varList));
                return true; 
            }
            // Right null, left not. putright
            else if(hasOnlyLeftChild(parNode)){

                parNode.setRight(new Node(1, varNum, getName(varNum), val,"",varList));
                return true; 
            }
            // Both not null. Choose where to put
            else if(childLess(parNode)){

                int choose = this.random.nextInt(2);

                switch (choose) {
                    case 0:
                        parNode.setLeft(new Node(1, varNum, getName(varNum), val,"",varList));
                        return true;
                    case 1:
                        parNode.setRight(new Node(1, varNum, getName(varNum), val,"",varList));
                        return true; 
                      default:   
                    }
                return false;
            }
            else{ 
                //no place to put.
                return true;
            }
        }

        
        int typeNum = this.random.nextInt(10);

        if(typeNum < 10){
            // Make function Node

            if(hasOnlyRightChild(parNode)){
               
                parNode.setLeft(new Node( 0, -1, "", 0,makeOperator(),varList));
                return true; 
            }
            // Right null, left not. putright
            else if(hasOnlyLeftChild(parNode)){

                parNode.setRight(new Node( 0, -1, "", 0,makeOperator(),varList));
                return true; 
            }
            // Both not null. Choose where to put
            else if(childLess(parNode)){

                int choose = this.random.nextInt(2);

                switch (choose) {
                    case 0:
                        parNode.setLeft(new Node( 0, -1, "", 0,makeOperator(),varList));
                        return true;
                    case 1:
                        parNode.setRight(new Node( 0, -1, "", 0,makeOperator(),varList));
                        return true; 
                      default:   
                    }
                return false;
            }
            else{ 
                //no place to put.
                return true;
            }
            

        }else{
            // Make terminal Node

            int varNum = makeVar();
            int val = makeConstant(varNum);

            if(hasOnlyRightChild(parNode)){
            
                parNode.setLeft(new Node(1, varNum, getName(varNum), val,"",varList));
                return true; 
            }
            // Right null, left not. putright
            else if(hasOnlyLeftChild(parNode)){

                parNode.setRight(new Node(1, varNum, getName(varNum), val,"",varList));
                return true; 
            }
            // Both not null. Choose where to put
            else if(childLess(parNode)){

                int choose = this.random.nextInt(2);

                switch (choose) {
                    case 0:
                        parNode.setLeft(new Node(1, varNum, getName(varNum), val,"",varList));
                        return true;
                    case 1:
                        parNode.setRight(new Node(1, varNum, getName(varNum), val,"",varList));
                        return true; 
                      default:   
                    }
                return false;
            }
            else{ 
                //no place to put.
                return true;
            }
        }

    } // End of insert Node


    public Node findInsertPlace(int currLevel, Node currNode){
        
        //Base case reached, either bottom or null branch
        if(currLevel == maxDepth-1 || (childLess(currNode) && currLevel <= maxDepth-1)){
            return currNode;
        }
        //Not base case
        else{   
                // Left null, right not. Choose go deeper or branch here
                if(hasOnlyRightChild(currNode)){
                    int deeperOrNot = this.random.nextInt(2);

                    switch (deeperOrNot) {
                        case 0:
                                return currNode;
                        case 1:
                                return findInsertPlace(currLevel++, currNode.getRight());
                          default:   
                        }
                        return currNode; 
                }
                // Right null, left not. Choose go deeper or branch here
                else if(hasOnlyLeftChild(currNode)){
                    int deeperOrNot = this.random.nextInt(2);

                    switch (deeperOrNot) {
                        case 0:
                                return currNode;
                        case 1:
                                return findInsertPlace(currLevel++, currNode.getLeft());
                          default:   
                        }
                        return currNode;
                }
                // Both not null. Choose where to go.
                else{

                    int deeper = this.random.nextInt(2);

                    switch (deeper) {
                        case 0:
                            return findInsertPlace(currLevel++, currNode.getRight());
                        case 1:
                                return findInsertPlace(currLevel++, currNode.getLeft());
                          default:   
                        }
                        return null;

                }

            
        }

       
    }// End of findInsertPlace function.


    public int findDepth(Node target) {
        return findNodeDepth(root, target, 0);
    }
    
    // Helper recursive function to calculate depth
    private int findNodeDepth(Node current, Node target, int depth) {
        if (current == null) {
            return -1; // Node not found
        }
        if (current == target) {
            return depth;
        }
    
        // Search left subtree
        int leftDepth = findNodeDepth(current.getLeft(), target, depth + 1);
        if (leftDepth != -1) return leftDepth;
    
        // Search right subtree
        return findNodeDepth(current.getRight(), target, depth + 1);
    }

    public String makeOperator(){

        this.seed = this.seed+ 1;
        this.random = new Random(this.seed);
        int opNumber = this.random.nextInt(6);
        String opString = "";
        // Choose function operator
        switch (opNumber) {
            case 0:
                opString = "+";
                break;
            case 1:
                opString = "-";
                break;
            case 2:
                opString = "*";
                break;
            case 3:
                opString = "/";
                break;
            case 4:
                opString = "%";
                break;
            case 5:
                opString = "^";
                break;
            default:   
        }

        return opString;

    }//End make Operator function


    public int makeVar() {
       return this.random.nextInt(varList.length+1); 
    }

    public int makeConstant(int num) {
        if (num == varList.length){
            return this.random.nextInt(11); // Generates a number between 0 and 10 (inclusive)
        }
        return 0; // Default value for other years
    }

    public String getName(int num){
        if(num == varList.length){
            return "Constant";
        }
        return varList[num];
    }

    public double calculateFitness(){
        // if(getDepth() > overallMaxDepth+2){
        //     this.score = 1000;
        // }else{
        this.score = calculateScore();
        // }
        
        // this.fitness = Math.pow(this.target - this.score, 2);
        // this.fitness =  1 / (1 + this.fitness);
        if(target == this.score){
        // System.out.println("the same" );
            this.fitness = 1;
        }
        else{
            this.fitness = -2;
            // System.out.println("NOT the same" );
        }
        // System.out.println("-----------------------");
        // System.out.println("Target score: "+ this.target );
        // System.out.println("calc score: "+ this.score );
        // System.out.println("fitness: "+ this.fitness );

        trainCounts++;
        // System.out.println("TrainCount: "+ this.trainCounts );
        totalFitness += fitness;
        // System.out.println("total fitness: "+ this.totalFitness );
        avgFitness = totalFitness/trainCounts;
        // System.out.println("varage fitness: "+ this.avgFitness );
        // System.out.println("-----------------------");
        return avgFitness;
    }

     public double calculateAccuracy(){
        // if(getDepth() > overallMaxDepth+2){
        //     this.score = 1000;
        // }else{
        this.score = calculateScore();
        // }
        
        // this.fitness = Math.pow(this.target - this.score, 2);
        // this.fitness =  1 / (1 + this.fitness);
        if(target == this.score){
        // System.out.println("the same" );
            this.fitness = 1;
        }
        else{
            this.fitness = 0;
            // System.out.println("NOT the same" );
        }
        // System.out.println("-----------------------");
        // System.out.println("Target score: "+ this.target );
        // System.out.println("calc score: "+ this.score );
        // System.out.println("fitness: "+ this.fitness );

        trainCounts++;
        // System.out.println("TrainCount: "+ this.trainCounts );
        totalFitness += fitness;
        // System.out.println("total fitness: "+ this.totalFitness );
        avgFitness = totalFitness/trainCounts;
        // System.out.println("varage fitness: "+ this.avgFitness );
        // System.out.println("-----------------------");
        return avgFitness;
    }

    public double calculateScore(){
        if(root == null){
            
            return 0;
        }
        this.score = recCalcScore(root);
        this.score = sigmoid(this.score);
        if(this.score <= 0.5){
            this.score = 1;
        }else{
            this.score = 2;
        }
        return this.score;

    }//End calc score 

    //Recursive score calculation
    public double recCalcScore(Node currNode){

        if(childLess(currNode)){ //base case
            return currNode.getValue();
        }
        else{
            if(hasFullChild(currNode)){ // if there are two children
                if(currNode.getType()==0){
                    return operatorCalculation(recCalcScore(currNode.getLeft()),recCalcScore(currNode.getRight()),currNode.getOperator());                
                }else{
                    return currNode.getValue();
                }
            }
                else if(hasOnlyLeftChild(currNode)){ //if only left child
                    if(currNode.getType()==0){
                        if(currNode.getType()==0){
                            return operatorCalculation(recCalcScore(currNode.getLeft()),2,currNode.getOperator());
                        }
                }else{
                    return currNode.getValue();
                }
            }
                else if(hasOnlyRightChild(currNode)){ // if only right child
                    if(currNode.getType()==0){
                        if(currNode.getType()==0){
                            return operatorCalculation(2,recCalcScore(currNode.getRight()),currNode.getOperator());
                        }
                }else{
                    return currNode.getValue();
                }
            }
        }

        return currNode.getValue();
    }

    public static double sigmoid(double x) {
        return 1.0 / (1.0 + Math.exp(-x));
    }

    // Check if node has only left child
    public boolean hasOnlyLeftChild(Node node) {
        return node.getLeft() != null && node.getRight() == null;
    }

    // Check if node has only right child
    public boolean hasOnlyRightChild(Node node) {
        return node.getLeft() == null && node.getRight() != null;
    }

    // Check if node has both children (full child)
    public boolean hasFullChild(Node node) {
        return node.getLeft() != null && node.getRight() != null;
    }

    // Check if node has no children
    public boolean childLess(Node node) {
        return node.getLeft() == null && node.getRight() == null;
    }
    
    public double operatorCalculation(double numOne, double numTwo, String opString){

        double result = 0;

        switch (opString) {
            case "+":
                    result = numOne + numTwo;  // Addition
                break;
            case "-":
                    result = numOne - numTwo;  // Subtraction
                break;
            case "*":
                    result = numOne * numTwo;  // Multiplication
                break;
            case "/":
                    if (numTwo != 0) {
                     result = numOne / numTwo;  // Division
                    }
                    else{
                        result = numOne / 1;  // Division
                    }
                break;
            case "%":
                    //result = numOne - numTwo;
                    if (numTwo != 0) {
                        result = numOne % numTwo;  // Modulus (remainder)
                    } else {
                        result = numOne - 1;  // Handling modulus by zero
                    }
                break;
            case "^":
                    //result = numOne + numTwo;
                    result = (double) Math.pow(numOne, numTwo);  // Exponentiation
                break;
            default:

        }

        return result;

    }

    // Method to get the depth of the tree
    public int getDepth() {
        return calculateDepth(root);
    }       

    private int calculateDepth(Node node) {
        if (node == null) return 0;
        return 1 + Math.max(calculateDepth(node.getLeft()), calculateDepth(node.getRight()));
    }       

    // Method to count total nodes in the tree
    public int countNodes() {
        return countNodesRecursive(root);
    }       

    private int countNodesRecursive(Node node) {
        if (node == null) return 0;
        return 1 + countNodesRecursive(node.getLeft()) + countNodesRecursive(node.getRight());
    }

    public Node getRandomNode(Random ran) {
        int totalNodes = countNodes();
        if (totalNodes == 0) return null;

        int randomIndex = ran.nextInt(totalNodes); // Generate random index
        return getNodeByIndex(root, new int[]{randomIndex}); // Get node at index
    }

    // Recursive method to find the node at a given index in pre-order traversal
    private Node getNodeByIndex(Node node, int[] index) {
        if (node == null) return null;

        if (index[0] == 0) {
            return node;
        }
        index[0]--; // Decrease index as we traverse

        Node leftResult = getNodeByIndex(node.getLeft(), index);
        if (leftResult != null) return leftResult;

        return getNodeByIndex(node.getRight(), index);
    }

    // Get a random node by type (0 for function, 1 for terminal)
    public Node getRandomNodeByType(int type) {
        List<Node> matchingNodes = new ArrayList<>();
        collectNodesByType(root, type, matchingNodes);

        if (matchingNodes.isEmpty()) return null; // No matching nodes found

        int randomIndex = random.nextInt(matchingNodes.size());
        return matchingNodes.get(randomIndex);
    }

    // Recursive helper method to collect nodes of the given type
    private void collectNodesByType(Node node, int type, List<Node> nodeList) {
        if (node == null) return;

        if (node.getType() == type) {
            nodeList.add(node);
        }

        collectNodesByType(node.getLeft(), type, nodeList);
        collectNodesByType(node.getRight(), type, nodeList);
    }


    public void fixLeaves(){
        recFixLeaves(root);
    }

    public void recFixLeaves(Node cNode){
        if (cNode == null) {
            return;
        }
        this.seed++;
        this.random = new Random(seed);
        cNode.fix(random);
        // If it's a leaf node (terminal node), return its value
        
    
        // Recursively build left and right expressions
        recFixLeaves(cNode.getLeft());
        recFixLeaves(cNode.getRight());
    
        return;
    }

    public void resetVarNames(){
        //System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAaa ");
        resetNamesRec(root);
    }

    public void resetNamesRec(Node cNode){
        if (cNode == null) {
            return;
        }
        cNode.varNameList = this.varList;
        //System.out.println("Var list " +  cNode.varNameList.length);
        // If it's a leaf node (terminal node), return its value
        
    
        // Recursively build left and right expressions
        resetNamesRec(cNode.getLeft());
        resetNamesRec(cNode.getRight());
    }




    public String toString() {
        return buildMathExpression(root);
    }
    
    private String buildMathExpression(Node node) {
        if (node == null) {
            return "2";
        }
    
        // If it's a leaf node (terminal node), return its value
        if (node.getLeft() == null && node.getRight() == null) {
            return node.toString();
        }
    
        // Recursively build left and right expressions
        String leftExpr = buildMathExpression(node.getLeft());
        String rightExpr = buildMathExpression(node.getRight());
    
        // Wrap in brackets to indicate execution order
        return "(" + leftExpr + " " + node.toString() + " " + rightExpr + ")";
    }




    
/*
    @Override
    public String toString() {
        return traverseTree(root, 0, true);
    }
    
    private String traverseTree(Node node, int depth, boolean isRight) {
        if (node == null) return "";
    
        StringBuilder sb = new StringBuilder();
    
        // Prefix for branches
        String prefix = "   ".repeat(depth) + (depth == 0 ? "" : (isRight ? "└── " : "├── "));
    
        // Append node info
        sb.append(prefix).append(node.toString()).append("\n");
    
        // Recursively append left and right children
        sb.append(traverseTree(node.getLeft(), depth + 1, false));
        sb.append(traverseTree(node.getRight(), depth + 1, true));
    
        return sb.toString();
    }
        */
    

}
