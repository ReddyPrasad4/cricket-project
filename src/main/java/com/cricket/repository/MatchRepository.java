package com.cricket.repository;

import com.cricket.entity.Matches;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MatchRepository extends JpaRepository<Matches, UUID> {


    List<Matches> findBySeriesId(UUID seriesId);
    @Query(value = "select * from matches order by start_date",nativeQuery = true)
    List<Matches> findAllMatchesByOrder();
}
