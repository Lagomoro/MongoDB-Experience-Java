package pers.lagomoro.mongodb.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.bson.Document;
import org.springframework.web.bind.annotation.*;
import pers.lagomoro.mongodb.controller.db.MDB;
import pers.lagomoro.mongodb.controller.db.PDB;

import java.sql.ResultSet;
import java.util.List;

@RestController
@RequestMapping("/pdb")
public class PDBController {

    public PDBController() {}

    @GetMapping("/2-1")
    public JSONArray E2_1() throws Exception {
        return PDB.E2_1();
    }
    @GetMapping("/2-2")
    public JSONArray E2_2() throws Exception {
        return PDB.E2_2();
    }
    @GetMapping("/2-3")
    public JSONArray E2_3() throws Exception {
        return PDB.E2_3();
    }
    @GetMapping("/2-4")
    public JSONArray E2_4() throws Exception {
        return PDB.E2_4();
    }
    @GetMapping("/2-5")
    public JSONArray E2_5() throws Exception {
        return PDB.E2_5();
    }
    @GetMapping("/2-6")
    public JSONArray E2_6() throws Exception {
        return PDB.E2_6();
    }
    @GetMapping("/2-7")
    public JSONArray E2_7() throws Exception {
        return PDB.E2_7();
    }
    @GetMapping("/2-8")
    public JSONArray E2_8() throws Exception {
        return PDB.E2_8();
    }
    @GetMapping("/2-9")
    public JSONArray E2_9() throws Exception {
        return PDB.E2_9();
    }
    @GetMapping("/2-10")
    public JSONArray E2_10() throws Exception {
        return PDB.E2_10();
    }

    @GetMapping("/3-1")
    public JSONArray E3_1() throws Exception {
        return PDB.All("student");
    }
    @GetMapping("/3-2")
    public JSONArray E3_2() throws Exception {
        return PDB.All("teacher");
    }
    @GetMapping("/3-3")
    public JSONArray E3_3() throws Exception {
        return PDB.All("course");
    }
    @GetMapping("/3-1-u")
    public String E3_1_U(@RequestParam(value="sid", defaultValue="0") String sid,
                         @RequestParam(value="name", defaultValue="") String name,
                         @RequestParam(value="sex", defaultValue="") String sex,
                         @RequestParam(value="age", defaultValue="0") int age,
                         @RequestParam(value="birthday", defaultValue="") String birthday,
                         @RequestParam(value="dname", defaultValue="") String dname,
                         @RequestParam(value="class", defaultValue="0") String classT) throws Exception {
        PDB.insertStudent(sid, name, sex, age, birthday, dname, classT);
        return "{status: 500, 'text': 'Success'}";
    }
    @GetMapping("/3-2-u")
    public String E3_2_U(@RequestParam(value="tid", defaultValue="0") String tid,
                         @RequestParam(value="name", defaultValue="") String name,
                         @RequestParam(value="sex", defaultValue="") String sex,
                         @RequestParam(value="age", defaultValue="0") int age,
                         @RequestParam(value="dname", defaultValue="") String dname) throws Exception {
        PDB.insertTeacher(tid, name, sex, age, dname);
        return "{status: 500, 'text': 'Success'}";
    }
    @GetMapping("/3-3-u")
    public String E3_3_U(@RequestParam(value="cid", defaultValue="0") String cid,
                         @RequestParam(value="name", defaultValue="") String name,
                         @RequestParam(value="fcid", defaultValue="0") String fcid,
                         @RequestParam(value="credit", defaultValue="0") double credit) throws Exception {
        PDB.insertCourse(cid, name, fcid, credit);
        return "{status: 500, 'text': 'Success'}";
    }

