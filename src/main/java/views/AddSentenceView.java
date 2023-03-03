package views;


import window_object.WindowObject;
import javax.swing.*;
import java.awt.*;

public class AddSentenceView {
    private final WindowObject mainWindow;
    private final Container container;
    private final SpringLayout layout;

    public AddSentenceView(WindowObject mainWindow) {
        this.mainWindow = mainWindow;
        this.container = this.mainWindow.getContentContainer();
        this.layout = this.mainWindow.getLayout();

        createView();
    }

    private void createView() {
        //Window title
        JLabel header = new JLabel("Input Sentences: ");
        layout.putConstraint(
                SpringLayout.WEST, header, 0,
                SpringLayout.WEST, container
        );
        layout.putConstraint(
                SpringLayout.NORTH, header, 0,
                SpringLayout.NORTH, container
        );
        container.add(header);
        //TODO: Source type drop down
        
    }
}
