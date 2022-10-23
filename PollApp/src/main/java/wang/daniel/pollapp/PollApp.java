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

/**
 *
 * @author wangd project started September 12, 2022
 */
public class PollApp {

    private static Poll poll;

    public static void main(String[] args) throws Exception {
        App app = new App();
        poll = new Poll();
        app.command("/date-poll", (req, ctx) -> {
            String userInput = req.getPayload().getText();
            try {
                poll.createPoll(PollTypes.datePoll, userInput);
                ctx.respond(res -> res
                        .responseType("in_channel")
                        .blocks(asBlocks(section(section
                                -> section.text(markdownText((a) -> {
                            return a.text("*Please pick a date!*");
                        }))),
                                divider(),
                                actions(actions -> actions
                                .elements(poll.getInteractiveButtons())
                                )))
                );
            } catch (Exception e) {
                return ctx.ack(e.getMessage());
            }
            return ctx.ack(); // respond with 200 OK
        });

        app.blockAction("datePicker0", (req, ctx) -> { // responding to first option
            String value = req.getPayload().getUser().getId();
            String message = poll.updateVote(value, "datePicker0");
            ctx.respond(message);
            return ctx.ack();
        });
        app.blockAction("datePicker1", (req, ctx) -> { // responding to second option
            String value = req.getPayload().getUser().getId();
            String message = poll.updateVote(value, "datePicker1");
            ctx.respond(message);
            return ctx.ack();
        });
        app.blockAction("datePicker2", (req, ctx) -> { // responding to third option
            String value = req.getPayload().getUser().getId();
            String message = poll.updateVote(value, "datePicker2");
            ctx.respond(message);
            return ctx.ack();
        });
        app.blockAction("datePicker3", (req, ctx) -> { // responding to fourth option
            String value = req.getPayload().getUser().getId();
            String message = poll.updateVote(value, "datePicker3");
            ctx.respond(message);
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
        // App oauthApp = new App().asOAuthApp(true);
//        SlackAppServer server = new SlackAppServer(Map.of(
//                "/slack/events", app, 
//                "/slack/oauth", oauthApp));
        SlackAppServer server = new SlackAppServer(app);
        server.start();
    }

}
