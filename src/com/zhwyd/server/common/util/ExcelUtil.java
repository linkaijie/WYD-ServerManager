package com.zhwyd.server.common.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Vector;
import javax.servlet.http.HttpServletResponse;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

public class ExcelUtil {
    /**
     * 将数组写入工作簿
     * 
     * @param sheet
     *            要写入的工作簿
     * @param col
     *            要写入的数据数组
     * @param rowNum
     *            要写入哪一行
     * @throws WriteException
     * @throws RowsExceededException
     */
    public static void  writeCol(WritableSheet sheet, Vector<?> col, int rowNum) throws RowsExceededException, WriteException {
        int size = col.size(); // 获取集合大小
        //格式
        WritableFont headerFont = new WritableFont(WritableFont.createFont("宋体"), 10);
        WritableCellFormat headerCellFormat = new WritableCellFormat(headerFont);
        headerCellFormat.setBorder(Border.ALL, BorderLineStyle.THIN); // 线条
        for (int i = 0; i < size; i++) { // 写入每一列
            if (col.get(i) instanceof Integer || col.get(i) instanceof Float) {
                jxl.write.Number label = new jxl.write.Number(i, rowNum, Float.parseFloat(col.get(i) + ""),headerCellFormat);// 数字类型
                sheet.addCell(label);
                
            } else {
                Label label = new Label(i, rowNum, col.get(i) + "",headerCellFormat);
                sheet.addCell(label);
            }
        }
    }
    
    /**
     * 通过Http，将Excel流数保存到本地。
     * @param response Http响应
     * @param filename 文件名
     * @param baos 数据流
     */
    public static void  writeExcelFileToLocal(HttpServletResponse response,String filename,ByteArrayOutputStream baos){
        OutputStream out = null;
        ByteArrayInputStream in = null;
        if (response != null) {
            try {
                filename = URLEncoder.encode(filename, "UTF-8");
                // 定义输出类型(下载)
                response.setContentType("application/force-download");
                response.setHeader("Location", filename);
                // 定义输出文件头
                response.setHeader("Content-Disposition", "attachment;filename=" + filename);
                out = response.getOutputStream();
                in = new ByteArrayInputStream(baos.toByteArray());
                byte[] buffer = new byte[1024];
                int i = -1;
                while ((i = in.read(buffer)) != -1) {
                    out.write(buffer, 0, i);
                }
                out.flush();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    in.close();
                    baos.close();
                    out.flush();
                    out.close();
                    out=null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
