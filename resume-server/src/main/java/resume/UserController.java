package resume;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import java.util.ArrayList;
import java.util.List;
import java.io.*;


@Controller
@RequestMapping("/user")
public class UserController{

  AmazonS3 s3= AmazonS3ClientBuilder.standard().withRegion("eu-west-2").build();

  // register a new user
  @ResponseBody
  @GetMapping(path="/createuser")
  public String createUser(@RequestParam String username, @RequestParam String password){
    createBucket("userinfo");
    ObjectListing ol = s3.listObjects("userinfo-bucket");
    List<S3ObjectSummary> objects = ol.getObjectSummaries();
    for (S3ObjectSummary os: objects) {
      String[] parts = os.getKey().split(",");
      if(parts[0].equals(username)){
        return "User Name Already Exists. Please try with a different one.";
      }
    }
    s3.putObject("userinfo-bucket",username+","+password,"Username:"+username+"\n"+"Password:"+password);
    createBucket(username);
    return "created "+username;
  }

  public Bucket getBucket(String name){
    List<Bucket> blist = s3.listBuckets();
    for(int i=0; i<blist.size(); i++){
      Bucket b = blist.get(i);
      if(b.getName().equals(name)){
        return b;
      }
    }
    return null;
  }

  // get all users to check during login
  @ResponseBody
  @GetMapping(path="/getusers")
  public Iterable<User> getUsers(){
    String r ="";
    ArrayList<User> users = new ArrayList<>();
    ObjectListing ol = s3.listObjects("userinfo-bucket");
    List<S3ObjectSummary> objects = ol.getObjectSummaries();
    for (S3ObjectSummary os: objects) {
      String[] parts = os.getKey().split(",");
      String username = parts[0];
      String password = parts[1];
      User user = new User(username,password);
      users.add(user);
    }
    return users;
  }

  // to be used by createuser and to create user specific buckets
  @ResponseBody
  @GetMapping(path="/createbucket")
  public String createBucket(@RequestParam String username){
    Bucket b =null;
    String bucket_name = username+"-bucket";
    String result = "";
    if(s3.doesBucketExist(bucket_name)){
      result = "Bucket " + bucket_name + " already exsits.\n" ;
      System.out.println(result);
    }
    else{
      try{
        b = s3.createBucket(bucket_name);
        result = "Saved";
      }catch(AmazonS3Exception e){
        result = "Failed";
        System.err.println(e.getErrorMessage());
      }
    }
    return result;
  }

  /* Initialize the user specific bucket directories for storing
     user information */
  @ResponseBody
  @GetMapping(path="/initializebucket")
  public String initializeBucket(@RequestParam String username){
    return "";
  }
}
