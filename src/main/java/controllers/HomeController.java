package controllers;

import database.Database;
import views.HomeView;

import javax.swing.*;

public class HomeController {
    private final HomeView homeView;
    private final Database database;
    private final JLabel entryCountLabel;
    private final JLabel sourceCountLabel;
    private final JLabel imageCountLabel;
    private final JLabel todayEntryCountLabel;
    private final JLabel averageEntriesLabel;

    //Constructor
    public HomeController(HomeView parentView) {
        homeView = parentView;
        database = homeView.getDatabase();
        entryCountLabel = homeView.getEntryCountLabel();
        sourceCountLabel = homeView.getSourceCountLabel();
        imageCountLabel = homeView.getImageCountLabel();
        todayEntryCountLabel = homeView.getTodayEntryCountLabel();
        averageEntriesLabel = homeView.getAverageEntriesLabel();

        updateCounts();
    }

    //Updates counts for the JLabels that display stats on the HomeView
    private void updateCounts() {
        String entryCount = "Total Entries: " + database.getMaxSentenceIndex();
        entryCountLabel.setText(entryCount);

        String sourceCount = "Total Sources: " + database.findTotalSourceCount();
        sourceCountLabel.setText(sourceCount);

        String imageCount = "Total Images: " + database.findTotalImageCount();
        imageCountLabel.setText(imageCount);

        String todayEntry = "Today's Entry Count: " + database.findTodayEntryCount();
        todayEntryCountLabel.setText(todayEntry);

        String averageEntry = "Average Entries Per Day: " + database.findAverageEntriesPerDay();
        averageEntriesLabel.setText(averageEntry);
    }

}
