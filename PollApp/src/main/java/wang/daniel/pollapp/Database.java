/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package wang.daniel.pollapp;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.regions.Regions;
import java.util.*;
import java.io.*;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.Bucket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author wangd
 */
public class Database {

    private List<Bucket> buckets;
    private String bucket;
    private AmazonS3 database;

    public Database() {
        this.database = AmazonS3ClientBuilder.standard().withRegion(Regions.US_EAST_2).build();
        this.bucket = "pollappbucket";
    }

    public void upload() {
        String path = "storage.txt";
        File file = new File("src/main/java/wang/daniel/pollapp/storage.txt");
        try {
            this.database.putObject(this.bucket, path, file);
        } catch (AmazonServiceException e) {
            System.out.println(e.getMessage());
            return;
        } catch (Exception e) {
            System.out.print(e.getMessage());
            return;
        }
        System.out.println("Added file!");
    }

    public void download() {
        String keyName = "storage.txt";
        var s3object = this.database.getObject(this.bucket, keyName);
        var inputStream = s3object.getObjectContent();
        try {
            FileUtils.copyInputStreamToFile(inputStream, new File("src/main/java/wang/daniel/pollapp/storage.txt"));
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }
        System.out.println("Downloaded file!");
    }

    public String save(Poll poll, String message) {
        if (poll.getType() == PollTypes.noPoll) {
            return "You don't have a poll running...";
        }
        try {
            BufferedReader reader = new BufferedReader(new FileReader("src/main/java/wang/daniel/pollapp/storage.txt"));
            var contents = new StringBuilder();
            while (true) {
                String s = reader.readLine();
                if (s != null) {
                    contents.append(s).append("\n");
                } else {
                    break;
                }
            }
            reader.close();
            contents.append("\nSEPERATOR-BETWEEN-POLLS\n");
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            String date;
            switch (poll.getType()) {
                case datePoll:
                    date = "*Date poll on " + dtf.format(now) + "*\n\n";
                    contents.append(date);
                    break;
                case timePoll:
                    date = "*Time poll on " + dtf.format(now) + "*\n\n";
                    contents.append(date);
                    break;
                case anonymousDatePoll:
                    date = "*Anonymouse date poll on " + dtf.format(now) + "*\n\n";
                    contents.append(date);
                    break;
                case anonymousTimePoll:
                    date = "*Anonymous time poll on " + dtf.format(now) + "*\n\n";
                    contents.append(date);
                    break;
            }
            contents.append(message);
            contents.append("\n");
            FileWriter writer = new FileWriter("src/main/java/wang/daniel/pollapp/storage.txt");
            writer.write(contents.toString());
            writer.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return "Poll saved!";
    }

    public String clear() {
        try {
            FileWriter writer = new FileWriter("src/main/java/wang/daniel/pollapp/storage.txt");
            writer.write("");
            writer.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return "Something went wrong...";
        }
        return "Deleted all past polls!";
    }

    public String view(String num) {
        final StringBuilder str = new StringBuilder();
        try {
            int count = Integer.parseInt(num);
            BufferedReader reader = new BufferedReader(new FileReader("src/main/java/wang/daniel/pollapp/storage.txt"));
            var contents = new StringBuilder();
            while (true) {
                String s = reader.readLine();
                if (s != null) {
                    contents.append(s).append("\n");
                } else {
                    break;
                }
            }
            reader.close();
            String[] polls = contents.toString().split("SEPERATOR-BETWEEN-POLLS");
            for (int i = polls.length - 1; i >= polls.length - count && i >= 0; i--) {
                str.append("\n***************\n");
                str.append(polls[i]);
            }
            if (str.toString().equals("\n***************\n")) {
                return "You have no saved polls.";
            }
        } catch (NumberFormatException e) {
            return "Please enter a number. Ex. _/view 8_";
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return str.toString();
    }

    public List<Bucket> getBuckets() {
        return this.buckets;
    }

}
