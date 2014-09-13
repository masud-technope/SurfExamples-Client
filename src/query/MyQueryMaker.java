package query;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import visitor.CodeFragment;
import visitor.CodeObject;
import visitor.Dependence;
import core.StaticData;

public class MyQueryMaker {

	int exceptionID;
	String queryContextCode;
	String exceptionName;
	int selectedLine;
	CodeFragment queryFragment = null;
	HashMap<String, String> classExcepPair;
	int MAX_QUERY = 5;

	public MyQueryMaker(int exceptionID, String exceptionName) {
		// initialization
		this.exceptionID = exceptionID;
		this.queryContextCode = getContextCode();
		this.exceptionName = exceptionName;
	}

	public MyQueryMaker(String exceptionName, String contextCode,
			int selectedLine) {
		// initialization
		this.exceptionName = exceptionName;
		this.queryContextCode = contextCode;
		this.selectedLine = selectedLine;
		this.classExcepPair = new HashMap<>();
	}

	protected String getContextCode() {
		// code for collecting the code
		String content = new String();
		try {
			String ccontextFile = StaticData.Surf_Data_Base + "/ccontext/"
					+ exceptionID + ".txt";
			File f = new File(ccontextFile);
			Scanner scanner = new Scanner(f);
			while (scanner.hasNext()) {
				String line = scanner.nextLine();
				content += line + "\n";
			}
		} catch (Exception exc) {
			// handle the exception
		}
		return content;
	}

	protected HashMap<CodeObject, Integer> getCodeObjectSortedTokens() {
		// analyzing the code tokens
		InputDocProcessor processor = new InputDocProcessor(exceptionName,
				queryContextCode, this.selectedLine);
		queryFragment = processor.extractInputDocumentInfo();
		// The map contains the actions and interactions of a class
		HashMap<CodeObject, Integer> LinkCount = new HashMap<>();

		// collecting from simple codeobject dict
		for (CodeObject cobject : queryFragment.codeObjectMap.values()) {
			int fieldcount = cobject.fields.size();
			int methodcount = cobject.methods.size();
			if (LinkCount.containsKey(cobject)) {
				int count = LinkCount.get(cobject).intValue();
				count += fieldcount + methodcount;
				LinkCount.put(cobject, count);
			} else {
				int count = fieldcount + methodcount;
				LinkCount.put(cobject, count);
			}
		}
		// collecting from dependencies
		for (Dependence dep : queryFragment.dependencies) {
			CodeObject fromObj = dep.fromObject;
			CodeObject toObject = dep.destObject;
			// adding from object
			if (LinkCount.containsKey(fromObj)) {
				int count = LinkCount.get(fromObj).intValue();
				count++;
				LinkCount.put(fromObj, count);

			} else {
				LinkCount.put(fromObj, 1);
			}
			// adding destination object
			if (LinkCount.containsKey(toObject)) {
				int count = LinkCount.get(toObject).intValue();
				count++;
				LinkCount.put(toObject, count);
			} else {
				LinkCount.put(toObject, 1);
			}
		}
		// now sort the item
		HashMap<CodeObject, Integer> sorted = MyItemSorter
				.sortCodeObjectMap(LinkCount);
		return sorted;
	}

	protected static Set<Set<String>> combinations(List<String> group, int k) {
		// getting all the combinations
		Set<Set<String>> allcombs = new HashSet<>();
		if (k == 0) {
			allcombs.add(new HashSet<String>());
			return allcombs;
		}
		if (k > group.size()) {
			return allcombs;
		}
		List<String> groupWithoutX = new ArrayList<>(group);
		String X = groupWithoutX.remove(group.size() - 1);
		Set<Set<String>> comboWithoutX = combinations(groupWithoutX, k);
		Set<Set<String>> comboWithX = combinations(groupWithoutX, k - 1);
		for (Set<String> combo : comboWithX) {
			combo.add(X);
		}
		allcombs.addAll(comboWithoutX);
		allcombs.addAll(comboWithX);
		return allcombs;
	}

