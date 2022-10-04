package wang.daniel.pollapp;

import com.slack.api.bolt.App;
import com.slack.api.bolt.jetty.SlackAppServer;
import static com.slack.api.model.block.Blocks.*;
import com.slack.api.model.block.LayoutBlock;
import static com.slack.api.model.block.composition.BlockCompositions.*;
import com.slack.api.model.block.element.BlockElement;
import com.slack.api.model.block.element.BlockElements;
import static com.slack.api.model.block.element.BlockElements.*;
import com.slack.api.model.block.element.ButtonElement;
import java.util.*;

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
            String[][] buttons = new String[5][2];
            buttons[0] = new String[]{"Monday", "1"};
            buttons[1] = new String[]{"Tuesday", "2"};
            buttons[2] = new String[]{"Wednesday", "3"};
            buttons[3] = new String[]{"Thursday", "4"};
            buttons[4] = new String[]{"Friday", "5"};
            ctx.respond(res -> res
                    .responseType("in_channel")
                    .blocks(asBlocks(section(section -> section.text(markdownText("Select a date:"))),
                            divider(),
                            actions(actions -> actions
                            .elements(generateButtons(buttons))
                            )))
            );
            return ctx.ack(); // respond with 200 OK
        });
        app.blockAction("buttonAction", (req, ctx) -> { // responding to this action_id
            String value = req.getPayload().getActions().get(0).getValue();
            if (req.getPayload().getResponseUrl() != null) {
                ctx.respond(res -> res
                        .responseType("in_channel")
                        .text("You sent the value of the button=" + value));
            }
            return ctx.ack();
        });

        SlackAppServer server = new SlackAppServer(app);
        server.start();
    }

    public static String[][] processedInput(String in) {
        return null;
    }

    /**
     * Takes a String[][] of buttons and creates a List of BlockElements
     *
     * @param buttons is a 2 by x list of String values, where the first value
     * refers to the text displayed, and the second refers to the value when
     * clicked
     * @return List<BlockElement> a list of buttons
     */
    public static List<BlockElement> generateButtons(String[][] buttons) {
        List<BlockElement> list = new ArrayList<>();
        for (String[] button : buttons) {
            list.add(button(b -> b.text(plainText(pt -> pt.emoji(true).text(button[0]))).actionId(button[0]+"buttonAction").value(button[1])));
        }
        return list;
    }
}
