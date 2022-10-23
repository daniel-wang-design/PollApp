/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package wang.daniel.pollapp;

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

    public Poll() {
        this.type = PollTypes.noPoll;
        buttons = new ArrayList<>();
        interactiveButtons = new ArrayList<>();
    }

    public void createPoll(PollTypes type, String userInput) throws Exception {
        if (this.type != PollTypes.noPoll) {
            throw new Exception("You already have a poll running! "
                    + "Type /display-results to end the poll and view results.");
        }
        switch (this.type) {
            case datePoll -> {
                try {
                    processedInput(userInput, 7, "datePoll");
                    generateButtons();
                } catch (Exception e) {
                    throw new Exception(e);
                }
            }
            case timePoll -> {
                try {
                    processedInput(userInput, 10, "timePoll");
                    generateButtons();
                } catch (Exception e) {
                    throw new Exception(e);
                }
            }
            default -> {
                throw new Exception("Something went wrong...");
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
                    .text(button.buttonText())))
                            .actionId(button.buttonID())));
        });
    }

    public String updateVote(String userID, String buttonID) {
        String str = "";
        switch (this.type) {
            case datePoll -> {
                int index = Integer.parseInt(buttonID.replace("datePicker", ""));
                str = this.buttons.get(index).updateVote(userID);
            }
            case timePoll -> {
                int index = Integer.parseInt(buttonID.replace("timePicker", ""));
                str = this.buttons.get(index).updateVote(userID);
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
                String val = "-> " + button.buttonText() + "\n";
                str.append(val);
            }
        });
        return str.toString();
    }

    public ArrayList<BlockElement> getInteractiveButtons() {
        return this.interactiveButtons;
    }
}

record Button(String buttonText, String buttonID) {

    private static ArrayList<String> users = new ArrayList<>();
    private static int counter;

    public String updateVote(String userID) {
        String message = "";
        if (users.contains(userID)) {
            counter--;
            users.remove(userID);
            message = "Your selection of: " + buttonText + " was removed.";
        } else {
            counter++;
            users.add(userID);
            message = "Your selection of: " + buttonText + " was recorded.";
        }
        return message;
    }

    public ArrayList<String> getUsers() {
        return this.users;
    }

}
