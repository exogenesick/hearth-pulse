package it.pajak.hearthpulse.hsreplay.consumers;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import it.pajak.hearthpulse.hsreplay.dto.Game;
import it.pajak.hearthpulse.hsreplay.dto.Player;
import it.pajak.hearthpulse.hsreplay.dto.ReplayResponse;
import it.pajak.hearthpulse.hsreplay.mongodb.RankingStanding;
import it.pajak.hearthpulse.hsreplay.mongodb.StandingsRepository;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ReplayConsumer {

    @Autowired
    private StandingsRepository standingsRepository;

    public void receiveMessage(String replayId) throws IOException {
        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<ReplayResponse> jsonAdapter = moshi.adapter(ReplayResponse.class);

        ReplayResponse replay = jsonAdapter.fromJson(downloadGivenReplay(replayId));

        updatePlayerStanding(replay.getFriendly_player(), replay.getGlobal_game());
        updatePlayerStanding(replay.getOpposing_player(), replay.getGlobal_game());
    }

    private String downloadGivenReplay(String replayId) throws IOException {
        OkHttpClient client = new OkHttpClient();

        String replayUrl = String.format("https://hsreplay.net/api/v1/games/%", replayId);

        Request request = new Request.Builder()
                .url(replayUrl)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

    private void updatePlayerStanding(Player player, Game game) {
//        RankingStanding foundPlayerRankingStanding = standingsRepository.findOneByName(player.getName());
        RankingStanding foundPlayerRankingStanding = null;

        if (foundPlayerRankingStanding == null) {
            Boolean isLegend = player.getLegend_rank() != null ? Boolean.TRUE : Boolean.FALSE;

            RankingStanding newRankingStanding = RankingStanding.builder()
                    .isLegend(isLegend)
                    .playerName(player.getName())
                    .rank(player.getRank())
                    .season(game.getLadder_season())
                    .updatedDate(game.getMatch_end())
                    .build();

            standingsRepository.insert(newRankingStanding);
            return;
        }

        foundPlayerRankingStanding.setRank(player.getRank());
        foundPlayerRankingStanding.setSeason(game.getLadder_season());
        foundPlayerRankingStanding.setUpdatedDate(game.getMatch_end());

        standingsRepository.save(foundPlayerRankingStanding);
    }
}