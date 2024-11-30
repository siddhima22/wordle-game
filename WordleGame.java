import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

public class WordleGame extends JFrame {
    private final HashSet<String> wordSet = new HashSet<>();
    private String targetWord;
    private JTextField[][] letterBoxes;
    private int currentRow = 0;
    private int score = 0;
    private JLabel scoreLabel;
    private JPanel inputPanel;

    public WordleGame() {
        setTitle("Wordle Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 800);
        setLayout(new BorderLayout());
        initializeWords();
        targetWord = getRandomWord();
        JPanel scorePanel = new JPanel();
        scorePanel.setLayout(new BorderLayout());
        scoreLabel = new JLabel("Score: " + score, SwingConstants.CENTER);
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 24));
        scorePanel.add(scoreLabel, BorderLayout.CENTER);
        add(scorePanel, BorderLayout.NORTH);
        JPanel gamePanel = new JPanel();
        gamePanel.setLayout(new GridLayout(6, 5, 5, 5));
        gamePanel.setBackground(Color.BLACK);

        letterBoxes = new JTextField[6][5];
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 5; j++) {
                letterBoxes[i][j] = createLetterBox();
                gamePanel.add(letterBoxes[i][j]);
            }
        }

        add(gamePanel, BorderLayout.CENTER);
        inputPanel = new JPanel();
        inputPanel.setLayout(new FlowLayout(FlowLayout.CENTER)); // Align components horizontally

        JTextField guessField = new JTextField();
        guessField.setFont(new Font("Arial", Font.BOLD, 24));
        guessField.setHorizontalAlignment(JTextField.CENTER);
        guessField.setPreferredSize(new Dimension(200, 40)); // Set a fixed size for the text field
        inputPanel.add(guessField);
        JButton submitButton = new JButton("Submit");
        submitButton.setFont(new Font("Arial", Font.BOLD, 24));
        inputPanel.add(submitButton);

        JButton playAgainButton = new JButton("Play Again");
        playAgainButton.setFont(new Font("Arial", Font.BOLD, 24));
        playAgainButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                restartGame();
            }
        });
        inputPanel.add(playAgainButton);
        add(inputPanel, BorderLayout.SOUTH);
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String guess = guessField.getText().toUpperCase();
                if (guess.length() == 5 && isValidGuess(guess)) {
                    processGuess(guess);
                    guessField.setText("");
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid guess! Enter a 5-letter word.");
                }
            }

            private boolean isValidGuess(String guess) {
                for (int i = 0; i < guess.length(); i++) {
                    char ch = guess.charAt(i);
                    if (ch < 'A' || ch > 'Z') {
                        return false;
                    }
                }
                return true;
            }
        });
    }

    private void initializeWords() {
        wordSet.add("APPLE");
        wordSet.add("HOUSE");
        wordSet.add("PLANE");
        wordSet.add("WATER");
        wordSet.add("EARTH");
        wordSet.add("MONEY");
        wordSet.add("TEACH");
        wordSet.add("BRICK");
        wordSet.add("SHIRT");
        wordSet.add("PLUCK");
    }

    private String getRandomWord() {
        ArrayList<String> words = new ArrayList<>(wordSet);
        Random random = new Random();
        return words.get(random.nextInt(words.size()));
    }

    private JTextField createLetterBox() {
        JTextField box = new JTextField();
        box.setHorizontalAlignment(JTextField.CENTER);
        box.setFont(new Font("Arial", Font.BOLD, 36));
        box.setEditable(false);
        box.setBackground(Color.LIGHT_GRAY);
        return box;
    }

    private void processGuess(String guess) {
        if (currentRow >= 6) {
            JOptionPane.showMessageDialog(null, "Game Over! The word was: " + targetWord);
            return;
        }

        boolean correctGuess = true;
        for (int i = 0; i < 5; i++) {
            char guessChar = guess.charAt(i);
            JTextField box = letterBoxes[currentRow][i];
            box.setText(String.valueOf(guessChar));

            if (guessChar == targetWord.charAt(i)) {
                box.setBackground(Color.GREEN);
            } else if (targetWord.contains(String.valueOf(guessChar))) {
                box.setBackground(Color.YELLOW);
            } else {
                box.setBackground(Color.RED);
                correctGuess = false;
            }
        }

        if (guess.equals(targetWord)) {
            JOptionPane.showMessageDialog(null, "Congratulations! You guessed the word!");
            score += 50;
            updateScore();
            return;
        }

        if (correctGuess) {
            score += 50;
            updateScore();
        }

        currentRow++;

        if (currentRow == 6) {
            JOptionPane.showMessageDialog(null, "Game Over! The word was: " + targetWord);
            updateScore();
        }
    }

    private void updateScore() {
        scoreLabel.setText("Score: " + score);
    }

    private void restartGame() {
        currentRow = 0;
        targetWord = getRandomWord();
        updateScore();
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 5; j++) {
                letterBoxes[i][j].setText("");
                letterBoxes[i][j].setBackground(Color.LIGHT_GRAY);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            WordleGame game = new WordleGame();
            game.setVisible(true);
        });
    }
}
