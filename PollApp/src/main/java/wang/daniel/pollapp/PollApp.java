package wang.daniel.pollapp;

import com.slack.api.bolt.App;
import com.slack.api.bolt.jetty.SlackAppServer;
import com.slack.api.webhook.WebhookResponse;

// ngrok http 3000
/**
 *
 * @author wangd project started September 12, 2022
 */
public class PollApp {

    public static void main(String[] args) throws Exception {
        App app = new App();
        app.command("/test-poll", (req, ctx) -> {
            String userInput = req.getPayload().getText();
            String channelId = req.getPayload().getChannelId();
            String channelName = req.getPayload().getChannelName();

            System.out.println("Userinput=" + userInput);
            System.out.println("channelId=" + channelId);
            System.out.println("channelName=" + channelName);
            ctx.respond(res -> res
                    .responseType("in_channel") // respond message visible to everyone in channel
                    .text("You just said: " + userInput));
            return ctx.ack(); // respond with 200 OK
        });

        SlackAppServer server = new SlackAppServer(app);
        server.start();
    }
}
