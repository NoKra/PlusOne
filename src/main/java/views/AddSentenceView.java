package views;


import content_objects.SentenceObject;
import controllers.AddSentenceControl;
import org.h2.expression.function.SysInfoFunction;
import window_object.WindowObject;

import javax.sound.midi.SysexMessage;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Set;

public class AddSentenceView {
    private final WindowObject mainWindow;
    private final Container container;
    private final SpringLayout layout;
    private final AddSentenceControl sentenceControl;
    private SetLinkView setLinkView = null;
    private final int padding = 20;
    private final Font jpFont = new Font("Meiryo", Font.BOLD, 16);
    //TODO: Consider making a separate font for ui elements?
    private JComboBox<String> sourceTypeCombo;
    private JTextArea sourceNameArea;
    private JTextArea sourceUrlArea;
    private JTextArea sentenceArea;
    private JCheckBox nsfwCheck;
    private JTextArea imageArea;
    private JLabel currentLinkStatusLabel;
    private SentenceObject currentBacklink;


    private int fieldColumns = 33;
    //TODO: After setting up window sizing options, make some kind of relational to column count for textarea

    public AddSentenceView(WindowObject mainWindow) {
        this.mainWindow = mainWindow;
        this.container = this.mainWindow.getContentContainer();
        this.layout = this.mainWindow.getLayout();


        sentenceControl = createView();
        /*
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new SetLinkView(mainWindow);
            }
        });
         */
    }

    public JComboBox<String> getSourceTypeCombo() {
        return sourceTypeCombo;
    }

    public JTextArea getSourceNameArea () {
        return sourceNameArea;
    }

    public JTextArea getSourceUrlArea() {
        return sourceUrlArea;
    }

    public JTextArea getSentenceArea() {
        return sentenceArea;
    }

    public JCheckBox getNsfwCheck() {
        return nsfwCheck;
    }

    public JTextArea getImageArea() {
        return imageArea;
    }

    public JLabel getCurrentLinkStatusLabel() {
        return currentLinkStatusLabel;
    }

