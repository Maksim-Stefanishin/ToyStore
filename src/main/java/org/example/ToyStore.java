package org.example;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.io.BufferedReader;
import java.io.FileReader;
public class ToyStore {
    private final List<Toy> toys;
    private final FileWriterUtilInterface fileWriterUtil;

    public ToyStore(FileWriterUtilInterface fileWriterUtil) {
        this.toys = new ArrayList<>();
        this.fileWriterUtil = fileWriterUtil;
    }

    public void addToy(int id, String name, int quantity, double baseWeight) {

        double calculatedWeight = calculatedWeight(baseWeight,quantity);

        Toy toy = new Toy(id, name, quantity, calculatedWeight);
        toys.add(toy);
        redistributeWeights();
        saveToysToFile("toys.txt");



    }
    private double calculatedWeight(double baseWeight, int quantity){

        return baseWeight  /  quantity;
    }

   public void redistributeWeights() {
        double totalWeight = toys.stream().mapToDouble(Toy::getWeight).sum();

        if (totalWeight > 100) {
            double scaleFactor = 100 / totalWeight;


            for (Toy toy : toys) {
                double newWeight = toy.getWeight() * scaleFactor;
                toy.setWeight(newWeight);
            }

            System.out.println("Total weight exceeds 100. Redistributed weights to maintain proportion.");
        }
    }


    public void updateWeight(int toyId, double newWeight) {
        for (Toy toy : toys) {
            if (toy.getId() == toyId) {
                toy.setWeight(newWeight);
                break;
            }
        }
    }

    public void updateQuantity(int toyId, int newQuantity) {
        Toy toyToUpdate = null;

        for (Toy toy : toys) {
            if (toy.getId() == toyId) {
                toyToUpdate = toy;
                break;
            }
        }

        if (toyToUpdate != null) {

            toyToUpdate.setQuantity(newQuantity);
            if (toyToUpdate.getQuantity()>0) {
                redistributeWeights();
            }
        } else {
            System.out.println("Toy with ID " + toyId + " not found.");
        }
    }

    public Toy drawToy() {
        double totalWeight = toys.stream().mapToDouble(Toy::getWeight).sum();
        double randomValue = new Random().nextDouble() * totalWeight;

        double cumulativeWeight = 0;
        for (Toy toy : toys) {
            cumulativeWeight += toy.getWeight();
            if (randomValue <= cumulativeWeight) {
                return toy;
            }
        }
        return null;
    }

    public void awardToy(Toy toy) {
        if (toys.contains(toy) && toy.getQuantity() > 0) {
            toy.setQuantity(toy.getQuantity() - 1);
            fileWriterUtil.appendToFile("awarded_toys.txt", toy.getName());
            if  (toy.getQuantity() == 0){
                toys.remove(toy);
            }

        } else {
            System.out.println("Invalid toy or out of stock.");
        }
    }
    public List<Toy>  getToys(){
        return new ArrayList<>(toys);
    }
    public void displayToysInfo() {

        System.out.println("===== Toys Information =====");
        for (Toy toy : toys) {
            System.out.println("Toy: " + toy.getName() +
                    ", Quantity: " + toy.getQuantity() +
                    ", Weight: " + toy.getWeight());
        }

    }
    public void removeToy(int toyId) {
        Toy toyToRemove = null;

        for (Toy toy : toys) {
            if (toy.getId() == toyId) {
                toyToRemove = toy;
                break;
            }
        }

        if (toyToRemove != null) {
            toys.remove(toyToRemove);
            System.out.println("Toy removed successfully.");
        } else {
            System.out.println("Toy with ID " + toyId + " not found.");
        }
    }
    public void saveToysToFile(String fileName) {
        try (FileWriter writer = new FileWriter(fileName)) {
            for (Toy toy : toys) {
                writer.write(toy.getId() + "," + toy.getName() + "," + toy.getQuantity() + "," + toy.getWeight() + "\n");
            }
            System.out.println("Toys saved to file: " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error saving toys to file.");
        }
    }
    public void loadToysFromFile(String fileName) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            List<Toy> loadedToys = new ArrayList<>();
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    int id = Integer.parseInt(parts[0]);
                    String name = parts[1];
                    int quantity = Integer.parseInt(parts[2]);
                    double weight = Double.parseDouble(parts[3]);

                    Toy toy = new Toy(id, name, quantity, weight);
                    loadedToys.add(toy);
                }
            }
            toys.clear();
            toys.addAll(loadedToys);
            System.out.println("Toys loaded from file: " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error loading toys from file.");
        }

    }
    public void clearToys() {
        toys.clear();
    }
}
