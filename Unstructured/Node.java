import java.util.Random;
public class Node {
    
    private int type;
    private String varName;
    private int varNum;
    private double value;
    private String operator;
    private Node left;
    private Node right;
    public String varNameList[];


    // Constructor

    public Node(int type, int varNum, String varName, double value, String operator, String[] varList) {
        this.type = type;
        this.varName = varName;
        this.varNum = varNum;
        this.value = value;
        this.operator = operator;
        this.varNameList = varList;
        this.left = null;
        this.right = null;
        //System.out.println(toString());
    }

    // Getters
    public int getType() { 
        return type;
    }

    public String getvarName() { 
        return varName; 
    }

    public int getvarNum() { 
        return varNum; 
    }

    public double getValue() { 
        return value; 
    }

    public String getOperator() { 
        return operator; 
    }

    public Node getLeft() { 
        return left; 
    }

    public Node getRight() { 
        return right; 
    }


    // Setters
    public void setType(int type) { 
        this.type = type; 
    }

    public void setvarName(String varName) { 
        this.varName = varName; 
    }

    public void setVarList(String[] list) { 
        this.varNameList = list; 
    }

    public void setvarNum(int varNum) { 
        this.varNum = varNum; 
    }

    public void setValue(double value) { 
        this.value = value; 
    }

    public void setOperator(String operator) { 
        this.operator = operator; 
    }

    public void setLeft(Node left) { 
        this.left = left; 
    }

    public void setRight(Node right) { 
        this.right = right; 
    }

    public Node copy(){
        Node newNode = new Node(this.type, this.varNum, this.varName, this.value, this.operator, this.varNameList);
        return newNode;
    }

    public void mutateNode(Random random){

        if(type ==1){
            int ran = random.nextInt(varNameList.length+1);
            if(ran < varNameList.length){
                this.varNum = ran;
                this.varName = this.varNameList[ran];
            }else{
                this.varNum = ran;
                this.varName = "Constant";
                this.value = random.nextInt(11);
            }
        }else{
               
                this.operator = makeOperator(random);

        }

    }
        public String makeOperator(Random random){

            int opNumber = random.nextInt(6);
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
                    opString = "-";
                    break;
                default:   
            }
    
            return opString;
    
        }
    

        public void fix(Random random){
            if(left== null && right == null && type != 1){
                    int ran = random.nextInt(varNameList.length);
                    Node left = new Node(1, ran, varNameList[ran], 0, "", varNameList);
                    Node right = new Node(1, ran, varNameList[ran], 0, "", varNameList);
                    this.left = left;
                    this.right =right;
                    
                }else if(type == 0  && (left== null || right == null)){
                    int ran = random.nextInt(varNameList.length);
                    if(left == null && right != null){
                        left = new Node(1, ran, varNameList[ran], 0, "", varNameList);
                    } if(right == null && left != null){
                        right = new Node(1, ran, varNameList[ran], 0, "", varNameList);
                    }
                    
            }else if(type != 0   && left!= null && right != null){
                    this.operator = makeOperator(random);
                    this.type = 0;
                    this.varName = "";
                    this.value = 0;
                    this.varName ="";
                    this.varNum = -1;

            }
        }


    // ToString Method
    public String toString() {

        if(this.left == null && this.right==null && this.type==0){
            return String.valueOf(value);
        }

        
        if (varName.equals("Constant")) {
            return String.valueOf(value); // Treat varName 1 as a constant
        } else if (type == 0) {
            return operator; // Operators remain unchanged
        }
        else{
            return varName;
        }
    }
    


}
