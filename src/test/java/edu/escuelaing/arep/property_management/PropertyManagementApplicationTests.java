package edu.escuelaing.arep.property_management;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import edu.escuelaing.arep.property_management.model.Property;
import edu.escuelaing.arep.property_management.repository.PropertyRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PropertyManagementApplicationTests {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private PropertyRepository propertyRepository;

    private String baseUrl;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port + "/api/properties";
        propertyRepository.deleteAll();
    }

    @Test
    void contextLoads() {
        assertNotNull(restTemplate);
        assertNotNull(propertyRepository);
    }

    @Test
    void testCreateProperty() {
        Property property = new Property("123 Main Street", 250000.0, 150.5, "Beautiful house");
        
        ResponseEntity<Property> response = restTemplate.postForEntity(baseUrl, property, Property.class);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody().getId());
        assertEquals("123 Main Street", response.getBody().getAddress());
    }

    @Test
    void testGetAllProperties() {
        propertyRepository.save(new Property("Address 1", 100000.0, 100.0, "Description 1"));
        propertyRepository.save(new Property("Address 2", 200000.0, 200.0, "Description 2"));
        
        ResponseEntity<Property[]> response = restTemplate.getForEntity(baseUrl, Property[].class);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().length);
    }

    @Test
    void testGetPropertyById() {
        Property saved = propertyRepository.save(new Property("Test Address", 150000.0, 120.0, "Test"));
        
        ResponseEntity<Property> response = restTemplate.getForEntity(baseUrl + "/" + saved.getId(), Property.class);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(saved.getId(), response.getBody().getId());
    }

    @Test
    void testUpdateProperty() {
        Property original = propertyRepository.save(new Property("Original", 100000.0, 100.0, "Original"));
        Property updated = new Property("Updated", 200000.0, 150.0, "Updated");
        
        HttpEntity<Property> request = new HttpEntity<>(updated);
        ResponseEntity<Property> response = restTemplate.exchange(
            baseUrl + "/" + original.getId(),
            HttpMethod.PUT,
            request,
            Property.class
        );
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Updated", response.getBody().getAddress());
    }

    @Test
    void testDeleteProperty() {
        Property property = propertyRepository.save(new Property("Delete", 100000.0, 100.0, "Delete"));
        Long id = property.getId();
        
        restTemplate.delete(baseUrl + "/" + id);
        
        assertFalse(propertyRepository.existsById(id));
    }
}