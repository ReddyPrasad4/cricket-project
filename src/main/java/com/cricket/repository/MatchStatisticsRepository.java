package com.cricket.repository;

import com.cricket.entity.MatchStatistics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MatchStatisticsRepository extends JpaRepository<MatchStatistics, UUID> {

    List<MatchStatistics> findByPlayerId(UUID playerId);


    MatchStatistics findByPlayerIdAndMatchesId(UUID playerId, UUID matchId);

    List<MatchStatistics> findByMatchesId(UUID matchId);
}