    private AddSentenceControl createView() {
        //Window title
        JLabel header = new JLabel("Input Sentences: ");
        header.setFont(jpFont);
        layout.putConstraint(
                SpringLayout.WEST, header, padding,
                SpringLayout.WEST, container
        );
        layout.putConstraint(
                SpringLayout.NORTH, header, padding,
                SpringLayout.NORTH, container
        );
        container.add(header);

        //Sources Options
        JLabel sourceTypeLabel = new JLabel("Source Type: ");
        sourceTypeLabel.setFont(jpFont);
        layout.putConstraint(
                SpringLayout.WEST, sourceTypeLabel, padding,
                SpringLayout.WEST, container
        );
        layout.putConstraint(
                SpringLayout.NORTH, sourceTypeLabel, padding,
                SpringLayout.SOUTH, header
        );
        container.add(sourceTypeLabel);

        String[] sourcesList = {"Visual Novel", "Manga", "Anime", "Online", "Newspaper", "Magazine"};
        sourceTypeCombo = new JComboBox<>(sourcesList);
        sourceTypeCombo.setSelectedIndex(0);
        sourceTypeCombo.setFont(jpFont);
        layout.putConstraint(
                SpringLayout.WEST, sourceTypeCombo, padding,
                SpringLayout.EAST, sourceTypeLabel);
        layout.putConstraint(
                SpringLayout.VERTICAL_CENTER, sourceTypeCombo, 0,
                SpringLayout.VERTICAL_CENTER, sourceTypeLabel);
        container.add(sourceTypeCombo);

        JCheckBox sequentialCheck = new JCheckBox("Is Sequential");
        sequentialCheck.setFont(jpFont);
        layout.putConstraint(
                SpringLayout.WEST, sequentialCheck, padding,
                SpringLayout.EAST, sourceTypeCombo
        );
        layout.putConstraint(
                SpringLayout.VERTICAL_CENTER, sequentialCheck, 0,
                SpringLayout.VERTICAL_CENTER, sourceTypeCombo
        );
        container.add(sequentialCheck);

        //Back Link Status, positioning is determined by sequential check status
        JLabel linkStatusLabel = new JLabel("Back Link Status: ");
        linkStatusLabel.setFont(jpFont);

        currentLinkStatusLabel = new JLabel("No Link");
        currentLinkStatusLabel.setFont(jpFont);

        JButton setLinkButton = new JButton("Set Link");
        setLinkButton.setFont(jpFont);
        setLinkButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setLinkView = new SetLinkView(mainWindow, sentenceControl);
            }
        });

        //TODO: Create function for viewing the currently set backlink
        JButton viewLinkButton = new JButton("View Link");
        viewLinkButton.setFont(jpFont);

        JButton setHeadButton = new JButton("Set Head");
        setHeadButton.setFont(jpFont);
        setHeadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setHeadClicked(currentLinkStatusLabel);
            }
        });

        //Source Name
        JLabel sourceNameLabel = new JLabel("Source Name: ");
        sourceNameLabel.setFont(jpFont);
        layout.putConstraint(
                SpringLayout.WEST, sourceNameLabel, padding,
                SpringLayout.WEST, container
        );
        layout.putConstraint(
                SpringLayout.NORTH, sourceNameLabel, padding,
                SpringLayout.SOUTH, sourceTypeLabel
        );
        container.add(sourceNameLabel);

        JCheckBox hasUrlCheck = new JCheckBox("Has URL");
        hasUrlCheck.setFont(jpFont);
        layout.putConstraint(
                SpringLayout.WEST, hasUrlCheck, padding,
                SpringLayout.EAST, sourceNameLabel
        );
        layout.putConstraint(
                SpringLayout.VERTICAL_CENTER, hasUrlCheck, 0,
                SpringLayout.VERTICAL_CENTER, sourceNameLabel
        );
        container.add(hasUrlCheck);

        sourceNameArea = new JTextArea();
        sourceNameArea.setWrapStyleWord(false);
        sourceNameArea.setLineWrap(true);
        sourceNameArea.setColumns(fieldColumns);
        sourceNameArea.setRows(1);
        sourceNameArea.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        sourceNameArea.setFont(jpFont);
        layout.putConstraint(
                SpringLayout.HORIZONTAL_CENTER, sourceNameArea, 0,
                SpringLayout.HORIZONTAL_CENTER, container
        );
        layout.putConstraint(
                SpringLayout.NORTH, sourceNameArea, padding,
                SpringLayout.SOUTH, sourceNameLabel
        );
        container.add(sourceNameArea);


        //URL Input
        JLabel sourceUrlLabel = new JLabel("URL: ");
        sourceUrlLabel.setFont(jpFont);

        sourceUrlArea = new JTextArea();
        sourceUrlArea.setWrapStyleWord(false);
        sourceUrlArea.setLineWrap(true);
        sourceUrlArea.setColumns(fieldColumns);
        sourceUrlArea.setRows(1);
        sourceUrlArea.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        sourceUrlArea.setFont(jpFont);

        //Sentence Input
        JLabel sentenceLabel = new JLabel("Sentence:");
        sentenceLabel.setFont(jpFont);
        layout.putConstraint(
                SpringLayout.WEST, sentenceLabel, padding,
                SpringLayout.WEST, container
        );
        layout.putConstraint(
                SpringLayout.NORTH, sentenceLabel, padding,
                SpringLayout.SOUTH, sourceNameArea
        );
        container.add(sentenceLabel);

        sentenceArea = new JTextArea();
        sentenceArea.setWrapStyleWord(false);
        sentenceArea.setLineWrap(true);
        sentenceArea.setColumns(fieldColumns);
        sentenceArea.setRows(7);
        sentenceArea.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        sentenceArea.setFont(jpFont);
        layout.putConstraint(
                SpringLayout.HORIZONTAL_CENTER, sentenceArea, 0,
                SpringLayout.HORIZONTAL_CENTER, container
        );
        layout.putConstraint(
                SpringLayout.NORTH, sentenceArea, padding,
                SpringLayout.SOUTH, sentenceLabel
        );
        container.add(sentenceArea);

        JLabel sentenceRequiredLabel = new JLabel("Sentence Is Required");
        sentenceRequiredLabel.setFont(jpFont);
        sentenceRequiredLabel.setForeground(new Color(214, 67, 56));

        //Image Input
        JLabel imageLabel = new JLabel("Image:");
        imageLabel.setFont(jpFont);
        layout.putConstraint(
                SpringLayout.WEST, imageLabel, padding,
                SpringLayout.WEST, container
        );
        layout.putConstraint(
                SpringLayout.NORTH, imageLabel, padding,
                SpringLayout.SOUTH, sentenceArea
        );
        container.add(imageLabel);

        nsfwCheck = new JCheckBox("NSFW");
        nsfwCheck.setFont(jpFont);
        layout.putConstraint(
                SpringLayout.WEST, nsfwCheck, padding,
                SpringLayout.EAST, imageLabel
        );
        layout.putConstraint(
                SpringLayout.VERTICAL_CENTER, nsfwCheck, 0,
                SpringLayout.VERTICAL_CENTER, imageLabel
        );
        container.add(nsfwCheck);

        JButton setCapture = new JButton("Set Capture");
        setCapture.setFont(jpFont);
        layout.putConstraint(
                SpringLayout.WEST, setCapture, padding,
                SpringLayout.EAST, nsfwCheck
        );
        layout.putConstraint(
                SpringLayout.VERTICAL_CENTER, setCapture, 0,
                SpringLayout.VERTICAL_CENTER, nsfwCheck
        );
        container.add(setCapture);

        imageArea = new JTextArea();
        imageArea.setWrapStyleWord(false);
        imageArea.setLineWrap(true);
        imageArea.setColumns(fieldColumns);
        imageArea.setRows(1);
        imageArea.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        imageArea.setFont(jpFont);
        layout.putConstraint(
                SpringLayout.HORIZONTAL_CENTER, imageArea, 0,
                SpringLayout.HORIZONTAL_CENTER, container
        );
        layout.putConstraint(
                SpringLayout.NORTH, imageArea, padding,
                SpringLayout.SOUTH, imageLabel
        );
        container.add(imageArea);

        //Add button
        JButton addButton = new JButton("Add");
        addButton.setFont(jpFont);
        layout.putConstraint(
                SpringLayout.WEST, addButton, padding,
                SpringLayout.WEST, container
        );
        layout.putConstraint(
                SpringLayout.NORTH, addButton, padding,
                SpringLayout.SOUTH, imageArea
        );
        container.add(addButton);
        //Check to make sure the sentenceArea has some content, Sentence field on SentenceTable must be NON-NULL
        final boolean[] goodAdd = {true};
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!sentenceArea.getText().equals("")) {
                    try {
                        sentenceControl.addSentence();
                        if(!currentLinkStatusLabel.getText().equals("No Link")) {
                            currentLinkStatusLabel.setText(sentenceArea.getText());
                        }
                        if(!goodAdd[0]) {
                            container.remove(sentenceRequiredLabel);
                            layout.putConstraint(
                                    SpringLayout.NORTH, imageLabel, padding,
                                    SpringLayout.SOUTH, sentenceArea
                            );
                            layout.putConstraint(
                                    SpringLayout.NORTH, nsfwCheck, padding,
                                    SpringLayout.SOUTH, sentenceArea
                            );
                            container.repaint();
                            container.revalidate();
                        }
                        sentenceArea.setText("");
                        imageArea.setText("");
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                }
                else {
                    goodAdd[0] = false;
                    layout.putConstraint(
                            SpringLayout.WEST, sentenceRequiredLabel, padding,
                            SpringLayout.WEST, container
                    );
                    layout.putConstraint(
                            SpringLayout.NORTH, sentenceRequiredLabel, padding,
                            SpringLayout.SOUTH, sentenceArea
                    );
                    container.add(sentenceRequiredLabel);

                    layout.putConstraint(
                            SpringLayout.NORTH, imageLabel, padding,
                            SpringLayout.SOUTH, sentenceRequiredLabel
                    );
                    layout.putConstraint(
                            SpringLayout.NORTH, nsfwCheck, padding,
                            SpringLayout.SOUTH, sentenceRequiredLabel
                    );
                    /*
                    layout.putConstraint(
                            SpringLayout.NORTH, setCapture, padding,
                            SpringLayout.SOUTH, sentenceRequiredLabel
                    );
                     */
                }
                container.repaint();
                container.revalidate();
            }
        });
        sourceNameArea.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_TAB) {
                    e.consume();
                    if(hasUrlCheck.isSelected()) {
                        sourceUrlArea.requestFocus();
                    }
                    else {
                        sentenceArea.requestFocus();
                    }
                }
            }
        });
        sourceUrlArea.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_TAB) {
                    e.consume();
                    sentenceArea.requestFocus();
                }
            }
        });
        sentenceArea.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_TAB) {
                    imageArea.requestFocus();
                }
            }
        });

        sequentialCheck.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(sequentialCheck.isSelected()) {
                    //Adding link labels and buttons
                    layout.putConstraint(
                            SpringLayout.WEST, linkStatusLabel, padding,
                            SpringLayout.WEST, container
                    );
                    layout.putConstraint(
                            SpringLayout.NORTH, linkStatusLabel, padding,
                            SpringLayout.SOUTH, sourceTypeLabel
                    );
                    container.add(linkStatusLabel);

                    layout.putConstraint(
                            SpringLayout.WEST, currentLinkStatusLabel, padding,
                            SpringLayout.EAST, linkStatusLabel
                    );
                    layout.putConstraint(
                            SpringLayout.NORTH, currentLinkStatusLabel, padding,
                            SpringLayout.SOUTH, sourceTypeLabel
                    );
                    container.add(currentLinkStatusLabel);
                    currentLinkStatusLabel.setForeground(new Color(214, 67, 56));

                    layout.putConstraint(
                            SpringLayout.WEST, setLinkButton, padding,
                            SpringLayout.WEST, container
                    );
                    layout.putConstraint(
                            SpringLayout.NORTH, setLinkButton, padding,
                            SpringLayout.SOUTH, linkStatusLabel
                    );
                    container.add(setLinkButton);

                    layout.putConstraint(
                            SpringLayout.WEST, viewLinkButton, padding,
                            SpringLayout.EAST, setLinkButton
                    );
                    layout.putConstraint(
                            SpringLayout.NORTH, viewLinkButton, padding,
                            SpringLayout.SOUTH, linkStatusLabel
                    );
                    container.add(viewLinkButton);

                    layout.putConstraint(
                            SpringLayout.WEST, setHeadButton, padding,
                            SpringLayout.EAST, viewLinkButton
                    );
                    layout.putConstraint(
                            SpringLayout.NORTH, setHeadButton, padding,
                            SpringLayout.SOUTH, linkStatusLabel
                    );
                    container.add(setHeadButton);

                    //Changing constraints on sourceName label to attach to new link labels/buttons
                    layout.putConstraint(
                            SpringLayout.NORTH, sourceNameLabel, padding,
                            SpringLayout.SOUTH, setLinkButton
                    );
                }
                else {
                    //Removes the back link components
                    container.remove(linkStatusLabel);
                    container.remove(currentLinkStatusLabel);
                    container.remove(setLinkButton);
                    container.remove(viewLinkButton);
                    container.remove(setHeadButton);
                    currentLinkStatusLabel.setText("No Link");

                    layout.putConstraint(
                            SpringLayout.NORTH, sourceNameLabel, padding,
                            SpringLayout.SOUTH, sourceTypeLabel
                    );
                }
                container.repaint();
                container.revalidate();
            }
        });

        hasUrlCheck.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(hasUrlCheck.isSelected()) {
                    //Linking ulr components to sourceName TextArea
                    layout.putConstraint(
                            SpringLayout.WEST, sourceUrlLabel, padding,
                            SpringLayout.WEST, container
                    );
                    layout.putConstraint(
                            SpringLayout.NORTH, sourceUrlLabel, padding,
                            SpringLayout.SOUTH, sourceNameArea
                    );
                    container.add(sourceUrlLabel);

                    layout.putConstraint(
                            SpringLayout.HORIZONTAL_CENTER, sourceUrlArea, 0,
                            SpringLayout.HORIZONTAL_CENTER, container
                    );
                    layout.putConstraint(
                            SpringLayout.NORTH, sourceUrlArea, padding,
                            SpringLayout.SOUTH, sourceUrlLabel
                    );
                    container.add(sourceUrlArea);

                    //Reassigning south link of Sentence Label to urlArea
                    layout.putConstraint(
                            SpringLayout.NORTH, sentenceLabel, padding,
                            SpringLayout.SOUTH, sourceUrlArea
                    );
                }
                else {
                    container.remove(sourceUrlLabel);
                    container.remove(sourceUrlArea);
                    sourceUrlArea.setText("");

                    layout.putConstraint(
                            SpringLayout.NORTH, sentenceLabel, padding,
                            SpringLayout.SOUTH, sourceNameArea
                    );

                }
                container.repaint();
                container.revalidate();
            }
        });
        return new AddSentenceControl(this, mainWindow.getDatabase());
    }

    private void setHeadClicked(JLabel currentBackLink) {
        currentBackLink.setText("Head");
        currentBackLink.setForeground(new Color(126, 214, 92));

    }

    private void checkFonts() {
        String[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();

        for(String font : fonts) {
            System.out.println(font);
        }
    }
}
