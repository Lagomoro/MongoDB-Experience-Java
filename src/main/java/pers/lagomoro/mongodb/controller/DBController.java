package pers.lagomoro.mongodb.controller;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.support.WebApplicationContextUtils;
import pers.lagomoro.mongodb.controller.mdb.MDB;

import javax.servlet.ServletContext;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/db")
public class DBController {

    public DBController() {}

    @GetMapping("/2-1")
    public List<Document> E2_1() throws Exception {
        return MDB.E2_1("student", "$lt", 20);
    }
    @GetMapping("/2-2")
    public List<Document> E2_2() throws Exception {
        return MDB.E2_2("student", "$lt", 20);
    }
    @GetMapping("/2-3")
    public List<Document> E2_3() throws Exception {
        return MDB.E2_3("student");
    }
    @GetMapping("/2-4")
    public List<Document> E2_4() throws Exception {
        return MDB.E2_4("student", "name", "age");
    }
    @GetMapping("/2-5")
    public List<Document> E2_5() throws Exception {
        return MDB.E2_5("student", "name", "sex");
    }
    @GetMapping("/2-6")
    public List<Document> E2_6() throws Exception {
        return MDB.E2_6("course");
    }
    @GetMapping("/2-7")
    public List<Document> E2_7() throws Exception {
        return MDB.E2_7("course", "$eq", 300001);
    }
    @GetMapping("/2-8")
    public List<Document> E2_8() throws Exception {
        return MDB.E2_8("teacher", "$gt", 50);
    }
    @GetMapping("/2-9")
    public List<Document> E2_9() throws Exception {
        return MDB.E2_9("teacher", "$eq", "M");
    }
    @GetMapping("/2-10")
    public List<Document> E2_10() throws Exception {
        return MDB.E2_10("teacher", "$eq", "CS");
    }

    @GetMapping("/3-1")
    public List<Document> E3_1() throws Exception {
        return MDB.All("student");
    }
    @GetMapping("/3-2")
    public List<Document> E3_2() throws Exception {
        return MDB.All("teacher");
    }
    @GetMapping("/3-3")
    public List<Document> E3_3() throws Exception {
        return MDB.All("course");
    }
    @GetMapping("/3-1-u")
    public String E3_1_U(@RequestParam(value="sid", defaultValue="0") long sid,
                         @RequestParam(value="name", defaultValue="") String name,
                         @RequestParam(value="sex", defaultValue="") String sex,
                         @RequestParam(value="age", defaultValue="0") int age,
                         @RequestParam(value="birthday", defaultValue="") String birthday,
                         @RequestParam(value="dname", defaultValue="") String dname,
                         @RequestParam(value="class", defaultValue="0") int classT) throws Exception {
        MDB.insertStudent("student", sid, name, sex, age, birthday, dname, classT);
        return "{status: 500, 'text': 'Success'}";
    }
    @GetMapping("/3-2-u")
    public String E3_2_U(@RequestParam(value="tid", defaultValue="0") int tid,
                         @RequestParam(value="name", defaultValue="") String name,
                         @RequestParam(value="sex", defaultValue="") String sex,
                         @RequestParam(value="age", defaultValue="0") int age,
                         @RequestParam(value="dname", defaultValue="") String dname) throws Exception {
        MDB.insertTeacher("teacher", tid, name, sex, age, dname);
        return "{status: 500, 'text': 'Success'}";
    }
    @GetMapping("/3-3-u")
    public String E3_3_U(@RequestParam(value="cid", defaultValue="0") int cid,
                         @RequestParam(value="name", defaultValue="") String name,
                         @RequestParam(value="fcid", defaultValue="0") int fcid,
                         @RequestParam(value="credit", defaultValue="0") double credit) throws Exception {
        MDB.insertCourse("course", cid, name, fcid, credit);
        return "{status: 500, 'text': 'Success'}";
    }

