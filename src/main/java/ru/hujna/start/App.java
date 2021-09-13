package ru.hujna.start;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"ru.hujna"})
public class App {

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

}
