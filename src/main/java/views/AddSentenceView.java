package views;


import controllers.AddSentenceControl;
import window_object.WindowObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;

public class AddSentenceView {
    private final WindowObject mainWindow;
    private final AddSentenceControl sentenceControl;
    private SetLinkView setLinkView = null;
    private final int horPanelPadding = 100;
    private final int padding = 20;
    private final int fieldColumns = 33;
    private final Font jpFont = new Font("Meiryo", Font.BOLD, 16);
    //TODO: Consider making a separate font for ui elements?

    private JPanel navPanel;
    private JPanel contentPanel;
    private SpringLayout contentPanelLayout;
    private JPanel sourceTypePanel;
    private final JLabel sourceTypeLabel = new JLabel("Source Type: ");
    private final String[] sourcesList = {"Visual Novel", "Manga", "Anime", "Online", "Newspaper", "Magazine"};
    private final JComboBox<String> sourceTypeCombo = new JComboBox<>(sourcesList);
    private final JCheckBox sequentialCheck = new JCheckBox("Is Sequential");
    private JPanel backlinkPanel;
    private final JLabel linkStatusLabel = new JLabel("Back Link Status: ");
    private final JLabel currentLinkStatusLabel = new JLabel("No Link");
    private final Color hasLinkColor = new Color(126, 214, 92);
    private final Color noLinkColor = new Color(214, 67, 56);
    private final JButton setLinkButton = new JButton("Set Link");
    private final JButton viewLinkButton = new JButton("View Link");
    private final JButton setHeadButton = new JButton("Set Head");
    private JPanel sourceNamePanel;
    private final JLabel sourceNameLabel = new JLabel("Source Name: ");
    private final JCheckBox hasUrlCheck = new JCheckBox("Has URL");
    private final JTextArea sourceNameArea = new JTextArea();
    private JPanel urlPanel;
    private final JLabel sourceUrlLabel = new JLabel("URL: ");
    private final JTextArea sourceUrlArea = new JTextArea();
    private JPanel sentencePanel;
    private final JLabel sentenceLabel = new JLabel("Sentence:");
    private final JTextArea sentenceArea = new JTextArea();
    private final JLabel sentenceRequiredLabel = new JLabel("Sentence Is Required");
    private JPanel imagePanel;
    private final JLabel imageLabel = new JLabel("Image:");
    private final JCheckBox nsfwCheck = new JCheckBox("NSFW");
    private final JButton setCapture = new JButton("Set Capture");
    private final JTextArea imageArea = new JTextArea();
    private final JButton addButton = new JButton("Add");


