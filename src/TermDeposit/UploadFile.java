package TermDeposit;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.file.*;
import java.sql.*;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public class UploadFile {
	public String path;
	private final JFrame frame;
	public UploadFile()
	{
		frame = new JFrame();
	}
	public String selectFile() {
    	
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("PDF Documents", "pdf"));
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Images", "jpg", "png", "jpeg", "bmp"));
        fileChooser.setAcceptAllFileFilterUsed(false);
        int result = fileChooser.showSaveDialog(frame);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
        	 path=selectedFile.getAbsolutePath().toString();
             return selectedFile.getName();
        }
        return "";
    }
    public boolean isValidFileType(File file) {
        String fileName = file.getName().toLowerCase();
        return fileName.endsWith(".pdf") || fileName.endsWith(".png") || fileName.endsWith(".jpg") || fileName.endsWith(".jpeg");
    }

    public byte[] readFileData(File file) throws IOException {
        FileInputStream fis = new FileInputStream(file);
        byte[] fileData = new byte[(int) file.length()];
        fis.read(fileData);
        fis.close();
        return fileData;
    }
}
