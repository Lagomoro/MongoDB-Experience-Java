package pers.lagomoro.mongodb.controller.db;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.result.DeleteResult;
import org.apache.log4j.Logger;
import org.bson.Document;

import java.sql.*;
import java.util.*;

public class PDB {

    public final static String SQL_USER = "lab_115731330";
    public final static String SQL_PASSWARD = "4da272f6bd78_#@Aa";
    public final static String SQL_SITE = "pc-bp18rn0tqu85a1600-public.rwlb.rds.aliyuncs.com:3306";
    public final static String SQL_DB = "polardb_mysql_13066nie";
    public final static String SQL_URL = "jdbc:mysql://" + SQL_SITE + "/" + SQL_DB + "?useSSL=true&serverTimezone=GMT%2B8&characterEncoding=utf8";

    public static Connection connection;
    private static Logger logger = Logger.getLogger(PDB.class);

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(SQL_URL, SQL_USER, SQL_PASSWARD);
            logger.info("数据库连接成功");
            logger.info("当前的数据是 : " + connection.getSchema());
        } catch (Exception e) {
            logger.error(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    public static JSONArray R2J(ResultSet resultSet) throws SQLException {
        JSONArray array = new JSONArray();
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();
        while (resultSet.next()) {
            JSONObject json = new JSONObject();
            for (int i = 1; i <= columnCount; i++) {
                String columnName = metaData.getColumnLabel(i);
                String value = resultSet.getString(columnName);
                json.put(columnName, value);
            }
            array.add(json);
        }
        return array;
    }

    //=================================================================================

    public static JSONArray Select(String sql) {
        PreparedStatement preparedStatement;
        try {
            preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            JSONArray json = R2J(resultSet);
            resultSet.close();
            preparedStatement.close();
            return json;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static JSONArray E2_1() {
        return Select("SELECT * FROM student WHERE age < 20");
    }

    public static JSONArray E2_2() {
        return Select("SELECT * FROM student WHERE age < 20 and dname = 'SC'");
    }

    public static JSONArray E2_3() {
        return Select("SELECT * FROM student");
    }

    public static JSONArray E2_4() {
        return Select("SELECT `name`, age FROM student");
    }

    public static JSONArray E2_5() {
        return Select("SELECT `name`, sex FROM student WHERE age < 20");
    }

    public static JSONArray E2_6() {
        return Select("SELECT * FROM course");
    }

    public static JSONArray E2_7() {
        return Select("SELECT `name` FROM course WHERE fcid = '300001'");
    }

    public static JSONArray E2_8() {
        return Select("SELECT * FROM teacher WHERE age > 50");
    }

    public static JSONArray E2_9() {
        return Select("SELECT * FROM teacher WHERE sex = 'M'");
    }

    public static JSONArray E2_10() {
        return Select("SELECT * FROM teacher WHERE dname = 'CS'");
    }

    //=================================================================================

    public static JSONArray All(String db) {
        return Select("SELECT * FROM " + db);
    }

    public static int insertStudent(String sid, String name, String sex, int age, String birthday, String dname, String classT) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO student (sid, `name`, sex, age, birthday, dname, classT) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, sid);
            preparedStatement.setString(2, name);
            preparedStatement.setString(3, sex);
            preparedStatement.setInt(4, age);
            preparedStatement.setString(5, birthday);
            preparedStatement.setString(6, dname);
            preparedStatement.setString(7, classT);
            preparedStatement.executeUpdate();
            preparedStatement.close();
            return 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return 1;
        }
    }

    public static int insertTeacher(String tid, String name, String sex, int age, String dname) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO teacher (tid, `name`, sex, age, dname) " +
                            "VALUES (?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, tid);
            preparedStatement.setString(2, name);
            preparedStatement.setString(3, sex);
            preparedStatement.setInt(4, age);
            preparedStatement.setString(5, dname);
            preparedStatement.executeUpdate();
            preparedStatement.close();
            return 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return 1;
        }
    }

    public static int insertCourse(String cid, String name, String fcid, double credit) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO course (cid, `name`, fcid, credit) " +
                            "VALUES (?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, cid);
            preparedStatement.setString(2, name);
            preparedStatement.setString(3, fcid);
            preparedStatement.setDouble(4, credit);
            preparedStatement.executeUpdate();
            preparedStatement.close();
            return 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return 1;
        }
    }

    //=================================================================================

    public static int updateStudent(String sid, String name, String sex, int age, String birthday, String dname, String classT) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "UPDATE student SET `name` = ?, sex = ?, age = ?, birthday = ?, dname = ?, classT = ? " +
                            "WHERE sid = ?)",
                    Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(7, sid);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, sex);
            preparedStatement.setInt(3, age);
            preparedStatement.setString(4, birthday);
            preparedStatement.setString(5, dname);
            preparedStatement.setString(6, classT);
            preparedStatement.executeUpdate();
            preparedStatement.close();
            return 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return 1;
        }
    }

    public static int updateTeacher(String tid, String name, String sex, int age, String dname) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "UPDATE teacher SET `name` = ?, sex = ?, age = ?, dname = ? WHERE tid = ?",
                    Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(5, tid);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, sex);
            preparedStatement.setInt(3, age);
            preparedStatement.setString(4, dname);
            preparedStatement.executeUpdate();
            preparedStatement.close();
            return 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return 1;
        }
    }

    public static int updateCourse(String cid, String name, String fcid, double credit) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "UPDATE course SET `name` = ?, fcid = ?, credit = ? WHERE cid = ?",
                    Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(4, cid);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, fcid);
            preparedStatement.setDouble(3, credit);
            preparedStatement.executeUpdate();
            preparedStatement.close();
            return 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return 1;
        }
    }

    //=================================================================================

    public static JSONArray findStudentCourse(String sid) {
        return Select("SELECT * FROM student_course WHERE sid = " + sid);
    }

    public static JSONArray findCourse() {
        return All("course");
    }

    public static int insertStudentCourse(String sid, String cid) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT tid FROM teacher_course WHERE cid = " + cid);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            String tid = resultSet.getString("tid");
            resultSet.close();
            preparedStatement.close();

            preparedStatement = connection.prepareStatement(
                    "INSERT INTO student_course (sid, cid, tid) VALUES (?, ? ? )",
                    Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, sid);
            preparedStatement.setString(2, cid);
            preparedStatement.setString(3, tid);
            preparedStatement.executeUpdate();
            preparedStatement.close();
            return 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return 1;
        }
    }

    public static int updateStudentCourse(String sid, String cid, double score, String tid) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "UPDATE student_course SET score = ? WHERE sid = ? and cid = ? and tid = ?",
                    Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setDouble(1, score);
            preparedStatement.setString(2, sid);
            preparedStatement.setString(3, cid);
            preparedStatement.setString(4, tid);
            preparedStatement.executeUpdate();
            preparedStatement.close();
            return 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return 1;
        }
    }

    public static int deleteStudentCourse(String sid, String cid) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "DELETE FROM student_course WHERE sid = ? and cid = ?",
                    Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, sid);
            preparedStatement.setString(2, cid);
            preparedStatement.executeUpdate();
            preparedStatement.close();
            return 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return 1;
        }
    }

    //=================================================================================

    public static JSONArray E6_1() {
        return Select("SELECT DISTINCT c.NAME FROM course AS c " +
                "WHERE cid = (SELECT sc.cid FROM student_course AS sc WHERE sc.sid = student.sid)");
    }

    public static JSONArray E6_2() {
        return Select("SELECT student.NAME " +
                "FROM ( SELECT sid, avg( score ) AS average FROM student_course GROUP BY sid " +
                "ORDER BY average DESC LIMIT 10 ) AS s, student " +
                "WHERE s.sid = student.sid");
    }

    public static JSONArray E6_3() {
        return Select("SELECT student.NAME " +
                "FROM ( SELECT sid, count( cid ) AS c FROM student_course " +
                "GROUP BY sid ORDER BY c DESC LIMIT 10 ) AS s, student " +
                "WHERE s.sid = student.sid");
    }

    public static JSONArray E6_4() {
        return Select("SELECT s.sname, s.top, s.cname " +
                "FROM ( SELECT student.NAME AS sname, sc.top, sc.cname " +
                "FROM student, ( SELECT sid, c.cname, max( score ) AS top " +
                "FROM student_course, ( SELECT cid, NAME AS cname FROM course " +
                "WHERE student_course.cid = course.cid ) AS c " +
                "GROUP BY sid ) AS sc " +
                " AS s");
    }

    public static JSONArray E6_5() {
        return Select("SELECT student.NAME, s.a, s.b, s.c, s.d " +
                "FROM ( ( SELECT sid, count( * ) AS a FROM student_course " +
                "WHERE score >= 80 GROUP BY sid ) AS atb " +
                "INNER JOIN ( SELECT sid, count( * ) AS b FROM student_course " +
                "WHERE score >= 70 AND score < 80 GROUP BY sid ) AS bt " +
                "INNER JOIN ( SELECT sid, count( * ) AS c FROM student_course " +
                "WHERE score >= 60 AND score < 70 GROUP BY sid ) AS ct " +
                "INNER JOIN ( SELECT sid, count( * ) AS d FROM student_course " +
                "WHERE score < 60 GROUP BY sid ) AS dt ON atb.sid = bt.sid " +
                "AND bt.sid = ct.sid AND ct.sid = dt.sid AND dt.sid = student.sid " +
                "), student");
    }

    public static JSONArray E6_6() {
        return Select("SELECT c.NAME, c.sum, c.average " +
                "FROM ( SELECT course.NAME, count( student_course.sid ) " +
                "AS sum, avg( student_course.score ) AS average " +
                "FROM student_course, course " +
                "WHERE student_course.cid = course.cid " +
                "GROUP BY cid " +
                ") AS c");
    }

    public static JSONArray E6_7() {
        return Select("SELECT sc.cname, sc.top, sc.sname\n" +
                "FROM ( SELECT course.NAME AS cname, max( score ) AS top, student.NAME AS sname\n" +
                "FROM student, student_course, course\n" +
                "WHERE student.sid = student_course.sid AND student_course.cid = course.cid\n" +
                "GROUP BY cid ) AS sc");
    }

    public static JSONArray E6_8() {
        return Select("SELECT NAME FROM course " +
                "WHERE cid = ( SELECT cid FROM student_course GROUP BY cid " +
                "ORDER BY avg( score ) DESC LIMIT 10 )");
    }

    public static JSONArray E6_9() {
        return Select("SELECT NAME FROM course WHERE" +
                "cid = ( SELECT cid FROM student_course " +
                "GROUP BY cid ORDER BY count( sid ) DESC LIMIT 10 )");
    }
}