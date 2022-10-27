/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package wang.daniel.pollapp;

import com.slack.api.Slack;
import static com.slack.api.model.block.composition.BlockCompositions.plainText;
import com.slack.api.model.block.element.BlockElement;
import static com.slack.api.model.block.element.BlockElements.button;
import java.util.*;

/**
 *
 * @author wangd This file handles everything related to running a Poll
 */
public class Poll {

    private ArrayList<Button> buttons;
    private PollTypes type;
    private ArrayList<BlockElement> interactiveButtons;
    private String userID;

    public Poll() {
        this.type = PollTypes.noPoll;
        buttons = new ArrayList<>();
        interactiveButtons = new ArrayList<>();
    }

    public void createPoll(PollTypes type, String userInput, String userID) throws Exception {
        if (this.type != PollTypes.noPoll) {
            throw new Exception("You already have a poll running! "
                    + "Type /display-results to end the poll and view results.");
        }
        this.type = type;
        this.userID = userID;
        switch (this.type) {
            case datePoll -> {
                try {
                    processedInput(userInput, 7, "datePicker");
                    generateButtons();
                } catch (Exception e) {
                    throw new Exception(e);
                }
            }
            case timePoll -> {
                try {
                    processedInput(userInput, 10, "timePicker");
                    generateButtons();
                } catch (Exception e) {
                    throw new Exception(e);
                }
            }
        }
    }

    public void processedInput(String in, int max, String buttonID) throws Exception {
        String[] items = in.split(" ");
        if (items.length > max) {
            throw new Exception(String.format("You added too many dates. Max is %d!", max));
        }
        for (int i = 0; i < items.length; i++) {
            this.buttons.add(new Button(items[i], buttonID + i));
        }
    }

    public void generateButtons() {
        interactiveButtons.clear();
        this.buttons.forEach(button -> {
            interactiveButtons.add(button(b
                    -> b.text(plainText(pt -> pt
                    .emoji(true)
                    .text(button.getButtonText())))
                            .actionId(button.getButtonID())));
        });
    }

    public String updateVote(String userID, String buttonID, String username) {
        String str = "";
        switch (this.type) {
            case datePoll -> {
                int index = Integer.parseInt(buttonID.replace("datePicker", ""));
                str = this.buttons.get(index).updateVote(userID, username);
            }
            case timePoll -> {
                int index = Integer.parseInt(buttonID.replace("timePicker", ""));
                str = this.buttons.get(index).updateVote(userID, username);
            }
        }
        str += getResponses(userID);
        return str;
    }

    public String getResponses(String user) {
        final StringBuilder str = new StringBuilder();
        str.append("You have selected the following:\n");
        this.buttons.forEach(button -> {
            if (button.getUsers().contains(user)) {
                String val = "• " + button.getButtonText() + "\n";
                str.append(val);
            }
        });
        return str.toString();
    }

    public String getResults(String userID) {
        final var str = new StringBuilder();
        if (this.type == PollTypes.noPoll) {
            return "No poll running! Type _/help_ for help!";
        }
        if (!userID.equals(this.userID)) {
            return "You don't have permission to view poll results :(";
        }
        this.buttons.forEach(button -> {
            str.append(String.format("• %s: _%d vote(s)_\n",
                    button.getButtonText(),
                    button.getCounter()));
        });
        str.append(mostVotes());
        str.append(getVoters());
        return str.toString();
    }

    public String getVoters() {
        StringBuilder str = new StringBuilder();
        str.append("\nHere is a list of all voters:\n");
        this.buttons.forEach(button -> {
            str.append(String.format("• %s: ", button.getButtonText()));
            button.getUsernames().forEach(user -> str.append(user + ", "));
            str.append("\n");
        });

        if (!this.buttons.get(this.buttons.size() - 1).getUsernames().isEmpty()) {
            str.deleteCharAt(str.length() - 1);
            str.deleteCharAt(str.length() - 2);
        }
        return str.toString();
    }

    public String mostVotes() {
        var sorted = this.buttons;
        sorted.sort((Button b1, Button b2) -> {
            return b2.getCounter() - b1.getCounter();
        });
        String str = "\nHere are the poll results sorted in order:\n";
        for (Button button : sorted) {
            str += String.format("• %s, _%d vote(s)_\n", button.getButtonText(), button.getCounter());
        }
        return str;
    }

    public ArrayList<BlockElement> getInteractiveButtons() {
        return this.interactiveButtons;
    }
}

class Button {

    private ArrayList<String> users = new ArrayList<>();
    private ArrayList<String> usernames = new ArrayList<>();
    private int counter;
    private String buttonText, buttonID;

    public Button(String buttonText, String buttonID) {
        this.buttonID = buttonID;
        this.buttonText = buttonText;
        this.counter = 0;
    }

    public String updateVote(String userID, String username) {
        String message = "";
        if (users.contains(userID)) {
            counter--;
            usernames.remove(username);
            users.remove(userID);
            message = String.format("Your selection of: _%s_ was removed! ", buttonText);
        } else {
            counter++;
            users.add(userID);
            usernames.add(username);
            message = String.format("Your selection of: _%s_ was recorded! ", buttonText);
        }
        return message;
    }

    public ArrayList<String> getUsers() {
        return this.users;
    }

    public ArrayList<String> getUsernames() {
        return this.usernames;
    }

    public String getButtonID() {
        return this.buttonID;
    }

    public String getButtonText() {
        return this.buttonText;
    }

    public int getCounter() {
        return this.counter;
    }

}
