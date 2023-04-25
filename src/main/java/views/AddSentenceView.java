package views;


import controllers.AddSentenceControl;
import window_object.WindowObject;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class AddSentenceView {
    private final WindowObject mainWindow;
    private final AddSentenceControl sentenceControl;
    private SetLinkView setLinkView = null;
    private final int padding = 20;
    private final int areaColumns = 33;
    private final Font jpFont = new Font("Meiryo", Font.BOLD, 16);
    private final Font uiFont = new Font("Meiryo UI", Font.BOLD, 14);
    private final Font buttonFont = new Font("Verdana", Font.BOLD, 16);
    private final Color hasLinkColor = new Color(  156, 204, 101  );
    private final Color noLinkColor = new Color( 244, 81, 30 );
    private final Color selectedColor = new Color(   33, 150, 243   );
    private BufferedImage imagePaneBuffered = null;
    private ImageIcon imagePaneIcon = null;

    //Window Components
    private JPanel navPanel;
    private JPanel contentPanel;
    private SpringLayout contentPanelLayout;
    private JPanel sourceTypePanel;
    private final JLabel sourceTypeLabel = new JLabel("Source Type: ");
    private final String[] sourcesList = {"Visual Novel", "Manga", "Anime", "Online", "Newspaper", "Magazine"};
    private final JComboBox<String> sourceTypeCombo = new JComboBox<>(sourcesList);
    private final JCheckBox sequentialCheck = new JCheckBox("Is Sequential");
    private JPanel backlinkPanel;
    private final JLabel linkStatusLabel = new JLabel("Back Link: ");
    private final JTextArea currentLinkArea = new JTextArea("No Link");
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
    private final JTextArea imageArea = new JTextArea();
    private final JTextPane imagePane = new JTextPane();
    private final JButton addButton = new JButton("Add");


    public AddSentenceView(WindowObject mainWindow) {
        this.mainWindow = mainWindow;
        sentenceControl = createView();
        mainWindow.getMainFrame().setLocationRelativeTo(null);
        JScrollBar contentVerticalBar = mainWindow.getContentScroll().getVerticalScrollBar();
        contentVerticalBar.addAdjustmentListener(new AdjustmentListener() {
            @Override
            public void adjustmentValueChanged(AdjustmentEvent e) {
                imagePane.repaint();
                imagePane.revalidate();
            }
        });
        /*
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new SetLinkView(mainWindow, sentenceControl);
            }
        });

         */
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
    public JCheckBox getHasUrlCheck() {return hasUrlCheck;}
    public JTextArea getSentenceArea() {
        return sentenceArea;
    }
    public JCheckBox getNsfwCheck() {
        return nsfwCheck;
    }
    public JTextArea getImageArea() {
        return imageArea;
    }
    public JTextArea getCurrentLinkArea() {
        return currentLinkArea;
    }
    public Color getHasLinkColor() {return hasLinkColor;}

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
        Dimension windowSize = new Dimension((int)(sourceNamePanel.getWidth() * 1.5),
                contentPanel.getHeight() + navPanel.getHeight() * 3);
        mainWindow.getMainFrame().setMinimumSize(windowSize);
        mainWindow.getMainFrame().setPreferredSize(windowSize);
        //TODO: change this to be on bottom of nav panel, so that it doesn't disappear when scrolling the content frame
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

    //Create panel that has source type options and gives checkbox for sequential sentence option
    private JPanel createSourceTypePanel() {
        JPanel returnPanel = new JPanel();
        SpringLayout panelLayout = new SpringLayout();
        returnPanel.setLayout(panelLayout);

        sourceTypeLabel.setFont(uiFont);
        sourceTypeCombo.setFont(uiFont);
        sequentialCheck.setFont(uiFont);

        //Prevents the sourceTypeCombo from stretching vertically when window is resized
        sourceTypeCombo.setMaximumSize(new Dimension());
        sourceTypeCombo.setSelectedIndex(0);
        //Either adds or removes backLinkPanel from window, depending on checkbox status
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
                    EventQueue.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            contentPanel.repaint();
                            contentPanel.revalidate();
                        }
                    });

                    mainWindow.getContentScroll().getVerticalScrollBar().setValue(0);
                }
                else {
                    contentPanel.remove(backlinkPanel);
                    currentLinkArea.setText("No Link");
                    currentLinkArea.setForeground(noLinkColor);
                    sentenceControl.removeBacklinkId();

                    contentPanelLayout.putConstraint(
                            SpringLayout.NORTH, sourceNamePanel, 0,
                            SpringLayout.SOUTH, sourceTypePanel
                    );
                    EventQueue.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            contentPanel.repaint();
                            contentPanel.revalidate();
                        }
                    });
                    mainWindow.getContentScroll().getVerticalScrollBar().repaint();
                    mainWindow.getContentScroll().getVerticalScrollBar().revalidate();
                }
            }
        });

        panelLayout.putConstraint(
                SpringLayout.WEST, sourceTypeLabel, padding,
                SpringLayout.WEST, returnPanel
        );
        panelLayout.putConstraint(
                SpringLayout.NORTH, sourceTypeLabel, padding,
                SpringLayout.NORTH, returnPanel
        );
        returnPanel.add(sourceTypeLabel);

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
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                returnPanel.setMinimumSize(new Dimension(returnPanel.getWidth(), returnPanel.getHeight()));
                returnPanel.setPreferredSize(new Dimension(returnPanel.getWidth(), returnPanel.getHeight()));
                returnPanel.repaint();
                returnPanel.revalidate();
            }
        });

        return returnPanel;
    }

    //Creates panel that displays status of link to previous sentence,
    //as well as buttons to set a link to current sentence and setting this sentence as first in sequence
    private JPanel createBacklinkPanel() {
        JPanel returnPanel = new JPanel();
        SpringLayout panelLayout = new SpringLayout();
        returnPanel.setLayout(panelLayout);

        linkStatusLabel.setFont(uiFont);
        currentLinkArea.setFont(jpFont);
        currentLinkArea.setForeground(noLinkColor);
        setLinkButton.setFont(buttonFont);
        viewLinkButton.setFont(buttonFont);
        setHeadButton.setFont(buttonFont);

        currentLinkArea.setEditable(false);
        currentLinkArea.setBackground(null);
        currentLinkArea.setWrapStyleWord(true);
        currentLinkArea.setLineWrap(true);
        currentLinkArea.setMaximumSize(new Dimension());
        currentLinkArea.setColumns(areaColumns);
        currentLinkArea.setRows(1);
        //currentLinkArea DocumentListener is used to refresh the JTextArea after setting a link,
        //larger link sentences break the UI
        currentLinkArea.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                EventQueue.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        currentLinkArea.repaint();
                        currentLinkArea.revalidate();
                    }
                });
            }
            @Override
            public void removeUpdate(DocumentEvent e) {
                //Just need the insert method, but this is required
            }
            @Override
            public void changedUpdate(DocumentEvent e) {
                //Just need the insert method, but this is required
            }
        });

        //Creates a SetLinkView for the user to choose a sentence to link backwards to
        setLinkButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setLinkView = new SetLinkView(mainWindow, sentenceControl);
            }
        });

        //TODO: Create function for viewing the currently set backlink

        //Sets the current sentence as the head for a sequence of sentences
        setHeadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentLinkArea.setText("Head");
                currentLinkArea.setForeground(hasLinkColor);
                sentenceControl.setBackLinkHead();
            }
        });

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
                SpringLayout.WEST, currentLinkArea, padding,
                SpringLayout.WEST, returnPanel
        );
        panelLayout.putConstraint(
                SpringLayout.NORTH, currentLinkArea, padding,
                SpringLayout.SOUTH, linkStatusLabel
        );
        returnPanel.add(currentLinkArea);

        panelLayout.putConstraint(
                SpringLayout.EAST, setLinkButton, -padding,
                SpringLayout.WEST, viewLinkButton
        );
        panelLayout.putConstraint(
                SpringLayout.NORTH, setLinkButton, padding,
                SpringLayout.SOUTH, currentLinkArea
        );
        returnPanel.add(setLinkButton);

        panelLayout.putConstraint(
                SpringLayout.HORIZONTAL_CENTER, viewLinkButton, 0,
                SpringLayout.HORIZONTAL_CENTER, returnPanel
        );
        panelLayout.putConstraint(
                SpringLayout.VERTICAL_CENTER, viewLinkButton, 0,
                SpringLayout.VERTICAL_CENTER, setLinkButton
        );
        returnPanel.add(viewLinkButton);

        panelLayout.putConstraint(
                SpringLayout.WEST, setHeadButton, padding,
                SpringLayout.EAST, viewLinkButton
        );
        panelLayout.putConstraint(
                SpringLayout.VERTICAL_CENTER, setHeadButton, 0,
                SpringLayout.VERTICAL_CENTER, viewLinkButton
        );
        returnPanel.add(setHeadButton);

        //Constraints for EAST and SOUTH of returnPanel
        panelLayout.putConstraint(
                SpringLayout.EAST, returnPanel, padding,
                SpringLayout.EAST, currentLinkArea
        );
        panelLayout.putConstraint(
                SpringLayout.SOUTH, returnPanel, 0,
                SpringLayout.SOUTH, setLinkButton
        );

        return returnPanel;
    }

    //Creates panel that lets user input name of sentence source as well as a checkbox to mark if a URL is present
    private JPanel createSourceNamePanel() {
        JPanel returnPanel = new JPanel();
        SpringLayout panelLayout = new SpringLayout();
        returnPanel.setLayout(panelLayout);

        sourceNameLabel.setFont(uiFont);
        hasUrlCheck.setFont(uiFont);
        sourceNameArea.setFont(jpFont);
        //Either adds or removes urlPanel from window, depending on checkbox status
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

                    EventQueue.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            contentPanel.repaint();
                            contentPanel.revalidate();
                        }
                    });
                    mainWindow.getContentScroll().getVerticalScrollBar().setValue(0);
                }
                else {
                    sourceUrlArea.setText("");
                    contentPanel.remove(urlPanel);

                    contentPanelLayout.putConstraint(
                            SpringLayout.NORTH, sentencePanel, 0,
                            SpringLayout.SOUTH, sourceNamePanel
                    );
                    EventQueue.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            contentPanel.repaint();
                            contentPanel.revalidate();
                        }
                    });
                    mainWindow.getContentScroll().getVerticalScrollBar().repaint();
                    mainWindow.getContentScroll().getVerticalScrollBar().revalidate();
                }
            }
        });

        sourceNameArea.setWrapStyleWord(false);
        sourceNameArea.setLineWrap(true);
        //setMaximumSize prevents name area from stretching
        sourceNameArea.setMaximumSize(new Dimension());
        sourceNameArea.setColumns(areaColumns);
        sourceNameArea.setRows(1);
        sourceNameArea.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2, true));
        sourceNameArea.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                sourceNameArea.setBorder(BorderFactory.createLineBorder(selectedColor, 2, true));
            }

            @Override
            public void focusLost(FocusEvent e) {
                sourceNameArea.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2, true));
            }
        });

        sourceNameArea.addKeyListener(new KeyAdapter() {
            //Allows user to tab to next input field
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
            //Refreshes window when the JTextArea for sourceNameArea needs to add rows because input is too large for
            //current row count
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
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                returnPanel.setMinimumSize(new Dimension(returnPanel.getWidth(), returnPanel.getHeight()));
                returnPanel.setPreferredSize(new Dimension(returnPanel.getWidth(), returnPanel.getHeight()));
                returnPanel.repaint();
                returnPanel.revalidate();
            }
        });

        return returnPanel;
    }
    //Creates a panel where user can input a URL associated with the sentence
    private JPanel createUrlPanel() {
        JPanel returnPanel = new JPanel();
        SpringLayout panelLayout = new SpringLayout();
        returnPanel.setLayout(panelLayout);

        sourceUrlLabel.setFont(uiFont);
        sourceUrlArea.setFont(jpFont);

        sourceUrlArea.setWrapStyleWord(false);
        sourceUrlArea.setLineWrap(true);
        //setMaximumSize prevents name area from stretching
        sourceUrlArea.setMaximumSize(new Dimension());
        sourceUrlArea.setColumns(areaColumns);
        sourceUrlArea.setRows(1);
        sourceUrlArea.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2, true));
        sourceUrlArea.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                sourceUrlArea.setBorder(BorderFactory.createLineBorder(selectedColor, 2, true));
            }

            @Override
            public void focusLost(FocusEvent e) {
                sourceUrlArea.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2, true));
            }
        });

        sourceUrlArea.addKeyListener(new KeyAdapter() {
            //Allows user to tab to next input field
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_TAB) {
                    e.consume();
                    sentenceArea.requestFocus();
                    mainWindow.getContentScroll().getViewport().scrollRectToVisible(sentencePanel.getBounds());
                }
            }
            //Refreshes window when the JTextArea for sourceUrlArea needs to add rows because input is too large for
            //current row count
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
                SpringLayout.NORTH, sourceUrlLabel, 0,
                SpringLayout.NORTH, returnPanel
        );
        panelLayout.putConstraint(
                SpringLayout.WEST, sourceUrlLabel, padding,
                SpringLayout.WEST, returnPanel
        );
        returnPanel.add(sourceUrlLabel);

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

        sentenceLabel.setFont(uiFont);
        sentenceArea.setFont(jpFont);
        sentenceRequiredLabel.setFont(uiFont);

        sentenceArea.setWrapStyleWord(false);
        sentenceArea.setLineWrap(true);
        //setMaximumSize prevents name area from stretching
        sentenceArea.setMaximumSize(new Dimension());
        sentenceArea.setColumns(areaColumns);
        sentenceArea.setRows(7);
        sentenceArea.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2, true));
        sentenceArea.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                sentenceArea.setBorder(BorderFactory.createLineBorder(selectedColor, 2, true));

            }

            @Override
            public void focusLost(FocusEvent e) {
                sentenceArea.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2, true));
            }
        });

        sentenceArea.addKeyListener(new KeyAdapter() {
            //Allows user to tab to next input field
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_TAB) {
                    e.consume();
                    imagePane.requestFocus();
                    mainWindow.getContentScroll().getViewport().scrollRectToVisible(imagePanel.getBounds());
                }
            }
            //Refreshes window when the JTextArea for sourceUrlArea needs to add rows because input is too large for
            //current row count
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

        sentenceRequiredLabel.setForeground(new Color(214, 67, 56));
        sentenceRequiredLabel.setVisible(false);

        panelLayout.putConstraint(
                SpringLayout.NORTH, sentenceLabel, 0,
                SpringLayout.NORTH, returnPanel
        );
        panelLayout.putConstraint(
                SpringLayout.WEST, sentenceLabel, padding,
                SpringLayout.WEST, returnPanel
        );
        returnPanel.add(sentenceLabel);

        panelLayout.putConstraint(
                SpringLayout.WEST, sentenceArea, padding,
                SpringLayout.WEST, returnPanel
        );
        panelLayout.putConstraint(
                SpringLayout.NORTH, sentenceArea, padding,
                SpringLayout.SOUTH, sentenceLabel
        );
        returnPanel.add(sentenceArea);

        //Constraints for EAST and SOUTH of returnPanel
        panelLayout.putConstraint(
                SpringLayout.EAST, returnPanel, padding,
                SpringLayout.EAST, sentenceArea
        );
        panelLayout.putConstraint(
                SpringLayout.SOUTH, returnPanel, padding,
                SpringLayout.SOUTH, sentenceArea
        );

        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                returnPanel.setMinimumSize(new Dimension(returnPanel.getWidth(), returnPanel.getHeight()));
                returnPanel.setPreferredSize(new Dimension(returnPanel.getWidth(), returnPanel.getHeight()));
                returnPanel.repaint();
                returnPanel.revalidate();
            }
        });

        return returnPanel;
    }

    private JPanel createImagePanel() {
        JPanel returnPanel = new JPanel();
        SpringLayout panelLayout = new SpringLayout();
        returnPanel.setLayout(panelLayout);

        imageLabel.setFont(uiFont);
        nsfwCheck.setFont(uiFont);
        addButton.setFont(buttonFont);
        imagePane.setFont(jpFont);
        //TODO: try using a compound border to add a decorative border and a border with margins to prevent moving on
        //TODO: change of decorative border thickness
        imagePane.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2, true));
        imagePane.setText("");
        imagePane.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                imagePane.setBorder(BorderFactory.createLineBorder( selectedColor, 2, true));
            }

            @Override
            public void focusLost(FocusEvent e) {
                imagePane.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2, true));
            }
        });

        imagePane.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                e.consume();
            }
            @Override
            public void keyPressed(KeyEvent e) {
                e.consume();
                //allows user to tab to add button
                if(e.getKeyCode() == KeyEvent.VK_TAB) {
                    addButton.requestFocus();
                } else if(e.isControlDown() && e.getKeyCode() == KeyEvent.VK_V) {
                    if(processImage()) {
                        imagePane.repaint();
                        imagePane.revalidate();
                        EventQueue.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                contentPanel.repaint();
                                contentPanel.revalidate();
                            }
                        });
                    }
                } else if(e.getKeyCode() == KeyEvent.VK_BACK_SPACE || e.getKeyCode() == KeyEvent.VK_DELETE) {
                    clearImage();
                }
            }
            @Override
            public void keyReleased(KeyEvent e) {
                e.consume();
            }
        });

        //adds current inputs as new sentence
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addSentence();
            }
        });
        //allows user to press enter to addSentence
        addButton.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER) {
                    e.consume();
                    addSentence();
                }
            }
        });

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
                SpringLayout.WEST, imagePane, padding,
                SpringLayout.WEST, returnPanel
        );
        panelLayout.putConstraint(
                SpringLayout.NORTH, imagePane, padding,
                SpringLayout.SOUTH, imageLabel
        );
        returnPanel.add(imagePane);


        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                imagePane.setMinimumSize(new Dimension(sourceNameArea.getWidth(), sourceNameArea.getHeight()));
                imagePane.setPreferredSize(new Dimension(sourceNameArea.getWidth(), sourceNameArea.getHeight()));
                imagePane.setMaximumSize(new Dimension(sourceNameArea.getWidth(), sourceNameArea.getHeight()));
                returnPanel.repaint();
                returnPanel.revalidate();

            }
        });

        panelLayout.putConstraint(
                SpringLayout.HORIZONTAL_CENTER, addButton, 0,
                SpringLayout.HORIZONTAL_CENTER, returnPanel
        );
        panelLayout.putConstraint(
                SpringLayout.NORTH, addButton, padding,
                SpringLayout.SOUTH, imagePane
        );
        returnPanel.add(addButton);

        //Constraints for EAST and SOUTH of returnPanel
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                panelLayout.putConstraint(
                        SpringLayout.EAST, returnPanel, padding,
                        SpringLayout.EAST, imagePane
                );
            }
        });

        panelLayout.putConstraint(
                SpringLayout.SOUTH, returnPanel, 0,
                SpringLayout.SOUTH, addButton
        );

        return returnPanel;
    }



    //Adds sentence to database, Sentence field must have some content or error is thrown
    private void addSentence() {
        if(!sentenceArea.getText().equals("")) {
            try {
                sentenceControl.addSentence();
                if(sentenceControl.hasBacklink()) {
                    currentLinkArea.setText(sentenceArea.getText());
                }
                if(sentenceRequiredLabel.isVisible()) {
                    SpringLayout sentenceLayout = (SpringLayout) sentencePanel.getLayout();
                    sentencePanel.remove(sentenceRequiredLabel);
                    sentenceLayout.putConstraint(
                            SpringLayout.SOUTH, sentencePanel, padding,
                            SpringLayout.SOUTH, sentenceArea
                    );
                    sentenceRequiredLabel.setVisible(false);
                }
                sentenceArea.setText("");
                imagePane.setText("");
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
                sentenceRequiredLabel.setVisible(true);
            }
        }
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                contentPanel.repaint();
                contentPanel.revalidate();
            }
        });
        mainWindow.getContentScroll().getVerticalScrollBar().repaint();
        mainWindow.getContentScroll().getVerticalScrollBar().revalidate();
    }

    //TODO: automate naming, should be the same name as the index? makes it unique
    private boolean processImage() {
        Transferable content = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);
        //Determines the image datatype
        if(content.isDataFlavorSupported(DataFlavor.stringFlavor)) {
            System.out.println("Is string flavor");
            return false;
        }
        if(content.isDataFlavorSupported(DataFlavor.imageFlavor)) {
            System.out.println("Is image flavor");
            try {
                imagePaneBuffered = (BufferedImage) content.getTransferData(DataFlavor.imageFlavor);
                imagePaneIcon = scaleImageToIcon(imagePaneBuffered, imagePane.getWidth());
                imagePane.setText("");
                imagePane.insertIcon(imagePaneIcon);
                imagePane.setMinimumSize(new Dimension(imagePane.getWidth(), imagePaneIcon.getIconHeight()));
                imagePane.setPreferredSize(new Dimension(imagePane.getWidth(), imagePaneIcon.getIconHeight()));
                imagePane.setMaximumSize(new Dimension(imagePane.getWidth(), imagePaneIcon.getIconHeight()));
                contentPanel.repaint();
                contentPanel.revalidate();
                //contentPanel.setMinimumSize(new Dimension(contentPanel.getWidth(), contentPanel.getWidth() + imagePaneIcon.getIconHeight()));
                //contentPanel.setPreferredSize(new Dimension(contentPanel.getWidth(), contentPanel.getWidth() + imagePaneIcon.getIconHeight()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if(content.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
            //TODO: will have to determine extension typ to make sure it's an image
            System.out.println("Is file flavor");
        } else {
            System.out.println("Some other flavor, investigate");
            return false;
        }
        return true;
    }

    //Used to reset the imagePane on user delete or backspace, since all input is consumed on imagePane
    private void clearImage() {
        //TODO: Need to also clear the currently buffered image
        imagePane.setText("");
        imagePane.setMinimumSize(new Dimension(sourceNameArea.getWidth(), sourceNameArea.getHeight()));
        imagePane.setPreferredSize(new Dimension(sourceNameArea.getWidth(), sourceNameArea.getHeight()));
        imagePane.setMaximumSize(new Dimension(sourceNameArea.getWidth(), sourceNameArea.getHeight()));
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                imagePanel.repaint();
                imagePanel.revalidate();
            }
        });
    }

    //Reduces a BufferedImage as a ImageIcon at a desired width while maintaining the images original aspect ratio
    //Useful for panels where the max width is based on another component
    private ImageIcon scaleImageToIcon(BufferedImage image, int desiredWidth) {
        if(image.getWidth() < desiredWidth) {
            return new ImageIcon(image);
        }
        double reductionMultiplier = (double) desiredWidth / (double)image.getWidth();
        int reducedWidth = (int)(image.getWidth() * reductionMultiplier);
        int reducedHeight = (int)(image.getHeight() * reductionMultiplier);
        return new ImageIcon(image.getScaledInstance(reducedWidth, reducedHeight, Image.SCALE_SMOOTH));
    }

    private void saveImageToPng(String filePath,String fileName, BufferedImage targetImage) {
        String fullPath = filePath + fileName + ".png";
        File outputFile = new File(fullPath);
        try{
            ImageIO.write(targetImage, "png", outputFile);
            System.out.println("Image successfully saved to: " + outputFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void checkFonts() {
        String[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();

        for(String font : fonts) {
            System.out.println(font);
        }
    }
}
