package com.cricket.repository;

import com.cricket.entity.Series;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SeriesRepository extends JpaRepository<Series, UUID> {

    @Query(value = "select * from series order by start_date",nativeQuery = true)
    List<Series> findAllSeriesByOrder();

}
