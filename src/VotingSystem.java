import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class VotingSystem {
    // Database connection details
    private static final String DB_URL = "jdbc:mysql://localhost:3306/voting_system";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "anand";

    public static void main(String[] args) {
        // Create the frame
        JFrame frame = new JFrame("Anand's Voting System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 400); // Set size of the frame
        frame.setLayout(new GridLayout(6, 1, 10, 10)); // 6 rows, 1 column with spacing

        // Set background color to dark gray
        frame.getContentPane().setBackground(new Color(30, 30, 30)); // Dark gray background

        // Add centered title at the top
        JLabel titleLabel = new JLabel("Anand's Voting System", JLabel.CENTER);
        titleLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
        titleLabel.setForeground(new Color(255, 255, 255)); // White text for contrast
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Padding around title

        // Add components with unique font and style
        JLabel nameLabel = new JLabel("📝 Enter Your Name:");
        nameLabel.setFont(new Font("Noto Color Emoji", Font.PLAIN, 16));  // Use emoji-compatible font
        nameLabel.setForeground(new Color(255, 255, 255)); // White color for text

        JTextField nameField = new JTextField();
        nameField.setFont(new Font("Arial", Font.PLAIN, 14));

        JLabel voteLabel = new JLabel("✔️ Cast Your Vote:");
        voteLabel.setFont(new Font("Noto Color Emoji", Font.PLAIN, 16)); // Use emoji-compatible font
        voteLabel.setForeground(new Color(255, 255, 255)); // White color for text

        JPanel votePanel = new JPanel(new FlowLayout(FlowLayout.CENTER)); // Panel for Yes/No buttons
        JRadioButton yesButton = new JRadioButton("✅ Yes");
        yesButton.setFont(new Font("Noto Color Emoji", Font.PLAIN, 16)); // Use emoji-compatible font
        yesButton.setForeground(new Color(0, 128, 0)); // Dark green color for better visibility
        yesButton.setBackground(new Color(230, 255, 230)); // Light green background

        JRadioButton noButton = new JRadioButton("❌ No");
        noButton.setFont(new Font("Noto Color Emoji", Font.PLAIN, 16)); // Use emoji-compatible font
        noButton.setForeground(new Color(204, 0, 0)); // Dark red color for better visibility
        noButton.setBackground(new Color(255, 230, 230)); // Light red background

        ButtonGroup group = new ButtonGroup();
        group.add(yesButton);
        group.add(noButton);
        votePanel.add(yesButton);
        votePanel.add(noButton);

        // Use an image instead of an emoji for the submit button
        ImageIcon submitIcon = new ImageIcon("path/to/target_emoji_image.png"); // Make sure to replace this with the correct path
        JButton submitButton = new JButton("Submit Your Vote", submitIcon); // Using image icon as the button icon
        submitButton.setBackground(new Color(75, 0, 130)); // Indigo color
        submitButton.setForeground(Color.WHITE);
        submitButton.setFont(new Font("Segoe UI", Font.BOLD, 18)); // Use Segoe UI for better emoji support

        // Add components to the frame
        frame.add(titleLabel); // Add the title label first for proper layout
        frame.add(nameLabel);
        frame.add(nameField);
        frame.add(voteLabel);
        frame.add(votePanel);
        frame.add(submitButton);

        // Button action listener
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText().trim();
                String response = yesButton.isSelected() ? "Yes" : noButton.isSelected() ? "No" : "";

                if (name.isEmpty() || response.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "⚠️ Please fill all fields.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Save to database
                saveVote(name, response);

                // Clear fields after submission
                nameField.setText("");
                group.clearSelection(); // Clears the selected radio button

                // Show success message
                JOptionPane.showMessageDialog(frame, "🎉 Vote submitted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        frame.setVisible(true);
    }

    // Method to save vote to database
    private static void saveVote(String name, String response) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "INSERT INTO Votes (name, response) VALUES (?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, name);
                statement.setString(2, response);
                int rows = statement.executeUpdate();
                if (rows > 0) {
                    // The success message is already shown after this in the action listener
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "⚠️ Error: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
