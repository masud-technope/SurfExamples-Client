package core;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Scanner;

import visitor.CodeFragment;

public class FragmentProvider {
	
	
	
	public static Result[] provideDummyResults()
	{
		//collect results
		ArrayList<CodeFragment> fragments=collectDummyFragments();
		ArrayList<Result> results=new ArrayList<>();
		String fileName=StaticData.Surf_Data_Base+"/weights/allsubscores.txt";
		try{
			Scanner scanner=new Scanner(new File(fileName));
			int count=0;
			while(scanner.hasNext()){
				String line=scanner.nextLine();
				String[] parts=line.split("\\s+");
				double lexical=Double.parseDouble(parts[0]);
				double structural=Double.parseDouble(parts[1]);
				double quality=Double.parseDouble(parts[2]);
				Result result=new Result();
				result.content_relevance=lexical;
				result.structural_relevance=structural;
				result.handler_quality=quality;
				CodeFragment cfragment=fragments.get(count);
				String keyword=getKeywords(cfragment);
				result.matchedKeyWords=keyword;
				result.totalScore=lexical+structural+quality;
				results.add(result);
				count++;
				if(count==10)break;
			}
			
		}catch(Exception exc){
			//handle the exception
		}
		Result[] resultArr=new Result[results.size()];
		resultArr=results.toArray(resultArr);
		return resultArr;
	}
	
	protected static String getKeywords(CodeFragment cfragment)
	{
		String matchedObjects=new String();
		for(String key:cfragment.codeObjectMap.keySet()){
			matchedObjects+=key+" ";
		}
		//returning the matched code objects
		return matchedObjects;
	}
	
	
	protected static ArrayList<CodeFragment> collectDummyFragments()
	{
		//code for providing dummy codes
		ArrayList<CodeFragment> fragments=new ArrayList<>();
		String folder=StaticData.Surf_Data_Base+"/fragmentsIndex/1";
		File fdir=new File(folder);
		File[] files=fdir.listFiles();
		for(File f:files){
			try {
				FileInputStream fis=new FileInputStream(f);
				ObjectInputStream ois=new ObjectInputStream(fis);
				CodeFragment cf=(CodeFragment)ois.readObject();
				fragments.add(cf);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				System.err.println(e.getMessage());
			}
		}
		return fragments;
	}
	public static void main(String[] args)
	{
		System.out.println(provideDummyResults().length);
	}
	
}
