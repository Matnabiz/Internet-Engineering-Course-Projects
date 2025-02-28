package org.example;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {

        Library library = new Library();
        JsonToData jsonProcessor = new JsonToData(library);
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter commands or type 'exit' to quit:");

        while (true) {
            System.out.print("> ");
            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("exit")) {
                System.out.println("Exiting...");
                Thread.sleep(1000);
                break;
            }

            if (!input.isEmpty()) {
                jsonProcessor.processInput(input);
            }
        }

        scanner.close();
    }
}