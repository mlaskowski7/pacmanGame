import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class usersFileManipulation {
    private static final String path = "users.txt";

    public static HashMap<String,Integer> getUsersMap(){
        var result = new HashMap<String, Integer>();

        try{
            var br = new BufferedReader(new FileReader(path));
            int index = 0;
            String tempKey = "";
            String current;
            while((current = br.readLine()) != null){
                if(index % 2 == 0){
                    tempKey = current;
                } else{
                    result.put(tempKey,Integer.valueOf(current));
                }
                index++;
            }
            br.close();
        } catch (FileNotFoundException ex){
            System.out.println("Users file was not found - " + ex.getMessage());
        } catch (Exception ex){
            System.out.println("An unexpected exception happened while trying to read users file - " + ex.getMessage());
        }

        System.out.println("Generated users map from file: ");
        result.forEach((key, value) -> System.out.print(key + " = " + value + ", "));

        return result;

    }

    public static void uploadUser(String user){
        var users = getUsersMap();
        if(!users.containsKey(user)){
            try{
                var pathAsPath = Paths.get(path);
                var linesList = Files.readAllLines(pathAsPath);
                linesList.add(user);
                linesList.add("0");
                Files.write(pathAsPath, linesList);
            } catch (FileNotFoundException ex){
                System.out.println("Users file was not found - " + ex.getMessage());
            } catch (Exception ex){
                System.out.println("An unexpected exception happened while trying to read users file - " + ex.getMessage());
            }
        }
    }

    public static void updateHighScore(String currentUser,int highScore){
        var users = getUsersMap();
        if(users.get(currentUser) < highScore){
            System.out.println("High score is lower than current user");
            try{
                var pathAsPath = Paths.get(path);
                var linesList = Files.readAllLines(pathAsPath);
                for(int i = 0; i < linesList.size(); i++){
                    if(linesList.get(i).equals(currentUser)){
                        linesList.set(i+1, String.valueOf(highScore));
                    }
                }
                Files.write(pathAsPath, linesList);
            } catch (FileNotFoundException ex){
                System.out.println("Users file was not found - " + ex.getMessage());
            } catch (Exception ex){
                System.out.println("An unexpected exception happened while trying to read users file - " + ex.getMessage());
            }
        }
    }

}
