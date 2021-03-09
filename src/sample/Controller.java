package sample;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.*;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

public class Controller {

    // This is where all the variables are made
    private Map<String, Integer> hamFrequency;
    private Map<String, Integer> spamFrequency;

    private Map<String, Double> spamProbWS;
    private Map<String, Double> hamProbWH;
    private Map<String,Double> probSW;

    private int hamFiles;
    private int spamFiles;

    @FXML
    private TableColumn<TestFile, String> FileName;
    private TableColumn<TestFile, String> classColumn;
    private TableColumn<TestFile, String>  probabilityColumn;

    Double truePos = 0.0;
    Double trueNeg = 0.0;
    Double falsePos = 0.0;


    @FXML
    private TableView myTable;


    //This is where the variables are initalized and the values are added to the chart
    public void initalize() throws IOException {
        //Initalizing the functions
        hamFrequency = new TreeMap<>();
        spamFrequency = new TreeMap<>();

        spamProbWS = new TreeMap<>();
        hamProbWH = new TreeMap<>();
        probSW = new TreeMap<>();

        //This is where the values are added to the chart
        FileName.setCellValueFactory(new PropertyValueFactory<>("filename"));
        classColumn.setCellValueFactory(new PropertyValueFactory<>("Class"));
        probabilityColumn.setCellValueFactory(new PropertyValueFactory<>("correctPrediction"));

    }

    //This section is the testing section
//-------------------------------------------------------------------------------------
    public void importValsFortesting(Map<String, Integer> hFreq, Map<String, Integer> sFreq, int hamF, int spamF){
        //This just sets all the frquency values into this file
        hamFrequency.get(hFreq);
        spamFrequency.get(sFreq);
        hamFiles = hamF;
        spamFiles = spamF;
    }


    //this calculates Pr(W|H)
    public void probabilityWH(){
        for (String token : hamFrequency.keySet()){
            hamProbWH.put(token, hamFrequency.get(token)/(double)hamFiles);
        }
    }

    //this calculates Pr(W|S)
    public void probabilityWS(){
        for (String token : spamFrequency.keySet()){
            spamProbWS.put(token, spamFrequency.get(token)/(double)spamFiles);
        }
    }

    //This calculates PR(S|W)
    public void probabilitySW(){
        for (String token : hamFrequency.keySet()){
            double probability = 0.0;
            probability = spamProbWS.get(token);
            double probability1 = 0.0;
            probability1 = hamProbWH.get(token);
            Double bottom = probability + probability1;
            Double total = probability/bottom;
            probSW.put(token,total);
        }
    }

    //This si the class to grab the true positivesm, false positives and true negatives
    public void probabilitySF(){


    }


    public double accuracy_cal(){
        //This is how you calculate the accuracy:
        Double accuracyCalc = (truePos + trueNeg)/ (spamFiles+hamFiles);
        return accuracyCalc;
    }

    public double precision_cal(){
        Double precisionCalc = truePos /(truePos + falsePos);
        return precisionCalc;
    }

}
