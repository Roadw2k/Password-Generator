import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.SecureRandom;

public class PasswordGenerator extends JFrame {
    private JTextField passwordField;
    private JCheckBox specialCharsCheckbox;
    private JCheckBox numbersCheckbox;
    private JCheckBox lowerCaseCheckbox;
    private JCheckBox upperCaseCheckbox;
    private JSlider lengthSlider;
    private JLabel lengthLabel;

    public PasswordGenerator() {
        setTitle("Password Generator");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Set the background color to light blue
        getContentPane().setBackground(new Color(217, 238, 255));

        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setOpaque(false); // Make the panel transparent so background color shows

        // Length slider
        lengthSlider = new JSlider(8, 64, 8);
        lengthLabel = new JLabel("Password Length: " + lengthSlider.getValue());

        lengthSlider.addChangeListener(e -> {
            int value = lengthSlider.getValue();
            lengthLabel.setText("Password Length: " + value);
        });

        mainPanel.add(lengthLabel);
        mainPanel.add(lengthSlider);

        // Checkboxes
        specialCharsCheckbox = new JCheckBox("Include Special Characters");
        numbersCheckbox = new JCheckBox("Include Numbers");
        lowerCaseCheckbox = new JCheckBox("Include Lowercase Letters");
        upperCaseCheckbox = new JCheckBox("Include Uppercase Letters");

        mainPanel.add(specialCharsCheckbox);
        mainPanel.add(numbersCheckbox);
        mainPanel.add(lowerCaseCheckbox);
        mainPanel.add(upperCaseCheckbox);

        // Password field
        passwordField = new JTextField(20);
        passwordField.setEditable(false);
        mainPanel.add(new JLabel("Generated Password:"));
        mainPanel.add(passwordField);

        // Buttons panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false); // Make the panel transparent so background color shows

        JButton generateButton = new JButton("Generate");
        JButton copyButton = new JButton("Copy");

        generateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generatePassword();
            }
        });

        copyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                copyToClipboard();
            }
        });

        buttonPanel.add(generateButton);
        buttonPanel.add(copyButton);

        add(mainPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void generatePassword() {
        int length = lengthSlider.getValue();

        StringBuilder password = new StringBuilder(length);
        SecureRandom random = new SecureRandom();

        // Define character pools
        String specialChars = "!@#$%^&*()-_=+[]{}|;:',.<>?/`~";
        String numbers = "0123456789";
        String lowerCase = "abcdefghijklmnopqrstuvwxyz";
        String upperCase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

        // Build the character pool based on selected options
        StringBuilder charPool = new StringBuilder();
        if (specialCharsCheckbox.isSelected()) {
            charPool.append(specialChars);
        }
        if (numbersCheckbox.isSelected()) {
            charPool.append(numbers);
        }
        if (lowerCaseCheckbox.isSelected()) {
            charPool.append(lowerCase);
        }
        if (upperCaseCheckbox.isSelected()) {
            charPool.append(upperCase);
        }

        // Ensure the character pool is not empty
        if (charPool.length() == 0) {
            JOptionPane.showMessageDialog(this, "Please select at least one character type.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(charPool.length());
            password.append(charPool.charAt(randomIndex));
        }

        // Ensure the generated password contains at least one character from each selected type
        if (!validatePassword(password.toString(), charPool.toString())) {
            generatePassword();  // Regenerate until valid
            return;
        }

        passwordField.setText(password.toString());
    }

    private boolean validatePassword(String password, String charPool) {
        for (char c : password.toCharArray()) {
            if (!charPool.contains(String.valueOf(c))) {
                return false;
            }
        }
        return true;
    }

    private void copyToClipboard() {
        StringSelection selection = new StringSelection(passwordField.getText());
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(selection, null);
        JOptionPane.showMessageDialog(this, "Password copied to clipboard!", "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new PasswordGenerator().setVisible(true);
            }
        });
    }
}