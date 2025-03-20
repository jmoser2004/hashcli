import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Scanner;

public class HashCLI
{
    final static String ERROR_IMPROPER_ARGS = """

                                       Error: Improper Arguments!
                                       Usage: java HashCLI.java [input type] [input] [output type] <output>
                                       Type "java HashCLI.java help" for more information.
                                       """;

    final static String ERROR_INVALID_FILE = """
                                             
                                             Error: File not found/invalid file!
                                             Error on input: """;
    
    final static String HELP_TEXT = """

                             To use this application, write "java HashCLI", followed by your arguments. Your full line should look like this:

                             java HashCLI [input type] [input] [output type] <output>

                             There are two options for input type. They are File and CLI.
                             For File, the program gets the input from a text file specified in the [input] section.
                             For CLI, the program gets the input directly from the cli. The input will be the exact text you enter in the [input] section.

                             There are two options for output type. They are File and CLI.
                             For File, the program writes the output to a text file specified in the [output] section.
                             For CLI, the program prints the output directly to the terminal. No [output] argument is necessary.
                             """;

    public static void main(String[] args)
    {
        if(args.length == 0)
        {
            System.out.println(ERROR_IMPROPER_ARGS);
            return;
        }
        
        char inputType;
        String input;
        String output;

        switch(args[0].toLowerCase())
        {
            case "help" -> {
                System.out.println(HELP_TEXT);
                return;
            }
            case "-file", "-f" -> inputType = 'f';
            case "-cli", "-c" -> inputType = 'c';
            default -> {
                System.out.println(ERROR_IMPROPER_ARGS);
                return;
            }
        }

        if(args.length < 3)
        {
            System.out.println(ERROR_IMPROPER_ARGS);
            return;
        }

        switch(inputType)
        {
            case 'f' -> {
                if(!fileValid(args[1], 'r'))
                {
                    System.out.println(ERROR_INVALID_FILE + args[1]);
                    return;
                }
                input = readFile(args[1]);
            }
            case 'c' -> input = args[1];
            default -> input = "";
        }

        output = hash(input);

        switch(args[2].toLowerCase())
        {
            case "-file", "-f" -> {
                if(args.length < 4)
                {
                    System.out.println(ERROR_IMPROPER_ARGS);
                    return;
                }

                if(!fileValid(args[3], 'w'))
                {
                    System.out.println(ERROR_INVALID_FILE);
                    return;
                }

                writeFile(args[3], output);
                return;
            }
            case "-cli", "-c" -> {
                System.out.println(output);
                return;
            }
        }
    }

    public static boolean fileValid(String filePath, char type)
    {
        try
        {
            File file = new File(filePath);

            if(type == 'r') return file.canRead();
            if(type == 'w') return file.canWrite();
        }
        catch(Exception e){System.out.println(e.getMessage());}

        return false;
    }

    public static String readFile(String filePath)
    {
        try
        {
            File file = new File(filePath);
            Scanner fileReader = new Scanner(file);
            String result = "";
            
            while(fileReader.hasNextLine())
            {
                String line = fileReader.nextLine();
                result += line + "\n";
            }

            fileReader.close();

            return result;
        }
        catch(FileNotFoundException e){System.out.println(e.getMessage());}

        return "";
    }

    public static void writeFile(String filePath, String output)
    {
        try
        {
            File file = new File(filePath);
            FileWriter fileWriter = new FileWriter(file);

            fileWriter.write(output);

            fileWriter.close();
        }
        catch(Exception e){System.out.println(e.getMessage());}
    }

    public static String hash(String input)
    {
        try
        {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] byteArray = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            
            StringBuilder hexBuilder = new StringBuilder();
            for(byte b:byteArray) hexBuilder.append(String.format("%02X", b));

            return new String(hexBuilder);
        }catch(Exception e){System.out.println(e.getMessage());}

        return "";
    }
}