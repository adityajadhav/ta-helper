package com.cs442.team4.tahelper;

import android.app.Activity;
import android.widget.Toast;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xslf.model.geom.Context;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by ullas on 11/21/2016.
 */

public class Export {

Activity callerActivity;
    Context callerContext;
    public void exportMarks(Activity activity)
    {
        this.callerActivity = activity;

        try {
            //FileInputStream file = new FileInputStream(new File("C:\\Users\\Mohammed\\Desktop\\Students.xlsx"));

            // Creating Input Stream
//            File file = new File(Environment.getExternalStorageDirectory()
//                    + "/Download/Students.xlsx");
            String path = callerActivity.getApplicationInfo().dataDir;

            Workbook wb = new XSSFWorkbook();
            String filename = "workbook.xls";
            //FileOutputStream fileOut = new FileOutputStream(new File(Environment.getExternalStorageDirectory().toString() + "/workbook.xlsx"));
            FileOutputStream fileOut = new FileOutputStream(new File(path + "/" + filename));
            Sheet o_sheet = wb.createSheet("Sheet 1");

            Row row1 = o_sheet.createRow((short)0);

            // Create a cell and put a value in it
            Cell cell1 = row1.createCell(0);
            cell1.setCellValue(2);



            // String[] format = filepath.split("\\.");
//            if (format[format.length - 1].equals("xls") || format[format.length - 1].equals("xlsx")) {
//
//
//                File file = new File(Environment.getExternalStorageDirectory() + "/" + "export.xlsx");
//
//
//                FileInputStream myInput = new FileInputStream(file);
//                //FileInputStream myInput = new FileInputStream(importfile);
//
//                // Create a POIFSFileSystem object
//                //POIFSFileSystem myFileSystem = new POIFSFileSystem(myInput);
//
//
//                //Create Workbook instance holding reference to .xlsx file
//                XSSFWorkbook workbook = new XSSFWorkbook(myInput);
//
//                //Get first/desired sheet from the workbook
//                XSSFSheet sheet = workbook.getSheetAt(0);
//
//                //Iterate through each rows one by one
//                Iterator<Row> rowIterator = sheet.iterator();
//
//               // int noOfColumns = sheet.getRow(0).getLastCellNum();
//                //Log.i("Number of col", Integer.toString(noOfColumns));
//                String name = null;
//
//                //if (noOfColumns != 4) {
//                  //  Toast.makeText(getContext(), "Number of columns should be 2. Import failed!", Toast.LENGTH_SHORT).show();
//                    //return;
//                //}
//
//                //while (rowIterator.hasNext()) {
//                    Row row = rowIterator.next();
//                    //For each row, iterate through all the columns
//
//                    Iterator<Cell> cellIterator = row.cellIterator();
//                    Cell cell = cellIterator.next();
//
//                    name = cell.getStringCellValue();
//                  //  myRef.child(courseId).child(name).setValue("");
//                    cell = cellIterator.next();
//                    //myRef.child(courseId).child(name).child("email").setValue(cell.getStringCellValue());
//                    cell = cellIterator.next();
//                    //myRef.child(courseId).child(name).child("lastName").setValue(cell.getStringCellValue());
//                    cell = cellIterator.next();
//                    //myRef.child(courseId).child(name).child("firstName").setValue(cell.getStringCellValue());
//
//
//
//                //}
//                //file.close();
//                //DatabaseReference myRef1 = database.getReference("courses");
//                //myRef1.child(courseId).child("imported").setValue(true);
//                Toast.makeText(getContext(), "Import Completed", Toast.LENGTH_SHORT).show();
//            } else {
//                Toast.makeText(getContext(), "only xls or xlsx file format supported", Toast.LENGTH_SHORT).show();
//                return;
//            }
            wb.write(fileOut);
            fileOut.close();
            Toast.makeText(callerActivity,"File name " + filename + " saved in "+ path,Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();

        }

    }

}