    public AddSentenceView(WindowObject mainWindow) {
        this.mainWindow = mainWindow;
        sentenceControl = createView();
        mainWindow.getMainFrame().setLocationRelativeTo(null);
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new SetLinkView(mainWindow, sentenceControl);
            }
        });

    }

    //For passing to AddSentenceControl
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

        navPanel = mainWindow.createNavPanel();
        contentPanel = mainWindow.createContentPanel(navPanel);
        contentPanelLayout = (SpringLayout)contentPanel.getLayout();

        //empty topPanel prevents stretching of fields in other panels
        JPanel topPanel = new JPanel();
        contentPanelLayout.putConstraint(
                SpringLayout.HORIZONTAL_CENTER, topPanel, 0,
                SpringLayout.HORIZONTAL_CENTER, contentPanel
        );
        contentPanelLayout.putConstraint(
                SpringLayout.NORTH, topPanel, 0,
                SpringLayout.NORTH, contentPanel
        );
        contentPanel.add(topPanel);

        sourceTypePanel = createSourceTypePanel();
        contentPanelLayout.putConstraint(
                SpringLayout.HORIZONTAL_CENTER, sourceTypePanel, 0,
                SpringLayout.HORIZONTAL_CENTER, contentPanel
        );
        contentPanelLayout.putConstraint(
                SpringLayout.NORTH, sourceTypePanel, 0,
                SpringLayout.SOUTH, topPanel
        );
        contentPanel.add(sourceTypePanel);

        backlinkPanel = createBacklinkPanel();

        sourceNamePanel = createSourceNamePanel();
        contentPanelLayout.putConstraint(
                SpringLayout.HORIZONTAL_CENTER, sourceNamePanel, 0,
                SpringLayout.HORIZONTAL_CENTER, contentPanel
        );
        contentPanelLayout.putConstraint(
                SpringLayout.NORTH, sourceNamePanel, 0,
                SpringLayout.SOUTH, sourceTypePanel
        );
        contentPanel.add(sourceNamePanel);

        urlPanel = createUrlPanel();

        sentencePanel = createSentencePanel();
        contentPanelLayout.putConstraint(
                SpringLayout.HORIZONTAL_CENTER, sentencePanel, 0,
                SpringLayout.HORIZONTAL_CENTER, contentPanel
        );
        contentPanelLayout.putConstraint(
                SpringLayout.NORTH, sentencePanel, 0,
                SpringLayout.SOUTH, sourceNamePanel
        );
        contentPanel.add(sentencePanel);

        imagePanel = createImagePanel();
        contentPanelLayout.putConstraint(
                SpringLayout.HORIZONTAL_CENTER, imagePanel, 0,
                SpringLayout.HORIZONTAL_CENTER, contentPanel
        );
        contentPanelLayout.putConstraint(
                SpringLayout.NORTH, imagePanel, 0,
                SpringLayout.SOUTH, sentencePanel
        );
        contentPanel.add(imagePanel);

        //Constraints for EAST and SOUTH of contentPanel, EAST must be widest panel and SOUTH must be bottom panel
        //empty bottomPanel prevents stretching of fields in other panels
        JPanel bottomPanel = new JPanel();
        contentPanelLayout.putConstraint(
                SpringLayout.HORIZONTAL_CENTER, bottomPanel, 0,
                SpringLayout.HORIZONTAL_CENTER, contentPanel
        );
        contentPanelLayout.putConstraint(
                SpringLayout.NORTH, bottomPanel, 0,
                SpringLayout.SOUTH, imagePanel
        );
        contentPanel.add(bottomPanel);
        contentPanelLayout.putConstraint(
                SpringLayout.SOUTH, contentPanel, 0,
                SpringLayout.SOUTH, bottomPanel
        );



        mainWindow.packFrame();
        mainWindow.getMainFrame().setMinimumSize(new Dimension(
                (int)(sourceNamePanel.getWidth() * 1.5),
                contentPanel.getHeight() + navPanel.getHeight() * 3));
        contentPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.BLACK));

        //TODO: this may need to go in windowobject
        mainWindow.getMainFrame().addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                contentPanel.setMinimumSize(mainWindow.getMainFrame().getSize());
            }
        });

        return new AddSentenceControl(this, mainWindow.getDatabase());
    }

    private JPanel createSourceTypePanel() {
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

        sequentialCheck.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(sequentialCheck.isSelected()) {
                    contentPanelLayout.putConstraint(
                            SpringLayout.HORIZONTAL_CENTER, backlinkPanel, 0,
                            SpringLayout.HORIZONTAL_CENTER, contentPanel
                    );
                    contentPanelLayout.putConstraint(
                            SpringLayout.NORTH, backlinkPanel, 0,
                            SpringLayout.SOUTH, sourceTypePanel
                    );
                    contentPanel.add(backlinkPanel);

                    contentPanelLayout.putConstraint(
                            SpringLayout.NORTH, sourceNamePanel, 0,
                            SpringLayout.SOUTH, backlinkPanel
                    );
                    addComponentResize(backlinkPanel);
                }
                else {
                    contentPanel.remove(backlinkPanel);
                    currentLinkStatusLabel.setText("No Link");
                    currentLinkStatusLabel.setForeground(noLinkColor);

                    contentPanelLayout.putConstraint(
                            SpringLayout.NORTH, sourceNamePanel, 0,
                            SpringLayout.SOUTH, sourceTypePanel
                    );
                    removeComponentResize(backlinkPanel);
                }
            }
        });

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

    private JPanel createBacklinkPanel() {
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
        currentLinkStatusLabel.setForeground(noLinkColor);

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
                currentLinkStatusLabel.setText("Head");
                currentLinkStatusLabel.setForeground(hasLinkColor);
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

    private JPanel createSourceNamePanel() {
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

        hasUrlCheck.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(hasUrlCheck.isSelected()) {
                    contentPanelLayout.putConstraint(
                            SpringLayout.HORIZONTAL_CENTER, urlPanel, 0,
                            SpringLayout.HORIZONTAL_CENTER, contentPanel
                    );
                    contentPanelLayout.putConstraint(
                            SpringLayout.NORTH, urlPanel, 0,
                            SpringLayout.SOUTH, sourceNamePanel
                    );
                    contentPanel.add(urlPanel);

                    contentPanelLayout.putConstraint(
                            SpringLayout.NORTH, sentencePanel, 0,
                            SpringLayout.SOUTH, urlPanel
                    );

                    addComponentResize(urlPanel);
                }
                else {
                    contentPanel.remove(urlPanel);

                    contentPanelLayout.putConstraint(
                            SpringLayout.NORTH, sentencePanel, 0,
                            SpringLayout.SOUTH, sourceNamePanel
                    );
                    removeComponentResize(urlPanel);
                }
            }
        });

        sourceNameArea.setWrapStyleWord(false);
        sourceNameArea.setLineWrap(true);
        //setMaximumSize prevents name area from stretching
        sourceNameArea.setMaximumSize(new Dimension());
        sourceNameArea.setColumns(fieldColumns);
        sourceNameArea.setRows(1);
        sourceNameArea.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        sourceNameArea.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_TAB) {
                    e.consume();
                    if(hasUrlCheck.isSelected()) {
                        sourceUrlArea.requestFocus();
                        mainWindow.getContentScroll().getViewport().scrollRectToVisible(urlPanel.getBounds());

                    }
                    else {
                        sentenceArea.requestFocus();
                        mainWindow.getContentScroll().getViewport().scrollRectToVisible(sentencePanel.getBounds());
                    }
                }
            }
            @Override
            public void keyReleased(KeyEvent e) {
                sourceNameArea.repaint();
                sourceNameArea.revalidate();

                EventQueue.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        contentPanel.repaint();
                        contentPanel.revalidate();
                    }
                });
            }
        });

        panelLayout.putConstraint(
                SpringLayout.WEST, sourceNameArea, padding,
                SpringLayout.WEST, returnPanel
        );
        panelLayout.putConstraint(
                SpringLayout.NORTH, sourceNameArea, padding,
                SpringLayout.SOUTH, sourceNameLabel
        );
        returnPanel.add(sourceNameArea);

        //Constraints for EAST and SOUTH of returnPanel
        panelLayout.putConstraint(
                SpringLayout.EAST, returnPanel, padding,
                SpringLayout.EAST, sourceNameArea
        );
        panelLayout.putConstraint(
                SpringLayout.SOUTH, returnPanel, padding,
                SpringLayout.SOUTH, sourceNameArea
        );

        return returnPanel;
    }

    private JPanel createUrlPanel() {
        JPanel returnPanel = new JPanel();
        SpringLayout panelLayout = new SpringLayout();
        returnPanel.setLayout(panelLayout);

        panelLayout.putConstraint(
                SpringLayout.NORTH, sourceUrlLabel, 0,
                SpringLayout.NORTH, returnPanel
        );
        panelLayout.putConstraint(
                SpringLayout.WEST, sourceUrlLabel, padding,
                SpringLayout.WEST, returnPanel
        );
        returnPanel.add(sourceUrlLabel);

        sourceUrlArea.setWrapStyleWord(false);
        sourceUrlArea.setLineWrap(true);
        sourceUrlArea.setMaximumSize(new Dimension());
        sourceUrlArea.setColumns(fieldColumns);
        sourceUrlArea.setRows(1);
        sourceUrlArea.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        sourceUrlArea.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_TAB) {
                    e.consume();
                    sentenceArea.requestFocus();
                    mainWindow.getContentScroll().getViewport().scrollRectToVisible(sentencePanel.getBounds());
                }
            }
            @Override
            public void keyReleased(KeyEvent e) {
                sourceUrlArea.repaint();
                sourceUrlArea.revalidate();

                EventQueue.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        contentPanel.repaint();
                        contentPanel.revalidate();
                    }
                });
            }
        });

        panelLayout.putConstraint(
                SpringLayout.WEST, sourceUrlArea, padding,
                SpringLayout.WEST, returnPanel
        );
        panelLayout.putConstraint(
                SpringLayout.NORTH, sourceUrlArea, padding,
                SpringLayout.SOUTH, sourceUrlLabel
        );
        returnPanel.add(sourceUrlArea);

        //Constraints for EAST and SOUTH of returnPanel
        panelLayout.putConstraint(
                SpringLayout.EAST, returnPanel, padding,
                SpringLayout.EAST, sourceUrlArea
        );
        panelLayout.putConstraint(
                SpringLayout.SOUTH, returnPanel, padding,
                SpringLayout.SOUTH, sourceUrlArea
        );

        return returnPanel;
    }

    private JPanel createSentencePanel() {
        JPanel returnPanel = new JPanel();
        SpringLayout panelLayout = new SpringLayout();
        returnPanel.setLayout(panelLayout);

        panelLayout.putConstraint(
                SpringLayout.NORTH, sentenceLabel, 0,
                SpringLayout.NORTH, returnPanel
        );
        panelLayout.putConstraint(
                SpringLayout.WEST, sentenceLabel, padding,
                SpringLayout.WEST, returnPanel
        );
        returnPanel.add(sentenceLabel);

        sentenceArea.setWrapStyleWord(false);
        sentenceArea.setLineWrap(true);
        sentenceArea.setMaximumSize(new Dimension());
        sentenceArea.setColumns(fieldColumns);
        sentenceArea.setRows(7);
        sentenceArea.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        sentenceArea.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_TAB) {
                    e.consume();
                    imageArea.requestFocus();
                    mainWindow.getContentScroll().getViewport().scrollRectToVisible(imagePanel.getBounds());
                }
            }
            @Override
            public void keyReleased(KeyEvent e) {
                sentenceArea.repaint();
                sentenceArea.revalidate();

                EventQueue.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        contentPanel.repaint();
                        contentPanel.revalidate();
                    }
                });
            }
        });

        panelLayout.putConstraint(
                SpringLayout.WEST, sentenceArea, padding,
                SpringLayout.WEST, returnPanel
        );
        panelLayout.putConstraint(
                SpringLayout.NORTH, sentenceArea, padding,
                SpringLayout.SOUTH, sentenceLabel
        );
        returnPanel.add(sentenceArea);

        sentenceRequiredLabel.setForeground(new Color(214, 67, 56));
        sentenceRequiredLabel.setVisible(false);

        //Constraints for EAST and SOUTH of returnPanel
        panelLayout.putConstraint(
                SpringLayout.EAST, returnPanel, padding,
                SpringLayout.EAST, sentenceArea
        );
        panelLayout.putConstraint(
                SpringLayout.SOUTH, returnPanel, padding,
                SpringLayout.SOUTH, sentenceArea
        );

        return returnPanel;
    }

    private JPanel createImagePanel() {
        JPanel returnPanel = new JPanel();
        SpringLayout panelLayout = new SpringLayout();
        returnPanel.setLayout(panelLayout);

        panelLayout.putConstraint(
                SpringLayout.WEST, imageLabel, padding,
                SpringLayout.WEST, returnPanel
        );
        panelLayout.putConstraint(
                SpringLayout.NORTH, imageLabel, padding,
                SpringLayout.NORTH, returnPanel
        );
        returnPanel.add(imageLabel);

        panelLayout.putConstraint(
                SpringLayout.WEST, nsfwCheck, padding,
                SpringLayout.EAST, imageLabel
        );
        panelLayout.putConstraint(
                SpringLayout.VERTICAL_CENTER, nsfwCheck, 0,
                SpringLayout.VERTICAL_CENTER, imageLabel
        );
        returnPanel.add(nsfwCheck);

        panelLayout.putConstraint(
                SpringLayout.WEST, setCapture, padding,
                SpringLayout.EAST, nsfwCheck
        );
        panelLayout.putConstraint(
                SpringLayout.VERTICAL_CENTER, setCapture, 0,
                SpringLayout.VERTICAL_CENTER, nsfwCheck
        );
        returnPanel.add(setCapture);

        imageArea.setWrapStyleWord(false);
        imageArea.setLineWrap(true);
        imageArea.setMaximumSize(new Dimension());
        imageArea.setColumns(fieldColumns);
        imageArea.setRows(1);
        imageArea.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        imageArea.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_TAB) {
                    e.consume();
                    addButton.requestFocus();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                imageArea.repaint();
                imageArea.revalidate();

                EventQueue.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        contentPanel.repaint();
                        contentPanel.revalidate();
                    }
                });
            }
        });

        panelLayout.putConstraint(
                SpringLayout.WEST, imageArea, padding,
                SpringLayout.WEST, returnPanel
        );
        panelLayout.putConstraint(
                SpringLayout.NORTH, imageArea, padding,
                SpringLayout.SOUTH, imageLabel
        );
        returnPanel.add(imageArea);

        panelLayout.putConstraint(
                SpringLayout.HORIZONTAL_CENTER, addButton, 0,
                SpringLayout.HORIZONTAL_CENTER, returnPanel
        );
        panelLayout.putConstraint(
                SpringLayout.NORTH, addButton, padding,
                SpringLayout.SOUTH, imageArea
        );
        returnPanel.add(addButton);

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addSentence();
            }
        });
        addButton.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER) {
                    e.consume();
                    addSentence();
                }
            }
        });

        //Constraints for EAST and SOUTH of returnPanel
        panelLayout.putConstraint(
                SpringLayout.EAST, returnPanel, padding,
                SpringLayout.EAST, imageArea
        );
        panelLayout.putConstraint(
                SpringLayout.SOUTH, returnPanel, 0,
                SpringLayout.SOUTH, addButton
        );

        return returnPanel;
    }

    //When adding component after window initialization, resizes main window
    private void addComponentResize(Component targetComponent) {
        Dimension minimumSize = mainWindow.getMainFrame().getMinimumSize();
        //prevents window from shrinking to minimum size after frame pack, if user has adjusted window size
        mainWindow.getMainFrame().setMinimumSize(mainWindow.getMainFrame().getSize());
        mainWindow.packFrame();
        minimumSize.setSize(
                minimumSize.getWidth(),
                minimumSize.getHeight() + targetComponent.getHeight());
        mainWindow.getMainFrame().setMinimumSize(minimumSize);
    }

    //When removing component after window initialization, resizes main window
    private void removeComponentResize(Component targetComponent) {
        Dimension minimumSize = mainWindow.getMainFrame().getMinimumSize();
        //prevents window from shrinking to minimum size after frame pack, if user has adjusted window size
        mainWindow.getMainFrame().setMinimumSize(mainWindow.getMainFrame().getSize());
        mainWindow.packFrame();
        minimumSize.setSize(
                minimumSize.getWidth(),
                minimumSize.getHeight() - targetComponent.getHeight());
        mainWindow.getMainFrame().setMinimumSize(minimumSize);
        if(mainWindow.getMainFrame().getSize().getHeight() <=
                minimumSize.getHeight() + targetComponent.getHeight() + 20) {
            mainWindow.getMainFrame().setSize(
                    (int)mainWindow.getMainFrame().getSize().getWidth(),
                    (int)minimumSize.getHeight());
        }
    }

    //Adds sentence to database, Sentence field must have some content or error is thrown
    private void addSentence() {
        if(!sentenceArea.getText().equals("")) {
            try {
                sentenceControl.addSentence();
                if(!currentLinkStatusLabel.getText().equals("No Link")) {
                    currentLinkStatusLabel.setText(sentenceArea.getText());
                }
                if(sentenceRequiredLabel.isVisible()) {
                    SpringLayout sentenceLayout = (SpringLayout) sentencePanel.getLayout();
                    sentencePanel.remove(sentenceRequiredLabel);
                    sentenceLayout.putConstraint(
                            SpringLayout.SOUTH, sentencePanel, padding,
                            SpringLayout.SOUTH, sentenceArea
                    );
                    removeComponentResize(sentenceRequiredLabel);
                    sentenceRequiredLabel.setVisible(false);
                }
                sentenceArea.setText("");
                imageArea.setText("");
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }
        else {
            if(!sentenceRequiredLabel.isVisible()) {
                SpringLayout sentenceLayout = (SpringLayout) sentencePanel.getLayout();
                sentenceLayout.putConstraint(
                        SpringLayout.WEST, sentenceRequiredLabel, padding,
                        SpringLayout.WEST, sentencePanel
                );
                sentenceLayout.putConstraint(
                        SpringLayout.NORTH, sentenceRequiredLabel, padding,
                        SpringLayout.SOUTH, sentenceArea
                );
                sentenceLayout.putConstraint(
                        SpringLayout.SOUTH, sentencePanel, 0,
                        SpringLayout.SOUTH, sentenceRequiredLabel
                );
                sentencePanel.add(sentenceRequiredLabel);
                addComponentResize(sentenceRequiredLabel);
                sentenceRequiredLabel.setVisible(true);
            }
        }
    }

    private void checkFonts() {
        String[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();

        for(String font : fonts) {
            System.out.println(font);
        }
    }
}