    @GetMapping("/4-1")
    public List<Document> E4_1() throws Exception {
        return MDB.All("student");
    }
    @GetMapping("/4-2")
    public List<Document> E4_2() throws Exception {
        return MDB.All("teacher");
    }
    @GetMapping("/4-3")
    public List<Document> E4_3() throws Exception {
        return MDB.All("course");
    }
    @GetMapping("/4-1-u")
    public String E4_1_U(@RequestParam(value="sid", defaultValue="0") long sid,
                         @RequestParam(value="name", defaultValue="") String name,
                         @RequestParam(value="sex", defaultValue="") String sex,
                         @RequestParam(value="age", defaultValue="0") int age,
                         @RequestParam(value="birthday", defaultValue="") String birthday,
                         @RequestParam(value="dname", defaultValue="") String dname,
                         @RequestParam(value="class", defaultValue="0") int classT) throws Exception {
        MDB.updateStudent("student", sid, name, sex, age, birthday, dname, classT);
        return "{status: 500, 'text': 'Success'}";
    }
    @GetMapping("/4-2-u")
    public String E4_2_U(@RequestParam(value="tid", defaultValue="0") int tid,
                         @RequestParam(value="name", defaultValue="") String name,
                         @RequestParam(value="sex", defaultValue="") String sex,
                         @RequestParam(value="age", defaultValue="0") int age,
                         @RequestParam(value="dname", defaultValue="") String dname) throws Exception {
        MDB.updateTeacher("teacher", tid, name, sex, age, dname);
        return "{status: 500, 'text': 'Success'}";
    }
    @GetMapping("/4-3-u")
    public String E4_3_U(@RequestParam(value="cid", defaultValue="0") int cid,
                         @RequestParam(value="name", defaultValue="") String name,
                         @RequestParam(value="fcid", defaultValue="0") int fcid,
                         @RequestParam(value="credit", defaultValue="0") double credit) throws Exception {
        MDB.updateCourse("course", cid, name, fcid, credit);
        return "{status: 500, 'text': 'Success'}";
    }

    @GetMapping("/5-1")
    public List<Document> E5_1(@RequestParam(value="sid", defaultValue="0") long sid) throws Exception {
        return MDB.findStudentCourse("student_course", sid);
    }
    @GetMapping("/5-1-c")
    public List<Document> E5_1_C() throws Exception {
        return MDB.findCourse("course");
    }
    @GetMapping("/5-1-i")
    public String E5_1_I(@RequestParam(value="sid", defaultValue="0") long sid,
                               @RequestParam(value="cid", defaultValue="0") int cid) throws Exception {
        MDB.insertStudentCourse(sid, cid);
        return "{status: 500, 'text': 'Success'}";
    }
    @GetMapping("/5-1-u")
    public String E5_1_U(@RequestParam(value="sid", defaultValue="0") long sid,
                         @RequestParam(value="cid", defaultValue="0") int cid,
                         @RequestParam(value="score", defaultValue="0") double score,
                         @RequestParam(value="tid", defaultValue="0") int tid) throws Exception {
        MDB.updateStudentCourse("student_course", sid, cid, score, tid);
        return "{status: 500, 'text': 'Success'}";
    }
    @GetMapping("/5-1-d")
    public String E5_1_D(@RequestParam(value="sid", defaultValue="0") long sid,
                               @RequestParam(value="cid", defaultValue="0") int cid) throws Exception {
        MDB.deleteStudentCourse(sid, cid);
        return "{status: 500, 'text': 'Success'}";
    }




    @PostMapping("/test")
    public String test() {
        /*MongoClient mongoClient = new MongoClient("localhost", 27017);
        MongoDatabase mongoDatabase = mongoClient.getDatabase("user201800301015");
        MongoCollection<Document> collection = mongoDatabase.getCollection("student");
        FindIterable findIterable = collection.find();
        MongoCursor cursor = findIterable.iterator();
        while (cursor.hasNext()) {
            System.out.println(cursor.next());
        }*/
        return "123";
    }
}