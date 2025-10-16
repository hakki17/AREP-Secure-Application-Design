package edu.escuelaing.arep.property_management.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import edu.escuelaing.arep.property_management.model.Property;
import edu.escuelaing.arep.property_management.repository.PropertyRepository;

import java.util.List;

@RestController
@RequestMapping("/api/properties")
@CrossOrigin(origins = "*")
public class PropertyController {

    @Autowired
    private PropertyRepository propertyRepository;

    @PostMapping
    public Property createProperty(@RequestBody Property property) {
        return propertyRepository.save(property);
    }

    @GetMapping
    public List<Property> getAllProperties() {
        return propertyRepository.findAll();
    }

    @GetMapping("/{id}")
    public Property getProperty(@PathVariable Long id) {
        return propertyRepository.findById(id).orElse(null);
    }

    @PutMapping("/{id}")
    public Property updateProperty(@PathVariable Long id, @RequestBody Property newProperty) {
        Property property = propertyRepository.findById(id).orElse(null);
        if (property != null) {
            property.setAddress(newProperty.getAddress());
            property.setPrice(newProperty.getPrice());
            property.setSize(newProperty.getSize());
            property.setDescription(newProperty.getDescription());
            return propertyRepository.save(property);
        }
        return null;
    }

    @DeleteMapping("/{id}")
    public void deleteProperty(@PathVariable Long id) {
        propertyRepository.deleteById(id);
    }
}