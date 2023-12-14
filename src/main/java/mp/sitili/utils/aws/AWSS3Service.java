package mp.sitili.utils.aws;


import org.springframework.web.multipart.MultipartFile;

public interface AWSS3Service {

    String uploadFile(MultipartFile file);

    Asset getObject(String key);

    void deleteObject(String key);

    String getObjectUrl(String key);
}