	public ArrayList<String> getRecommendedQueries() {
		HashMap<CodeObject, Integer> sorted = getCodeObjectSortedTokens();

		// class name object mapping
		HashMap<String, CodeObject> classObjMap = new HashMap<>();
		for (CodeObject key : sorted.keySet()) {
			classObjMap.put(key.className, key);
		}

		ArrayList<String> tokens = new ArrayList<>();
		int count = 0;
		// collect top 5 tokens
		for (CodeObject cobj : sorted.keySet()) {
			tokens.add(cobj.className);
			count++;
			if (count == 5)
				break;
		}
		Set<Set<String>> tokencombs = combinations(tokens, 2); // combination of
																// 2 tokens
		HashMap<String, Integer> exceptions = recommendException(queryFragment);
		ArrayList<QueryPhrase> phrases = new ArrayList<>();
		for (Set<String> comb : tokencombs) {
			double score = 0;
			String tokenStr = new String();
			for (String token : comb) {
				score += sorted.get(classObjMap.get(token));
				tokenStr += token + " ";
			}
			for (String excep : exceptions.keySet()) {
				QueryPhrase qphrase = new QueryPhrase();
				qphrase.tokenQuery = excep + " " + tokenStr;
				// qphrase.tokens=comb;
				qphrase.score = score + exceptions.get(excep);
				phrases.add(qphrase);
			}

		}
		Collections.sort(phrases, new Comparator<QueryPhrase>() {
			@Override
			public int compare(QueryPhrase o1, QueryPhrase o2) {
				// TODO Auto-generated method stub
				Double v1 = o1.score;
				Double v2 = o2.score;
				return v2.compareTo(v1);
			}
		});

		int qcount = 0;
		ArrayList<String> queries = new ArrayList<>();
		for (QueryPhrase phrase : phrases) {
			queries.add(phrase.tokenQuery);
			qcount++;
			if (qcount == MAX_QUERY)
				break;
		}

		// returning the recommendation
		return queries;
	}

	public String getGitHubSearchQuery() {
		// code for GitHub query
		String query = new String();
		// HashMap<CodeObject, Integer> sorted=getCodeObjectSortedTokens();
		try {
			/*
			 * for (CodeObject cobject : sorted.keySet()) { query =
			 * cobject.className; break; }
			 * if(this.exceptionName.equals("Exception")){ //in case of generic
			 * exception
			 * this.exceptionName=recommendException(queryFragment).get(0);
			 * query=this.exceptionName+" "+query; }else{ //in case of specified
			 * exception query=this.exceptionName+" "+query; }
			 */
			query = getRecommendedQueries().get(0);
			query = query.trim();
		} catch (Exception exc) {
			// handle the exception`
		}
		return query;
	}

	protected String refineCanonicalName(String className) {
		// code for refining the canonical name
		String temp = className;
		if (temp.indexOf('<') > 0) {
			className = temp.substring(0, className.indexOf('<'));
		}
		if (temp.indexOf('[') > 0) {
			className = temp.substring(0, className.indexOf('['));
		}
		return className;
	}

	protected HashMap<String, Integer> recommendException(
			CodeFragment queryFragment) {
		String recommededExceptionName = new String();
		HashMap<String, Integer> checkedExceptions = new HashMap<>();
		HashMap<String, CodeObject> extractedObjects = queryFragment.codeObjectMap;
		for (String key : extractedObjects.keySet()) {
			CodeObject cobject = extractedObjects.get(key);
			try {
				String canonical = refineCanonicalName(cobject.canonicalClassName);
				Class<?> myclass = ClassLoader.class.forName(canonical);
				HashSet<String> usedMethods = cobject.methods;
				Method[] allMethods = myclass.getMethods();
				for (Method method : allMethods) {
					if (usedMethods.contains(method.getName())) {
						Class<?>[] exceptions = method.getExceptionTypes();
						for (Class<?> excep : exceptions) {
							try {
								Object excepObj = excep.newInstance();
								// checked exceptions
								if (excepObj instanceof Exception
										&& !(excepObj instanceof RuntimeException)) {
									String exceptionName = excep
											.getSimpleName();
									if (checkedExceptions
											.containsKey(exceptionName)) {
										int count = checkedExceptions.get(
												exceptionName).intValue();
										count++;
										checkedExceptions.put(exceptionName,
												count);
									} else {
										checkedExceptions.put(exceptionName, 1);
									}
									this.classExcepPair.put(cobject.className,
											exceptionName);
								}
							} catch (Exception e) {
								// handle the exception
							}
						}
					}
				}
			} catch (Exception e) {
			}
		}
		// now sort the exception count
		HashMap<String, Integer> sorted = MyItemSorter
				.sortExceptionMap(checkedExceptions);
		// ArrayList<String> exceptions=new ArrayList<>();
		// exceptions.addAll(sorted.keySet());
		return sorted;

	}

	public static void main(String[] args) {
		int exceptionID = 113;
		String exceptionName = "IIOException";
		MyQueryMaker maker = new MyQueryMaker(exceptionID, exceptionName);
		String query = maker.getGitHubSearchQuery();
		System.out.println("Returned query: " + query);
	}
}
