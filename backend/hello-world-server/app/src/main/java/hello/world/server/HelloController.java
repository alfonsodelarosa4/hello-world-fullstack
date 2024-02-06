package hello.world.server;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RestController
@CrossOrigin(origins="*")
public class HelloController {
    @GetMapping("/")
    public ResponseEntity<String> sayHelloWorld() {
        String message = "Hello world";
        return ResponseEntity.ok(message);
    }

    @PostMapping("/sayHello")
    public ResponseEntity<Map<String, String>> sayHello(@RequestBody Map<String, String> request) {
        System.out.println("Request received");
        String name = request.get("name");

        if (name == null || name.trim().isEmpty()) {
            // Return 400 Bad Request response if 'name' is missing or empty
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Name is required");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

        Map<String, String> response = new HashMap<>();
        response.put("message", "Hello " + name);
        return ResponseEntity.ok(response);
    }

}