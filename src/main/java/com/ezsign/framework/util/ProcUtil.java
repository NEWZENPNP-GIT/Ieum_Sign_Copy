package com.ezsign.framework.util;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;



import com.jarvis.common.util.FileUtil;
import com.jarvis.pdf.vo.PageVO;

public class ProcUtil {


    public static void main(String[] args) throws Exception {
    	String fileName = "D:\\workspace\\PNP_KForm\\WebContent\\temp\\file\\201712\\20171226\\84b119295c6c44158949a863f1636d61.pdf";
    	String savePath = "D:\\workspace\\PNP_KForm\\WebContent\\temp\\file\\201712\\20171226";
    	String exeName = "D:\\workspace\\PNP_KForm\\WebContent\\data\\mudraw.exe";
    	List<PageVO> list = readPDF(exeName, fileName, savePath);
    	
    }
    
    

    public static List<PageVO> readPDF(String exeName, String fileName, String savePath) throws IOException {
    	List<PageVO> rtnList = new ArrayList<PageVO>();
	    
		SimpleDateFormat dayTime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		System.out.println("start box work time: " + dayTime.format(new Date(System.currentTimeMillis())));
		
        String docId = UUID.randomUUID().toString();

        if (!FileUtil.setDirectory(savePath)) return null;
        
        String outputFileName = savePath + "/" + docId + "%02d.png";
        
        Runtime rt = Runtime.getRuntime();
        String exeFile = exeName + " -r 140 -o " + outputFileName + " " +  fileName;
        System.out.println("exeFile: " + exeFile);
        Process p;
                     
        try {
            p = rt.exec(exeFile);
            p.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        File dir = new File(savePath); 

		File[] fileList = dir.listFiles();
		
		int page = 0;
		for(int i = 0 ; i < fileList.length ; i++){

			File file = fileList[i]; 

			if(file.isFile()&&file.getName().indexOf(docId)>=0){
				
		    	int width = 595;
		    	int height = 841;
		    	page+=1;
		        PageVO imgVO = new PageVO();
		        imgVO.setPage(page);
		        imgVO.setHeight(height);
		        imgVO.setWidth(width);
		        imgVO.setImageFormat("png");
		        imgVO.setTitle("");
		        imgVO.setFilePath("");
		        imgVO.setFileName(file.getName());
		        imgVO.setImageURL("");
		        
		        rtnList.add(imgVO);
			}
		}
	    
		System.out.println("end box time: " + dayTime.format(new Date(System.currentTimeMillis())));
			
	    return rtnList;
	}

	
}
