# Genetic Programming for Hepatitis Classification

## Overview

This project implements a Genetic Programming (GP) algorithm designed to classify medical records from the `hepatitis.tsv` dataset. The goal is to evolve mathematical equation trees that can predict whether a patient has hepatitis based on various medical attributes.

A key feature of this implementation is the use of **Structure Control**, which dynamically alters fitness scores to promote global and local search space exploration, combating premature convergence and overfitting caused by the dataset's class imbalance.

## Dataset

The system utilizes `hepatitis.tsv`, a dataset containing medical attributes of patients.

* **Structure:** The data is processed into three arrays: Headers, Target Values (Classification), and Spec Values (2D array of attributes).
* **Target Classes:**
* `1`: Has Hepatitis
* `2`: Does Not Have Hepatitis


* **Challenge:** The dataset is heavily skewed towards Class 2. The algorithm addresses this via a custom fitness function rather than altering the dataset itself.

## Algorithm Details

### Representation

* **Structure:** Tree-based structure representing mathematical equations.
* **Terminal Nodes:** Medical attribute values and constants (0-99).
* **Functional Nodes:** Mathematical operators including `+`, `-`, `*`, `/`, `%` (modulo), and `pow` (power).

### Evolutionary Process

1. **Initialization:** The population is generated using the **Grow Method**, creating an even spread of tree depths up to the maximum depth with randomized fullness.
2. **Selection:** **Tournament Selection** is used to choose parents, favoring better fitness while avoiding pure elitism.
3. **Crossover:** Subtrees are swapped between two parent trees. Deep copies are created to ensure the original population remains intact until the next generation.
4. **Mutation:** A random node is selected and mutated. Terminal nodes morph into other terminals/constants; functional nodes morph into other operators. Types are preserved (terminal vs. functional) to prevent invalid tree structures.

### Fitness Function

Accuracy is *not* used directly for fitness due to the class imbalance. Instead, the following logic is applied:

1. **Sigmoid Activation:** The tree's output is passed through a sigmoid function. Values  are classified as Class 1; others as Class 2.
2. **Scoring:**
* **Class 1 Match (Has Hepatitis):** +2 points (Weighted higher to prioritize detecting the positive class).
* **Class 2 Match (No Hepatitis):** +1 point.
* **Mismatch (Target was 1):** -2 points.
* **Mismatch (Target was 2):** 0 points.


3. **Penalties:** 20 points are deducted if the tree depth exceeds the specified limit or is too shallow (preventing "bloat" and unusable tiny trees).

### Structure Control (Variability)

To ensure diverse evolution, the fitness is dynamically adjusted based on population similarity:

* **Phase 1 (Global Search):** For the first half of generations, the algorithm calculates similarity based on the **top three layers** of the trees. Trees that are too similar to the population have their fitness reduced to force exploration of the global search space.
* **Phase 2 (Local Search):** For the second half, similarity is calculated using **layers below layer three**. This promotes local variation and fine-tuning.

## Parameters

The final hyperparameters used for the best-performing models were:

| Parameter | Value |
| --- | --- |
| **Epochs** | 200 |
| **Population Size** | 50 |
| **Max Depth** | 6 |
| **Genetic Operator Split** | 0.4 (40% Mutation, 60% Crossover) |
| **Train/Test Split** | 0.8 |
| **Seed** | System Time |

## Results

The project compared a standard Non-Structure GP against the Structure-Controlled GP.

* **Non-Structure GP:**
* Average Accuracy: ~36.13%
* Best Accuracy: ~80.43%


* **Structure-Controlled GP:**
* **Average Accuracy: ~58.47%**
* **Best Accuracy: ~87.09%**



The introduction of structure control significantly improved average performance and consistency by preventing the algorithm from settling on local optima defined by the majority class.
