package com.spring.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.xmlbeans.impl.xb.xsdschema.Public;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.spring.model.FormBean;

/**
 * 
 * @author Varun Batra
 *
 */
@Controller
public class FormController {

	
	HashMap<Integer, HashMap<Integer, ArrayList<Cell>>> Columns1 = new HashMap();
	String[] total_columns_string =new String[2];
	@GetMapping("/")
	public String employeeForm() {
		return "employeeForm";
	}

	@PostMapping(value = "/saveEmployee")
	public @ResponseBody String[] uploadFile(Model model, MultipartFile file, HttpServletRequest request)
			throws IOException, FileUploadException, EncryptedDocumentException, InvalidFormatException {

		StringBuffer[] total_columns = new StringBuffer[2];
		int maxFileSize = 5000 * 1024;
		int maxMemSize = 5000 * 1024;
		String contentType = request.getContentType();
		if ((contentType.indexOf("multipart/form-data") >= 0)) {

			DiskFileItemFactory factory = new DiskFileItemFactory();
			factory.setSizeThreshold(maxMemSize);
			factory.setRepository(new File("c:\\temp"));
			ServletFileUpload upload = new ServletFileUpload(factory);
			upload.setSizeMax(maxFileSize);
			try {
				List fileItems = upload.parseRequest(request);
				ArrayList<Cell> columnData;
				for (int i = 0; i < 2; i++) {
					HashMap<Integer, ArrayList<Cell>> Columns= new HashMap();
					total_columns[i] = new StringBuffer("");
					InputStream fi = ((DiskFileItem) fileItems.get(i)).getInputStream();

					Workbook workbook = WorkbookFactory.create(fi);
					Sheet sheet = workbook.getSheetAt(0);
					for (int c = 0; c < 15; c++) {
						columnData = new ArrayList<>();
						for (int j = 0; j < sheet.getPhysicalNumberOfRows(); j++) {
						if (sheet.getRow(0).getCell(c) != null) {
							columnData.add(sheet.getRow(j).getCell(c));	
						}
					}
						if (sheet.getRow(0).getCell(c) != null) {
						total_columns[i].append("Col" + c);
						Columns.put(c,columnData);
						}
							
						if (sheet.getRow(0).getCell(c+1) != null  && !total_columns[i].toString().equals("")) {
							total_columns[i].append(",");
						}
					}
				Columns1.put(i, Columns);
				}
			} catch (Exception ex) {
				System.out.println(ex);
			}
		}
		total_columns_string[0] = total_columns[0].toString();
		total_columns_string[1] = total_columns[1].toString();

		return total_columns_string;

	}

	@PostMapping(value = "/getFile")
	public @ResponseBody String downloadFile(@ModelAttribute FormBean formBean,
			HttpServletRequest request)
			throws IOException, FileUploadException, EncryptedDocumentException, InvalidFormatException {
		String order = formBean.getOrder();
		String fileType = formBean.getFileType();
		String delimeter = formBean.getDelimeter();
		String fileName = "";
		String[] arraySize = order.split("}");
		String[] arraySize1 = arraySize[0].split(",");
		String[] arraySize2 = arraySize[1].split(",");
		int[][] columnNo = new int[2][4];
		for (int i = 0; i < arraySize1.length-1; i++) {
			String a = arraySize1[i].trim().substring(arraySize1[i].length() - 1, arraySize1[i].length());
			columnNo[0][i] = Integer.valueOf(a);
		}
		for (int i = 0; i < arraySize1.length-1; i++) {
			String a = arraySize2[i].trim().substring(arraySize2[i].length() - 1, arraySize2[i].length());
			columnNo[1][i] = Integer.valueOf(a);
		}
		
		String url=request.getContextPath()+"/static/Test."+fileType;
			
		if (!fileType.equals("txt")) {
			fileName = System.getProperty("catalina.base") + "/Container/" + "Test."+fileType;// name
																							// of
																							// file

			String sheetName = "Sheet1";// name of sheet

			XSSFWorkbook xssfWorkbook = new XSSFWorkbook();
			XSSFSheet sheet = xssfWorkbook.createSheet(sheetName);
			int c;
			int r;
			for (r = 0; r < Columns1.get(0).get(columnNo[0][0]).size()-1; r++) {
				XSSFRow row = sheet.createRow(r);
			for ( c= 0; c < columnNo[0].length ; c++) {


					// iterating c number of columns
				XSSFCell 					cell = row.createCell(c);

					cell.setCellValue(Columns1.get(0).get(columnNo[0][c]).get(r).toString());
				}
			}

			for (r = 0; r < Columns1.get(0).get(columnNo[1][0]).size()-1; r++) {
				XSSFRow row = sheet.createRow(r);
			for ( c= 0; c < columnNo[1].length ; c++) {


					// iterating c number of columns
				XSSFCell 					cell = row.createCell(c);

					cell.setCellValue(Columns1.get(0).get(columnNo[1][c]).get(r).toString());
				}
			}
			

			FileOutputStream fileOut = new FileOutputStream(fileName);

			// write this workbook to an Outputstream.
			xssfWorkbook.write(fileOut);
			fileOut.flush();
			fileOut.close();

		} else {

			try {
				fileName = System.getProperty("catalina.base") + "/Container/" + "Test."+fileType;
				FileWriter writer = new FileWriter(fileName + fileType, true);
				for (int r = 0; r < Columns1.get(0).get(0).size(); r++) {
					for (int c = 0; c < columnNo.length ; c++) {

						writer.write(Columns1.get(0).get(columnNo[0][c]).get(r).toString());
						if(c!=columnNo.length-1){
							writer.write(delimeter);
						}
					}
					writer.write("\r\n");
				}
				for (int r = 0; r < Columns1.get(0).get(0).size(); r++) {
					for (int c = 0; c < columnNo.length ; c++) {

						writer.write(Columns1.get(0).get(columnNo[1][c]).get(r).toString());
						if(c!=columnNo.length-1){
							writer.write(delimeter);
						}
					}
					writer.write("\r\n");
				}
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return url;

	}
}
