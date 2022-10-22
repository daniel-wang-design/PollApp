package wang.daniel.pollapp;

import com.slack.api.bolt.App;
import com.slack.api.bolt.jetty.SlackAppServer;
import com.slack.api.model.ModelConfigurator;
import static com.slack.api.model.block.Blocks.*;
import com.slack.api.model.block.LayoutBlock;
import static com.slack.api.model.block.composition.BlockCompositions.*;
import com.slack.api.model.block.composition.MarkdownTextObject;
import com.slack.api.model.block.composition.TextObject;
import com.slack.api.model.block.element.BlockElement;
import com.slack.api.model.block.element.BlockElements;
import static com.slack.api.model.block.element.BlockElements.*;
import com.slack.api.model.block.element.ButtonElement;
import com.slack.api.model.block.element.RichTextSectionElement.Text;
import java.util.*;
import com.slack.api.bolt.service.builtin.AmazonS3InstallationService;
import com.slack.api.bolt.service.builtin.AmazonS3OAuthStateService;
import wang.daniel.pollapp.Polls;


/**
 *
 * @author wangd project started September 12, 2022
 */
public class PollApp {

    // for picking meeting date
    private static String[][] dateButtons;
    private static int[] dateButtonsCount = new int[7];
    private static ArrayList[] dateButtonsCheck = new ArrayList[7];

    // for picking meeting time
    private static String[][] timeButtons;
    private static int[] timeButtonsCount = new int[10];
    private static ArrayList[] timeButtonsCheck = new ArrayList[10];
    private static Polls pollRunning = Polls.noPoll;

