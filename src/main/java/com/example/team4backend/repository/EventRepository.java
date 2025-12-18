package com.example.team4backend.repository;

import com.example.team4backend.domain.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByTargetPersonId(Long targetPersonId);
    void deleteByTargetPersonId(Long targetPersonId);
}
