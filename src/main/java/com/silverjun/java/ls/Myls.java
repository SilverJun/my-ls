package com.silverjun.java.ls;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;


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
	
	private void printFileDirectory(File directory) throws IOException
	{
		File[] fileList = directory.listFiles();
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
