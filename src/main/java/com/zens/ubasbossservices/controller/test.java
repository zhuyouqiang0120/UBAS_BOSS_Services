package com.zens.ubasbossservices.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zhuyq@zensvision.com
 * @date 2018年9月20日-上午10:32:41
 * @address sh
 */

public class test {

	public void importCsv() {
		List<String> dataList = new ArrayList<String>();

		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(
					new FileInputStream(new File("/Users/zyq/MyWork/Work/云南/西双版纳/test.csv")), "GBK"));
			String line = "";
			while ((line = br.readLine()) != null) {
				dataList.add(line.trim());
			}
		} catch (Exception e) {
		} finally {
			if (br != null) {
				try {
					br.close();
					br = null;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		// List<String> dataList = importCsv(new
		// File("/Users/zyq/MyWork/Work/云南/西双版纳/test.csv"));
		if (dataList != null && !dataList.isEmpty()) {
			for (int j = 1; j < dataList.size(); j++) {
				String[] pills = dataList.get(j).replace("	", "").replaceAll("\"", "").split(",");
				for (int i = 0; i < pills.length; i++) {
					System.out.print(pills[i].trim() + "|");
				}
				System.out.println("");
				System.out.println("insert!!!!!!!");
				// System.out.println(pills[0].trim()+"|"+pills[1].trim()+"|"+pills[2].trim()+"|"+pills[3].trim()+"|"+pills[4].trim()+"|"+pills[5].trim()+"|"+pills[6].trim()+"|"+pills[7].trim()+"|"+pills[8].trim()+"|"+pills[9].trim()+"|"+pills[10].trim()+"|"+pills[11].trim()+"|"+pills[12].trim()+"|"+pills[8].trim()+"|"+pills[8].trim()+"|"+pills[8].trim()+"|"+pills[8].trim()+"|"+pills[8].trim()+"|"+pills[8].trim()+"|"+pills[8].trim());
				// System.out.println(dataList.get(1).trim().replace(" ", ""));
			}
		}
	}

	public static void main(String[] args) {
		new test().importCsv();
	}
}
