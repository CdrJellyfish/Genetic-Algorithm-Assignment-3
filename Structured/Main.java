import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {

      
        
        //int seed = -1169561460 ; // This is the best seed as stated in the document
      
        
       
        int seed = (int) System.currentTimeMillis();
        singleTestRun(-387497161);
       
        //transferTest();
        //paramTuneTest();
      
        // finalTestNoStruct();
        //}
        //testCSVReader();
     

    }

    public static void testCSVReader() {

        CSVReader reader = new CSVReader();
        reader.extractFile();
        int one = 0;
        int two = 0;
        for (int j = 0; j < reader.target.length; j++) {
            if(Integer.parseInt(reader.target[j]) == 1){
                one++;
            }else{
                two++;
            }
        }
         System.out.println(one);
          System.out.println(two);
        // for (int j = 0; j < reader.data.length; j++) {
        //     for (int i = 0; i < reader.data[j].length; i++) {
        //         System.out.println(reader.data[j][i]);
        //     }
        // }

    }

    public static void singleRun(int seed, int epochs, int size, int depth, double gSplit, double tSplit) {

        GP g = new GP(seed, epochs, size, depth, gSplit, tSplit);
        g.train();
        System.out.println();
        System.out.println(" tree: " + g.bestTree.toString());
        System.out.println("Train average fitness: " + g.bestTree.avgFitness + "\n" + "---------" + "\n");
        g.test();
        System.out.println("Test average fitness: " + g.bestTree.avgFitness + "\n" + "---------" + "\n");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("SingleRun.txt", true))) {
            writer.write("Seed : " + seed + " Best Tree Fitness : " + g.bestTree.avgFitness + " Bets Function :"
                    + g.bestTree.toString() + "\n");
        } catch (IOException e) {
            System.err.println("Error writing to seed file: " + e.getMessage());
        }

    }

    public static void singleTestRun(int seed) {

       int epochs = 200;
        int size = 50;
        int depth = 6;
        double gSplit = 0.4;
        double tSplit = 0.8;
     
        GP g = new GP(seed,200,50,6,0.4,0.8);
        g.train();
        g.test();
        System.out.println( "\n" + "---------" + "\n" + "seed: " + seed + " Tree: " + g.bestTree.toString() + " MAE: " + g.bestTree.avgFitness
                + "\n" + "---------" + "\n");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("SingelTransferRun.txt", true))) {
            writer.write(" Seed: " + +seed + " Best tree AveFitness: " + g.bestTree.avgFitness + " Tree Form:" + g.bestTree.toString() + "\\" + "\\"
                    + "\n");
        } catch (IOException e) {
            System.err.println("Error writing to seed file: " + e.getMessage());
        }
    }

    
    public static void finalTest() {

        double ave = 0;
        double[] sdCalc = new double[10];
        int epochs = 200;
        int size = 50;
        int depth = 6;
        double gSplit = 0.4;
        double tSplit = 0.8;

        Tree bTree = null;
        int bSeed = 0;

        ave = 0;
        bTree = null;
        bSeed = 0;
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("FinalTests.txt", true))) {
            writer.write("FinalTests :" + "\n");
        } catch (IOException e) {
            System.err.println("Error writing to seed file: " + e.getMessage());
        }

        for (int i = 0; i < 10; i++) {
            System.out.println(i);

            int seed = (int) System.currentTimeMillis();

            GP g = new GP(seed, epochs, size, depth, gSplit, tSplit);
            g.train();
            g.testAll();
            // System.out.println("seed: " + seed + " tree: " + g.bestTree.toString() + " aveF: " + g.bestTree.avgFitness
            //         + "\n" + "---------" + "\n");

            try (BufferedWriter writer = new BufferedWriter(new FileWriter("FinalTests.txt", true))) {
                writer.write(i + "&" + +seed + "&" + g.bestTree.avgFitness + "&" + g.bestTree.toString() + "\\" + "\\"
                        + "\n");
            } catch (IOException e) {
                System.err.println("Error writing to seed file: " + e.getMessage());
            }

            if (bTree == null) {
                bTree = g.bestTree.copyTree();
                bTree.avgFitness = g.bestTree.avgFitness;
                bSeed = seed;
            } else if (g.bestTree.avgFitness > bTree.avgFitness) {
                bTree = g.bestTree.copyTree();
                bTree.avgFitness = g.bestTree.avgFitness;
                bSeed = seed;
            }

            if (Double.isNaN(g.bestTree.avgFitness) || Double.isInfinite(g.bestTree.avgFitness)) {
                ave = ave + 4;
                sdCalc[i] = 4;
            } else {
                ave = ave + g.bestTree.avgFitness;
                sdCalc[i] = g.bestTree.avgFitness;
            }
        }
        double sd = 0;
        ave = ave / 10;
        for (int i = 0; i < 10; i++) {
            sd = sd + Math.pow((sdCalc[i] - ave), 2);
        }
        sd = Math.sqrt(sd / 10);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("FinalTests.txt", true))) {
            writer.write("\n\n**********************************\n\nAVERAGE TEST  \n" + "= " + ave + " sd: " + sd
                    + "\nBest tree seed: "

                    + bSeed + " avgFitness: " + bTree.avgFitness + " function: " + bTree.toString()
                    + "\n**********************************\n\n");
        } catch (IOException e) {
            System.err.println("Error writing to seed file: " + e.getMessage());
        }

    }

     public static void finalTestNoStruct() {

        double ave = 0;
        double[] sdCalc = new double[10];
        int epochs = 200;
        int size = 50;
        int depth = 6;
        double gSplit = 0.4;
        double tSplit = 0.8;

        Tree bTree = null;
        int bSeed = 0;

        ave = 0;
        bTree = null;
        bSeed = 0;
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("NoStruct.txt", true))) {
            writer.write("NoStruct :" + "\n");
        } catch (IOException e) {
            System.err.println("Error writing to seed file: " + e.getMessage());
        }

        for (int i = 0; i < 10; i++) {
            System.out.println(i);

            int seed = (int) System.currentTimeMillis();

            GP g = new GP(seed, epochs, size, depth, gSplit, tSplit);
            g.train();
            g.testAll();
            // System.out.println("seed: " + seed + " tree: " + g.bestTree.toString() + " aveF: " + g.bestTree.avgFitness
            //         + "\n" + "---------" + "\n");

            try (BufferedWriter writer = new BufferedWriter(new FileWriter("NoStruct.txt", true))) {
                writer.write(i + "&" + +seed + "&" + g.bestTree.avgFitness + "&" + g.bestTree.toString() + "\\" + "\\"
                        + "\n");
            } catch (IOException e) {
                System.err.println("Error writing to seed file: " + e.getMessage());
            }

            if (bTree == null) {
                bTree = g.bestTree.copyTree();
                bTree.avgFitness = g.bestTree.avgFitness;
                bSeed = seed;
            } else if (g.bestTree.avgFitness > bTree.avgFitness) {
                bTree = g.bestTree.copyTree();
                bTree.avgFitness = g.bestTree.avgFitness;
                bSeed = seed;
            }

            if (Double.isNaN(g.bestTree.avgFitness) || Double.isInfinite(g.bestTree.avgFitness)) {
                ave = ave + 4;
                sdCalc[i] = 4;
            } else {
                ave = ave + g.bestTree.avgFitness;
                sdCalc[i] = g.bestTree.avgFitness;
            }
        }
        double sd = 0;
        ave = ave / 10;
        for (int i = 0; i < 10; i++) {
            sd = sd + Math.pow((sdCalc[i] - ave), 2);
        }
        sd = Math.sqrt(sd / 10);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("NoStruct.txt", true))) {
            writer.write("\n\n**********************************\n\nAVERAGE TEST  \n" + "= " + ave + " sd: " + sd
                    + "\nBest tree seed: "

                    + bSeed + " avgFitness: " + bTree.avgFitness + " function: " + bTree.toString()
                    + "\n**********************************\n\n");
        } catch (IOException e) {
            System.err.println("Error writing to seed file: " + e.getMessage());
        }

    }

    public static void paramTuneTest() {

        double ave = 0;
        int epochs = 5;
        int size = 50;
        int depth = 4;
        double gSplit = 0.5;
        double tSplit = 0.8;

        Tree bTree = null;
        int bSeed = 0;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("seedsAndStuff.txt", true))) {
            writer.write("Testing epochs variability :" + "\n\n");
        } catch (IOException e) {
            System.err.println("Error writing to seed file: " + e.getMessage());
        }

        for (int j = 0; j < 5; j++) {
            ave = 0;
            epochs = epochs * 2;
            bTree = null;
            bSeed = 0;
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("seedsAndStuff.txt", true))) {
                writer.write("TESTING EPOCHS : " + epochs + "\n");
            } catch (IOException e) {
                System.err.println("Error writing to seed file: " + e.getMessage());
            }

            for (int i = 0; i < 10; i++) {
                System.out.println(i);

                int seed = (int) System.currentTimeMillis();

                GP g = new GP(seed, epochs, size, depth, gSplit, tSplit);
                g.train();
                // System.out.println("seed: " +seed + " tree: "+ g.bestTree.toString() + "
                // aveF: " + g.bestTree.avgFitness+ "\n"+"---------"+"\n");

                // try (BufferedWriter writer = new BufferedWriter(new
                // FileWriter("seedsAndStuff.txt", true))) {
                // writer.write("Seed: " +seed + " -- Tree form: "+ g.bestTree.toString() + " --
                // Average traning fitness: " + g.bestTree.avgFitness);
                // } catch (IOException e) {
                // System.err.println("Error writing to seed file: " + e.getMessage());
                // }
                g.test();
                // try (BufferedWriter writer = new BufferedWriter(new
                // FileWriter("seedsAndStuff.txt", true))) {
                // writer.write("Test average fitness: " + g.bestTree.avgFitness+ " -- Tree
                // form: "+ g.bestTree.toString() +"\n"+"---------"+"\n");
                // } catch (IOException e) {
                // System.err.println("Error writing to seed file: " + e.getMessage());
                // }

                if (bTree == null) {
                    bTree = g.bestTree.copyTree();
                    bTree.avgFitness = g.bestTree.avgFitness;
                    bSeed = seed;
                } else if (g.bestTree.avgFitness > bTree.avgFitness) {
                    bTree = g.bestTree.copyTree();
                    bTree.avgFitness = g.bestTree.avgFitness;
                    bSeed = seed;
                }

                ave = ave + g.bestTree.avgFitness;
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter("seedsAndStuff.txt", true))) {
                writer.write("\n\n**********************************\n\nAVERAGE OF EPOCHS TEST  " + epochs + "\n" + "= "
                        + ave / 10 + "\nBest tree seed: "
                        + bSeed + " avgFitness: " + bTree.avgFitness + " function: " + bTree.toString()
                        + "\n**********************************\n\n");
            } catch (IOException e) {
                System.err.println("Error writing to seed file: " + e.getMessage());
            }

        }

        epochs = 50;
        size = 5;
        depth = 5;
        gSplit = 0.5;
        tSplit = 0.8;
       

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("seedsAndStuff.txt", true))) {
            writer.write("\n\n\nTesting size variability :" + "\n\n");
        } catch (IOException e) {
            System.err.println("Error writing to seed file: " + e.getMessage());
        }

        for (int j = 0; j < 5; j++) {
            ave = 0;
            size = size * 2;
            bTree = null;
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("seedsAndStuff.txt", true))) {
                writer.write("TESTING SIZE : " + size + "\n");
            } catch (IOException e) {
                System.err.println("Error writing to seed file: " + e.getMessage());
            }

            for (int i = 0; i < 10; i++) {

                System.out.println(i);
                int seed = (int) System.currentTimeMillis();

                GP g = new GP(seed, epochs, size, depth, gSplit, tSplit);
                g.train();
                // System.out.println("seed: " +seed + " tree: "+ g.bestTree.toString() + "
                // aveF: " + g.bestTree.avgFitness+ "\n"+"---------"+"\n");

                // try (BufferedWriter writer = new BufferedWriter(new
                // FileWriter("seedsAndStuff.txt", true))) {
                // writer.write("Seed: " +seed + " -- Tree form: "+ g.bestTree.toString() + " --
                // Average traning fitness: " + g.bestTree.avgFitness);
                // } catch (IOException e) {
                // System.err.println("Error writing to seed file: " + e.getMessage());
                // }
                g.test();
                // try (BufferedWriter writer = new BufferedWriter(new
                // FileWriter("seedsAndStuff.txt", true))) {
                // writer.write("Test average fitness: " + g.bestTree.avgFitness+
                // "\n"+"---------"+"\n");
                // } catch (IOException e) {
                // System.err.println("Error writing to seed file: " + e.getMessage());
                // }

                if (bTree == null) {
                    bTree = g.bestTree.copyTree();
                    bTree.avgFitness = g.bestTree.avgFitness;
                    bSeed = seed;
                } else if (g.bestTree.avgFitness > bTree.avgFitness) {
                    bTree = g.bestTree.copyTree();
                    bTree.avgFitness = g.bestTree.avgFitness;
                    bSeed = seed;
                }

                ave = ave + g.bestTree.avgFitness;
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter("seedsAndStuff.txt", true))) {
                writer.write("\n\n**********************************\n\nAVERAGE OF SIZE TEST  " + size + "\n" + "= "
                        + ave / 10 + "\nBest tree seed: "
                        + bSeed + " avgFitness: " + bTree.avgFitness + " function: " + bTree.toString()
                        + "\n**********************************\n\n");
            } catch (IOException e) {
                System.err.println("Error writing to seed file: " + e.getMessage());
            }

        }

        epochs = 50;
        size = 50;
        depth = 2;
        gSplit = 0.5;
        tSplit = 0.8;
        

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("seedsAndStuff.txt", true))) {
            writer.write("\n\n\nTesting Depth variability :" + "\n\n");
        } catch (IOException e) {
            System.err.println("Error writing to seed file: " + e.getMessage());
        }

        for (int j = 0; j < 5; j++) {
            ave = 0;
            depth = depth + 1;
            bTree = null;
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("seedsAndStuff.txt", true))) {
                writer.write("TESTING DEPTH : " + depth + "\n");
            } catch (IOException e) {
                System.err.println("Error writing to seed file: " + e.getMessage());
            }

            for (int i = 0; i < 10; i++) {
                System.out.println(i);
                int seed = (int) System.currentTimeMillis();

                GP g = new GP(seed, epochs, size, depth, gSplit, tSplit);
                g.train();
                // System.out.println("seed: " +seed + " tree: "+ g.bestTree.toString() + "
                // aveF: " + g.bestTree.avgFitness+ "\n"+"---------"+"\n");

                // try (BufferedWriter writer = new BufferedWriter(new
                // FileWriter("seedsAndStuff.txt", true))) {
                // writer.write("Seed: " +seed + " -- Tree form: "+ g.bestTree.toString() + " --
                // Average traning fitness: " + g.bestTree.avgFitness);
                // } catch (IOException e) {
                // System.err.println("Error writing to seed file: " + e.getMessage());
                // }
                g.test();
                // try (BufferedWriter writer = new BufferedWriter(new
                // FileWriter("seedsAndStuff.txt", true))) {
                // writer.write("Test average fitness: " + g.bestTree.avgFitness+
                // "\n"+"---------"+"\n");
                // } catch (IOException e) {
                // System.err.println("Error writing to seed file: " + e.getMessage());
                // }

                if (bTree == null) {
                    bTree = g.bestTree.copyTree();
                    bTree.avgFitness = g.bestTree.avgFitness;
                    bSeed = seed;
                } else if (g.bestTree.avgFitness > bTree.avgFitness) {
                    bTree = g.bestTree.copyTree();
                    bTree.avgFitness = g.bestTree.avgFitness;
                    bSeed = seed;
                }

                ave = ave + g.bestTree.avgFitness;
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter("seedsAndStuff.txt", true))) {
                writer.write("\n\n**********************************\n\nAVERAGE OF DEPTH TEST  " + depth + "\n" + "= "
                        + ave / 10 + "\nBest tree seed: "
                        + bSeed + " avgFitness: " + bTree.avgFitness + " function: " + bTree.toString()
                        + "\n**********************************\n\n");
            } catch (IOException e) {
                System.err.println("Error writing to seed file: " + e.getMessage());
            }

        }

        epochs = 50;
        size = 50;
        depth = 2;
        gSplit = 0.2;
        tSplit = 0.8;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("seedsAndStuff.txt", true))) {
            writer.write("\n\n\nTesting gSplit variability :" + "\n\n");
        } catch (IOException e) {
            System.err.println("Error writing to seed file: " + e.getMessage());
        }

        for (int j = 0; j < 5; j++) {
            ave = 0;
            gSplit = gSplit + 0.1;
            bTree = null;
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("seedsAndStuff.txt", true))) {
                writer.write("TESTING GSPLIT : " + gSplit + "\n");
            } catch (IOException e) {
                System.err.println("Error writing to seed file: " + e.getMessage());
            }

            for (int i = 0; i < 10; i++) {
                System.out.println(i);
                int seed = (int) System.currentTimeMillis();

                GP g = new GP(seed, epochs, size, depth, gSplit, tSplit);
                g.train();
                // System.out.println("seed: " +seed + " tree: "+ g.bestTree.toString() + "
                // aveF: " + g.bestTree.avgFitness+ "\n"+"---------"+"\n");

                // try (BufferedWriter writer = new BufferedWriter(new
                // FileWriter("seedsAndStuff.txt", true))) {
                // writer.write("Seed: " +seed + " -- Tree form: "+ g.bestTree.toString() + " --
                // Average traning fitness: " + g.bestTree.avgFitness);
                // } catch (IOException e) {
                // System.err.println("Error writing to seed file: " + e.getMessage());
                // }
                g.test();
                // try (BufferedWriter writer = new BufferedWriter(new
                // FileWriter("seedsAndStuff.txt", true))) {
                // writer.write("Test average fitness: " + g.bestTree.avgFitness+
                // "\n"+"---------"+"\n");
                // } catch (IOException e) {
                // System.err.println("Error writing to seed file: " + e.getMessage());
                // }
                if (bTree == null) {
                    bTree = g.bestTree.copyTree();
                    bTree.avgFitness = g.bestTree.avgFitness;
                    bSeed = seed;
                } else if (g.bestTree.avgFitness > bTree.avgFitness) {
                    bTree = g.bestTree.copyTree();
                    bTree.avgFitness = g.bestTree.avgFitness;
                    bSeed = seed;
                }

                ave = ave + g.bestTree.avgFitness;
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter("seedsAndStuff.txt", true))) {
                writer.write("\n\n**********************************\n\nAVERAGE OF GSPLIT TEST  " + gSplit + "\n" + "= "
                        + ave / 10 + "\nBest tree seed: "
                        + bSeed + " avgFitness: " + bTree.avgFitness + " function: " + bTree.toString()
                        + "\n**********************************\n\n");
            } catch (IOException e) {
                System.err.println("Error writing to seed file: " + e.getMessage());
            }

        }

        epochs = 50;
        size = 50;
        depth = 2;
        gSplit = 0.5;
        tSplit = 0.3;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("seedsAndStuff.txt", true))) {
            writer.write("\n\n\nTesting tSplit variability :" + "\n\n");
        } catch (IOException e) {
            System.err.println("Error writing to seed file: " + e.getMessage());
        }

        for (int j = 0; j < 5; j++) {
            ave = 0;
            tSplit = tSplit + 0.1;
            bTree = null;
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("seedsAndStuff.txt", true))) {
                writer.write("TESTING TSPLIT : " + tSplit + "\n");
            } catch (IOException e) {
                System.err.println("Error writing to seed file: " + e.getMessage());
            }

            for (int i = 0; i < 10; i++) {
                System.out.println(i);
                int seed = (int) System.currentTimeMillis();

                GP g = new GP(seed, epochs, size, depth, gSplit, tSplit);
                g.train();
                // System.out.println("seed: " +seed + " tree: "+ g.bestTree.toString() + "
                // aveF: " + g.bestTree.avgFitness+ "\n"+"---------"+"\n");

                // try (BufferedWriter writer = new BufferedWriter(new
                // FileWriter("seedsAndStuff.txt", true))) {
                // writer.write("Seed: " +seed + " -- Tree form: "+ g.bestTree.toString() + " --
                // Average traning fitness: " + g.bestTree.avgFitness);
                // } catch (IOException e) {
                // System.err.println("Error writing to seed file: " + e.getMessage());
                // }
                g.test();
                // try (BufferedWriter writer = new BufferedWriter(new
                // FileWriter("seedsAndStuff.txt", true))) {
                // writer.write("Test average fitness: " + g.bestTree.avgFitness+
                // "\n"+"---------"+"\n");
                // } catch (IOException e) {
                // System.err.println("Error writing to seed file: " + e.getMessage());
                // }

                if (bTree == null) {
                    bTree = g.bestTree.copyTree();
                    bTree.avgFitness = g.bestTree.avgFitness;
                    bSeed = seed;
                } else if (g.bestTree.avgFitness > bTree.avgFitness) {
                    bTree = g.bestTree.copyTree();
                    bTree.avgFitness = g.bestTree.avgFitness;
                    bSeed = seed;
                }

                ave = ave + g.bestTree.avgFitness;
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter("seedsAndStuff.txt", true))) {
                writer.write("\n\n**********************************\n\nAVERAGE OF TSPLIT TEST  " + tSplit + "\n" + "= "
                        + ave / 10 + "\nBest tree seed: "
                        + bSeed + " avgFitness: " + bTree.avgFitness + " function: " + bTree.toString()
                        + "\n**********************************\n\n");
            } catch (IOException e) {
                System.err.println("Error writing to seed file: " + e.getMessage());
            }

        }

    }

}
