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
// https://vercel.com/pricing
// https://signup.heroku.com/ 
/**
 *
 * @author wangd project started September 12, 2022
 */
public class PollApp {

    private static String[][] buttons1;
    private static int[] buttons1Count = new int[7];
    private static ArrayList[] buttons1Check = new ArrayList[7];

    public static void main(String[] args) throws Exception {
        App app = new App();
        for (int i = 0; i < 7; i++) { // initialize user id check
            buttons1Check[i] = new ArrayList<String>();
        }
        app.command("/date-poll", (req, ctx) -> {
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

        app.blockAction("datePicker0", (req, ctx) -> { // responding to first option
            String value = req.getPayload().getUser().getId();
            if (!buttons1Check[0].contains(value)) {
                buttons1Count[0]++;
                buttons1Check[0].add(value);
                ctx.respond("Your selection of: " + buttons1[0][0] + " , was recorded!");
                System.out.println(Arrays.toString(buttons1Count));
            } else {
                buttons1Count[0]--;
                buttons1Check[0].remove(value);
                ctx.respond("Your selection of: " + buttons1[0][0] + " , was removed!");
                System.out.println(Arrays.toString(buttons1Count));
            }
            return ctx.ack();
        });
        app.blockAction("datePicker1", (req, ctx) -> { // responding to first option
            String value = req.getPayload().getUser().getId();
            if (!buttons1Check[1].contains(value)) {
                buttons1Count[1]++;
                buttons1Check[1].add(value);
                ctx.respond("Your selection of: " + buttons1[1][0] + " , was recorded!");
                System.out.println(Arrays.toString(buttons1Count));
            } else {
                buttons1Count[1]--;
                buttons1Check[1].remove(value);
                ctx.respond("Your selection of: " + buttons1[1][0] + " , was removed!");
                System.out.println(Arrays.toString(buttons1Count));
            }
            return ctx.ack();
        });
        app.blockAction("datePicker2", (req, ctx) -> { // responding to first option
            String value = req.getPayload().getUser().getId();
            if (!buttons1Check[2].contains(value)) {
                buttons1Count[2]++;
                buttons1Check[2].add(value);
                ctx.respond("Your selection of: " + buttons1[2][0] + " , was recorded!");
                System.out.println(Arrays.toString(buttons1Count));
            } else {
                buttons1Count[2]--;
                buttons1Check[2].remove(value);
                ctx.respond("Your selection of: " + buttons1[2][0] + " , was removed!");
                System.out.println(Arrays.toString(buttons1Count));
            }
            return ctx.ack();
        });
        app.blockAction("datePicker3", (req, ctx) -> { // responding to first option
            String value = req.getPayload().getUser().getId();
            if (!buttons1Check[3].contains(value)) {
                buttons1Count[3]++;
                buttons1Check[3].add(value);
                ctx.respond("Your selection of: " + buttons1[3][0] + " , was recorded!");
                System.out.println(Arrays.toString(buttons1Count));
            } else {
                buttons1Count[3]--;
                buttons1Check[3].remove(value);
                ctx.respond("Your selection of: " + buttons1[3][0] + " , was removed!");
                System.out.println(Arrays.toString(buttons1Count));
            }
            return ctx.ack();
        });
        app.blockAction("datePicker4", (req, ctx) -> { // responding to first option
            String value = req.getPayload().getUser().getId();
            if (!buttons1Check[4].contains(value)) {
                buttons1Count[4]++;
                buttons1Check[4].add(value);
                ctx.respond("Your selection of: " + buttons1[4][0] + " , was recorded!");
                System.out.println(Arrays.toString(buttons1Count));
            } else {
                buttons1Count[4]--;
                buttons1Check[4].remove(value);
                ctx.respond("Your selection of: " + buttons1[4][0] + " , was removed!");
                System.out.println(Arrays.toString(buttons1Count));
            }
            return ctx.ack();
        });
        app.blockAction("datePicker5", (req, ctx) -> { // responding to first option
            String value = req.getPayload().getUser().getId();
            if (!buttons1Check[5].contains(value)) {
                buttons1Count[5]++;
                buttons1Check[5].add(value);
                ctx.respond("Your selection of: " + buttons1[5][0] + " , was recorded!");
                System.out.println(Arrays.toString(buttons1Count));
            } else {
                buttons1Count[5]--;
                buttons1Check[5].remove(value);
                ctx.respond("Your selection of: " + buttons1[5][0] + " , was removed!");
                System.out.println(Arrays.toString(buttons1Count));
            }
            return ctx.ack();
        });
        app.blockAction("datePicker6", (req, ctx) -> { // responding to first option
            String value = req.getPayload().getUser().getId();
            if (!buttons1Check[6].contains(value)) {
                buttons1Count[6]++;
                buttons1Check[6].add(value);
                ctx.respond("Your selection of: " + buttons1[6][0] + " , was recorded!");
                System.out.println(Arrays.toString(buttons1Count));
            } else {
                buttons1Count[6]--;
                buttons1Check[6].remove(value);
                ctx.respond("Your selection of: " + buttons1[6][0] + " , was removed!");
                System.out.println(Arrays.toString(buttons1Count));
            }
            return ctx.ack();
        });
        SlackAppServer server = new SlackAppServer(app);
        server.start();
    }

    // processes the user input to give a String[][] of options that can be made into buttons
    public static String[][] processedInput(String in) throws Exception {
        // takes input in the form /test-poll Option1 Option2 ...
        String[] items = in.split(" ");
        String[][] input = new String[items.length][2];
        if (input.length > 7) {
            throw new Exception("You added too many dates. Max is 7!");
        }
        for (int i = 0; i < items.length; i++) {
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
                            .actionId("datePicker" + button[1])
                            .value(button[1])));
        }
        return list;
    }
}
