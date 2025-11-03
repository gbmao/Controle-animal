package com.projeto.controleanimal.database;

import com.projeto.controleanimal.model.Animal;
import com.projeto.controleanimal.model.Cat;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Db {

    private static final String FILE_PATH = "C:\\Users\\debor\\Controle-animal\\controle-animalAPI\\controle-animal\\src\\main\\java\\com\\projeto\\controleanimal\\database\\output.txt";
   // private static final String FILE_PATH = "src/main/java/com/projeto/controleanimal/database/output.txt";

    public static void saveList(Map<String, Animal> list) {
        try {
            FileWriter writer = new FileWriter(FILE_PATH);

            list.forEach((k,v) -> {
                try {
                    writer.write(v.getName() + "," + v.getAge() + "\n");
                } catch (IOException e) {
                    throw new RuntimeException("Erro ao escrever animal: " + v.getName(), e);
                }
            });
            writer.close();
        } catch (IOException e) {
            System.err.println("Error : " + e.getMessage());
        }
    }

    public static Map<String, Animal> loadList() {
        Map<String, Animal> list = new HashMap<>();

        try (Scanner scanner = new Scanner(new File(FILE_PATH))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();

                // Divide a linha em partes: nome, idade
                String[] parts = line.split(",");

                if (parts.length == 2) {
                    String name = parts[0].trim();
                    int age = Integer.parseInt(parts[1].trim());
                    list.put(name, new Cat(name, age));
                }
            }
            System.out.println("Arquivo carregado com sucesso!");
        } catch (FileNotFoundException e) {
            System.err.println("Arquivo não encontrado: " + FILE_PATH);
        }

        return list;
    }
}
