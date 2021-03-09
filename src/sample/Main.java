
package sample;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;


public class Main extends Application {

    Controller controller;

    public Map<String, Integer> tempHamNum;
    public Map<String, Integer> tempSpamNum;

    public Map<String, Integer> hamFrequency;
    public Map<String, Integer> spamFrequency;

    public Map<String, Double> spamProbWS;
    public Map<String, Double> hamProbWH;
    public Map<String, Double> Pr_SW;


    public int hamFiles;
    public int spamFiles;

    //This is where the constructor is called and allt he maps and vars are initalized
    public void Main(){
        tempHamNum = new TreeMap<>();
        tempSpamNum = new TreeMap<>();
        hamFrequency = new TreeMap<>();
        spamFrequency = new TreeMap<>();
        spamProbWS = new TreeMap<>();
        hamProbWH = new TreeMap<>();
        Pr_SW = new TreeMap<>();

        hamFiles= 0;
        spamFiles=0;
    }



    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Spam Detection");
        primaryStage.setScene(new Scene(root, 900, 700));
        primaryStage.show();

        //This is the code that gets the directory
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(new File("."));
        File mainDirectory = directoryChooser.showDialog(null);

        //This checks if the main directory is not empty and runs the training function in this file
        if (mainDirectory != null) {
            training(mainDirectory,false);
        }
        //This runs if there is an IOException
        try{
            training(mainDirectory,false);
        }catch(FileNotFoundException e){
            System.err.println("Invalid input dir: " + mainDirectory.getAbsolutePath());
            e.printStackTrace();
        }catch(IOException e) {
            e.printStackTrace();
        }
        //This checks if gthe main direcotry is not empty and runs the test function in the controller file
        if (mainDirectory != null) {
            //The first function brings the values into the controller class so they can be used there
            controller.importValsFortesting(hamFrequency,spamFrequency,hamFiles,spamFiles);

            //Below runs all the probability functions along with the accuracy and precision calculator
            controller.probabilityWH();
            controller.probabilityWS();
            controller.probabilitySW();
            controller.probabilitySF();
            controller.accuracy_cal();
            controller.precision_cal();
        }

    }
    public void training(File file , Boolean isHam) throws IOException{
        System.out.println("Starting parsing the file:" + file.getAbsolutePath());
        //This checks if the file entered is actually a file or a directory.
        if(file.isDirectory()){
            //Checking if the folder is a ham folder or a spam folder
            if (file.getName() == "ham") {
                isHam = true;
            }
            if(file.getName() == "spam"){
                isHam = false;
            }
            //parse each file inside the directory
            File[] content = file.listFiles();
            for(File current: content) {
                training(current, isHam);
                //Counter to count how many ham and spam files there are
                if (isHam == true) {
                    hamFiles = hamFiles + 1;
                }
                if (isHam == false) {
                    spamFiles = spamFiles + 1;
                }
            }
        }else{
            // This part of the code runs when it is checking a file
            if (isHam == true) {
                //This saves the ham values into a temp map to compare it to see if the word has already been added
                tempHamNum.putAll(hamFrequency);
                //Goes through every word in the file
                Scanner scanner = new Scanner(file);
                while (scanner.hasNext()) {
                    String token = scanner.next();
                    if (isValidWord(token)) {
                        trainingCountHam(token);
                    }
                }
                //This clears the temp tree map for ham
                tempHamNum.clear();
            }
            if (isHam == false) {
                //This saves the spam values into a temp map to compare to see if the word has already been added
                tempSpamNum.putAll(spamFrequency);

                //Goes through every word in the file
                Scanner scanner = new Scanner(file);
                while (scanner.hasNext()) {
                    String token = scanner.next();
                    if (isValidWord(token)) {
                        trainingCountSpam(token);
                    }
                }
                //This clears the temp tree map for spam
                tempSpamNum.clear();
            }
        }
    }

    //This is the function that uses regex to say any word
    private boolean isValidWord(String word){
        String allLetters = "^[a-zA-Z]+$";
        // returns true if the word is composed by only letters otherwise returns false;
        return word.matches(allLetters);
    }

    //This is what adds one to the trainHamNum if the word appears in the folder
    public void trainingCountHam(String token){
        //Checks if the word is not in the trainHamNum
        if(!hamFrequency.containsKey(token)){
            hamFrequency.put(token,1);
            tempHamNum.put(token,1);
        }
        //If the word is in trainHamNum, it checks if it has already been added.
        //If has not already been added, add one to  trainHamNum.
        else if(tempHamNum.get(token) == hamFrequency.get(token)+1){
            int previous = hamFrequency.get(token);
            hamFrequency.put(token, previous+1);
        }
    }

    //This is what adds one to the trainHamNum if the word appears in the folder
    //If has not already been added, add one to  trainHamNum.
    public void trainingCountSpam(String token){
        if(!spamFrequency.containsKey(token)){
            spamFrequency.put(token,1);
            tempSpamNum.put(token,1);
        }
        else if(tempSpamNum.get(token) == spamFrequency.get(token)+1){
            int previous = spamFrequency.get(token);
            spamFrequency.put(token, previous+1);
        }
    }
    public static void main(String[] args) {
        launch(args);
    }
}
