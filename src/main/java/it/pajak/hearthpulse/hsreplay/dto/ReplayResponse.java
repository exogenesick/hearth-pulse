package it.pajak.hearthpulse.hsreplay.dto;

import lombok.Data;

@Data
public class ReplayResponse {
    private Game global_game;
    private Player friendly_player;
    private Player opposing_player;
}
