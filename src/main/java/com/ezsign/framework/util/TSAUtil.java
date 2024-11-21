package com.ezsign.framework.util;

/**
 * @Class Name  : com.newzenpnp.framework.util.TokenUtil
 * @Description :
 * @Modification Information  
 * @
 * @     수정일                         수정자             수정내용
 * @ -------------     ---------   ---------------------------------
 * @  2015. 8. 10.      유지운                 최초생성
 * 
 * @Company : Newzen Pnp. Inc
 * @Author  : 유지운
 * @Date    : 2015. 8. 10. 오후 4:21:02
 * @version : 1.0
 */


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;
import java.security.Security;
import java.security.cert.X509Certificate;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.x509.util.StreamParsingException;
import org.junit.Assert;

import com.lowagie.text.BadElementException;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Image;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Rectangle;
import com.lowagie.text.TransformMatrix;
import com.lowagie.text.pdf.AcroFields;
import com.lowagie.text.pdf.PdfName;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.RandomAccessFileOrArray;
import com.lowagie.text.pdf.codec.TiffImage;
import com.oss.asn1.DecodeFailedException;
import com.oss.asn1.DecodeNotSupportedException;
import com.oss.asn1.EncodeFailedException;
import com.oss.asn1.EncodeNotSupportedException;

import tradesign.crypto.provider.CaPubs;
import tradesign.crypto.provider.JeTS;
import tradesign.pki.asn1.ASN1Exception;
import tradesign.pki.asn1.exception.ASN1ChoiceException;
import tradesign.pki.pkcs.PKCSException;

import com.ktnet.pdf.security.PdfTimeStamp;
import com.ktnet.pdf.security.exception.PdfSignatureException;

public class TSAUtil {


	public String testOutputDir = "./data/test/" + getClass().getSimpleName();

	public String ORIGINAL = testOutputDir + "/plain.pdf";

	public String TIMESTAMPED_WITH_TEXT = testOutputDir + "/timestamped_with_text.pdf";
	public String TIMESTAMPED_WITH_TEXT2 = testOutputDir + "/timestamped_with_text2.pdf";
	private String TIMESTAMPED_WITH_NO_TEXT = testOutputDir + "/timestamped_with_no_text.pdf";

	public String WITH_IMAGES = testOutputDir + "/pdf_with_images.pdf";

	//public static final String RESOURCE = "./data/img/stamp_tsa.png";
	public static final String RESOURCE = "/data/signdata/img/kform_tsa.png";
	public static final String[] RESOURCE_IMGS = { "data/img/ktnet_large.jpg", "data/img/ktnet_tsa.png",
			"data/img/rubber_stamp.jpg", "data/img/rubber_stamp_invalid.png", "data/img/tiff_image.tif" };

	private static String HASH_ALGORITHM = "SHA256";
	private static String timeStampName = "time_stamp_";

	/**
	 * Creates a PDF document.
	 * 
	 * @param filename
	 *            the path to the new PDF document
	 * @throws DocumentException
	 * @throws IOException
	 */
	public static void createPdf(String filename) throws IOException, DocumentException {
		Document document = new Document();
		PdfWriter.getInstance(document, new FileOutputStream(filename));
		document.open();
		document.add(new Paragraph("Hello World!"));
		document.close();
	}

	/**
	 * Creates a PDF document.
	 * 
	 * @param filename
	 *            the path to the new PDF document
	 * @throws DocumentException
	 * @throws IOException
	 */
	public void createPdfWithImg(String filename) throws IOException, DocumentException {
		Document document = new Document();
		PdfWriter.getInstance(document, new FileOutputStream(filename));
		document.open();
		for (int i = 0; i < RESOURCE_IMGS.length; i++) {
			String file_url = RESOURCE_IMGS[i];
			System.out.println(i + " : " + file_url);
			if (file_url.endsWith(".tif")) {
				RandomAccessFileOrArray ra = new RandomAccessFileOrArray(file_url);
				Image image = TiffImage.getTiffImage(ra, TiffImage.getNumberOfPages(ra));

				// 이미지 크기 조절 비율
				float scaler = ((document.getPageSize().getWidth() - document.leftMargin() - document.rightMargin())
						/ image.getWidth()) * 100;

				// 비율에 따라 이미지 크기 조절
				image.scalePercent(scaler);
				// 이미지 좌표 설정
				image.setAbsolutePosition(0, 0);
				document.add(image);
			} else
				document.add(Image.getInstance(RESOURCE_IMGS[i]));

			document.newPage();
		}
		document.close();
	}