    public static void main(String[] args) throws Exception {
        App app = new App();
        for (int i = 0; i < 7; i++) { // initialize user id check
            dateButtonsCheck[i] = new ArrayList<String>();
        }
        for (int i = 0; i < 10; i++) { // initialize user id check
            timeButtonsCheck[i] = new ArrayList<String>();
        }

        app.command("/date-poll", (req, ctx) -> {
            if (pollRunning == Polls.noPoll) {
                pollRunning = Polls.datePoll;
            } else {
                return ctx.ack("You already have a poll running! "
                        + "Type /display-results to end the poll and view results.");
            }
            String userInput = req.getPayload().getText();
            try {
                final String[][] buttons = processedInput(userInput, 7);
                dateButtons = buttons;
                ctx.respond(res -> res
                        .responseType("in_channel")
                        .blocks(asBlocks(section(section
                                -> section.text(markdownText((a) -> {
                            return a.text("*Please pick a date!*");
                        }))),
                                divider(),
                                actions(actions -> actions
                                .elements(generateButtonsDate(buttons))
                                )))
                );
            } catch (Exception e) {
                ctx.respond(e.getMessage());
            }
            return ctx.ack(); // respond with 200 OK
        });

        app.blockAction("datePicker0", (req, ctx) -> { // responding to first option
            String value = req.getPayload().getUser().getId();
            if (!dateButtonsCheck[0].contains(value)) {
                dateButtonsCount[0]++;
                dateButtonsCheck[0].add(value);
                ctx.respond("Your selection of: " + dateButtons[0][0] + ", was recorded!");
                System.out.println(Arrays.toString(dateButtonsCount));
            } else {
                dateButtonsCount[0]--;
                dateButtonsCheck[0].remove(value);
                ctx.respond("Your selection of: " + dateButtons[0][0] + ", was removed!");
                System.out.println(Arrays.toString(dateButtonsCount));
            }
            ctx.respond(getResponses(dateButtonsCheck, dateButtons, value));
            return ctx.ack();
        });
        app.blockAction("datePicker1", (req, ctx) -> { // responding to second option
            String value = req.getPayload().getUser().getId();
            if (!dateButtonsCheck[1].contains(value)) {
                dateButtonsCount[1]++;
                dateButtonsCheck[1].add(value);
                ctx.respond("Your selection of: " + dateButtons[1][0] + ", was recorded!");
                System.out.println(Arrays.toString(dateButtonsCount));
            } else {
                dateButtonsCount[1]--;
                dateButtonsCheck[1].remove(value);
                ctx.respond("Your selection of: " + dateButtons[1][0] + ", was removed!");
                System.out.println(Arrays.toString(dateButtonsCount));
            }
            ctx.respond(getResponses(dateButtonsCheck, dateButtons, value));
            return ctx.ack();
        });
        app.blockAction("datePicker2", (req, ctx) -> { // responding to third option
            String value = req.getPayload().getUser().getId();
            if (!dateButtonsCheck[2].contains(value)) {
                dateButtonsCount[2]++;
                dateButtonsCheck[2].add(value);
                ctx.respond("Your selection of: " + dateButtons[2][0] + ", was recorded!");
                System.out.println(Arrays.toString(dateButtonsCount));
            } else {
                dateButtonsCount[2]--;
                dateButtonsCheck[2].remove(value);
                ctx.respond("Your selection of: " + dateButtons[2][0] + ", was removed!");
                System.out.println(Arrays.toString(dateButtonsCount));
            }
            ctx.respond(getResponses(dateButtonsCheck, dateButtons, value));
            return ctx.ack();
        });
        app.blockAction("datePicker3", (req, ctx) -> { // responding to fourth option
            String value = req.getPayload().getUser().getId();
            if (!dateButtonsCheck[3].contains(value)) {
                dateButtonsCount[3]++;
                dateButtonsCheck[3].add(value);
                ctx.respond("Your selection of: " + dateButtons[3][0] + ", was recorded!");
                System.out.println(Arrays.toString(dateButtonsCount));
            } else {
                dateButtonsCount[3]--;
                dateButtonsCheck[3].remove(value);
                ctx.respond("Your selection of: " + dateButtons[3][0] + ", was removed!");
                System.out.println(Arrays.toString(dateButtonsCount));
            }
            ctx.respond(getResponses(dateButtonsCheck, dateButtons,  value));
            return ctx.ack();
        });
        app.blockAction("datePicker4", (req, ctx) -> { // responding to fifth option
            String value = req.getPayload().getUser().getId();
            if (!dateButtonsCheck[4].contains(value)) {
                dateButtonsCount[4]++;
                dateButtonsCheck[4].add(value);
                ctx.respond("Your selection of: " + dateButtons[4][0] + ", was recorded!");
                System.out.println(Arrays.toString(dateButtonsCount));
            } else {
                dateButtonsCount[4]--;
                dateButtonsCheck[4].remove(value);
                ctx.respond("Your selection of: " + dateButtons[4][0] + ", was removed!");
                System.out.println(Arrays.toString(dateButtonsCount));
            }
            ctx.respond(getResponses(dateButtonsCheck, dateButtons, value));
            return ctx.ack();
        });
        app.blockAction("datePicker5", (req, ctx) -> { // responding to sixth option
            String value = req.getPayload().getUser().getId();
            if (!dateButtonsCheck[5].contains(value)) {
                dateButtonsCount[5]++;
                dateButtonsCheck[5].add(value);
                ctx.respond("Your selection of: " + dateButtons[5][0] + ", was recorded!");
                System.out.println(Arrays.toString(dateButtonsCount));
            } else {
                dateButtonsCount[5]--;
                dateButtonsCheck[5].remove(value);
                ctx.respond("Your selection of: " + dateButtons[5][0] + ", was removed!");
                System.out.println(Arrays.toString(dateButtonsCount));
            }
            ctx.respond(getResponses(dateButtonsCheck, dateButtons, value));
            return ctx.ack();
        });
        app.blockAction("datePicker6", (req, ctx) -> { // responding to seventh option
            String value = req.getPayload().getUser().getId();
            if (!dateButtonsCheck[6].contains(value)) {
                dateButtonsCount[6]++;
                dateButtonsCheck[6].add(value);
                ctx.respond("Your selection of: " + dateButtons[6][0] + ", was recorded!");
                System.out.println(Arrays.toString(dateButtonsCount));
            } else {
                dateButtonsCount[6]--;
                dateButtonsCheck[6].remove(value);
                ctx.respond("Your selection of: " + dateButtons[6][0] + ", was removed!");
                System.out.println(Arrays.toString(dateButtonsCount));
            }
            ctx.respond(getResponses(dateButtonsCheck, dateButtons, value));
            return ctx.ack();
        });
        app.command("/time-poll", (req, ctx) -> {
            String userInput = req.getPayload().getText();
            try {
                final String[][] buttons = processedInput(userInput, 10);
                timeButtons = buttons;
                ctx.respond(res -> res
                        .responseType("in_channel")
                        .blocks(asBlocks(section(section
                                -> section.text(markdownText((a) -> {
                            return a.text("*Please pick a iime!*");
                        }))),
                                divider(),
                                actions(actions -> actions
                                .elements(generateButtonsTime(buttons))
                                )))
                );
            } catch (Exception e) {
                ctx.respond(e.getMessage());
            }
            return ctx.ack(); // respond with 200 OK
        });
        app.blockAction("timePicker0", (req, ctx) -> { // responding to first option
            String value = req.getPayload().getUser().getId();
            if (!timeButtonsCheck[0].contains(value)) {
                timeButtonsCount[0]++;
                timeButtonsCheck[0].add(value);
                ctx.respond("Your selection of: " + timeButtons[0][0] + ", was recorded!");
                System.out.println(Arrays.toString(timeButtonsCount));
            } else {
                timeButtonsCount[0]--;
                timeButtonsCheck[0].remove(value);
                ctx.respond("Your selection of: " + timeButtons[0][0] + ", was removed!");
                System.out.println(Arrays.toString(timeButtonsCount));
            }
            ctx.respond(getResponses(timeButtonsCheck, timeButtons, value));
            return ctx.ack();
        });
        app.blockAction("timePicker1", (req, ctx) -> { // responding to first option
            String value = req.getPayload().getUser().getId();
            if (!timeButtonsCheck[1].contains(value)) {
                timeButtonsCount[1]++;
                timeButtonsCheck[1].add(value);
                ctx.respond("Your selection of: " + timeButtons[1][0] + ", was recorded!");
                System.out.println(Arrays.toString(timeButtonsCount));
            } else {
                timeButtonsCount[1]--;
                timeButtonsCheck[1].remove(value);
                ctx.respond("Your selection of: " + timeButtons[1][0] + ", was removed!");
                System.out.println(Arrays.toString(timeButtonsCount));
            }
            ctx.respond(getResponses(timeButtonsCheck, timeButtons, value));
            return ctx.ack();
        });
        app.blockAction("timePicker2", (req, ctx) -> { // responding to first option
            String value = req.getPayload().getUser().getId();
            if (!timeButtonsCheck[2].contains(value)) {
                timeButtonsCount[2]++;
                timeButtonsCheck[2].add(value);
                ctx.respond("Your selection of: " + timeButtons[2][0] + ", was recorded!");
                System.out.println(Arrays.toString(timeButtonsCount));
            } else {
                timeButtonsCount[2]--;
                timeButtonsCheck[2].remove(value);
                ctx.respond("Your selection of: " + timeButtons[2][0] + ", was removed!");
                System.out.println(Arrays.toString(timeButtonsCount));
            }
            ctx.respond(getResponses(timeButtonsCheck, timeButtons, value));
            return ctx.ack();
        });
        app.blockAction("timePicker3", (req, ctx) -> { // responding to first option
            String value = req.getPayload().getUser().getId();
            if (!timeButtonsCheck[3].contains(value)) {
                timeButtonsCount[3]++;
                timeButtonsCheck[3].add(value);
                ctx.respond("Your selection of: " + timeButtons[3][0] + ", was recorded!");
                System.out.println(Arrays.toString(timeButtonsCount));
            } else {
                timeButtonsCount[3]--;
                timeButtonsCheck[3].remove(value);
                ctx.respond("Your selection of: " + timeButtons[3][0] + ", was removed!");
                System.out.println(Arrays.toString(timeButtonsCount));
            }
            ctx.respond(getResponses(timeButtonsCheck, timeButtons, value));
            return ctx.ack();
        });
        app.blockAction("timePicker4", (req, ctx) -> { // responding to first option
            String value = req.getPayload().getUser().getId();
            if (!timeButtonsCheck[4].contains(value)) {
                timeButtonsCount[4]++;
                timeButtonsCheck[4].add(value);
                ctx.respond("Your selection of: " + timeButtons[4][0] + ", was recorded!");
                System.out.println(Arrays.toString(timeButtonsCount));
            } else {
                timeButtonsCount[4]--;
                timeButtonsCheck[4].remove(value);
                ctx.respond("Your selection of: " + timeButtons[4][0] + ", was removed!");
                System.out.println(Arrays.toString(timeButtonsCount));
            }
            ctx.respond(getResponses(timeButtonsCheck, timeButtons, value));
            return ctx.ack();
        });
        app.blockAction("timePicker5", (req, ctx) -> { // responding to first option
            String value = req.getPayload().getUser().getId();
            if (!timeButtonsCheck[5].contains(value)) {
                timeButtonsCount[5]++;
                timeButtonsCheck[5].add(value);
                ctx.respond("Your selection of: " + timeButtons[5][0] + ", was recorded!");
                System.out.println(Arrays.toString(timeButtonsCount));
            } else {
                timeButtonsCount[5]--;
                timeButtonsCheck[5].remove(value);
                ctx.respond("Your selection of: " + timeButtons[5][0] + ", was removed!");
                System.out.println(Arrays.toString(timeButtonsCount));
            }
            ctx.respond(getResponses(timeButtonsCheck, timeButtons, value));
            return ctx.ack();
        });
        app.blockAction("timePicker6", (req, ctx) -> { // responding to first option
            String value = req.getPayload().getUser().getId();
            if (!timeButtonsCheck[6].contains(value)) {
                timeButtonsCount[6]++;
                timeButtonsCheck[6].add(value);
                ctx.respond("Your selection of: " + timeButtons[6][0] + ", was recorded!");
                System.out.println(Arrays.toString(timeButtonsCount));
            } else {
                timeButtonsCount[6]--;
                timeButtonsCheck[6].remove(value);
                ctx.respond("Your selection of: " + timeButtons[6][0] + ", was removed!");
                System.out.println(Arrays.toString(timeButtonsCount));
            }
            ctx.respond(getResponses(timeButtonsCheck, timeButtons, value));
            return ctx.ack();
        });
        App oauthApp = new App().asOAuthApp(true);
//        SlackAppServer server = new SlackAppServer(Map.of(
//                "/slack/events", app, 
//                "/slack/oauth", oauthApp));
        SlackAppServer server = new SlackAppServer(app);
        server.start();
    }

