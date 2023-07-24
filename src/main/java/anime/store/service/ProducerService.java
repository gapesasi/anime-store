package anime.store.service;

import anime.store.domain.Producer;
import anime.store.repository.ProducerRepository;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class ProducerService {
    private static final Scanner SCANNER = new Scanner(System.in);

    public static void menu(int op) {
        //Enhanced switch, introduzido na versão 12
//        int i = switch (op){
//            case 1, 2, 3, 4, 5, 6: yield 100;
//            default: yield 0;
//        };

        switch (op) {
            case 1 -> findByName();
            case 2 -> delete();
            case 3 -> save();
            case 4 -> update();
            default -> throw new IllegalArgumentException("Not a valid option");
        }
    }

    public static void printProducers(List<Producer> producers) {
        producers.forEach(p -> System.out.printf("[ID: %d] - %s%n", p.getId(), p.getName()));
    }

    private static void findByName() {
        System.out.println("Type the name or empty to all");
        String name = SCANNER.nextLine();
        printProducers(ProducerRepository.findByName(name));
    }

    private static void delete() {
        System.out.println("Type one of the ids below to delete");
        printProducers(ProducerRepository.findByName(""));

        int id = Integer.parseInt(SCANNER.nextLine());
        System.out.println("Are you sure? Y/N");
        String choice = SCANNER.nextLine();

        if (choice.toUpperCase().startsWith("Y")) {
            ProducerRepository.delete(id);
        }
    }

    private static void save() {
        System.out.println("Type the name of the new producer:");
        String name = SCANNER.nextLine();
        Producer producer = Producer.builder().name(name).build();
        ProducerRepository.save(producer);
    }

    private static void update() {
        System.out.println("Choose one of the ids below to update:");
        List<Producer> producers = ProducerRepository.findByName("");
        printProducers(producers);
        int id = Integer.parseInt(SCANNER.nextLine());

        Optional<Producer> producerOptional = ProducerRepository.findById(id);
        if (producerOptional.isEmpty()) {
            System.out.println("Producer not found");
            return;
        }

        System.out.println("Type the new name or enter to keep the same:");
        String name = SCANNER.nextLine();
        Producer producer = Producer.builder().id(id).name(name).build();

        ProducerRepository.update(producer);
    }
}
