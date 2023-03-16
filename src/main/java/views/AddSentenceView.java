package views;


import content_objects.SentenceObject;
import controllers.AddSentenceControl;
import window_object.WindowObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.SQLException;

public class AddSentenceView {
    private final WindowObject mainWindow;
    private final Container container;
    private final SpringLayout layout;
    private final AddSentenceControl sentenceControl;
    private SetLinkView setLinkView = null;
    private final int padding = 20;
    private final Font jpFont = new Font("Meiryo", Font.BOLD, 16);
    //TODO: Consider making a separate font for ui elements?


    private final JLabel sourceTypeLabel = new JLabel("Source Type: ");
    private final String[] sourcesList = {"Visual Novel", "Manga", "Anime", "Online", "Newspaper", "Magazine"};
    private final JComboBox<String> sourceTypeCombo = new JComboBox<>(sourcesList);
    private final JCheckBox sequentialCheck = new JCheckBox("Is Sequential");
    private final JLabel linkStatusLabel = new JLabel("Back Link Status: ");
    private final JLabel currentLinkStatusLabel = new JLabel("No Link");
    private final JButton setLinkButton = new JButton("Set Link");
    private final JButton viewLinkButton = new JButton("View Link");
    private final JButton setHeadButton = new JButton("Set Head");
    private final JLabel sourceNameLabel = new JLabel("Source Name: ");
    private final JCheckBox hasUrlCheck = new JCheckBox("Has URL");

    private final JTextArea sourceNameArea = new JTextArea();
    private JTextArea sourceUrlArea;
    private JTextArea sentenceArea;
    private JCheckBox nsfwCheck;
    private JTextArea imageArea;


    private int fieldColumns = 33;
    //TODO: After setting up window sizing options, make some kind of relational to column count for textarea

