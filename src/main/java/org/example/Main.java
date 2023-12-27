package org.example;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        FileWriterUtilInterface fileWriterUtil = new FileWriterUtil();
        ToyStore toyStore = new ToyStore(fileWriterUtil);
        toyStore.loadToysFromFile("toys.txt");




        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            toyStore.saveToysToFile("toys.txt");
            System.out.println("Program exited. Toys saved to file.");
        }));

        Scanner scanner = new Scanner(System.in);



        while (true) {
            System.out.println("===== Toy Store Menu =====");
            System.out.println("1. Add Toy");
            System.out.println("2. Update Toy Weight");
            System.out.println("3. Update Toy Quantity");
            System.out.println("4. Draw Toys");
            System.out.println("5. Display Remaining Toys");
            System.out.println("6. Redistribute Weights");
            System.out.println("7. Display Toys Info");
            System.out.println("8. Remove Toy");
            System.out.println("9. Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.println("Enter toy ID: ");
                    int toyId = scanner.nextInt();

                    System.out.println("Enter toy name: ");
                    String toyName = scanner.next();

                    System.out.println("Enter toy quantity: ");
                    int toyQuantity = scanner.nextInt();

                    System.out.println("Enter toy weight: ");
                    double toyWeight = scanner.nextDouble();

                    toyStore.addToy(toyId, toyName, toyQuantity, toyWeight);
                    break;

                case 2:
                    System.out.println("Enter toy ID to update weight: ");
                    int toyIdToUpdateWeight = scanner.nextInt();

                    System.out.println("Enter new weight for the toy: ");
                    double newWeight = scanner.nextDouble();
                    toyStore.updateWeight(toyIdToUpdateWeight, newWeight);
                    break;

                case 3:
                    System.out.println("Enter toy ID to update quantity: ");
                    int toyIdToUpdateQuantity = scanner.nextInt();

                    System.out.println("Enter new quantity for the toy: ");
                    int newQuantity = scanner.nextInt();
                    toyStore.updateQuantity(toyIdToUpdateQuantity, newQuantity);
                    break;

                case 4:
                    drawToys(scanner, toyStore);
                    break;

                case 5:
                    displayRemainingToys(toyStore);
                    break;

                case 6:
                    toyStore.redistributeWeights();
                    break;

                case 7:
                    toyStore.displayToysInfo();
                    break;

                case 8:
                    removeToy(scanner, toyStore);
                    break;

                case 9:
                    System.exit(0);



                default:
                    System.out.println("Invalid choice. Please enter a valid option.");
                    break;
            }
        }
    }

    private static void drawToys(Scanner scanner, ToyStore toyStore) {
        System.out.print("Enter the number of toys to draw: ");
        int numberOfToysToDraw = scanner.nextInt();

        for (int i = 0; i < numberOfToysToDraw; i++) {
            Toy drawnToy = toyStore.drawToy();
            if (drawnToy != null) {
                System.out.println("Drawn toy: " + drawnToy.getName());
                toyStore.awardToy(drawnToy);
            } else {
                System.out.println("No toys available for drawing.");
            }

        }
    }
    private static void displayRemainingToys(ToyStore toyStore) {
        List<Toy> remainingToys = toyStore.getToys();

        if (remainingToys.isEmpty()) {
            System.out.println("No toys remaining.");
        } else {
            System.out.println("===== Remaining Toys =====");
            for (Toy toy : remainingToys) {
                System.out.println("Toy: " + toy.getName() +
                        ", Quantity: " + toy.getQuantity() +
                        ", Weight: " + toy.getWeight());
            }
        }
    }

    private static void removeToy(Scanner scanner, ToyStore toyStore) {
        System.out.print("Enter toy ID to remove: ");
        int toyIdToRemove = scanner.nextInt();
        toyStore.removeToy(toyIdToRemove);
    }


}


