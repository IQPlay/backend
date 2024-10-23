package fr.parisNanterre.FideliTech.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@RestController
@CrossOrigin(origins = "http://localhost:3000") // Allow requests from the React app
public class HelloController {

    @GetMapping("/hello")
    public String getHello() {
        return "Hello, world!";
    }

    @GetMapping("/data")
    public ResponseEntity<Map<String, Integer>> getData() {
        Random random = new Random();
        int n1 = random.nextInt(10);
        int n2 = random.nextInt(10);

        Map<String, Integer> numbers = new HashMap<>();
        numbers.put("number1", n1);
        numbers.put("number2", n2);

        return ResponseEntity.ok(numbers);
    }
}
