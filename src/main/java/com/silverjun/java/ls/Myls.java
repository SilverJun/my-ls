package com.silverjun.java.ls;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.FileTime;
import java.nio.file.attribute.PosixFileAttributes;
import java.nio.file.attribute.PosixFilePermissions;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

import static java.nio.file.LinkOption.NOFOLLOW_LINKS;

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
			Path path = file.toPath();
			// check file type
			String type;
			if (file.isDirectory())	type = "d";
			else if (file.isFile())	type = "-";
			else if (Files.isSymbolicLink(file.toPath()))	type = "l";
			else type = " ";
			// get permission
			PosixFileAttributes posixAttr = Files.readAttributes(path, PosixFileAttributes.class);
			String ownerName = posixAttr.owner().getName();
			String groupName = posixAttr.group().getName();
			String permission = PosixFilePermissions.toString(posixAttr.permissions());
			
			long fileSize = (long)Files.getAttribute(path, "basic:size", NOFOLLOW_LINKS);
			String fileSizeStr = String.valueOf(fileSize);
			if (ishOption)
			{
				fileSizeStr = readableFileSize(fileSize);
			}
			
			FileTime fileTime = (FileTime)Files.getAttribute(path, "basic:lastModifiedTime", NOFOLLOW_LINKS);
			
			int linkCount = (int)Files.getAttribute(path, "unix:nlink", NOFOLLOW_LINKS);
			SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd HH:mm yyyy", Locale.US);
			String lastFixedTime = dateFormat.format(new Date(fileTime.toMillis()));
			
			System.out.print(String.format("%s%s%4d %10s %6s %5s %s %s", type, permission, linkCount, ownerName, groupName, fileSizeStr, lastFixedTime, file.getName()));
		}
		else
			System.out.print(file.getName());
		
		if (isFOption)
		{
			System.out.println("...");
		}
		System.out.println();
	}
	
	private void printFileDirectory(File directory) throws IOException
	{
		ArrayList<File> fileList = new ArrayList<File>();
		// 1. get file list
		if (isaOption)
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
		
		if (islOption)
		{
			System.out.println("total " + fileList.size());
		}
		if (ishOption)
		{
			// . and .. print
		}
		for (File f:fileList)
		{
			printOneFile(f);
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
	
	// https://stackoverflow.com/questions/3263892/format-file-size-as-mb-gb-etc
	public static String readableFileSize(long size) {
	    if(size <= 0) return "0";
	    final String[] units = new String[] { "B", "K", "M", "G", "T" };
	    int digitGroups = (int) (Math.log10(size)/Math.log10(1024));
	    return new DecimalFormat("#,##0.#").format(size/Math.pow(1024, digitGroups)) + units[digitGroups];
	}
}