    public static String getResponses(ArrayList[] list, String[][] buttons, String user) {
        String val = "You have selected the following:\n";
        for (int i = 0; i < list.length; i++) {
            if (list[i].contains(user)) {
                val += "-> " + buttons[i][0] + "\n";
            }
        }
        return val;
    }

    // processes the user input to give a String[][] of options that can be made into buttons
    public static String[][] processedInput(String in, int max) throws Exception {
        // takes input in the form /test-poll Option1 Option2 ...
        String[] items = in.split(" ");
        String[][] input = new String[items.length][2];
        if (input.length > max) {
            throw new Exception("You added too many dates. Max is 7!");
        }
        for (int i = 0; i < items.length; i++) {
            input[i] = new String[]{items[i], Integer.toString(i)};
        }
        return input;
    }

    // takes String[][] and creates a list of buttons
    public static List<BlockElement> generateButtonsDate(String[][] buttons) {
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

    public static List<BlockElement> generateButtonsTime(String[][] buttons) {
        List<BlockElement> list = new ArrayList<>();
        for (String[] button : buttons) {
            list.add(button(b
                    -> b.text(plainText(pt -> pt
                    .emoji(true)
                    .text(button[0])))
                            .actionId("timePicker" + button[1])
                            .value(button[1])));
        }
        return list;
    }
}
