package sample;
import javafx.event.ActionEvent;
import javafx.stage.DirectoryChooser;

import java.io.*;
import java.util.*;
import java.util.Scanner;

public class Controller {
    private final Map<String, Integer> trainHamNum;
    private final Map<String, Integer> trainSpamNum;

    private final Map<String, Integer> tempHamNum;
    private final Map<String, Integer> tempSpamNum;

    private final Map<String, Integer> hamFrequency;
    private final Map<String, Integer> spamFrequency;

    private final Map<String, Double> hamProb;
    private final Map<String, Double> spamProb;




    public Controller(){
        trainHamNum = new TreeMap<>();
        trainSpamNum = new TreeMap<>();
        tempHamNum = new TreeMap<>();
        tempSpamNum = new TreeMap<>();
        hamFrequency = new TreeMap<>();
        spamFrequency = new TreeMap<>();
        hamProb= new TreeMap<>();
        spamProb = new TreeMap<>();

    }

// This section is the training section
//------------------------------------------------
    public void TrainButtonAction(ActionEvent event) throws IOException {

        //This is where I get the directory that the user chooses

        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(new File("."));
        File mainDirectory = directoryChooser.showDialog(null);

        //Checks if the mainDirectory in not null
        if (mainDirectory != null) {
            training(mainDirectory,false);
        }
        //This runs if there is an IOException
        try{
            training(mainDirectory,false);
        }catch(FileNotFoundException e){
            System.err.println("Invalid input dir: " + mainDirectory.getAbsolutePath());
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    //This is the training function where the function to check spam and ham are called
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
            for(File current: content){
                training(current,isHam);
            }
            //After parsing each file, these if statements call
            if(isHam == true){
                hamFrequencyCalc();
            }
            if(isHam == false){
                spamFrequencyCalc();
            }
        }else{
            // This part of the code runs when it is checking a file
            if (isHam == true) {

                //This saves the ham values into a temp map to compare
                tempHamNum.putAll(trainHamNum);

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
                //This saves the spam values into a temp map to compare
                tempSpamNum.putAll(trainSpamNum);

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
        if(!trainHamNum.containsKey(token)){
            trainHamNum.put(token,1);
            tempHamNum.put(token,1);
        }
        //If the word is in trainHamNum, it checks if it has already been added.
        //If has not already been added, add one to  trainHamNum.
        else if(tempHamNum.get(token) == trainHamNum.get(token)+1){
            int previous = trainHamNum.get(token);
            trainHamNum.put(token, previous+1);
        }
    }

    //This is what adds one to the trainHamNum if the word appears in the folder
    //If has not already been added, add one to  trainHamNum.
    public void trainingCountSpam(String token){
        if(!trainSpamNum.containsKey(token)){
            trainSpamNum.put(token,1);
            tempSpamNum.put(token,1);
        }
        else if(tempSpamNum.get(token) == trainSpamNum.get(token)+1){
            int previous = trainSpamNum.get(token);
            trainSpamNum.put(token, previous+1);
        }
    }

    //This function calculates Pr(W1|H)
    public void hamFrequencyCalc(){
        Set<String> set1 = hamFrequency.keySet();
        int totalFiles = 0;
        for(String token : set1){
            totalFiles = totalFiles + 1;
        }

        for(String token : set1){
            double currentAmount = hamFrequency.get(token);
            hamProb.put(token, currentAmount/totalFiles);
        }

    }

    public void spamFrequencyCalc(){
        Set<String> set1 = spamFrequency.keySet();
        int totalFiles = 0;
        for(String token : set1){
            totalFiles = totalFiles + 1;
        }

        for(String token : set1){
            double currentAmount = spamFrequency.get(token);
            spamProb.put(token, currentAmount/totalFiles);
        }

    }


//This section is the testing section
//-------------------------------------------------------------------------------------

    public void TestButtonAction(ActionEvent event) throws IOException {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(new File("."));
        File mainDirectory = directoryChooser.showDialog(null);

        //Checks if the mainDirectory in not null
        if (mainDirectory != null) {
            training(mainDirectory,false);
        }
        //This runs if there is an IOException
        try{
            training(mainDirectory,false);
        }catch(FileNotFoundException e){
            System.err.println("Invalid input dir: " + mainDirectory.getAbsolutePath());
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    public void testing(File file , Boolean isHam) throws IOException{

    }




}