    public AddSentenceView(WindowObject mainWindow) {
        this.mainWindow = mainWindow;
        this.container = this.mainWindow.getContainer();
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

        JPanel navPanel = mainWindow.createNavPanel();
        JPanel contentPanel = mainWindow.createContentPanel(navPanel);
        SpringLayout contentLayout = (SpringLayout)contentPanel.getLayout();

        JPanel sourceTypePanel = createSourceTypePanel();
        contentLayout.putConstraint(
                SpringLayout.WEST, sourceTypePanel, 0,
                SpringLayout.WEST, contentPanel
        );
        contentLayout.putConstraint(
                SpringLayout.NORTH, sourceTypePanel, 0,
                SpringLayout.NORTH, contentPanel
        );
        contentPanel.add(sourceTypePanel);

        JPanel backlinkPanel = createBacklinkPanel();
        contentLayout.putConstraint(
                SpringLayout.WEST, backlinkPanel, 0,
                SpringLayout.WEST, contentPanel
        );
        contentLayout.putConstraint(
                SpringLayout.NORTH, backlinkPanel, 0,
                SpringLayout.SOUTH, sourceTypePanel
        );
        contentPanel.add(backlinkPanel);

        JPanel sourceNamePanel = createSourceNamePanel();
        contentLayout.putConstraint(
                SpringLayout.WEST, sourceNamePanel, 0,
                SpringLayout.WEST, contentPanel
        );
        contentLayout.putConstraint(
                SpringLayout.NORTH, sourceNamePanel, 0,
                SpringLayout.SOUTH, backlinkPanel
        );
        contentPanel.add(sourceNamePanel);


        //Constraints for EAST and SOUTH of contentPanel, EAST must be widest panel and SOUTH must be bottom panel
        JPanel bottomPanel = new JPanel();
        contentLayout.putConstraint(
                SpringLayout.NORTH, bottomPanel, 0,
                SpringLayout.SOUTH, sourceNamePanel
        );
        contentLayout.putConstraint(
                SpringLayout.WEST, bottomPanel, 0,
                SpringLayout.WEST, contentPanel
        );

        contentLayout.putConstraint(
                SpringLayout.EAST, contentPanel, 0,
                SpringLayout.EAST,  sourceTypePanel
        );
        contentLayout.putConstraint(
                SpringLayout.SOUTH, contentPanel, 0,
                SpringLayout.SOUTH, bottomPanel
        );



        mainWindow.packFrame();
        mainWindow.getMainFrame().setMinimumSize(
                new Dimension(contentPanel.getWidth() * 2, contentPanel.getHeight() + navPanel.getHeight() * 2));
        contentPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.BLACK));

        return new AddSentenceControl(this, mainWindow.getDatabase());
    }

    private AddSentenceControl createView1() {

        hasUrlCheck.setFont(jpFont);



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


        sentenceArea = new JTextArea();
        sentenceArea.setWrapStyleWord(false);
        sentenceArea.setLineWrap(true);
        sentenceArea.setColumns(fieldColumns);
        sentenceArea.setRows(7);
        sentenceArea.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        sentenceArea.setFont(jpFont);


        JLabel sentenceRequiredLabel = new JLabel("Sentence Is Required");
        sentenceRequiredLabel.setFont(jpFont);
        sentenceRequiredLabel.setForeground(new Color(214, 67, 56));

        //Image Input
        JLabel imageLabel = new JLabel("Image:");
        imageLabel.setFont(jpFont);


        nsfwCheck = new JCheckBox("NSFW");
        nsfwCheck.setFont(jpFont);


        JButton setCapture = new JButton("Set Capture");
        setCapture.setFont(jpFont);


        imageArea = new JTextArea();
        imageArea.setWrapStyleWord(false);
        imageArea.setLineWrap(true);
        imageArea.setColumns(fieldColumns);
        imageArea.setRows(1);
        imageArea.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        imageArea.setFont(jpFont);

        //Add button
        JButton addButton = new JButton("Add");
        addButton.setFont(jpFont);

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

    public JPanel createSourceTypePanel() {
        JPanel returnPanel = new JPanel();
        SpringLayout panelLayout = new SpringLayout();
        returnPanel.setLayout(panelLayout);

        panelLayout.putConstraint(
                SpringLayout.WEST, sourceTypeLabel, padding,
                SpringLayout.WEST, returnPanel
        );
        panelLayout.putConstraint(
                SpringLayout.NORTH, sourceTypeLabel, padding,
                SpringLayout.NORTH, returnPanel
        );
        returnPanel.add(sourceTypeLabel);

        sourceTypeCombo.setSelectedIndex(0);
        //Prevents the sourceTypeCombo from stretching vertically when window is resized
        sourceTypeCombo.setMaximumSize(new Dimension());
        panelLayout.putConstraint(
                SpringLayout.VERTICAL_CENTER, sourceTypeCombo, 0,
                SpringLayout.VERTICAL_CENTER, sourceTypeLabel
        );
        panelLayout.putConstraint(
                SpringLayout.WEST, sourceTypeCombo, padding,
                SpringLayout.EAST, sourceTypeLabel
        );
        returnPanel.add(sourceTypeCombo);

        panelLayout.putConstraint(
                SpringLayout.VERTICAL_CENTER, sequentialCheck, 0,
                SpringLayout.VERTICAL_CENTER, sourceTypeCombo
        );
        panelLayout.putConstraint(
                SpringLayout.WEST, sequentialCheck, padding,
                SpringLayout.EAST, sourceTypeCombo
        );
        returnPanel.add(sequentialCheck);


        //Constraints for EAST and SOUTH of returnPanel
        panelLayout.putConstraint(
                SpringLayout.EAST, returnPanel, padding,
                SpringLayout.EAST, sequentialCheck
        );
        panelLayout.putConstraint(
                SpringLayout.SOUTH, returnPanel, 0,
                SpringLayout.SOUTH, sourceTypeCombo
        );

        return returnPanel;
    }

    public JPanel createBacklinkPanel() {
        JPanel returnPanel = new JPanel();
        SpringLayout panelLayout = new SpringLayout();
        returnPanel.setLayout(panelLayout);

        panelLayout.putConstraint(
                SpringLayout.WEST, linkStatusLabel, padding,
                SpringLayout.WEST, returnPanel
        );
        panelLayout.putConstraint(
                SpringLayout.NORTH, linkStatusLabel, padding,
                SpringLayout.NORTH, returnPanel
        );
        returnPanel.add(linkStatusLabel);

        panelLayout.putConstraint(
                SpringLayout.WEST, currentLinkStatusLabel, padding,
                SpringLayout.EAST, linkStatusLabel
        );
        panelLayout.putConstraint(
                SpringLayout.NORTH, currentLinkStatusLabel, padding,
                SpringLayout.NORTH, returnPanel
        );
        returnPanel.add(currentLinkStatusLabel);

        panelLayout.putConstraint(
                SpringLayout.WEST, setLinkButton, padding,
                SpringLayout.WEST, returnPanel
        );
        panelLayout.putConstraint(
                SpringLayout.NORTH, setLinkButton, padding,
                SpringLayout.SOUTH, linkStatusLabel
        );
        returnPanel.add(setLinkButton);
        setLinkButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setLinkView = new SetLinkView(mainWindow, sentenceControl);
            }
        });

        panelLayout.putConstraint(
                SpringLayout.WEST, viewLinkButton, padding,
                SpringLayout.EAST, setLinkButton
        );
        panelLayout.putConstraint(
                SpringLayout.NORTH, viewLinkButton, padding,
                SpringLayout.SOUTH, linkStatusLabel
        );
        returnPanel.add(viewLinkButton);
        //TODO: Create function for viewing the currently set backlink

        panelLayout.putConstraint(
                SpringLayout.WEST, setHeadButton, padding,
                SpringLayout.EAST, viewLinkButton
        );
        panelLayout.putConstraint(
                SpringLayout.NORTH, setHeadButton, padding,
                SpringLayout.SOUTH, linkStatusLabel
        );
        returnPanel.add(setHeadButton);
        setHeadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setHeadClicked(currentLinkStatusLabel);
            }
        });

        //Constraints for EAST and SOUTH of returnPanel
        panelLayout.putConstraint(
                SpringLayout.EAST, returnPanel, padding,
                SpringLayout.EAST, setHeadButton
        );
        panelLayout.putConstraint(
                SpringLayout.SOUTH, returnPanel, 0,
                SpringLayout.SOUTH, setLinkButton
        );

        return returnPanel;
    }

    public JPanel createSourceNamePanel() {
        JPanel returnPanel = new JPanel();
        SpringLayout panelLayout = new SpringLayout();
        returnPanel.setLayout(panelLayout);

        panelLayout.putConstraint(
                SpringLayout.WEST, sourceNameLabel, padding,
                SpringLayout.WEST, returnPanel
        );
        panelLayout.putConstraint(
                SpringLayout.NORTH, sourceNameLabel, padding,
                SpringLayout.NORTH, returnPanel
        );
        returnPanel.add(sourceNameLabel);

        panelLayout.putConstraint(
                SpringLayout.WEST, hasUrlCheck, padding,
                SpringLayout.EAST, sourceNameLabel
        );
        panelLayout.putConstraint(
                SpringLayout.VERTICAL_CENTER, hasUrlCheck, 0,
                SpringLayout.VERTICAL_CENTER, sourceNameLabel
        );
        returnPanel.add(hasUrlCheck);

        sourceNameArea.setWrapStyleWord(false);
        sourceNameArea.setLineWrap(true);
        sourceNameArea.setColumns(fieldColumns);
        sourceNameArea.setRows(1);
        sourceNameArea.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        //Constraints for EAST and SOUTH of returnPanel
        panelLayout.putConstraint(
                SpringLayout.EAST, returnPanel, padding,
                SpringLayout.EAST, hasUrlCheck
        );
        panelLayout.putConstraint(
                SpringLayout.SOUTH, returnPanel, padding,
                SpringLayout.SOUTH, sourceNameLabel
        );

        return returnPanel;
    }

    public void createUrlPanel() {}

    public void createSentencePanel() {}

    public void createImagePanel() {}


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
