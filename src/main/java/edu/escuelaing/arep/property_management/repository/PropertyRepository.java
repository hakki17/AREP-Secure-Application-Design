package edu.escuelaing.arep.property_management.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import edu.escuelaing.arep.property_management.model.Property;

import java.util.List;


@Repository
public interface PropertyRepository extends JpaRepository<Property, Long>{

    List<Property> findByAddressContainingIgnoreCase(String address);
    List<Property> findByPriceBetween(Double minPrice, Double maxPrice);
    List<Property> findBySizeGreaterThanEqual(Double minSize);
}
