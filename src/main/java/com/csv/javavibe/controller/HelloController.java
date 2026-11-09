package com.csv.javavibe.controller;

import com.csv.javavibe.service.HelloService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for Hello Service operations.
 * Provides endpoints for greeting and Kafka message sending.
 */
@RestController
@RequestMapping("/api/hello")
@Tag(name = "Hello API", description = "Endpoints for greeting and Kafka messaging")
public class HelloController {

    private final HelloService helloService;

    public HelloController(HelloService helloService) {
        this.helloService = helloService;
    }

    @GetMapping("/greeting")
    @Operation(summary = "Get greeting message", description = "Returns a simple greeting message from the service")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved greeting",
            content = @Content(mediaType = "text/plain", schema = @Schema(type = "string", example = "Hello World from Service!"))),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<String> getGreeting() {
        return ResponseEntity.ok(helloService.getGreeting());
    }

    @PostMapping("/send")
    @Operation(summary = "Send message to Kafka", description = "Sends a text message to the configured Kafka topic")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Message sent successfully",
            content = @Content(mediaType = "text/plain", schema = @Schema(type = "string", example = "Message sent to Kafka: Test Message"))),
        @ApiResponse(responseCode = "400", description = "Invalid request"),
        @ApiResponse(responseCode = "500", description = "Failed to send message to Kafka")
    })
    public ResponseEntity<String> sendMessageToKafka(
            @Parameter(description = "Message to send to Kafka", required = true, example = "Test Message")
            @RequestBody String message) {
        helloService.sendMessageToKafka(message);
        return ResponseEntity.ok("Message sent to Kafka: " + message);
    }
}