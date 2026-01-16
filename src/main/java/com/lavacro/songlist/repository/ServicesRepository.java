package com.lavacro.songlist.repository;

import com.lavacro.songlist.model.ServiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServicesRepository extends JpaRepository<ServiceEntity, Integer> { }
