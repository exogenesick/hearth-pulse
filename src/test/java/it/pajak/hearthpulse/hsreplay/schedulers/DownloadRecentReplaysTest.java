package it.pajak.hearthpulse.hsreplay.schedulers;

import org.junit.Test;

import java.io.IOException;

public class DownloadRecentReplaysTest {
    @Test
    public void name() throws IOException {
        DownloadRecentReplays downloadRecentReplays = new DownloadRecentReplays();
        downloadRecentReplays.download();
    }
}