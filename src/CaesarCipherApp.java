import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.io.File;
import java.io.IOException;
import javax.sound.sampled.*;


public class CaesarCipherApp {

    private JFrame frame;
    private JTextArea inputArea;
    private JTextArea outputArea;
    private JLabel quoteLabel;
    private Timer borderFadeTimer;
    private float borderOpacity = 0.0f;
    private boolean fadingIn = true;
    private String clickAudio = "C:/Users/Venom/OneDrive/Documents/PROJECTS (Software Engineering)/Caesar Cipher/UI FIles/clickAudioTapFile.wav";


    private final int SHIFT = 3;
    private final Color BORDER_COLOR = Color.GREEN;
    private final int FADE_DURATION = 800;
    private final int FADE_STEPS = 20;

    public CaesarCipherApp() {
        frame = new JFrame("Caesar Cipher");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600,400);
        frame.setLayout(new BorderLayout());

        //Application Logo
        ImageIcon icon = new ImageIcon("C:/Users/Venom/OneDrive/Documents/PROJECTS (Software Engineering)/Caesar Cipher/UI FIles/laurel_wreath.png");
        frame.setIconImage(icon.getImage());

        // Panel creation
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(10,10,10,10);
        panel.setBackground(Color.GRAY);
        Border etched = BorderFactory.createEtchedBorder();
        panel.setBorder(etched);

