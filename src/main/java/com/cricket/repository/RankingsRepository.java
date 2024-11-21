package com.cricket.repository;

import com.cricket.entity.Player;
import com.cricket.entity.Rankings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RankingsRepository extends JpaRepository<Rankings, UUID> {

    Player findByPlayerId(UUID playerId);

    @Query(value = "select * from rankings order by ranking" ,nativeQuery = true)
    List<Rankings> getAllRankingsInOrder();
}
