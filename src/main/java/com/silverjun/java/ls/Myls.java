package com.silverjun.java.ls;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.io.filefilter.HiddenFileFilter;


public class Myls {
	boolean isFOption;
	boolean isfOption;
	boolean isaOption;
	boolean islOption;
	boolean ishOption;
	
	public void run(String[] args)
	{
		CommandLineParser parser = new DefaultParser();

		Options options = createOptions();
		
		try {
			CommandLine cmd = parser.parse(options, args);
			
			isFOption = cmd.hasOption("F");
			isfOption = cmd.hasOption("f");
			isaOption = cmd.hasOption("a");
			islOption = cmd.hasOption("l");
			ishOption = cmd.hasOption("h");
			
			File pwd = new File(System.getProperty("user.dir"));
			printFileDirectory(pwd);
		}
		catch (ParseException e) {
			printHelp(options);
			System.exit(-1);
		}
		catch (IOException e) {
			System.out.println(e.getMessage());
			System.exit(-1);
		}
	}

	private void printOneFile(File file) throws IOException
	{
		if (islOption)
		{
			// get permission
			
			
			System.out.print(String.format("%10s", ""));
		}
		else
			System.out.print(file.getName());
		
		if (isFOption)
		{
			System.out.println("...");
		}
	}
	
	private void printFileDirectory(File directory) throws IOException
	{
		ArrayList<File> fileList = new ArrayList<File>();
		// 1. get file list
		if (!ishOption)
			fileList.addAll(Arrays.asList(directory.listFiles()));
		else
			fileList.addAll(Arrays.asList(directory.listFiles((FileFilter)HiddenFileFilter.VISIBLE)));
		
		// 2. sort if there is no f option.
		if (!isfOption)
		{
			fileList.sort(new Comparator<File>() {
				public int compare(File o1, File o2) {
					return o1.getName().compareTo(o2.getName());
				};
			});
		}
		
		// 3. print
		// if there are h option, . .. must be print.
		
		if (ishOption)
			System.out.println("total");
		for (File f:fileList)
		{
			System.out.println(f.getName());
		}
	}

	private Options createOptions() {
		Options options = new Options();

		options.addOption(Option.builder("l")
				.desc("long print option")
				.build());

		options.addOption(Option.builder("f")
				.desc("no sort option")
				.build());
		
		options.addOption(Option.builder("F")
				.desc("add file character")
				.build());
		
		options.addOption(Option.builder("a")
				.desc("print all. '.', '..' like files")
				.build());
		
		options.addOption(Option.builder("h")
		        .desc("Human readable option")
		        .build());
		
		return options;
	}

	private void printHelp(Options options) {
		// automatically generate the help statement
		HelpFormatter formatter = new HelpFormatter();
		String header = "My ls: implementation of ls command.";
		String footer = "";
		formatter.printHelp("my-ls", header, options, footer, true);
	}
}