        //Button Class for UI customization
        class RoundedButton extends JButton {
            public RoundedButton(String label) {
                super(label);
                setOpaque(false);
                setContentAreaFilled(false); // Do not fill the content area
                setBorderPainted(false); // Do not paint the border
                setFocusPainted(false); // Do not paint focus (optional)
            }

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isPressed()) {
                    g2.setColor(getBackground().darker());
                } else if (getModel().isRollover()) {
                    g2.setColor(getBackground().brighter());
                } else {
                    g2.setColor(getBackground());
                }
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                super.paintComponent(g2);
                g2.dispose();
            }
        }

        String dailyQuote = getDailyQuote();
        String encryptedDaily = CaesarCipher.encrypt(dailyQuote, SHIFT);

        //Creating and adding quotes to top of application
        quoteLabel = new JLabel(encryptedDaily, JLabel.CENTER);
        quoteLabel.setFont(new Font("Consolas", Font.ITALIC, 18));
        quoteLabel.setOpaque(true);
        quoteLabel.setBorder(etched);
        quoteLabel.setBackground(new Color(70,130,180));
        frame.add(quoteLabel, BorderLayout.NORTH);

        // Input label creation and style
        JLabel inputLabel = new JLabel("Enter Message: ");
        inputLabel.setFont(new Font("Consolas", Font.BOLD | Font.ITALIC, 25));
        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.NORTH;
        panel.add(inputLabel, c);

        // Input Area Creation and Style
        inputArea = new JTextArea();
        inputArea.setFont(new Font("Consolas", Font.PLAIN, 20));
        inputArea.setLineWrap(true);
        inputArea.setWrapStyleWord(true);
        DefaultCaret caret = (DefaultCaret) inputArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        JScrollPane inputScrollPane = new JScrollPane(inputArea);
        inputScrollPane.setPreferredSize(new Dimension(300,50));
        c.gridx = 1;
        c.gridy = 0;
        c.anchor = GridBagConstraints.CENTER;
        c.fill = GridBagConstraints.HORIZONTAL;
        panel.add(inputScrollPane, c);

        //Encrypt Button Creation and Style
        RoundedButton encryptButton = new RoundedButton("Encrypt");
        encryptButton.setFont(new Font("Consolas", Font.BOLD, 20));
        encryptButton.setBackground(new Color(0,128,0));
        encryptButton.setForeground(Color.BLACK);
        encryptButton.setPreferredSize(new Dimension(150,50));
        c.gridx = 0; //Column 0
        c.gridy = 1; // Row 1
        c.weightx = 0;
        c.weighty = 0;
        c.insets = new Insets(10,10,10,5);
        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.SOUTHWEST;
        panel.add(encryptButton, c);

        //Decrypt Button Creation and Style
        RoundedButton decryptButton = new RoundedButton("Decrypt");
        decryptButton.setFont(new Font("Consolas", Font.BOLD, 20));
        decryptButton.setBackground(new Color(120,0,0));
        decryptButton.setForeground(Color.WHITE);
        decryptButton.setPreferredSize(new Dimension(150,50));
        c.gridx = 1;
        c.gridy = 1;
        c.weightx = 0;
        c.weighty = 0;
        c.insets = new Insets(10,5,10,10);
        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.SOUTHEAST;
        panel.add(decryptButton, c);

        //copyOutput Button Creation and Style
        RoundedButton copyButton = new RoundedButton("Copy Output");
        copyButton.setFont(new Font("Consolas", Font.BOLD, 20));
        copyButton.setBackground(Color.ORANGE);
        copyButton.setForeground(Color.BLACK);
        copyButton.setPreferredSize(new Dimension(150,50));
        c.gridx = 0;
        c.gridy = 2;
        c.weightx = 0;
        c.weighty = 0;
        c.insets = new Insets(10,10,10,10);
        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.CENTER;
        panel.add(copyButton, c);

        //Clear Button Creation and Style
        RoundedButton clearButton = new RoundedButton("Clear Output");
        clearButton.setFont(new Font("Consolas", Font.BOLD, 20));
        clearButton.setBackground(new Color(255,215,0));
        clearButton.setForeground(Color.BLACK);
        clearButton.setPreferredSize(new Dimension(150,50));
        c.gridx = 1;
        c.gridy = 2;
        c.weightx = 0;
        c.weighty = 0;
        c.insets = new Insets(10,5,10,10);
        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.CENTER;
        panel.add(clearButton, c);

        //copyQuote Button Creation and Style
        RoundedButton copyQuoteButton = new RoundedButton("Copy Quote");
        copyQuoteButton.setFont(new Font("Consolas", Font.BOLD, 20));
        copyQuoteButton.setBackground(new Color(32,178,170));
        copyQuoteButton.setForeground(Color.BLACK);
        copyQuoteButton.setPreferredSize(new Dimension(150,50));
        c.gridx = 0;
        c.gridy = 3;
        c.weightx = 0;
        c.weighty = 0;
        c.insets = new Insets(10,10,10,10);
        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.CENTER;
        panel.add(copyQuoteButton, c);


        //START BUTTON FUNCTIONS
        encryptButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String input = inputArea.getText();
                String encryptedMessage = CaesarCipher.encrypt(input, SHIFT);
                outputArea.setText(encryptedMessage);
                triggerBorderFade();
            }
        });

        decryptButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String input = inputArea.getText();
                String decryptedMessage = CaesarCipher.decrypt(input, SHIFT);
                outputArea.setText(decryptedMessage);
                triggerBorderFade();
            }
        });

        copyButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String outputText = outputArea.getText();
                StringSelection stringSelection = new StringSelection(outputText);
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                clipboard.setContents(stringSelection, null);
            }
        });

        clearButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                outputArea.setText("");
            }
        });

        copyQuoteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String encryptedQuote = CaesarCipher.encrypt(getDailyQuote(), SHIFT);
                StringSelection stringSelection = new StringSelection(encryptedQuote);
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                clipboard.setContents(stringSelection, null);
            }
        });
        //END BUTTON FUNCTIONS

        frame.add(panel, BorderLayout.CENTER);

        // Wrap outputArea in a JPanel with BoxLayout to manage its size
        JPanel outputPanel = new JPanel(new BorderLayout());
        outputPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        outputArea = new JTextArea();
        outputArea.setFont(new Font("Consolas", Font.PLAIN, 20));
        outputArea.setLineWrap(true);
        outputArea.setWrapStyleWord(true);

        JScrollPane outputScrollPane = new JScrollPane(outputArea);
        outputScrollPane.setPreferredSize(new Dimension(300,100));

        outputPanel.add(outputScrollPane, BorderLayout.CENTER);
        frame.add(outputPanel, BorderLayout.SOUTH);

        frame.pack();
        frame.repaint();

    }

    private void playSound(String filePath) {
        try {
            File audioFile = new File(filePath);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);

            //Volume Control
            FloatControl volume = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            if (volume != null) {
                float currVolume = volume.getValue();
                float newVolume = currVolume - 10.0f;
                volume.setValue(newVolume);
            }

            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    //Output Highlight
    private void triggerBorderFade() {
        if (borderFadeTimer != null && borderFadeTimer.isRunning()) {
            borderFadeTimer.stop();
        }

        borderOpacity = 0.0f;
        fadingIn = true;
        borderFadeTimer = new Timer(FADE_DURATION / FADE_STEPS, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (fadingIn) {
                    borderOpacity += 1.0f / FADE_STEPS;
                    if (borderOpacity >= 1.0f) {
                        borderOpacity = 1.0f;
                        fadingIn = false;
                    }
                } else {
                    borderOpacity -= 1.0f / FADE_STEPS;
                    if (borderOpacity <= 0.0f) {
                        borderOpacity = 0.0f;
                        borderFadeTimer.stop();
                        return;
                    }
                }
                updateBorderColor();
            }
        });
        borderFadeTimer.start();

        //Play sound
        playSound(clickAudio);
    }

    private void updateBorderColor() {
        Color borderColor = new Color(BORDER_COLOR.getRed(), BORDER_COLOR.getGreen(), BORDER_COLOR.getBlue(), (int) (borderOpacity * 255));
        Border line = BorderFactory.createLineBorder(borderColor, 4);
        Border margin = BorderFactory.createEmptyBorder(10, 10, 10, 10);
        TitledBorder titledBorder = BorderFactory.createTitledBorder(line, "OUTPUT", TitledBorder.LEFT, TitledBorder.TOP);
        Border compoundBorder = BorderFactory.createCompoundBorder(titledBorder, margin);
        outputArea.setBorder(compoundBorder);
    }
    //END Output Highlight

    public void display() {
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new CaesarCipherApp().display();
        });
    }

    //DailyQuotes
    private static final String[] QUOTES = {
            "Be yourself; everyone else is already taken. - Oscar Wilde",
            "Two things are infinite: the universe and human stupidity; and I'm not sure about the universe. - Albert Einstein",
            "In three words I can sum up everything I've learned about life: it goes on. - Robert Frost",
            "To be yourself in a world that is constantly trying to make you something else is the greatest accomplishment. - Ralph Waldo Emerson",
            "The only way to do great work is to love what you do. - Steve Jobs",
            "The best revenge is massive success. - Frank Sinatra",
            "The purpose of our lives is to be happy. - Dalai Lama",
            "Get your facts first, then you can distort them as you please. - Mark Twain",
            "Life is what happens when you're busy making other plans. - John Lennon",
            "I have not failed. I've just found 10,000 ways that won't work. - Thomas Edison",
            "To live is the rarest thing in the world. Most people exist, that is all. - Oscar Wilde",
            "You only live once, but if you do it right, once is enough. - Mae West",
            "The only limit to our realization of tomorrow is our doubts of today. - Franklin D. Roosevelt",
            "In the end, we will remember not the words of our enemies, but the silence of our friends. - Martin Luther King Jr.",
            "I am not a product of my circumstances. I am a product of my decisions. - Stephen R. Covey",
            "It does not do to dwell on dreams and forget to live. - J.K. Rowling",
            "We do not remember days; we remember moments. - Cesare Pavese",
            "The greatest glory in living lies not in never falling, but in rising every time we fall. - Nelson Mandela",
            "Life is either a daring adventure or nothing at all. - Helen Keller",
            "The purpose of life is not to be happy. It is to be useful, to be honorable, to be compassionate, to have it make some difference that you have lived and lived well. - Ralph Waldo Emerson",
            "You miss 100% of the shots you don’t take. - Wayne Gretzky",
            "The unexamined life is not worth living. - Socrates",
            "Happiness is not something ready-made. It comes from your own actions. - Dalai Lama",
            "You can never cross the ocean until you have the courage to lose sight of the shore. - Christopher Columbus",
            "The greatest wealth is to live content with little. - Plato",
            "A person who never made a mistake never tried anything new. - Albert Einstein",
            "The only thing we have to fear is fear itself. - Franklin D. Roosevelt",
            "Life isn’t about finding yourself. Life is about creating yourself. - George Bernard Shaw",
            "You can’t go back and change the beginning, but you can start where you are and change the ending"
    };

    private String getDailyQuote() {
        LocalDate today = LocalDate.now();
        int dayOfYear = today.getDayOfYear();
        int quoteIndex = (dayOfYear - 1) % QUOTES.length;
        return QUOTES[quoteIndex];
    }

}

class CaesarCipher {
    public static String encrypt(String input, int shift) {
        StringBuilder encryptedMessage = new StringBuilder();

        for (int i = 0; i < input.length(); i++) {
            char currentChar = input.charAt(i);

            if (Character.isLetter(currentChar)) {
                char base = Character.isLowerCase(currentChar) ? 'a' : 'A';
                currentChar = (char) ((currentChar - base + shift) % 26 + base);
            }

            encryptedMessage.append(currentChar);
        }
        return encryptedMessage.toString();
    }

    public static String decrypt(String encryptedMessage, int shift) {
        return encrypt(encryptedMessage, 26 - shift);
    }
}
