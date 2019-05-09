package it.pajak.hearthpulse.hsreplay.mongodb;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Builder
@Data
@Document
public class RankingStanding {
    @Id
    private String id;
    private String playerName;
    private Integer rank;
    private Boolean isLegend;
    private String updatedDate;
    private Integer season;
}
