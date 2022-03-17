package com.everis.appf1.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.everis.appf1.Entity.DriverEntity;

@Repository
public interface DriverRepository extends MongoRepository<DriverEntity, Long> {
	
}