package resume;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import org.springframework.web.multipart.MultipartFile;
import java.io.*;

@Controller
@RequestMapping("/upload")
public class UploadController{

AmazonS3 s3 = AmazonS3ClientBuilder.standard().withRegion("eu-west-2").build();

// path is of the form "Directory-name/file-name.file-type"
@ResponseBody
@GetMapping(path="/uploadinfo")
public String personalInfo(@RequestParam String username, @RequestParam String path, @RequestParam String infoJson){
  try{
  s3.putObject(username+"-bucket", path , infoJson);
  }catch(Exception e){
    e.printStackTrace();
    return "Error";
  }
  return "Saved";
}

@ResponseBody
@PostMapping(path="/uploadfile")
public String personalInfo(@RequestParam String username, @RequestParam String path, @RequestParam MultipartFile file){
  try{
    s3.putObject(username+"-bucket", path, file.getInputStream(), new ObjectMetadata());
  }catch(AmazonS3Exception e){
    e.printStackTrace();
    return "Error";
  }catch(IOException e){
    e.printStackTrace();
    return "Error";
  }
  return "Saved";
}
}
