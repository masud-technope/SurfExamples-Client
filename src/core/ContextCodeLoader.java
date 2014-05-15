package core;

import java.io.File;
import java.util.Scanner;
import core.StaticData;

public class ContextCodeLoader {

	public static String loadContextCode(int exceptionID) {
		// code for loading the context code
		String contextCode = new String();
		try {
			String contextFilePath = StaticData.Surf_Data_Base + "/ccontext/"
					+ exceptionID + ".java";
			File queryFile=new File(contextFilePath);
			if(!queryFile.exists())return "";
			Scanner scanner = new Scanner(queryFile);
			while (scanner.hasNext()) {
				contextCode += scanner.nextLine() + "\n";
			}
			scanner.close();
		} catch (Exception exc) {
			// handle the exception
		}
		return contextCode;
	}
}
