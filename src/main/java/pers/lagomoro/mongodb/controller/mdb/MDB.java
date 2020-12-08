package pers.lagomoro.mongodb.controller.mdb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import org.apache.log4j.Logger;
import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.BasicDBObject;
import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.CreateCollectionOptions;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

public class MDB {
    /**
     * @方法简介：声明静态配置，链接MongoDB数据库
     */
    static MongoClient mongoClient = null;
    static MongoDatabase mgdb = null;

    private static Logger logger = Logger.getLogger(MDB.class);

    static {
        try {
            // 链接到MongoDB服务器
            mongoClient = new MongoClient("localhost", 27017);
            // 链接MongoDB数据库
            mgdb = mongoClient.getDatabase("user201800301015");
            logger.info("数据库连接成功");
            logger.info("当前的数据是 : " + mgdb.getName());
            // 如果MongoDB运行在安全模式需要添加如下认证.
            // boolean auth = db.authenticate(myUserName, myPassword);
            // System.out.println("Authentication: "+auth);
        } catch (Exception e) {
            logger.error(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    /**
     * @方法简介 创建集合，列出所有的集合名称
     * @collectionName 集合名称
     */
    public static int CreateCollection(String collectionName) {
        for (String name : mgdb.listCollectionNames()) {
            if (name.equals(collectionName)) {
                System.out.println("需要创建的集合已经存在");
                return 0;
            }
        }
        // 创建集合
        mgdb.createCollection(collectionName, new CreateCollectionOptions().capped(false).sizeInBytes(0x100000));
        System.out.println("----集合创建成功----");
        System.out.println("当前数据库中的所有集合是：");
        for (String name : mgdb.listCollectionNames()) {
            System.out.print(name+" ");
        }
        return 1;
    }

    /**
     * 移除集合
     *
     * @param collectionName
     */
    public static void RemoveCollection(String collectionName) {
        for (String name : mgdb.listCollectionNames()) {
            if (name.equals(collectionName)){
                mgdb.getCollection(collectionName).drop();
                System.out.println("----移除集合" + collectionName+"----");
            }
        }
    }

    /**
     * @方法简介 插入一个文档
     * @param collectionName
     */
    public static void InsertDocument(String collectionName, Map<String, Object> map) {
        try {
            MongoCollection<Document> collection = mgdb.getCollection(collectionName);
            collection.insertOne(new Document(map));
            System.out.println("\n"+"----插入一个文档----");
            findAllDoc(collectionName);
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    /**
     * @方法简介 插入多个文档
     * @param collectionName
     *            是一个key-value集合
     * @throws Exception
     */
    public static void insertMany(String collectionName, ArrayList<Map<String, Object>> mapList) throws Exception {
        List<Document> documents = new ArrayList<Document>();
        MongoCollection<Document> collection = mgdb.getCollection(collectionName);
        for (Map<String, Object> map : mapList) {
            Document doc = new Document(map);
            documents.add(doc);
        }
        collection.insertMany(documents);
        System.out.println("----插入多个文档----");
        findAllDoc(collectionName);
    }

    /**
     * @方法简介 更新查到的第一个文档
     * @query 更新前的值
     * @update 更新后的值
     */
    public static void updateDocument(String collectionName, BasicDBObject query, BasicDBObject update) {
        try {
            MongoCollection<Document> collection = mgdb.getCollection(collectionName);
            UpdateResult result = collection.updateOne(query, new Document("$set",update));
            System.out.println("----更新查到的第一个文档-----");
            findAllDoc(collectionName);
            System.out.println("更新的统计结果是："+result.toString());
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    /**
     * @方法简介 更新所有的文档
     * @query 更新前的值
     * @update 更新后的值
     */
    public static void updateManyDocument(String collectionName, BasicDBObject query, BasicDBObject update) {
        try {
            MongoCollection<Document> collection = mgdb.getCollection(collectionName);
            UpdateResult result = collection.updateMany(query, new Document("$set",update));
            System.out.println("----更新所有的文档----");
            findAllDoc(collectionName);
            System.out.println("更新的统计结果是："+result.toString());

        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    /**
     * @方法简介 移除第一个符合条件的文档
     */
    public static void removeFirstDoc(String collectionName, String key, Object value) {
        try {
            MongoCollection<Document> collection = mgdb.getCollection(collectionName);
            System.out.println("----移除第一个符合条件的文档前----");
            collection.find().forEach((Consumer<? super Document>) printBlock);
            // 删除文档集合
            DeleteResult result = collection.deleteOne(Filters.eq(key, value));
            System.out.println("----移除第一个符合条件的文档后----");
            findAllDoc(collectionName);
            System.out.println("移除的统计结果是："+result.toString());
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    /**
     * @方法简介 移除所有符合条件的文档
     */
    public static void removeAllDoc(String collectionName, String key, Object value) {
        try {
            MongoCollection<Document> collection = mgdb.getCollection(collectionName);
            System.out.println("----移除所有符合条件的文档前----");
            collection.find().forEach((Consumer<? super Document>) printBlock);
            // 删除文档集合
            DeleteResult result = collection.deleteMany(Filters.eq(key, value));
            System.out.println("----移除所有符合条件的文档后----");
            findAllDoc(collectionName);
            System.out.println("移除的统计结果是："+result.toString());
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    static Block<Document> printBlock = new Block<Document>() {
        public void apply(final Document document) {
            System.out.println(document.toJson());
        }
    };

    /**
     * @方法简介 检索指定集合中所有的文档
     * @param collectionName
     *            集合名称
     */
    public static void findAllDoc(String collectionName) throws Exception {

        MongoCollection<Document> collection = mgdb.getCollection(collectionName);
        System.out.println(collectionName + "集合选择成功,文档集合如下：");
        /**
         * 1. 获取迭代器FindIterable<Document> 2. 获取游标MongoCursor<Document> 3.
         * 通过游标遍历检索出的文档集合
         **/
        FindIterable<Document> findIterable = collection.find();
        MongoCursor<Document> mongoCursor = findIterable.iterator();
        while (mongoCursor.hasNext()) {
            System.out.println(mongoCursor.next());
        }
    }

    /**
     * 根据条件查询
     *
     * @param filter
     * @查询条件 注意Bson的几个实现类，BasicDBObject, BsonDocument, BsonDocumentWrapper,
     *       CommandResult, Document, RawBsonDocument
     * @return 返回集合列表
     */
    public static void findBy(Bson filter, String collectionName) {
        System.out.println("----查询指定条件的文档----");
        MongoCollection<Document> collection = mgdb.getCollection(collectionName);
        FindIterable<Document> iterables = collection.find(filter);
        MongoCursor<Document> cursor = iterables.iterator();
        while (cursor.hasNext()) {
            System.out.println(cursor.next());
        }
    }

    public static void main(String[] args) throws Exception {
        String collectionName = "student";// j集合名称
        Map<String, Object> map1 = new HashMap<String, Object>();
        map1.put("sid", "2023222");
        map1.put("name", "wangfang");
        map1.put("age", 12);
        Map<String, Object> map2 = new HashMap<String, Object>();
        map2.put("sid", "2023224");
        map2.put("name", "lihong");
        map2.put("age", 13);
        Map<String, Object> map3 = new HashMap<String, Object>();
        map3.put("sid", "2023220");
        map3.put("name", "jianghong");
        map3.put("age", 13);
        Map<String, Object> map4 = new HashMap<String, Object>();
        map4.put("sid", "2023221");
        map4.put("name", "liqian");
        map4.put("age", 16);
        ArrayList<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
        mapList.add(map2);
        mapList.add(map3);
        mapList.add(map4);
        BasicDBObject query1 = new BasicDBObject("name", "lihong");
        BasicDBObject update1 = new BasicDBObject("name", "xumao");
        BasicDBObject query2 = new BasicDBObject("age", 13);
        BasicDBObject update2 = new BasicDBObject("age", 17);
        // 移除集合
        RemoveCollection(collectionName);
        // 创建集合
        CreateCollection(collectionName);
        // 插入一个文档
        InsertDocument(collectionName, map1);
        // 插入多个文档
        insertMany(collectionName,mapList);
        // 更新第一个文档
        updateDocument(collectionName, query1, update1);
        // 更新所有的文档
        updateManyDocument(collectionName, query2, update2);
        // 删除第一个文档
        removeFirstDoc(collectionName,"sid","2023224");
        // 删除所有满足条件的文档
        removeAllDoc(collectionName,"age",17);
        //查询所有的文档
        findAllDoc(collectionName);
        //查询指定条件的文档:Document,BasicDBOject用法基本一致
        System.out.println("小于等于12的输出集合为：");
        findBy(new Document("age", new BasicDBObject("$lte", 12)),collectionName);
        System.out.println("大于12小于20的输出集合为：");
        findBy(new Document("age", new BasicDBObject("$gt", 12).append("$lte",20)),collectionName);
    }

    //=================================================================================
    //  * 实验内容
    //=================================================================================

    public static List<Document> findDocument(MongoCollection<Document> collection, Document document) {
        List<Document> findList = new ArrayList<>();
        FindIterable<Document> iterables = collection.find(document);
        MongoCursor<Document> cursor = iterables.iterator();
        while (cursor.hasNext()) {
            findList.add(cursor.next());
        }
        return findList;
    }

    public static List<Document> findAll(MongoCollection<Document> collection) {
        List<Document> findList = new ArrayList<>();
        FindIterable<Document> iterables = collection.find();
        MongoCursor<Document> cursor = iterables.iterator();
        while (cursor.hasNext()) {
            findList.add(cursor.next());
        }
        return findList;
    }

    public static List<Document> findProj(MongoCollection<Document> collection, BasicDBObject basicDBObject) {
        List<Document> findList = new ArrayList<>();
        FindIterable<Document> iterables = collection.find().projection(basicDBObject);
        MongoCursor<Document> cursor = iterables.iterator();
        while (cursor.hasNext()) {
            findList.add(cursor.next());
        }
        return findList;
    }

    public static List<Document> findProjDocument(MongoCollection<Document> collection, Document document, BasicDBObject basicDBObject) {
        List<Document> findList = new ArrayList<>();
        FindIterable<Document> iterables = collection.find(document).projection(basicDBObject);
        MongoCursor<Document> cursor = iterables.iterator();
        while (cursor.hasNext()) {
            findList.add(cursor.next());
        }
        return findList;
    }

    //=================================================================================

    public static List<Document> E2_1(String collectionName, String condition, int age) {
        return findDocument(mgdb.getCollection(collectionName),
                new Document("age", new BasicDBObject(condition, age)));
    }
    public static List<Document> E2_2(String collectionName, String condition, int age) {
        return findDocument(mgdb.getCollection(collectionName),
                new Document("age", new BasicDBObject(condition, age)).append("dname", "SC"));
    }
    public static List<Document> E2_3(String collectionName) {
        return findAll(mgdb.getCollection(collectionName));
    }
    public static List<Document> E2_4(String collectionName, String a, String b) {
        return findProj(mgdb.getCollection(collectionName),
                new BasicDBObject().append(a,1).append(b,1));
    }
    public static List<Document> E2_5(String collectionName, String a, String b) {
        return findProjDocument(mgdb.getCollection(collectionName),
                new Document("age", new BasicDBObject("$lt", 20)),
                new BasicDBObject().append(a,1).append(b,1));
    }
    public static List<Document> E2_6(String collectionName) {
        return findAll(mgdb.getCollection(collectionName));
    }
    public static List<Document> E2_7(String collectionName, String condition, int fcid) {
        return findDocument(mgdb.getCollection(collectionName),
                new Document("fcid", new BasicDBObject(condition, fcid)));
    }
    public static List<Document> E2_8(String collectionName, String condition, int age) {
        return findDocument(mgdb.getCollection(collectionName),
                new Document(new Document("age", new BasicDBObject(condition, age))));
    }
    public static List<Document> E2_9(String collectionName, String condition, String sex) {
        return findDocument(mgdb.getCollection(collectionName),
                new Document(new Document("sex", new BasicDBObject(condition, sex))));
    }
    public static List<Document> E2_10(String collectionName, String condition, String dname) {
        return findDocument(mgdb.getCollection(collectionName),
                new Document("dname", new BasicDBObject(condition, dname)));
    }

    //=================================================================================

    public static List<Document> All(String collectionName) {
        return findAll(mgdb.getCollection(collectionName));
    }

    public static int insertStudent(String collectionName, long sid, String name, String sex, int age, String birthday, String dname, int classT) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("sid", sid);
        map.put("name", name);
        map.put("sex", sex);
        map.put("age", age);
        map.put("birthday", birthday);
        map.put("dname", dname);
        map.put("class", classT);
        try {
            MongoCollection<Document> collection = mgdb.getCollection(collectionName);
            collection.insertOne(new Document(map));
            findAllDoc(collectionName);
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            return 0;
        }
        return 1;
    }
    public static int insertTeacher(String collectionName, int tid, String name, String sex, int age, String dname) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("tid", tid);
        map.put("name", name);
        map.put("sex", sex);
        map.put("age", age);
        map.put("dname", dname);
        try {
            MongoCollection<Document> collection = mgdb.getCollection(collectionName);
            collection.insertOne(new Document(map));
            findAllDoc(collectionName);
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            return 0;
        }
        return 1;
    }
    public static int insertCourse(String collectionName, int cid, String name, int fcid, double credit) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("cid", cid);
        map.put("name", name);
        map.put("fcid", fcid);
        map.put("credit", credit);
        try {
            MongoCollection<Document> collection = mgdb.getCollection(collectionName);
            collection.insertOne(new Document(map));
            findAllDoc(collectionName);
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            return 0;
        }
        return 1;
    }

    //=================================================================================

    public static int updateStudent(String collectionName, long sid, String name, String sex, int age, String birthday, String dname, int classT){
        try {
            BasicDBObject sidObject = new BasicDBObject("sid", sid);
            MongoCollection<Document> collection = mgdb.getCollection("student");
            collection.updateOne(sidObject, new Document("$set",new BasicDBObject("name", name)));
            collection.updateOne(sidObject, new Document("$set",new BasicDBObject("sex", sex)));
            collection.updateOne(sidObject, new Document("$set",new BasicDBObject("age", age)));
            collection.updateOne(sidObject, new Document("$set",new BasicDBObject("birthday", birthday)));
            collection.updateOne(sidObject, new Document("$set",new BasicDBObject("dname", dname)));
            collection.updateOne(sidObject, new Document("$set",new BasicDBObject("class", classT)));
            return 0;
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            return 1;
        }
    }
    public static int updateTeacher(String collectionName, int tid, String name, String sex, int age, String dname){
        try {
            BasicDBObject tidObject = new BasicDBObject("tid", tid);
            MongoCollection<Document> collection = mgdb.getCollection(collectionName);
            collection.updateOne(tidObject, new Document("$set", new BasicDBObject("name", name)));
            collection.updateOne(tidObject, new Document("$set", new BasicDBObject("sex", sex)));
            collection.updateOne(tidObject, new Document("$set", new BasicDBObject("age", age)));
            collection.updateOne(tidObject, new Document("$set", new BasicDBObject("dname", dname)));
            return 0;
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            return 1;
        }
    }
    public static int updateCourse(String collectionName, int cid, String name, int fcid, double credit){
        try {
            BasicDBObject cidObject = new BasicDBObject("cid", cid);
            MongoCollection<Document> collection = mgdb.getCollection(collectionName);
            collection.updateOne(cidObject, new Document("$set",new BasicDBObject("name", name)));
            collection.updateOne(cidObject, new Document("$set",new BasicDBObject("fcid", fcid)));
            collection.updateOne(cidObject, new Document("$set",new BasicDBObject("credit", credit)));
            return 0;
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            return 1;
        }
    }

    //=================================================================================

    public static List<Document> findStudentCourse(String collectionName, long sid) {
        List<Document> findList = new ArrayList<>();
        MongoCollection<Document> collection = mgdb.getCollection(collectionName);
        FindIterable<Document> iterables = collection.find(new Document("sid", new Document("$eq", sid)));
        MongoCursor<Document> cursor = iterables.iterator();
        while (cursor.hasNext()) {
            findList.add(cursor.next());
        }
        return findList;
    }
    public static List<Document> findCourse(String collectionName) {
        return findAll(mgdb.getCollection(collectionName));
    }
    public static int insertStudentCourse(long sid, int cid){
        Map<String, Object> map = new HashMap<String, Object>();
        double tid = 0;
        MongoCollection<Document> collection = mgdb.getCollection("teacher_course");
        FindIterable<Document> findIterable = collection.find(
                new Document("cid", new Document("$eq", cid))).projection(
                        new BasicDBObject().append("tid",1));
        MongoCursor<Document> mongoCursor = findIterable.iterator();
        while (mongoCursor.hasNext()) {
            tid = (double)mongoCursor.next().get("tid");
        }
        try {
            MongoCollection<Document> collection2 = mgdb.getCollection("student_course");
            map.put("sid",sid);
            map.put("cid",cid);
            map.put("tid",tid);
            collection2.insertOne(new Document(map));
            return 1;
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            return 0;
        }
    }
    public static int updateStudentCourse(String collectionName, long sid, int cid, double score, int tid){
        try {
            BasicDBObject idObject = new BasicDBObject("sid", sid).append("cid", cid);
            MongoCollection<Document> collection = mgdb.getCollection(collectionName);
            collection.updateOne(idObject, new Document("$set",new BasicDBObject("score", score)));
            collection.updateOne(idObject, new Document("$set",new BasicDBObject("tid", tid)));
            return 0;
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            return 1;
        }
    }
    public static int deleteStudentCourse(long sid, int cid){
        try {
            MongoCollection<Document> collection = mgdb.getCollection("student_course");
            DeleteResult result = collection.deleteOne(new Document("sid",
                    new BasicDBObject("$eq", sid)).append("cid", new BasicDBObject("$eq", cid)));
            System.out.println("移除：" + result.toString());
            return 1;
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            return 0;
        }
    }

}
