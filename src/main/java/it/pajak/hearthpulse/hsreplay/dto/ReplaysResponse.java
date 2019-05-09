package it.pajak.hearthpulse.hsreplay.dto;

import it.pajak.hearthpulse.hsreplay.mongodb.Replay;
import lombok.Data;

import java.util.List;

@Data
public class ReplaysResponse {
    private List<Replay> data;
}
