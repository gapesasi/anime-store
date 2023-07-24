package anime.store.test;

import anime.store.service.AnimeService;
import anime.store.service.ProducerService;

import java.util.Scanner;


public class CrudTest {
    private static final Scanner SCANNER = new Scanner(System.in);

    public static void main(String[] args) {
        int op;
        while (true) {
            menu();
            op = Integer.parseInt(SCANNER.nextLine());

            if (op == 0) break;
            switch (op) {
                case 1 -> {
                    int selectedOption;
                    while (true) {
                        producerMenu();
                        selectedOption = Integer.parseInt(SCANNER.nextLine());
                        if (selectedOption == 9) break;

                        ProducerService.menu(selectedOption);
                    }
                }
                case 2 -> {
                    int selectedOption;
                    while (true) {
                        animeMenu();
                        selectedOption = Integer.parseInt(SCANNER.nextLine());
                        if (selectedOption == 9) break;

                        AnimeService.menu(selectedOption);
                    }
                }
                default -> throw new IllegalArgumentException("Not a valid option");
            }
        }
    }

    private static void menu() {
        System.out.println("Type the number of your operation");
        System.out.println("1. Producer");
        System.out.println("2. Anime");
        System.out.println("0. Exit");
    }

    private static void producerMenu() {
        System.out.println("Type the number of your operation");
        System.out.println("1. Search for producer");
        System.out.println("2. Delete producer");
        System.out.println("3. Create new producer");
        System.out.println("4. Update a producer");
        System.out.println("9. Go back");
    }

    private static void animeMenu() {
        System.out.println("Type the number of your operation");
        System.out.println("1. Search for anime");
        System.out.println("2. Delete anime");
        System.out.println("3. Create new anime");
        System.out.println("4. Update a anime");
        System.out.println("9. Go back");
    }
}