	/**
	 * 
	 * @param src
	 *            원본 PDF 경로
	 * @param dest
	 *            전자서명될 PDF 경로
	 * @param inlucdeTST
	 *            타임스탬프토큰 포함 여부
	 * @param withText
	 *            전자서명 이미지에 서명 텍스트 정보 포함 여부
	 * @param append
	 *            원본이 이미 전자서명 되어 있는경우 true(다중서명)
	 * @param timeStampName
	 *            전자서명 이름. 다중서명시 다른것으로 해야 함.
	 * @throws ASN1Exception 
	 */
	public static void timestampPdf(String src, String output, boolean withText, boolean append, String timeStampName, String userDate)
			throws IOException, DocumentException, GeneralSecurityException, BadElementException,
			EncodeFailedException, DecodeFailedException, PKCSException, EncodeNotSupportedException,
			DecodeNotSupportedException, ASN1ChoiceException, URISyntaxException, PdfSignatureException, ASN1Exception {

		String provider = System.getProperty("ktnet.properties.file");//"D:/workspace/PNP_KForm/WebContent/data/signdata/config/tradesign3280.properties"; 
		JeTS.installProvider(provider);
		JeTS.setCapubs(CaPubs.all);
		
		Security.addProvider(new BouncyCastleProvider());

        PdfReader reader = null;
		// read pdf file
		FileInputStream fin = new FileInputStream(src);
		if(userDate.length()>0) {
			reader = new PdfReader(fin, userDate.getBytes());
		} else {
			reader = new PdfReader(fin);
		}
		PdfTimeStamp timestampMaker = new PdfTimeStamp(reader, append);

		// time stamp agent 설정
		String url = System.getProperty("ktnet.tsa.url");//"http://tsatest.tradesign.net:8090/service/timestamp/issue"; 
		String id = System.getProperty("ktnet.tsa.id");//"nzpnp";
		String password = System.getProperty("ktnet.tsa.pwd");//"psh1112";
		
		System.out.println("TSAInfo "+url+"/"+id+"/"+password);
		timestampMaker.setTSAInfo(url, id, password, HASH_ALGORITHM);

		// timestamped pdf output
		FileOutputStream fout = new FileOutputStream(output);

		// 페이지 왼쪽 아래 코너 좌표 : (0,0)
		Rectangle imgRect = null;
		if (!append) {
			System.out.println("imgRect");
			//imgRect = new Rectangle(447, 709, 575, 837);
			imgRect = new Rectangle(484, 730, 590, 836);
		} else {
			imgRect = new Rectangle(100, 700, 220, 800);
		}
		
		String imgPath = MultipartFileUtil.getSystemPath() + RESOURCE; //"D:/workspace/PNP_KForm/WebContent/"+RESOURCE;
		
		timestampMaker.setVisibleSignature(Image.getInstance(imgPath), 1, imgRect);
		
		// timestamp image with text
		if (withText) {
			Rectangle textRect = null;
			Rectangle timeRect = null;
			if (!append) {
				System.out.println("withText");
				textRect = new Rectangle(500, 773, 577, 785);
			} else {
				textRect = new Rectangle(105, 660, 240, 680);
			}			
			SimpleDateFormat sd = new SimpleDateFormat("yyyy.MM.dd HH:mm");
	        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			String textDate = sd.format(timestamp);
			Font font = FontFactory.getFont("Helvetica", 9);
			
			timestampMaker.setVisibleText(textDate, font, textRect);
		}
		
		// layer1 : adobe 검증성공 이미지 크기/사이즈 설정.
		TransformMatrix layer1TransMatrix = new TransformMatrix();
		layer1TransMatrix.setXTrans(70); //x 축 이동(pixel)
		layer1TransMatrix.setYTrans(70); //y 축 이동
		layer1TransMatrix.setXScale((float) 0.4); //x 축 크기변환(0.5->0.5배)
		layer1TransMatrix.setYScale((float) 0.4); //y 축 크기변환
		timestampMaker.setLayer1TransMatrix(layer1TransMatrix);
		
		timestampMaker.make(fout, timeStampName);
		
		fin.close();
		fout.close();
	}

