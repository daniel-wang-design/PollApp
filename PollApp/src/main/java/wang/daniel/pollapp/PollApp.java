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
import java.util.regex.Pattern;
import java.util.regex.Matcher;

// ngrok http 3000
// https://vercel.com/pricing
/**
 *
 * @author wangd project started September 12, 2022
 */
public class PollApp {

    private static String[][] buttons1;

    public static void main(String[] args) throws Exception {
        App app = new App();
        app.command("/test-poll", (req, ctx) -> {
            String userInput = req.getPayload().getText();
            try {
                final String[][] buttons = processedInput(userInput);
                buttons1 = buttons;
                ctx.respond(res -> res
                        .responseType("in_channel")
                        .blocks(asBlocks(section(section
                                -> section.text(markdownText("Select a day:"))),
                                divider(),
                                actions(actions -> actions
                                .elements(generateButtons(buttons))
                                )))
                );
            } catch (Exception e) {
                ctx.respond(e.getMessage());
            }
            return ctx.ack(); // respond with 200 OK
        });

        Pattern buttonPattern = Pattern.compile("buttonAction");
        app.blockAction(buttonPattern, (req, ctx) -> { // responding to this action_id
            String value = req.getPayload().getActions().get(0).getValue();
            if (req.getPayload().getResponseUrl() != null) {
                for (String[] str : buttons1) {
                  //  if (("buttonAction"+str[1]).equals(value))
                }
            }
            return ctx.ack();
        });

        SlackAppServer server = new SlackAppServer(app);
        server.start();
    }

    // processes the user input to give a String[][] of options that can be made into buttons
    public static String[][] processedInput(String in) throws Exception {
        // takes input in the form /test-poll "Title" "Option 1" "Option 2" ...
        String[] items = in.split(" ");
        String[][] input = new String[items.length - 1][2];
        for (int i = 0; i < items.length - 1; i++) {
            input[i] = new String[]{items[i], Integer.toString(i)};
        }
        return input;
    }

    // takes String[][] and creates a list of buttons
    public static List<BlockElement> generateButtons(String[][] buttons) {
        List<BlockElement> list = new ArrayList<>();
        for (String[] button : buttons) {
            list.add(button(b
                    -> b.text(plainText(pt -> pt
                    .emoji(true)
                    .text(button[0])))
                            .actionId("buttonAction" + button[0])
                            .value(button[1])));
        }
        return list;
    }
}
