package it.pajak.hearthpulse.hsreplay.mongodb;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
public class Replay {
    private String id;
    private String player1_archetype;
    private String player1_legend_rank;
    private String player1_rank;
    private String player1_won;
    private String player2_archetype;
    private String player2_legend_rank;
    private String player2_rank;
    private String player2_won;
    private String gameDate;
}
