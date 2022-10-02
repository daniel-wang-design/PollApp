package wang.daniel.pollapp;

import com.slack.api.bolt.App;
import com.slack.api.bolt.jetty.SlackAppServer;
import static com.slack.api.model.block.Blocks.*;
import static com.slack.api.model.block.composition.BlockCompositions.*;
import static com.slack.api.model.block.element.BlockElements.*;

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

            ctx.respond(res -> res
                    .responseType("in_channel")
                    .blocks(asBlocks(section(section -> section.text(markdownText("Select a date:"))),
                            divider(),
                            actions(actions -> actions
                            .elements(asElements(
                                    button(b -> b.text(plainText(pt -> pt.emoji(true).text("Monday"))).value("day1")),
                                    button(b -> b.text(plainText(pt -> pt.emoji(true).text("Tuesday"))).value("day2"))
                            ))
                            ))));
            return ctx.ack(); // respond with 200 OK
        });

        SlackAppServer server = new SlackAppServer(app);
        server.start();
    }
}