	public void verify(String src) throws Exception {
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println("**************** verify results for " + src + " ******************");
		System.out.println();

		FileInputStream fin = new FileInputStream(src);

		PdfReader reader = new PdfReader(fin);
		AcroFields af = reader.getAcroFields();
		ArrayList<String> sigNames = reader.getAcroFields().getSignatureNames();

		for (int i = 0; i < sigNames.size(); i++) {
			System.out.println();
			System.out.println("*** signame : " + sigNames.get(i) + "***");
			System.out.println();
			PdfName subFilter = af.getSubFilter(sigNames.get(i));
			if (subFilter.compareTo(PdfName.ETSI_RFC3161) == 0) {
				verifyEtsi(reader, sigNames.get(i));
			} else if (subFilter.compareTo(PdfName.ADBE_PKCS7_DETACHED) == 0) {
				throw new UnsupportedOperationException("PdfTimeStamp 참고");
			} else {
				throw new UnsupportedOperationException(subFilter + "는 지원하지 않습니다");
			}
		}
	}

	public void verifyEtsi(PdfReader reader, String sigName)
			throws GeneralSecurityException, IOException, DecodeFailedException, EncodeFailedException,
			DecodeNotSupportedException, PKCSException, EncodeNotSupportedException, StreamParsingException {
		PdfTimeStamp pdfTimeStamp = new PdfTimeStamp(reader);
		OutputStream output = new FileOutputStream(testOutputDir + "/extractRevision_" + sigName + ".pdf");
		pdfTimeStamp.extractRevision(output, sigName);
		System.out.println("* Token Date : " + pdfTimeStamp.getTimeStampDate(sigName) + "\n");

		X509Certificate tokenCert = pdfTimeStamp.getTimeStampTokenCertificate(sigName);
		System.out.println("* Token Cert : " + tokenCert);
		System.out.println("* 경로검증 : ");
		//CertPathValidate.validate(tokenCert);
		System.out.println();

		System.out.println("* Token 서명 검증 : " + pdfTimeStamp.verify(sigName) + "\n");
		System.out.println("* Token HASH(PDF대비) 검증 : " + pdfTimeStamp.verifyTimestampImprint(sigName) + "\n");
		Assert.assertEquals(true, pdfTimeStamp.verify(sigName));
	}

	public static void main(String[] args) {
		try {
			TSAUtil timeStampTest = new TSAUtil();

			File file = new File(timeStampTest.testOutputDir);
			if (!file.exists())
				file.mkdirs();

			// 샘플pdf 생성
			timeStampTest.createPdf(timeStampTest.ORIGINAL);
		
			// 이미지를 포함한 pdf 생성
	//		timeStampTest.createPdfWithImg(timeStampTest.WITH_IMAGES);
	
	
			// timestamp 이미지 위에 text 넣지 않기
	//		File d = new File("d:/300.cut_pdf");
	//		File[] dirs = d.listFiles();
	//		for (File dir : dirs) {
	//			File[] files = dir.listFiles();
	//			for (File f : files) {
	//				System.out.println(f.getAbsolutePath());
	//				timeStampTest.timestampPdf(f.getAbsolutePath(), f.getPath().replace("save", "save_tsa"), false, false, timeStampName);
	//			}
	//		}
	
			// timestamp 찍어서 pdf에 저장
			timeStampTest.timestampPdf(timeStampTest.ORIGINAL, timeStampTest.TIMESTAMPED_WITH_TEXT, true, false, timeStampName, "");
	
			// timestamp 찍은 pdf위에 timestamp 한번 더 찍고 저장
			 //timeStampTest.timestampPdf(timeStampTest.TIMESTAMPED_WITH_TEXT, timeStampTest.TIMESTAMPED_WITH_TEXT2, true, true, timeStampName + "2");
		
			// timestamp pdf 검증
			 //timeStampTest.verify(timeStampTest.TIMESTAMPED_WITH_TEXT2);
			 //timeStampTest.verify(timeStampTest.TIMESTAMPED_WITH_NO_TEXT);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
}
