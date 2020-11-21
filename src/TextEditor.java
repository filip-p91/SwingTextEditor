import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class TextEditor extends JFrame {

    private JMenu menuFile;
    private JMenu menuSearch;
    private JPanel panel;
    private JTextField searchField;
    private JScrollPane scrollPane;
    private JTextArea textArea;
    private JButton saveButton;
    private JButton openButton;
    private JButton startSearchButton;
    private JButton previousMatchButton;
    private JButton nextMatchButton;

    private JFileChooser fileChooser;

    private List<Integer> indexFound;
    private List<Integer> lengthFound;

    private int counter = 0;
    private int nextCounter = 0;

    public TextEditor() {

        super("Text Editor");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        Menu();
        UI();
        setVisible(true);
    }

    private void Menu() {
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        menuFile = new JMenu("File");
        menuFile.setMnemonic(KeyEvent.VK_F);
        menuBar.add(menuFile);

        JMenuItem menuOpen = new JMenuItem("Open");
        menuFile.add(menuOpen);

        JMenuItem menuSave = new JMenuItem("Save");
        menuFile.add(menuSave);

        menuFile.addSeparator();

        JMenuItem menuExit = new JMenuItem("Exit");
        menuFile.add(menuExit);

        menuSearch = new JMenu("Search");
        menuSearch.setMnemonic(KeyEvent.VK_S);
        menuBar.add(menuSearch);

        JMenuItem menuStartSearch = new JMenuItem("Start search");
        menuSearch.add(menuStartSearch);

        JMenuItem menuPreviousMatch = new JMenuItem("Previous match");
        menuSearch.add(menuPreviousMatch);

        JMenuItem menuNextMatch = new JMenuItem("Next match");
        menuSearch.add(menuNextMatch);

        menuOpen.addActionListener(event -> openFile());
        menuSave.addActionListener(event -> saveFile());
        menuExit.addActionListener(event -> dispose());

        menuStartSearch.addActionListener(event -> SearchEngine());
        menuPreviousMatch.addActionListener(event -> PreviousSearch());
        menuNextMatch.addActionListener(event -> NextSearch());

    }

    private void UI() {

        searchField = new JTextField();
        searchField.setColumns(30);

        ImageIcon saveIcon = createImageIcon("Icons/Save.png");
        saveButton = new JButton(saveIcon);

        ImageIcon openIcon = createImageIcon("Icons/Open.png");
        openButton = new JButton(openIcon);

        ImageIcon startSearchIcon = createImageIcon("Icons/Search.png");
        startSearchButton = new JButton(startSearchIcon);

        ImageIcon previousMatchIcon = createImageIcon("Icons/Previous.png");
        previousMatchButton = new JButton(previousMatchIcon);

        ImageIcon nextMatchIcon = createImageIcon("Icons/Next.png");
        nextMatchButton = new JButton(nextMatchIcon);

        panel = new JPanel();
        panel.setLayout(new FlowLayout());
        panel.setBackground(Color.lightGray);
        add(panel, BorderLayout.NORTH);

        panel.add(openButton);
        panel.add(saveButton);
        panel.add(searchField);
        panel.add(startSearchButton);
        panel.add(previousMatchButton);
        panel.add(nextMatchButton);

        openButton.addActionListener(event -> openFile());
        saveButton.addActionListener(event -> saveFile());
        startSearchButton.addActionListener(actionEvent -> SearchEngine());
        previousMatchButton.addActionListener(actionEvent -> PreviousSearch());
        nextMatchButton.addActionListener(actionEvent -> NextSearch());

        fileChooser = new JFileChooser();
        this.add(fileChooser, BorderLayout.CENTER);

        textArea = new JTextArea();

        scrollPane = new JScrollPane(textArea);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void openFile() {
        if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            File chooseFile = fileChooser.getSelectedFile();
            textArea.setText("");
            try {
                textArea.setText(new String(Files.readAllBytes(chooseFile.toPath())));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private void saveFile() {
        if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
            File chooseFile = fileChooser.getSelectedFile();
            try {
                FileWriter writer = new FileWriter(chooseFile);
                writer.write(textArea.getText());
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private ImageIcon createImageIcon(String path) {
        ImageIcon icon = new ImageIcon(path);
        Image newIcon = icon.getImage().getScaledInstance(10, 10, Image.SCALE_SMOOTH);
        return new ImageIcon(newIcon);
    }

    private void SearchEngine() {
        indexFound = new ArrayList<>();
        lengthFound = new ArrayList<>();
        String findText = searchField.getText();
        String allText = textArea.getText();
        int index = -1;
        int lengthFind = findText.length();

        while (true) {
            index = allText.indexOf(findText, index + 1);
            if (index == -1) {
                break;
            }
            indexFound.add(index);
            lengthFound.add(lengthFind);
        }

        counter = indexFound.size();
        nextCounter = 0;

        if (counter > 0) {
            textArea.setCaretPosition(indexFound.get(0) + lengthFound.get(0));
            textArea.select(indexFound.get(0), indexFound.get(0) + lengthFound.get(0));
            textArea.grabFocus();
        }
    }

    private void NextSearch() {
        if (counter > 0) {
            if (counter - 1 > nextCounter) {
                nextCounter++;
            } else {
                nextCounter = 0;
            }
            textArea.setCaretPosition(indexFound.get(nextCounter) + lengthFound.get(nextCounter));
            textArea.select(indexFound.get(nextCounter), indexFound.get(nextCounter) + lengthFound.get(nextCounter));
            textArea.requestFocusInWindow();
        }
    }

    private void PreviousSearch() {
        if (counter > 0) {
            if (nextCounter != 0) {
                nextCounter--;
            } else {
                nextCounter = counter - 1;
            }
            textArea.setCaretPosition(indexFound.get(nextCounter) + lengthFound.get(nextCounter));
            textArea.select(indexFound.get(nextCounter), indexFound.get(nextCounter) + lengthFound.get(nextCounter));
            textArea.requestFocusInWindow();
        }
    }
}