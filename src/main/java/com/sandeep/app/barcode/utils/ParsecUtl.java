package com.sandeep.app.barcode.utils;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;

public class ParsecUtl {

        private static final String EMPTY="";
	private static final String PARSEC_URL = "http://www.lbgi.fr/PARSEC/ParsecWebInterface";
	
	public static Set<String> prepareAndSend(String genomeType, Set<String> inputSet)
			throws JSONException, IOException {
		Set<String> newSet = new HashSet<String>();
		StringBuilder parsecCode = new StringBuilder();
		
		inputSet.stream().forEach(s -> parsecCode.append(",\""+s+"\""));

		List<String> returnList = isMatchWithGenome(parsecCode.toString().substring(1), genomeType);
		parsecCode.setLength(0);
		// parsecPassedSet.addAll(returnList);
		newSet.addAll(returnList);

		return newSet;
	}

	private static List<String> isMatchWithGenome(String barcode,
			String genomeType) throws JSONException, IOException {
		String output = "";
		String data = "type=POST&action=SequenceFilterAPI&input={\"genome\":\""+ genomeType + "\",\"sequences\":[" + barcode+ "]}&dataType=json";

		URL url = new URL(PARSEC_URL);
		URLConnection conn;
		try {
			conn = url.openConnection();
			conn.setDoOutput(true);
			conn.setDoInput(true);
			OutputStreamWriter wr = new OutputStreamWriter(
					conn.getOutputStream());
			wr.write(data);
			wr.flush();
			BufferedReader in = new BufferedReader(new InputStreamReader(
					conn.getInputStream()));
			String inputLine;
			StringBuffer sb = new StringBuffer();
			while ((inputLine = in.readLine()) != null) {
				sb.append(inputLine);
			}
			in.close();
			JSONObject jsonObj = new JSONObject(sb.toString());
			output = jsonObj.get("output").toString();

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("External System communication error");
			System.exit(0);
		}
		return convertToList(output);
		// return false;
	}

	private static List<String> convertToList(String successCodes) {
		List<String> list = new ArrayList<String>();
		if (successCodes != null && successCodes.length() > 2) {
			successCodes = successCodes.substring(1, successCodes.length() - 1);
			successCodes = successCodes.replaceAll("\"", EMPTY);
			String[] arr = successCodes.split(",");
			for (int i = 0; i < arr.length; i++) {
				list.add(arr[i]);
			}
		}
		return list;
	}

}
