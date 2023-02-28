package views;

import window_object.WindowObject;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainView {
    private final WindowObject mainWindow;
    private final Container container;
    private final SpringLayout layout;
    private JLabel copyLabel;
    private Clipboard clip;

    public MainView(WindowObject mainWindow) {
        this.mainWindow = mainWindow;
        this.container = this.mainWindow.getContentContainer();
        this.layout = this.mainWindow.getLayout();

        createView();
        listenClipForLabel();
    }

    //Listens to the clipboard for Strings, sets copyLabel to value of clipboard Strings
    public void listenClipForLabel() {
        clip = Toolkit.getDefaultToolkit().getSystemClipboard();
        //On flavorsChanged, the event only is called if the DataFlavor type is changed (i.e. from string to img)
        Toolkit.getDefaultToolkit().getSystemClipboard().addFlavorListener(new FlavorListener() {
            @Override
            public void flavorsChanged(FlavorEvent e) {
                try {
                    Transferable data = clip.getContents(null);
                    if(data.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                        copyLabel.setText(data.getTransferData(DataFlavor.stringFlavor).toString());
                        clip.setContents(data, null);
                    }
                } catch (Exception error) {
                    System.out.println(error);
                }
            }
        });
    }

    private void createView() {
        JLabel welcome = new JLabel("Plus(+) One(1)");
        layout.putConstraint(
                SpringLayout.HORIZONTAL_CENTER, welcome, 0,
                SpringLayout.HORIZONTAL_CENTER, container
        );
        layout.putConstraint(
                SpringLayout.NORTH, welcome, 0,
                SpringLayout.NORTH, container
        );
        container.add(welcome);

        copyLabel = new JLabel("testing");
        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER , copyLabel, 0,
                SpringLayout.HORIZONTAL_CENTER, container);
        layout.putConstraint(SpringLayout.NORTH, copyLabel, 20,
                SpringLayout.SOUTH, welcome);
        container.add(copyLabel);

        JButton testingButton = new JButton("Function Test");
        testingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                testingFunction();
            }
        });
        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, testingButton, 0,
                SpringLayout.HORIZONTAL_CENTER, container);
        layout.putConstraint(SpringLayout.NORTH, testingButton, 100,
                SpringLayout.SOUTH, copyLabel);
        container.add(testingButton);

    }

    private void testingFunction() {
        copyLabel.setText("Label Reset");
    }


    public void GrabClipboard() {
        Clipboard clippy = Toolkit.getDefaultToolkit().getSystemClipboard();

    }
}
