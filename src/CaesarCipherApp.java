import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class CaesarCipherApp {

    private JFrame frame;
    private JTextArea inputArea;
    private JTextArea outputArea;

    private final int SHIFT = 3;

    public CaesarCipherApp() {
        frame = new JFrame("Caesar Cipher App");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600,400);
        frame.setLayout(new BorderLayout());

        // Panel creation
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(10,10,10,10);
        panel.setBackground(Color.GRAY);
        Border etched = BorderFactory.createEtchedBorder();
        panel.setBorder(etched);

        //Button Class intended to make UI more customizable and aesthetic.
        class RoundedButton extends JButton {
            public RoundedButton(String label) {
                super(label);
                setOpaque(false); // Make the button non-opaque
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
        encryptButton.setPreferredSize(new Dimension(100,50));
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.weightx = 0.5;
        c.weighty = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.CENTER;
        c.ipadx = 175;
        c.ipady = 50;
        panel.add(encryptButton, c);

        //Decrypt Button Creation and Style
        RoundedButton decryptButton = new RoundedButton("Decrypt");
        decryptButton.setFont(new Font("Consolas", Font.BOLD, 20));
        decryptButton.setBackground(new Color(120,0,0));
        decryptButton.setForeground(Color.WHITE);
        decryptButton.setPreferredSize(new Dimension(100,50));
        c.gridx = 1;
        c.gridy = 1;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.weightx = 1.0;
        c.weighty = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.CENTER;
        c.ipadx = 175;
        c.ipady = 50;
        panel.add(decryptButton, c);

        //copyOutput Button Creation and Style
        RoundedButton copyButton = new RoundedButton("Copy Output");
        copyButton.setFont(new Font("Consolas", Font.BOLD, 20));
        copyButton.setBackground(Color.ORANGE);
        copyButton.setForeground(Color.BLACK);
        copyButton.setPreferredSize(new Dimension(100,50));
        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 2;
        c.weightx = 1.0;
        c.weighty = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.CENTER;
        c.ipadx = 0;
        c.ipady = 10;
        panel.add(copyButton, c);

        encryptButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String input = inputArea.getText();
                String encryptedMessage = CaesarCipher.encrypt(input, SHIFT);
                outputArea.setText(encryptedMessage);
            }
        });

        decryptButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String input = inputArea.getText();
                String decryptedMessage = CaesarCipher.decrypt(input, SHIFT);
                outputArea.setText(decryptedMessage);
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

        frame.add(panel, BorderLayout.CENTER);

        // Wrap outputArea in a JPanel with BoxLayout to manage its size
        JPanel outputPanel = new JPanel(new BorderLayout());
        outputPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));


        //outputArea Creation and Style
        Border line = BorderFactory.createLineBorder(Color.black, 2);
        Border margin = BorderFactory.createEmptyBorder(10,10,10,10);
        TitledBorder titledBorder = BorderFactory.createTitledBorder(line, "OUTPUT", TitledBorder.LEFT, TitledBorder.TOP);
        Border compoundBorder = BorderFactory.createCompoundBorder(titledBorder, margin);

        outputArea = new JTextArea();
        outputArea.setFont(new Font("Consolas", Font.PLAIN, 20));
        outputArea.setLineWrap(true);
        outputArea.setWrapStyleWord(true);
        outputArea.setBorder(compoundBorder);

        JScrollPane outputScrollPane = new JScrollPane(outputArea);
        outputScrollPane.setPreferredSize(new Dimension(400,100));

        outputPanel.add(outputScrollPane, BorderLayout.CENTER);
        frame.add(outputPanel, BorderLayout.SOUTH);

    }

    public void display() {
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new CaesarCipherApp().display();
        });
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
