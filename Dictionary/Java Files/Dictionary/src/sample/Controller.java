package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.scene.layout.Pane;

import java.sql.*;
import javafx.scene.image.ImageView;
public class Controller {

    @FXML
    private Text choiceText;

    @FXML
    private Button butonAzEn;

    @FXML
    private Button buttonEnAz;

    @FXML
    private TextField inputText;

    @FXML
    private Text inputLanguageType;

    @FXML
    private Text outputLanguageType;

    @FXML
    private Text translationOfWord;

    @FXML
    private Button buttonFind;

    @FXML
    private Button buttonAddDatabase;

    @FXML
    private Pane paneTranslation;

    @FXML
    private Pane paneSelection;

    @FXML
    private Button buttonBack;

    @FXML
    private Text textAskAdd;

    @FXML
    private Text textAddEnglish;

    @FXML
    private TextField inputAddEnglish;

    @FXML
    private Text textAddAzerbaijani;

    @FXML
    private TextField inputAddAzerbaijani;

    @FXML
    private Button buttonYesAdd;

    @FXML
    private Button buttonExitAdminMode;

    @FXML
    private ImageView imageID;

    private     int         choice; //0-default; 1-AzEn;2-EnAz
    private     String      word;

    public void selectAzEn()
    {
        inputLanguageType.setText("From Azerbaijani:");
        outputLanguageType.setText("To English:");
        choice = 1;
        paneSelection.setOpacity(0);
        paneSelection.setDisable(true);
        paneTranslation.setDisable(false);
        paneTranslation.setOpacity(1);
        translationOfWord.setText(" ");

    }
    public void selectEnAz()
    {
        outputLanguageType.setText("To Azerbaijani:");
        inputLanguageType.setText("From English:");
        choice = 2;
        paneSelection.setOpacity(0);
        paneSelection.setDisable(true);
        paneTranslation.setDisable(false);
        paneTranslation.setOpacity(1);
        translationOfWord.setText(" ");

    }
    public void setButtonBack(){
        paneSelection.setOpacity(1);
        paneSelection.setDisable(false);
        paneTranslation.setDisable(true);
        paneTranslation.setOpacity(0);
        inputText.setText("");
    }

    public void setButtonFind() //doldurmaq lazim
    {
        String column;
        switch (choice)
        {
            case 1: //AzEn
                word = inputText.getText().toLowerCase().replaceAll("[^a-zA-ZəçşığöüƏÖĞÜÇIŞ]","");
                 column = "az";
           // String command = "SELECT * FROM WORDS WHERE az='" + word + "'";
                getConnectionToDB(word,column);
                translationOfWord.setText(word);
                break;

            case 2: //EnAz
                word = inputText.getText().toLowerCase().replaceAll("[^a-zA-Z]","");
                column = "en";
                // String command = "SELECT * FROM WORDS WHERE az='" + word + "'";
                getConnectionToDB(word,column);
                translationOfWord.setText(word);
                break;
        }

    }
    public void setButtonAddDatabase(){
        try {
            //name of db is dictionarydatabase
            String az = inputAddAzerbaijani.getText().toLowerCase().replaceAll("[^a-zA-ZəçşığöüƏÖĞÜÇIŞ]","");
            String en = inputAddEnglish.getText().toLowerCase().replaceAll("[^a-zA-Z]","");
           // System.out.println(az + "  " + en);
            String sql = "insert into words" + " (az, en)" + " values ('" + az + "', '" + en + "')" ;
            String url2 = "jdbc:mysql://localhost:3306/dictionarydatabase?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
            String user = "root";
            String password = "data1234";
            Connection mycon = DriverManager.getConnection(url2,user,password);
            Statement myStatement =  mycon.createStatement();
            myStatement.executeUpdate(sql);//words - name of table
            inputAddAzerbaijani.setText("");
            inputAddEnglish.setText("");

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }//doldurmaq lazim
    public void getConnectionToDB(String unknownWord,String column)
    {
        try {
            //name of db is dictionarydatabase
            String url2 = "jdbc:mysql://localhost:3306/dictionarydatabase?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
            String user = "root";
            String password = "data1234";
            Connection mycon = DriverManager.getConnection(url2,user,password);
            Statement myStatement =  mycon.createStatement();
            ResultSet myResultx = myStatement.executeQuery("select * from words");//words - name of table

            String wordAz,wordEn;
            int key = 1;
            switch (column) {
                case "az":
                    while (myResultx.next()) {
                        wordAz = myResultx.getString(column);
                        wordEn = myResultx.getString("en");
                        if (wordAz.compareTo(unknownWord) == 0) {
                             word = wordEn;
                             key = 0;
                             break;
                        }
                    }
                if (key == 1)
                    word = "Not Found.\nPlease add";
                break;
                //*************************case 2*********************
                case "en":
                    while (myResultx.next()) {
                        wordAz = myResultx.getString("az");
                        wordEn = myResultx.getString(column);
                        if (wordEn.compareTo(unknownWord) == 0) {
                            word = wordAz;
                            key = 0;
                            break;
                        }
                    }
                    if (key == 1)
                        word = "Not Found.\nPlease add";
                break;
            }
            myStatement.close();
            myResultx.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void setButtonYesAdd(){

        setButtonBack();
        textAddAzerbaijani.setDisable(false);
        textAddEnglish.setDisable(false);
        buttonAddDatabase.setDisable(false);
        inputAddAzerbaijani.setDisable(false);
        inputAddEnglish.setDisable(false);
        textAddAzerbaijani.setOpacity(1);
        textAddEnglish.setOpacity(1);
        buttonAddDatabase.setOpacity(1);
        inputAddEnglish.setOpacity(1);
        inputAddAzerbaijani.setOpacity(1);
        buttonExitAdminMode.setOpacity(1);
        buttonExitAdminMode.setDisable(false);
    }
    public void setButtonExitAdminMode(){

        textAddAzerbaijani.setDisable(true);
        textAddEnglish.setDisable(true);
        buttonAddDatabase.setDisable(true);
        inputAddAzerbaijani.setDisable(true);
        inputAddEnglish.setDisable(true);
        textAddAzerbaijani.setOpacity(0);
        textAddEnglish.setOpacity(0);
        buttonAddDatabase.setOpacity(0);
        inputAddEnglish.setOpacity(0);
        inputAddAzerbaijani.setOpacity(0);
        buttonExitAdminMode.setDisable(true);
        buttonExitAdminMode.setOpacity(0);
    }
}
