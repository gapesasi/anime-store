package anime.store.service;

import anime.store.domain.Anime;
import anime.store.domain.Producer;
import anime.store.repository.AnimeRepository;
import anime.store.repository.ProducerRepository;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class AnimeService {
    private static final Scanner SCANNER = new Scanner(System.in);

    public static void menu(int op) {
        switch (op) {
            case 1 -> findByName();
            case 2 -> delete();
            case 3 -> save();
            case 4 -> update();
            default -> throw new IllegalArgumentException("Not a valid option");
        }
    }

    public static void printAnimes(List<Anime> animes) {
        animes.forEach(p -> System.out.printf("[ID: %d] - %s - %d eps - %s%n",
                p.getId(), p.getName(), p.getEpisodes(), p.getProducer().getName()));
    }

    private static void findByName() {
        System.out.println("Type the name or empty to all");
        String name = SCANNER.nextLine();
        printAnimes(AnimeRepository.findByName(name));
    }

    private static void delete() {
        System.out.println("Type one of the ids below to delete");
        printAnimes(AnimeRepository.findByName(""));

        int id = Integer.parseInt(SCANNER.nextLine());
        System.out.println("Are you sure? Y/N");
        String choice = SCANNER.nextLine();

        if (choice.toUpperCase().startsWith("Y")) {
            AnimeRepository.delete(id);
        }
    }

    private static void save() {
        System.out.println("Type the name of the new anime:");
        String name = SCANNER.nextLine();

        System.out.println("Type the number of episodes:");
        int eps = Integer.parseInt(SCANNER.nextLine());

        System.out.println("Choose a producer from the list:");
        List<Producer> producers = ProducerRepository.findByName("");
        ProducerService.printProducers(producers);
        int producerId = Integer.parseInt(SCANNER.nextLine());
        Optional<Producer> producerOptional = ProducerRepository.findById(producerId);
        if (producerOptional.isEmpty()) {
            System.out.println("Producer not found");
            return;
        }
        Producer selectedProducer = producerOptional.get();

        Anime anime = Anime.builder()
                .name(name)
                .episodes(eps)
                .producer(selectedProducer)
                .build();
        AnimeRepository.save(anime);
    }

    private static void update() {
        System.out.println("Choose one of the ids below to update:");
        List<Anime> animes = AnimeRepository.findByName("");
        printAnimes(animes);
        int id = Integer.parseInt(SCANNER.nextLine());

        Optional<Anime> animeOptional = AnimeRepository.findById(id);
        if (animeOptional.isEmpty()) {
            System.out.println("Anime not found");
            return;
        }
        Anime animeToUpdate = animeOptional.get();

        System.out.println("Type the name or enter to keep the same:");
        String name = SCANNER.nextLine();
        name = name.isEmpty() ? animeToUpdate.getName() : name;

        System.out.println("Type the number of episodes or enter to keep the same:");
        String epsString = SCANNER.nextLine();
        int eps = epsString.isEmpty() ? animeToUpdate.getEpisodes() : Integer.parseInt(epsString);

        System.out.println("Choose a producer from the list or enter to keep the same:");
        List<Producer> producers = ProducerRepository.findByName("");
        ProducerService.printProducers(producers);

        String producerIdString = SCANNER.nextLine();
        int producerId = producerIdString.isEmpty()
                ? animeToUpdate.getProducer().getId()
                : Integer.parseInt(producerIdString);

        Optional<Producer> producerOptional = ProducerRepository.findById(producerId);
        if (producerOptional.isEmpty()) {
            System.out.println("Producer not found");
            return;
        }
        Producer selectedProducer = producerOptional.get();

        Anime anime = Anime.builder()
                .id(id)
                .name(name)
                .episodes(eps)
                .producer(selectedProducer)
                .build();

        AnimeRepository.update(anime);
    }
}
