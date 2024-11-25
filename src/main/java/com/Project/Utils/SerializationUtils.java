package com.Project.Utils;

import java.io.*;

public class SerializationUtils {
    public static void SerializeFile(String input, String location) {
        try (PrintWriter printWriter = new PrintWriter(new BufferedWriter(new FileWriter(location, false)))) {
            printWriter.println(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String DeserializeFile(String location) {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(location))) {
            return bufferedReader.readLine();
        } catch (IOException e) {
            SerializeFile("",location);
        }
        return "";
    }

    public static void ClearFile(String fileName){
        try (PrintWriter printWriter = new PrintWriter(new BufferedWriter(new FileWriter(fileName, false)))) {
            printWriter.println("");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
