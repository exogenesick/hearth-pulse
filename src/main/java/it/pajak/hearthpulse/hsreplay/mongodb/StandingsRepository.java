package it.pajak.hearthpulse.hsreplay.mongodb;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StandingsRepository extends MongoRepository<RankingStanding, String> {
}
