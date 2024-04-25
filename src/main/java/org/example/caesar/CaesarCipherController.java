package org.example.caesar;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

import java.io.*;

public class CaesarCipherController {
    @FXML
    private TextField inputField;

    @FXML
    private TextField keyField;

    @FXML
    private TextField additionalKeyField;

    @FXML
    private TextField outputField;

    private String permuteAlphabet(String key2) {
        String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder permutedAlphabet = new StringBuilder();

        for (char c : key2.toCharArray()) {
            if (Character.isLetter(c) && !permutedAlphabet.toString().contains(String.valueOf(c).toUpperCase())) {
                permutedAlphabet.append(Character.toUpperCase(c));
            }
        }

        for (char c : ALPHABET.toCharArray()) {
            if (!permutedAlphabet.toString().contains(String.valueOf(c))) {
                permutedAlphabet.append(c);
            }
        }

        return permutedAlphabet.toString();
    }

    private String readFileContent(String filePath) throws IOException {
        StringBuilder content = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                content.append(line).append("\n");
            }
        }
        return content.toString();
    }

    private void updateOutput(String result) {
        outputField.setText(result);
    }

    @FXML
    protected void browseTextFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Text File");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Text files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            String filePath = selectedFile.getAbsolutePath();
            try {
                String fileContent = readFileContent(filePath);
                inputField.setText(fileContent);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    protected void saveToFile() {
        String encryptedText = outputField.getText();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Encrypted Text");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text Files", "*.txt"),
                new FileChooser.ExtensionFilter("All Files", "*.*"));
        File selectedFile = fileChooser.showSaveDialog(null);
        if (selectedFile != null) {
            try (PrintWriter writer = new PrintWriter(selectedFile)) {
                writer.print(encryptedText);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    protected void encryptText() {
        String text = inputField.getText();
        int key = Integer.parseInt(keyField.getText());
        String additionalKey = additionalKeyField.getText();
        String permutedAlphabet = permuteAlphabet(additionalKey);
        String encryptedText = encryptCesar(text, key, permutedAlphabet);
        updateOutput(encryptedText);
    }

    @FXML
    protected void decryptText() {
        String text = inputField.getText();
        int key = Integer.parseInt(keyField.getText());
        String additionalKey = additionalKeyField.getText();
        String permutedAlphabet = permuteAlphabet(additionalKey);
        String decryptedText = encryptCesar(text, -key, permutedAlphabet);
        updateOutput(decryptedText);
    }

    private String encryptCesar(String text, int key, String permutedAlphabet) {
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < text.length(); i++) {
            char ch = text.charAt(i);
            int index = permutedAlphabet.indexOf(Character.toUpperCase(ch));

            if (index != -1) {
                char base = Character.isLowerCase(ch) ? 'a' : 'A';
                int newIndex;
                if (!additionalKeyField.getText().isEmpty()) {
                    newIndex = (index + key) % permutedAlphabet.length();
                } else {
                    newIndex = (index + key + permutedAlphabet.length()) % permutedAlphabet.length();
                }
                char newChar = permutedAlphabet.charAt(newIndex);
                result.append(Character.isLowerCase(ch) ? Character.toLowerCase(newChar) : newChar);
            } else {
                result.append(ch);
            }
        }

        return result.toString();
    }
}
