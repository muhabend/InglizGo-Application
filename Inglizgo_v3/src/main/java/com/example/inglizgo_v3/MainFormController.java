package com.example.inglizgo_v3;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.sql.*;
import java.util.Optional;
import java.util.ResourceBundle;

public class MainFormController implements Initializable {

    // FXML elements for various forms and controls
    @FXML
    private Button ChangePasswordPane_CancelChangeBtn;

    @FXML
    private Button ChangePasswordPane_ChangePasswordBtn;

    @FXML
    private Label ChangePasswordPane_ChangePasswordLbl;

    @FXML
    private Label ChangePasswordPane_ChangePasswordLbl2;

    @FXML
    private PasswordField ChangePasswordPane_ConfirmPassword;

    @FXML
    private PasswordField ChangePasswordPane_NewPassword;

    @FXML
    private Button DeleteAccountCancelBtn;

    @FXML
    private AnchorPane DeleteAccountPane;

    @FXML
    private Label DeleteAccountPaneAreYouLbl1;

    @FXML
    private Label DeleteAccountPaneIfYouLbl;

    @FXML
    private Button DeleteAccountYesBtn;

    @FXML
    private Button HomePage_AddCardBtn;

    @FXML
    private Button HomePage_StartQuizBtn;

    @FXML
    private AnchorPane Home_Page;

    @FXML
    private StackPane Program_StackPane;

    @FXML
    private AnchorPane UserInfo_ChangePasswordPane;

    @FXML
    private Button UserInfo_ConfirmNewUsername;

    @FXML
    private TextField UserInfo_NewUsernameText;

    @FXML
    private AnchorPane UserInfo_mainPane;

    @FXML
    private AnchorPane UserInfo_pane;

    @FXML
    private TextField WordCard_FirstEx;

    @FXML
    private AnchorPane WordCard_Screen;

    @FXML
    private TextField WordCard_SecondEx;

    @FXML
    private TextField WordCard_Translate;

    @FXML
    private TextField WordCard_addWord;

    @FXML
    private Button WordCard_uploadImageBtn;

    @FXML
    private ImageView WordCard_uploadedImageView;

    @FXML
    private AnchorPane WordScreenTopPane;

    @FXML
    private PasswordField changePassword_CurrentPssword;

    @FXML
    private PasswordField confirmDeletePasswordField;

    @FXML
    private Pane defaultUserImagePane;

    @FXML
    private Pane defaultUserImagePane1;

    @FXML
    private AnchorPane lowerPane_WordCard;

    @FXML
    private AnchorPane mainForm_AddCardPane;

    @FXML
    private AnchorPane mainForm_mainPane;

    @FXML
    private AnchorPane mainForm_upperPane;

    @FXML
    private Button upperPane_HomeBtn;

    @FXML
    private Button upperPane_SettingsBtn;

    @FXML
    private FontAwesomeIcon upperPane_SignoutBtn;

    @FXML
    private Label upperPane_logo;

    @FXML
    private Button upperPane_signOutBtn;

    @FXML
    private Label upperPane_userName;

    @FXML
    private Button userInfo_ChangePasswordBtn;

    @FXML
    private Button userInfo_DeleteAccountBtn;

    @FXML
    private Button userInfo_DeleteUserImageBtn;

    @FXML
    private Button userInfo_EditUserNameBtn;

    @FXML
    private ImageView userInfo_ImageView;

    @FXML
    private ImageView userInfo_ImageView1;

    @FXML
    private Label userInfo_UserName;

    @FXML
    private Button userInfo_changeImage;

    @FXML
    private AnchorPane userInfo_userImageContainer;

    @FXML
    private AnchorPane userInfo_userImageContainer1;

    @FXML
    private Button startQuizButton;

    // Database connection variables
    private Connection connect;
    private PreparedStatement prepare;
    private ResultSet result;
    private Statement statement;
    public String loggedInUsername; // Assuming username is used as the identifier
    private long userId;

