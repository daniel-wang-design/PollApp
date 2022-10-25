package wang.daniel.pollapp;

import com.slack.api.bolt.App;
import com.slack.api.bolt.jetty.SlackAppServer;
import static com.slack.api.model.block.Blocks.*;
import static com.slack.api.model.block.composition.BlockCompositions.*;

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
            String userID = req.getPayload().getUserId();
            try {
                poll.createPoll(PollTypes.datePoll, userInput, userID);
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
            return ctx.ack();
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
            String message = poll.updateVote(value, "datePicker4");
            ctx.respond(message);
            return ctx.ack();
        });
        app.blockAction("datePicker5", (req, ctx) -> { // responding to sixth option
            String value = req.getPayload().getUser().getId();
            String message = poll.updateVote(value, "datePicker5");
            ctx.respond(message);
            return ctx.ack();
        });
        app.blockAction("datePicker6", (req, ctx) -> { // responding to seventh option
            String value = req.getPayload().getUser().getId();
            String message = poll.updateVote(value, "datePicker6");
            ctx.respond(message);
            return ctx.ack();
        });
        app.command("/time-poll", (req, ctx) -> {
            String userInput = req.getPayload().getText();
            String userID = req.getPayload().getUserId();
            try {
                poll.createPoll(PollTypes.timePoll, userInput, userID);
                ctx.respond(res -> res
                        .responseType("in_channel")
                        .blocks(asBlocks(section(section
                                -> section.text(markdownText((a) -> {
                            return a.text("*Please pick a time!*");
                        }))),
                                divider(),
                                actions(actions -> actions
                                .elements(poll.getInteractiveButtons())
                                )))
                );
            } catch (Exception e) {
                return ctx.ack(e.getMessage());
            }
            return ctx.ack();
        });
        app.blockAction("timePicker0", (req, ctx) -> {
            String value = req.getPayload().getUser().getId();
            String message = poll.updateVote(value, "timePicker0");
            ctx.respond(message);
            return ctx.ack();
        });
        app.blockAction("timePicker1", (req, ctx) -> {
            String value = req.getPayload().getUser().getId();
            String message = poll.updateVote(value, "timePicker1");
            ctx.respond(message);
            return ctx.ack();
        });
        app.blockAction("timePicker2", (req, ctx) -> {
            String value = req.getPayload().getUser().getId();
            String message = poll.updateVote(value, "timePicker2");
            ctx.respond(message);
            return ctx.ack();
        });
        app.blockAction("timePicker3", (req, ctx) -> {
            String value = req.getPayload().getUser().getId();
            String message = poll.updateVote(value, "timePicker3");
            ctx.respond(message);
            return ctx.ack();
        });
        app.blockAction("timePicker4", (req, ctx) -> {
            String value = req.getPayload().getUser().getId();
            String message = poll.updateVote(value, "timePicker4");
            ctx.respond(message);
            return ctx.ack();
        });
        app.blockAction("timePicker5", (req, ctx) -> {
            String value = req.getPayload().getUser().getId();
            String message = poll.updateVote(value, "timePicker5");
            ctx.respond(message);
            return ctx.ack();
        });
        app.blockAction("timePicker6", (req, ctx) -> {
            String value = req.getPayload().getUser().getId();
            String message = poll.updateVote(value, "timePicker6");
            ctx.respond(message);
            return ctx.ack();
        });
        app.blockAction("timePicker7", (req, ctx) -> {
            String value = req.getPayload().getUser().getId();
            String message = poll.updateVote(value, "timePicker6");
            ctx.respond(message);
            return ctx.ack();
        });
        app.blockAction("timePicker8", (req, ctx) -> {
            String value = req.getPayload().getUser().getId();
            String message = poll.updateVote(value, "timePicker6");
            ctx.respond(message);
            return ctx.ack();
        });
        app.blockAction("timePicker9", (req, ctx) -> {
            String value = req.getPayload().getUser().getId();
            String message = poll.updateVote(value, "timePicker6");
            ctx.respond(message);
            return ctx.ack();
        });
        app.command("/display-results", (req, ctx) -> {
            String userID = req.getPayload().getUserId(),
                    type = req.getPayload().getText(),
                    message = poll.getResults(userID);
            if (type == null) {
                ctx.respond("""
                            Hmm... use either _/display-results self_ to make results available only to yourself.
                            Or use _/display-results all_ to make results available to everyone.""");
                return ctx.ack();
            }
            if (type.equals("all")) {
                ctx.respond(res -> res
                        .responseType("in_channel")
                        .text(message));
                return ctx.ack();
            } else if (type.equals("self")) {
                ctx.respond(message);
                return ctx.ack();
            }
            ctx.respond("""
                            Hmm... use either _/display-results self_ to make results available only to yourself.
                            Or use _/display-results all_ to make results available to everyone.""");
            return ctx.ack();
        });
        app.command("/help", (req, ctx) -> {
            ctx.respond("""
                        • _/help_ --> Open help menu.
                        
                        
                        • _/date-poll date1 date2 ..._ --> Creates a new poll for choosing a date. List all date options by seperating each choice with a single space. 
                        
                        
                        • _/time-poll time1 time2 ..._ --> Creates a new poll for choosing a time. List all time options by seperating each choice with a single space.
                        
                        
                        • _/display-results [self, all]_ --> Lists poll results. Pass either _self_ for the results to be only to you, or _all_ for results to be available to everyone.""");
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
