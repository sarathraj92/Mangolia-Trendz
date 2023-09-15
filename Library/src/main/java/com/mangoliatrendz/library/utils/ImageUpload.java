package com.mangoliatrendz.library.utils;


import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;

@Service
public class ImageUpload {

    String rootPath = System.getProperty("user.home");

    String UPLOAD_FOLDER = rootPath + "/Mangolia-Trendz/Admin/src/main/resources/static/imgs/images";

    String UPLOAD_FOLDER_CUSTOMER =rootPath + "/Mangolia-Trendz/Customer/src/main/resources/static/imgs/images";


    public String storeFile(MultipartFile file) throws IOException {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        Path uploadPath = Paths.get(UPLOAD_FOLDER);
        Path uploadPathCustomer = Paths.get(UPLOAD_FOLDER_CUSTOMER);


        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
            Files.createDirectories(uploadPathCustomer);
        }
        try {
            // Get the original filename
            String originalFilename = file.getOriginalFilename();

            // Save the file to the first directory
            String filePath1 = UPLOAD_FOLDER + "/" + originalFilename;
            file.transferTo(new File(filePath1));

            // Save the file to the second directory
            String filePath2 = UPLOAD_FOLDER_CUSTOMER + "/" + originalFilename;
            file.transferTo(new File(filePath2));

            return originalFilename;
        } catch (IOException e) {
            return "File upload failed: " + e.getMessage();
        }



//        try (InputStream inputStream = file.getInputStream()) {
//            byte[] buffer = new byte[inputStream.available()];
//            inputStream.read(buffer);
//            Path filePath = uploadPath.resolve(fileName);
//            Path filePathCustomer = uploadPathCustomer.resolve(fileName);
//            Files.write(filePath,buffer, StandardOpenOption.CREATE,StandardOpenOption.TRUNCATE_EXISTING);
//            Files.write(filePathCustomer,buffer, StandardOpenOption.CREATE,StandardOpenOption.TRUNCATE_EXISTING);
//
//            return fileName.toString();
//        } catch (IOException e) {
//            throw new IOException("Could not store file " + fileName, e);
//        }
    }
    public boolean checkExist(MultipartFile File){
        boolean isExist = false;
        try {
            File file = new File(UPLOAD_FOLDER +"/" + File.getOriginalFilename());
            isExist = file.exists();
        }catch (Exception e){
            e.printStackTrace();
        }
        return isExist;
    }
}


