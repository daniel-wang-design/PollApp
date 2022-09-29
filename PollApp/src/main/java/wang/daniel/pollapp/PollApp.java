package wang.daniel.pollapp;

import com.slack.api.bolt.App;
import com.slack.api.bolt.jetty.SlackAppServer;

/**
 *
 * @author wangd project started September 12, 2022
 */
public class PollApp {

    public static void main(String[] args) throws Exception {
        App app = new App();
        app.command("/test-poll", (req, ctx) -> {
            String userInput = req.getPayload().getText();
            ctx.respond("You said: " + userInput); // perform an HTTP request
            return ctx.ack(); // respond with 200 OK
        });

        SlackAppServer server = new SlackAppServer(app);
        server.start();
    }
}
