package it.pajak.hearthpulse.hsreplay.schedulers;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import it.pajak.hearthpulse.hsreplay.dto.ReplaysResponse;
import it.pajak.hearthpulse.hsreplay.mongodb.Replay;
import it.pajak.hearthpulse.hsreplay.mongodb.ReplaysRepository;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class DownloadRecentReplays {

    @Autowired
    private ReplaysRepository replaysRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Scheduled(cron = "${hsreplay.replays.download.scheduler}")
    public List<Replay> download() throws IOException {
        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<ReplaysResponse> jsonAdapter = moshi.adapter(ReplaysResponse.class);

        ReplaysResponse response = jsonAdapter.fromJson(downloadRecentReplays());

        response.getData().stream().forEach(r -> {
            try {
                Replay newReplay = replaysRepository.insert(r);
                rabbitTemplate.convertAndSend("replays", "replay.consume", newReplay);

            } catch (Exception e) {
            }
        });

        return response.getData();
    }

    private String downloadRecentReplays() throws IOException {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://hsreplay.net/api/v1/live/replay_feed")
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }
}