    // METHOD ESTABLISHES A CONNECTION TO A MySQL DATABASE NAMED "inglizgo" HOSTED ON THE LOCALHOST SERVER
    public Connection connectDB(){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/inglizgo_app","root","");
        }
        catch(Exception e){e.printStackTrace();}
        return null;
    }

    // Setter for loggedInUsername
    public void setLoggedInUsername(String username) {
        this.loggedInUsername = username;

        // Fetch and display word cards for the logged-in user
        fetchAndDisplayWordCards();
        updateUsernameLabel();
    }

    // Setter for userId
    public void setUserId(long userId) {
        this.userId = userId;
    }

    // Getter for userId
    public long getUserId() {
        return userId;
    }

    // Method to update the label text with the loggedInUsername
    private void updateUsernameLabel() {
        if (loggedInUsername != null) {
            upperPane_userName.setText("Welcome, " + loggedInUsername);
            userInfo_UserName.setText("@" + loggedInUsername);
        } else {
            upperPane_userName.setText("Not logged in"); // Or any other default text you want to display
        }
    }

    @FXML
    public void switchBetweenFroms(ActionEvent event) {
        if (event.getSource() == upperPane_HomeBtn) {
            Home_Page.setVisible(true);
            UserInfo_pane.setVisible(false);
            mainForm_AddCardPane.setVisible(false);
            reloadDisplayedWordCards(); // Ensure this method properly fetches and displays cards
        } else if (event.getSource() == upperPane_SettingsBtn) {
            UserInfo_pane.setVisible(true);
            UserInfo_mainPane.setVisible(true);
            Home_Page.setVisible(false);
            mainForm_AddCardPane.setVisible(false);
            UserInfo_ChangePasswordPane.setVisible(false);
            DeleteAccountPane.setVisible(false);
        }  else if (event.getSource() == HomePage_AddCardBtn) {
            Home_Page.setVisible(false);
            UserInfo_pane.setVisible(false);
            mainForm_AddCardPane.setVisible(true);
        } else if (event.getSource() == userInfo_ChangePasswordBtn) {
            UserInfo_mainPane.setVisible(false);
            UserInfo_ChangePasswordPane.setVisible(true);
            DeleteAccountPane.setVisible(false);
        } else if (event.getSource() == ChangePasswordPane_CancelChangeBtn
                || event.getSource() == DeleteAccountCancelBtn) {
            UserInfo_mainPane.setVisible(true);
            UserInfo_ChangePasswordPane.setVisible(false);
            DeleteAccountPane.setVisible(false);
        } else if (event.getSource() == userInfo_DeleteAccountBtn) {
            UserInfo_mainPane.setVisible(false);
            UserInfo_ChangePasswordPane.setVisible(false);
            DeleteAccountPane.setVisible(true);
        } else {
            // Default case to handle other buttons if necessary
            UserInfo_pane.setVisible(false);
            Home_Page.setVisible(true);
            mainForm_AddCardPane.setVisible(false);
            fetchAndDisplayWordCards(); // Reload cards when defaulting back to home
        }
    }

    // Method to open a file dialog and return the selected file
    private File openFileDialog(String title) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(title);
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );

        // Show the file chooser dialog
        Stage stage = (Stage) WordCard_uploadImageBtn.getScene().getWindow();
        return fileChooser.showOpenDialog(stage);
    }

    @FXML
    void chooseImage(ActionEvent event) {
        File selectedFile = openFileDialog("Choose an image file");

        // If a file is selected, update the appropriate ImageView with the chosen image
        if (selectedFile != null) {
            Image image = new Image(selectedFile.toURI().toString());
            if (event.getSource() == userInfo_changeImage || event.getSource() == userInfo_ImageView) {
                userInfo_ImageView.setImage(image);
                userInfo_ImageView1.setImage(image);
                saveUserImageToDatabase(image);
            } else if (event.getSource() == WordCard_uploadImageBtn) {
                WordCard_uploadedImageView.setImage(image);
            }
        }
    }

    // Method to save the user image to the database
    private void saveUserImageToDatabase(Image userImage) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            BufferedImage bufferedImage = SwingFXUtils.fromFXImage(userImage, null);
            ImageIO.write(bufferedImage, "png", outputStream);
            ImageIO.write(bufferedImage, "jpg", outputStream);
            ImageIO.write(bufferedImage, "gif", outputStream);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        byte[] imageBytes = outputStream.toByteArray();

        // Now you can insert or update the imageBytes into your database along with other relevant information
        try {
            connect = connectDB(); // Assuming you have a method to establish a database connection

            // Check if the user already has an image in the database
            PreparedStatement selectStatement = connect.prepareStatement("SELECT User_Photo FROM user_info WHERE UserName = ?");
            selectStatement.setString(1, loggedInUsername);
            ResultSet resultSet = selectStatement.executeQuery();

            if (resultSet.next()) {
                // Update the existing image
                PreparedStatement updateStatement = connect.prepareStatement("UPDATE user_info SET User_Photo = ? WHERE UserName = ?");
                updateStatement.setBytes(1, imageBytes);
                updateStatement.setString(2, loggedInUsername);
                updateStatement.executeUpdate();
            } else {
                // Insert a new image
                PreparedStatement insertStatement = connect.prepareStatement("UPDATE user_info SET User_Photo = ? WHERE UserName = ?");
                insertStatement.setBytes(1, imageBytes);
                insertStatement.setString(2, loggedInUsername);
                insertStatement.executeUpdate();
            }
            connect.close(); // Close the connection when done
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to set the user image in the UI
    public void setUserImage(Image image) {
        userInfo_ImageView.setImage(image);
        userInfo_ImageView1.setImage(image);
    }

    // To load the user's image when the program starts or the user logs in
    public Image loadUserImageFromDatabase(String username) {
        try {
            Connection connection = connectDB(); // Assuming you have a method to establish a database connection
            PreparedStatement statement = connection.prepareStatement("SELECT User_Photo FROM user_info WHERE UserName = ?");
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                byte[] imageBytes = resultSet.getBytes("User_Photo");
                if (imageBytes != null) { // Check if imageBytes is not null
                    InputStream inputStream = new ByteArrayInputStream(imageBytes);
                    return new Image(inputStream);
                }
            }
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @FXML
    void emptyUserImageView(ActionEvent event) {
        if (event.getSource() == userInfo_DeleteUserImageBtn) {
            userInfo_ImageView.setImage(null);
            userInfo_ImageView1.setImage(null);
            deleteImageFromDatabase(); // Call method to delete image from the database
        }
    }

    // Method to delete the user image from the database
    private void deleteImageFromDatabase() {
        try {
            Connection connection = connectDB(); // Establish database connection
            AlertMessage alert = new AlertMessage();

            // Prepare statement to delete user image from the database
            PreparedStatement deleteStatement = connection.prepareStatement("UPDATE user_info SET User_Photo = NULL WHERE UserName = ?");
            deleteStatement.setString(1, loggedInUsername);

            // Execute the delete statement
            int rowsAffected = deleteStatement.executeUpdate();

            if (rowsAffected > 0) {
                alert.successMessage("Your Image has been deleted");
            }

            connection.close(); // Close the database connection
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void EditUsername(ActionEvent event) {
        if (event.getSource() == userInfo_EditUserNameBtn) {
            userInfo_UserName.setVisible(false);
            UserInfo_NewUsernameText.setVisible(true);
            UserInfo_ConfirmNewUsername.setVisible(true);
        }
    }

    @FXML
    void ConfirmNewUsername(ActionEvent event) {
        AlertMessage alert = new AlertMessage();

        String newUsername = UserInfo_NewUsernameText.getText().trim();
        if (!newUsername.isEmpty() && !newUsername.equals(loggedInUsername)) {
            try {
                Connection connection = connectDB();
                // Check if the new username is already in use
                PreparedStatement selectStatement = connection.prepareStatement("SELECT * FROM user_info WHERE UserName = ?");
                selectStatement.setString(1, newUsername);
                ResultSet resultSet = selectStatement.executeQuery();
                if (!resultSet.next()) {
                    // New username is available, proceed with the update
                    PreparedStatement updateStatement = connection.prepareStatement("UPDATE user_info SET UserName = ? WHERE UserName = ?");
                    updateStatement.setString(1, newUsername);
                    updateStatement.setString(2, loggedInUsername);
                    int rowsAffected = updateStatement.executeUpdate();
                    if (rowsAffected > 0) {
                        // Update successful
                        alert.successMessage("Username updated successfully.");
                        closeUsernameTextField();
                        loggedInUsername = newUsername; // Update the loggedInUsername
                        updateUsernameLabel(); // Update the username label in the UI
                    } else {
                        // No user found with the current username
                        alert.successMessage("No user found with the current username.");
                    }
                } else {
                    // Username already in use
                    alert.errorMessage("Username already in use. Please choose a different username.");
                }
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            // Username field is empty or same as current username
            alert.errorMessage("The new Username can not be empty, or the same as current username.");
        }
    }

    @FXML
    private void changePasswordFromUserInfoPane() {
        AlertMessage alert = new AlertMessage();

        String currentPassword = changePassword_CurrentPssword.getText(); // Retrieve the current password from your UI components
        String newPassword = ChangePasswordPane_NewPassword.getText(); // Retrieve the new password from your UI components
        String confirmPassword = ChangePasswordPane_ConfirmPassword.getText(); // Retrieve the confirmation password from your UI components

        // Check if the new password meets certain criteria (e.g., length requirements)
        if (newPassword.length() < 8) {
            alert.errorMessage("Password must be at least 8 characters long.");
            return;
        }

        // Check if the new password matches the confirmation password
        if (!newPassword.equals(confirmPassword)) {
            alert.errorMessage("Passwords do not match.");
            return;
        }

        try {
            connect = connectDB();

            // Check if the current password is correct
            PreparedStatement selectStatement = connect.prepareStatement("SELECT UserPassword FROM user_info WHERE UserName = ?");
            selectStatement.setString(1, loggedInUsername);
            ResultSet resultSet = selectStatement.executeQuery();
            if (resultSet.next()) {
                String storedPassword = resultSet.getString("UserPassword");

                if (currentPassword.equals(storedPassword)) {
                    // Update the password in the database
                    PreparedStatement updateStatement = connect.prepareStatement("UPDATE user_info SET UserPassword = ? WHERE UserName = ?");
                    updateStatement.setString(1, newPassword);
                    updateStatement.setString(2, loggedInUsername);

                    int rowsAffected = updateStatement.executeUpdate();

                    if (rowsAffected > 0) {
                        // Password updated successfully
                        alert.successMessage("Password changed successfully.");
                        UserInfo_mainPane.setVisible(true);
                        UserInfo_ChangePasswordPane.setVisible(false);

                        // TO CLEAR THE FORM AFTER PASSWORD CHANGED SUCCESSFULLY
                        changePassword_CurrentPssword.setText("");
                        ChangePasswordPane_NewPassword.setText("");
                        ChangePasswordPane_ConfirmPassword.setText("");
                    } else {
                        // Password update failed
                        alert.errorMessage("Failed to change password. Please try again.");
                    }
                } else {
                    // Current password is incorrect
                    alert.errorMessage("Current password is incorrect.");
                }
            } else {
                // No user found with the current username
                alert.errorMessage("No user found with the current username.");
            }

            connect.close();
        } catch (SQLException e) {
            e.printStackTrace();
            alert.errorMessage("Error occurred while changing password.");
        }
    }

    private void ForgotPasswordFromUserInfo() {
        // Method to handle forgot password from user info pane (empty for now)
    }

    @FXML
    private void deleteAccount() {
        AlertMessage alert = new AlertMessage();

        // Show confirmation message to confirm account deletion
        Optional<ButtonType> result = alert.confirmationMessage("Are you sure you want to delete your account? This action cannot be undone.");

        // Check user's response to the confirmation message
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                Connection connection = connectDB();

                // Verify the user's credentials (e.g., password)
                String password = confirmDeletePasswordField.getText(); // Retrieve the user's password from your UI components
                PreparedStatement selectStatement = connection.prepareStatement("SELECT UserPassword FROM user_info WHERE UserName = ?");
                selectStatement.setString(1, loggedInUsername);
                ResultSet resultSet = selectStatement.executeQuery();
                if (resultSet.next()) {
                    String storedPassword = resultSet.getString("UserPassword");
                    if (password.equals(storedPassword)) {
                        // Delete the account from the database
                        PreparedStatement deleteStatement = connection.prepareStatement("DELETE FROM user_info WHERE UserName = ?");
                        deleteStatement.setString(1, loggedInUsername);
                        int rowsAffected = deleteStatement.executeUpdate();
                        if (rowsAffected > 0) {
                            // Account deleted successfully
                            alert.successMessage("Your account has been deleted successfully.");
                            // Close the current stage (main form)
                            Stage stage = (Stage) upperPane_signOutBtn.getScene().getWindow();
                            stage.close();
                            // Show the login form
                            showLoginForm();
                        } else {
                            // Account deletion failed
                            alert.errorMessage("Failed to delete account. Please try again.");
                        }
                    } else {
                        // Incorrect password
                        alert.errorMessage("Incorrect password.");
                    }
                } else {
                    // No user found with the current username
                    alert.errorMessage("No user found with the current username.");
                }

                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
                alert.errorMessage("Error occurred while deleting account.");
            }
        }
    }

    // Method to show the login form
    private void showLoginForm() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Login_SignUp_ForotPassword.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage loginStage = new Stage();
            loginStage.setScene(scene);
            loginStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to convert JavaFX Image to byte array
    private byte[] convertImageToBytes(Image image) throws IOException {
        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "png", byteArrayOutputStream);
        ImageIO.write(bufferedImage, "jpg", byteArrayOutputStream);
        ImageIO.write(bufferedImage, "gif", byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    @FXML
    private void saveWordCardToDatabase(ActionEvent event) {
        AlertMessage alert = new AlertMessage();

        if (WordCard_addWord.getText().isEmpty() || WordCard_Translate.getText().isEmpty()
                || WordCard_FirstEx.getText().isEmpty() || WordCard_SecondEx.getText().isEmpty()) {
            alert.errorMessage("Please fill all the fields");
        } else {
            try (Connection connect = connectDB()) {
                byte[] imageBytes = null;
                if (WordCard_uploadedImageView.getImage() != null) {
                    imageBytes = convertImageToBytes(WordCard_uploadedImageView.getImage());
                }

                String sql = "INSERT INTO wordcards (EN_word, TR_translate, FirstEx, SecondEx, UserName, Word_Image) VALUES (?, ?, ?, ?, ?, ?)";
                try (PreparedStatement prepare = connect.prepareStatement(sql)) {
                    prepare.setString(1, WordCard_addWord.getText());
                    prepare.setString(2, WordCard_Translate.getText());
                    prepare.setString(3, WordCard_FirstEx.getText());
                    prepare.setString(4, WordCard_SecondEx.getText());
                    prepare.setString(5, loggedInUsername);
                    prepare.setBytes(6, imageBytes);
                    prepare.executeUpdate();
                }

                alert.successMessage("Card successfully added.");
                WordCard_addWord.setText("");
                WordCard_Translate.setText("");
                WordCard_FirstEx.setText("");
                WordCard_SecondEx.setText("");
                WordCard_uploadedImageView.setImage(null);

                reloadDisplayedWordCards();
            } catch (SQLException | IOException e) {
                alert.errorMessage("Error occurred while saving the card: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void signOut() {
        // Close the current stage (main form)
        Stage stage = (Stage) upperPane_signOutBtn.getScene().getWindow();
        stage.close();

        // Show the login form
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Login_SignUp_ForotPassword.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage loginStage = new Stage();
            loginStage.setScene(scene);
            loginStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to close the new username text field
    private void closeUsernameTextField() {
        UserInfo_NewUsernameText.setVisible(false);
        UserInfo_NewUsernameText.setText(""); // Clear the text field
        UserInfo_ConfirmNewUsername.setVisible(false);
        userInfo_UserName.setVisible(true); // Show the username label
    }

    // Method to fetch and display word cards
    private void fetchAndDisplayWordCards() {
        try {
            AlertMessage alert = new AlertMessage();

            connect = connectDB();
            PreparedStatement selectStatement = connect.prepareStatement("SELECT EN_word, TR_translate, FirstEx, SecondEx, Word_Image FROM wordcards WHERE UserName = ? ORDER BY word_id DESC");
            selectStatement.setString(1, loggedInUsername);
            ResultSet resultSet = selectStatement.executeQuery();

            // Clear previous cards
            WordCard_Screen.getChildren().clear();

            // Create a VBox to hold the card containers with spacing
            VBox cardContainerBox = new VBox();
            cardContainerBox.setSpacing(20); // Adjust spacing as needed
            cardContainerBox.setAlignment(Pos.CENTER); // Align the card container box to the center
            cardContainerBox.setStyle("-fx-background-color: #00000000; -fx-pref-width: 1321px; -fx-padding: 20px;");

            while (resultSet.next()) {
                // Retrieve the word associated with the card being deleted
                String word = resultSet.getString("EN_word");

                // Create a container for the card
                HBox cardContainer = new HBox();
                cardContainer.setSpacing(50); // Adjust spacing as needed
                cardContainer.setAlignment(Pos.CENTER);
                cardContainer.getStyleClass().add("word-card"); // You can add a CSS class for styling
                cardContainer.setStyle("-fx-background-color: #3d4654; -fx-padding: 20px; -fx-pref-width: 1300px; " +
                        "-fx-background-radius: 6px; -fx-border-color: #585a5e; -fx-border-radius: 6px");

                // Create labels for each piece of information
                Label wordLabel = new Label("Word:  " + resultSet.getString("EN_word"));
                Label translateLabel = new Label("Translation:  " + resultSet.getString("TR_translate"));
                Label firstExLabel = new Label("First Example:  " + resultSet.getString("FirstEx"));
                Label secondExLabel = new Label("Second Example:  " + resultSet.getString("SecondEx"));

                // Set styles for the labels
                wordLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: white; -fx-font-size: 17px; -fx-pref-width: 130px; -fx-wrap-text: true;");
                translateLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: white; -fx-font-size: 17px; -fx-pref-width: 180px; -fx-wrap-text: true;");
                firstExLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: white; -fx-font-size: 17px; -fx-pref-width: 220px; -fx-wrap-text: true;");
                secondExLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: white; -fx-font-size: 17px; -fx-pref-width: 220px; -fx-wrap-text: true;");

                // Create an ImageView for the word image
                byte[] imageData = resultSet.getBytes("Word_Image");
                if (imageData != null) {
                    Image image = new Image(new ByteArrayInputStream(imageData));
                    ImageView imageView = new ImageView(image);
                    imageView.setFitWidth(200); // Set your preferred width
                    imageView.setFitHeight(150); // Set your preferred height
                    cardContainer.getChildren().add(imageView);
                }

                Button deleteButton = new Button();
                deleteButton.setStyle("-fx-background-color: red; -fx-background-radius: 20px; -fx-pref-width: 32px; -fx-pref-height: 32px;");

                FontAwesomeIcon deleteIcon = new FontAwesomeIcon();
                deleteIcon.setGlyphName("TRASH");
                deleteIcon.setStyle("-fx-font-family: 'FontAwesome'; -fx-fill: white; -fx-font-size: 1.1em;");
                deleteButton.setGraphic(deleteIcon);

                deleteButton.setOnAction(event -> {
                    // Handle card deletion
                    cardContainerBox.getChildren().remove(cardContainer); // Remove from UI
                    // Call a method to delete the word card from the database
                    deleteWordCardFromDatabase(word);
                    alert.successMessage("Your word card deleted successfully.");
                });

                // Add labels to the card container
                cardContainer.getChildren().addAll(wordLabel, translateLabel, firstExLabel, secondExLabel);
                // Add the delete button to the card container
                cardContainer.getChildren().add(deleteButton);

                // Add the card container to the VBox with spacing
                cardContainerBox.getChildren().add(cardContainer);
            }

            // Add the VBox with card containers to the AnchorPane and center it
            WordCard_Screen.getChildren().add(cardContainerBox);
            AnchorPane.setTopAnchor(cardContainerBox, 0.0);
            AnchorPane.setLeftAnchor(cardContainerBox, 0.0);

            connect.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to delete a word card from the database
    private void deleteWordCardFromDatabase(String word) {
        try {
            connect = connectDB();
            PreparedStatement deleteStatement = connect.prepareStatement("DELETE FROM wordcards WHERE UserName = ? AND EN_word = ?");
            deleteStatement.setString(1, loggedInUsername);
            deleteStatement.setString(2, word);
            deleteStatement.executeUpdate();
            connect.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to clear displayed word cards
    private void clearDisplayedWordCards() {
        // Remove all existing card containers from the main form pane
        WordCard_Screen.getChildren().removeIf(node -> node.getStyleClass().contains("word-card"));
    }

    // Method to reload displayed word cards
    private void reloadDisplayedWordCards() {
        // Clear existing displayed cards
        clearDisplayedWordCards();

        // Fetch and display new cards
        fetchAndDisplayWordCards();
    }

    @FXML
    private void startQuiz(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("quiz_screen.fxml"));
            if (loader.getLocation() == null) {
                throw new IllegalStateException("FXML file not found");
            }
            Parent quizRoot = loader.load();
            QuizScreenController quizController = loader.getController();
            quizController.setLoggedInUsername(loggedInUsername); // Pass the username to the quiz controller

            Stage quizStage = new Stage();
            quizStage.setTitle("Quiz");
            quizStage.setScene(new Scene(quizRoot));
            quizStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Unable to load the quiz screen.");
        }
    }

    // Method to show an alert with the specified title and message
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // Set Home_Page visible initially
        Home_Page.setVisible(true);
        UserInfo_pane.setVisible(false);
        mainForm_AddCardPane.setVisible(false);

        // Add event listener to the main pane to listen for mouse clicks
        mainForm_mainPane.setOnMouseClicked(event -> {
            Node source = (Node) event.getTarget();
            if (source != UserInfo_ConfirmNewUsername) {
                // If the user did not click on Confirm New Username
                closeUsernameTextField();
            }
        });
    }
}
