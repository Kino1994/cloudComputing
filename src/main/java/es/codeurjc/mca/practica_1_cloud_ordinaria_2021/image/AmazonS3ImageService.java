package es.codeurjc.mca.practica_1_cloud_ordinaria_2021.image;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;

@Service("storageService")
@Profile("production")
public class AmazonS3ImageService implements ImageService {

	@Value("${amazon.s3.bucket-name}")
	private String bucketName;

	@Value("${amazon.s3.endpoint}")
	private String endpoint;

	@Value("${amazon.s3.region}")
	private String region;

	public static AmazonS3 s3;

	public AmazonS3ImageService() {
		s3 = AmazonS3ClientBuilder.standard().withRegion(region).build();
	}

	@Override
	public String createImage(MultipartFile multiPartFile) {

		String fileName = multiPartFile.getOriginalFilename() + UUID.randomUUID().toString();
		File file = new File(System.getProperty("java.io.tmpdir") + "/" + fileName);
		try {
			multiPartFile.transferTo(file);
			PutObjectRequest por = new PutObjectRequest(bucketName, fileName, file);
			por.setCannedAcl(CannedAccessControlList.PublicRead);
			s3.putObject(por);
		} catch (IllegalStateException | IOException e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Image can not be saved on Amazon S3");
		}
		return s3.getUrl(bucketName, fileName).toString();
	}

	@Override
	public void deleteImage(String image) {
		s3.deleteObject(bucketName, image);
	}

}
