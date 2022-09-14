package wang.daniel.pollapp;

import com.slack.api.bolt.App;
import com.slack.api.bolt.jetty.SlackAppServer;

/**
 *
 * @author wangd
 * project started September 12, 2022
 */
public class PollApp {

    public static void main(String[] args) throws Exception {
        App app = new App();
        app.command("/hello", (req, ctx) -> {
            return ctx.ack(":wave: Hello!");
        });

        SlackAppServer server = new SlackAppServer(app);
        server.start();
    }
}