    @GetMapping("/4-1")
    public JSONArray E4_1() throws Exception {
        return PDB.All("student");
    }
    @GetMapping("/4-2")
    public JSONArray E4_2() throws Exception {
        return PDB.All("teacher");
    }
    @GetMapping("/4-3")
    public JSONArray E4_3() throws Exception {
        return PDB.All("course");
    }
    @GetMapping("/4-1-u")
    public String E4_1_U(@RequestParam(value="sid", defaultValue="0") String sid,
                         @RequestParam(value="name", defaultValue="") String name,
                         @RequestParam(value="sex", defaultValue="") String sex,
                         @RequestParam(value="age", defaultValue="0") int age,
                         @RequestParam(value="birthday", defaultValue="") String birthday,
                         @RequestParam(value="dname", defaultValue="") String dname,
                         @RequestParam(value="class", defaultValue="0") String classT) throws Exception {
        PDB.updateStudent(sid, name, sex, age, birthday, dname, classT);
        return "{status: 500, 'text': 'Success'}";
    }
    @GetMapping("/4-2-u")
    public String E4_2_U(@RequestParam(value="tid", defaultValue="0") String tid,
                         @RequestParam(value="name", defaultValue="") String name,
                         @RequestParam(value="sex", defaultValue="") String sex,
                         @RequestParam(value="age", defaultValue="0") int age,
                         @RequestParam(value="dname", defaultValue="") String dname) throws Exception {
        PDB.updateTeacher(tid, name, sex, age, dname);
        return "{status: 500, 'text': 'Success'}";
    }
    @GetMapping("/4-3-u")
    public String E4_3_U(@RequestParam(value="cid", defaultValue="0") String cid,
                         @RequestParam(value="name", defaultValue="") String name,
                         @RequestParam(value="fcid", defaultValue="0") String fcid,
                         @RequestParam(value="credit", defaultValue="0") double credit) throws Exception {
        PDB.updateCourse(cid, name, fcid, credit);
        return "{status: 500, 'text': 'Success'}";
    }

    @GetMapping("/5-1")
    public JSONArray E5_1(@RequestParam(value="sid", defaultValue="0") String sid) throws Exception {
        return PDB.findStudentCourse(sid);
    }
    @GetMapping("/5-1-c")
    public JSONArray E5_1_C() throws Exception {
        return PDB.findCourse();
    }
    @GetMapping("/5-1-i")
    public String E5_1_I(@RequestParam(value="sid", defaultValue="0") String sid,
                               @RequestParam(value="cid", defaultValue="0") String cid) throws Exception {
        PDB.insertStudentCourse(sid, cid);
        return "{status: 500, 'text': 'Success'}";
    }
    @GetMapping("/5-1-u")
    public String E5_1_U(@RequestParam(value="sid", defaultValue="0") String sid,
                         @RequestParam(value="cid", defaultValue="0") String cid,
                         @RequestParam(value="score", defaultValue="0") double score,
                         @RequestParam(value="tid", defaultValue="0") String tid) throws Exception {
        PDB.updateStudentCourse(sid, cid, score, tid);
        return "{status: 500, 'text': 'Success'}";
    }
    @GetMapping("/5-1-d")
    public String E5_1_D(@RequestParam(value="sid", defaultValue="0") String sid,
                               @RequestParam(value="cid", defaultValue="0") String cid) throws Exception {
        PDB.deleteStudentCourse(sid, cid);
        return "{status: 500, 'text': 'Success'}";
    }

    @GetMapping("/6-1")
    public JSONArray E6_1() throws Exception {
        return PDB.E6_1();
    }
    @GetMapping("/6-2")
    public JSONArray E6_2() throws Exception {
        return PDB.E6_2();
    }
    @GetMapping("/6-3")
    public JSONArray E6_3() throws Exception {
        return PDB.E6_3();
    }
    @GetMapping("/6-4")
    public JSONArray E6_4() throws Exception {
        return PDB.E6_4();
    }
    @GetMapping("/6-5")
    public JSONArray E6_5() throws Exception {
        return PDB.E6_5();
    }
    @GetMapping("/6-6")
    public JSONArray E6_6() throws Exception {
        return PDB.E6_6();
    }
    @GetMapping("/6-7")
    public JSONArray E6_7() throws Exception {
        return PDB.E6_7();
    }
    @GetMapping("/6-8")
    public JSONArray E6_8() throws Exception {
        return PDB.E6_8();
    }
    @GetMapping("/6-9")
    public JSONArray E6_9() throws Exception {
        return PDB.E6_9();
    }

}