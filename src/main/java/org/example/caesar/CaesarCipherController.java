package org.example.caesar;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;

public class CaesarCipherController {
    @FXML
    private TextField inputField;

    @FXML
    private TextField keyField;

    @FXML
    private TextArea outputField;

    @FXML
    protected void browseInputFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Input Text File");
        Stage stage = (Stage) inputField.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(selectedFile));
                StringBuilder text = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    text.append(line).append("\n");
                }
                inputField.setText(text.toString());
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    protected void saveOutputToFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Output Text File");
        Stage stage = (Stage) outputField.getScene().getWindow();
        File file = fileChooser.showSaveDialog(stage);
        if (file != null) {
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter(file));
                writer.write(outputField.getText());
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    protected void encryptText() {
        String text = inputField.getText();
        int key = Integer.parseInt(keyField.getText());
        String encryptedText = encryptCesar(text, key);
        outputField.setText(encryptedText);
    }

    @FXML
    protected void decryptText() {
        String text = inputField.getText();
        int key = Integer.parseInt(keyField.getText());
        String decryptedText = encryptCesar(text, -key);
        outputField.setText(decryptedText);
    }

    private String encryptCesar(String text, int key) {
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < text.length(); i++) {
            char ch = text.charAt(i);
            if (Character.isLetter(ch)) {
                char base = Character.isLowerCase(ch) ? 'a' : 'A';
                ch = (char) (((ch - base + key) % 26 + 26) % 26 + base);
            }
            result.append(ch);
        }

        return result.toString();
    }
}
