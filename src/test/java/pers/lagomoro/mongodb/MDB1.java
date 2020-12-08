package pers.lagomoro.mongodb;

import java.util.*;

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
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class MDB1 {
	/**
	 * @方法简介：声明静态配置，链接MongoDB数据库
	 */
	static MongoClient mongoClient = null;
	static MongoDatabase mgdb = null;
	static {
		try {
			// 链接到MongoDB服务器
			mongoClient = new MongoClient("localhost", 27017);
			// 链接MongoDB数据库
			mgdb = mongoClient.getDatabase("user201800301158");
			System.out.println("----数据库连接成功----");
			System.out.println("当前的数据是 : " + mgdb.getName());
			// 如果MongoDB运行在安全模式需要添加如下认证.
			// boolean auth = db.authenticate(myUserName, myPassword);
			// System.out.println("Authentication: "+auth);
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
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
			collection.find().forEach(printBlock);
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
			collection.find().forEach(printBlock);
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
	@RequestMapping("/findAllDoc")
	@ResponseBody
	public static List<Document> findAllDoc(String collectionName) throws Exception {
		List<Document> findAll = new ArrayList<>();
		MongoCollection<Document> collection = mgdb.getCollection(collectionName);
		System.out.println(collectionName + "集合选择成功,文档集合如下：");
		/**
		 * 1. 获取迭代器FindIterable<Document> 2. 获取游标MongoCursor<Document> 3.
		 * 通过游标遍历检索出的文档集合
		 **/
		FindIterable<Document> findIterable = collection.find();
		MongoCursor<Document> mongoCursor = findIterable.iterator();
		while (mongoCursor.hasNext()) {
			findAll.add(mongoCursor.next());
		}
		return findAll;
	}

	/**
	 * 根据条件查询
	 *
	 * @param
	 * @查询条件 注意Bson的几个实现类，BasicDBObject, BsonDocument, BsonDocumentWrapper,
	 *       CommandResult, Document, RawBsonDocument
	 * @return 返回集合列表
	 */
	@RequestMapping("/find1")
	@ResponseBody
	public List<Document> findByage(String age,String condition, String collectionName) {

		List<Document> findList = new ArrayList<>();
		System.out.println("----查询指定条件的文档----");
		MongoCollection<Document> collection = mgdb.getCollection(collectionName);
		FindIterable<Document> iterables = collection.find(new Document("age", new BasicDBObject(condition, Integer.parseInt(age))));
		MongoCursor<Document> cursor = iterables.iterator();
		while (cursor.hasNext()) {
			findList.add(cursor.next());
		}
		return findList;
	}

	@RequestMapping("/find2")
	@ResponseBody
	public List<Document> findBy2(String age,String condition, String collectionName) {

		List<Document> findList = new ArrayList<>();
		System.out.println("----查询指定条件的文档----");
		MongoCollection<Document> collection = mgdb.getCollection(collectionName);
		FindIterable<Document> iterables = collection.find(new Document("age", new BasicDBObject(condition, Integer.parseInt(age))).append("dname","SC"));
		MongoCursor<Document> cursor = iterables.iterator();
		while (cursor.hasNext()) {
			findList.add(cursor.next());
		}
		return findList;
	}

	@RequestMapping("/find3")
	@ResponseBody
	public static List<Document> findAll(String collectionName) throws Exception {
		List<Document> findAll = new ArrayList<>();
		MongoCollection<Document> collection = mgdb.getCollection("student");
		System.out.println(collectionName + "集合选择成功,文档集合如下：");
		/**
		 * 1. 获取迭代器FindIterable<Document> 2. 获取游标MongoCursor<Document> 3.
		 * 通过游标遍历检索出的文档集合
		 **/
		FindIterable<Document> findIterable = collection.find();
		MongoCursor<Document> mongoCursor = findIterable.iterator();
		while (mongoCursor.hasNext()) {
			findAll.add(mongoCursor.next());
		}
		return findAll;
	}

	@RequestMapping("/find4")
	@ResponseBody
	public static List<Document> findAllStudent(String collectionName,String l,String m) throws Exception {
		List<Document> findAll = new ArrayList<>();
		MongoCollection<Document> collection = mgdb.getCollection("student");
		System.out.println(collectionName + "集合选择成功,文档集合如下：");
		/**
		 * 1. 获取迭代器FindIterable<Document> 2. 获取游标MongoCursor<Document> 3.
		 * 通过游标遍历检索出的文档集合
		 **/
		BasicDBObject b = new BasicDBObject();
		b.append(l,1);
		b.append(m,1);

		FindIterable<Document> findIterable = collection.find().projection(b);
		MongoCursor<Document> mongoCursor = findIterable.iterator();
		while (mongoCursor.hasNext()) {
			findAll.add(mongoCursor.next());
		}
		return findAll;
	}

	@RequestMapping("/find5")
	@ResponseBody
	public List<Document> findBy5(String collectionName) {

		BasicDBObject b = new BasicDBObject();
		b.append("name",1);
		b.append("sex",1);

		List<Document> findList = new ArrayList<>();
		System.out.println("----查询指定条件的文档----");
		MongoCollection<Document> collection = mgdb.getCollection(collectionName);
		FindIterable<Document> iterables = collection.find(new Document("age", new BasicDBObject("$lt", 20))).projection(b);
		MongoCursor<Document> cursor = iterables.iterator();
		while (cursor.hasNext()) {
			findList.add(cursor.next());
		}
		return findList;
	}

	@RequestMapping("/find6")
	@ResponseBody
	public List<Document> findBy6() {

		List<Document> findList = new ArrayList<>();
		System.out.println("----查询指定条件的文档----");
		MongoCollection<Document> collection = mgdb.getCollection("course");
		FindIterable<Document> iterables = collection.find();
		MongoCursor<Document> cursor = iterables.iterator();
		while (cursor.hasNext()) {
			findList.add(cursor.next());
		}
		return findList;
	}

	@RequestMapping("/find7")
	@ResponseBody
	public List<Document> findBy7() {

		List<Document> findList = new ArrayList<>();
		System.out.println("----查询指定条件的文档----");
		MongoCollection<Document> collection = mgdb.getCollection("course");
		FindIterable<Document> iterables = collection.find(new Document("fcid",new Document("$eq","300001")));
		MongoCursor<Document> cursor = iterables.iterator();
		while (cursor.hasNext()) {
			findList.add(cursor.next());
		}
		return findList;
	}

	@RequestMapping("/find8")
	@ResponseBody
	public List<Document> findBy8() {
		List<Document> findList = new ArrayList<>();
		System.out.println("----查询指定条件的文档----");
		MongoCollection<Document> collection = mgdb.getCollection("teacher");
		FindIterable<Document> iterables = collection.find(new Document("age", new BasicDBObject("$gt", 50)));
		MongoCursor<Document> cursor = iterables.iterator();
		while (cursor.hasNext()) {
			findList.add(cursor.next());
		}
		return findList;
	}

	@RequestMapping("/find9")
	@ResponseBody
	public List<Document> findBy9() {
		List<Document> findList = new ArrayList<>();
		System.out.println("----查询指定条件的文档----");
		MongoCollection<Document> collection = mgdb.getCollection("teacher");
		FindIterable<Document> iterables = collection.find(new Document("sex", new BasicDBObject("$eq", "M")));
		MongoCursor<Document> cursor = iterables.iterator();
		while (cursor.hasNext()) {
			findList.add(cursor.next());
		}
		return findList;
	}

	@RequestMapping("/find10")
	@ResponseBody
	public List<Document> findBy10() {
		List<Document> findList = new ArrayList<>();
		System.out.println("----查询指定条件的文档----");
		MongoCollection<Document> collection = mgdb.getCollection("teacher");
		FindIterable<Document> iterables = collection.find(new Document("dname", new BasicDBObject("$eq", "CS")));
		MongoCursor<Document> cursor = iterables.iterator();
		while (cursor.hasNext()) {
			findList.add(cursor.next());
		}
		return findList;
	}

	@RequestMapping("/find11")
	@ResponseBody
	public static List<Document> findAllt() throws Exception {
		List<Document> findAll = new ArrayList<>();
		MongoCollection<Document> collection = mgdb.getCollection("teacher");
		System.out.println("teacher" + "集合选择成功,文档集合如下：");
		/**
		 * 1. 获取迭代器FindIterable<Document> 2. 获取游标MongoCursor<Document> 3.
		 * 通过游标遍历检索出的文档集合
		 **/
		FindIterable<Document> findIterable = collection.find();
		MongoCursor<Document> mongoCursor = findIterable.iterator();
		while (mongoCursor.hasNext()) {
			findAll.add(mongoCursor.next());
		}
		return findAll;
	}

	@RequestMapping("/find12")
	@ResponseBody
	public static List<Document> findAllc() throws Exception {
		List<Document> findAll = new ArrayList<>();
		MongoCollection<Document> collection = mgdb.getCollection("course");
		System.out.println("course" + "集合选择成功,文档集合如下：");
		/**
		 * 1. 获取迭代器FindIterable<Document> 2. 获取游标MongoCursor<Document> 3.
		 * 通过游标遍历检索出的文档集合
		 **/
		FindIterable<Document> findIterable = collection.find();
		MongoCursor<Document> mongoCursor = findIterable.iterator();
		while (mongoCursor.hasNext()) {
			findAll.add(mongoCursor.next());
		}
		return findAll;
	}

	@RequestMapping("/insert/student")
	@ResponseBody
	public int insertS(String collectionName,String sid,String name,String sex,String age,String birthday,String dname,String classT) {
		System.out.println("----像student表中插入数据----");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sid", sid);
		map.put("name", name);
		map.put("sex", sex);
		map.put("age", Integer.parseInt(age));
		map.put("birthday", birthday);
		map.put("dname", dname);
		map.put("class", classT);
		try {
			MongoCollection<Document> collection = mgdb.getCollection(collectionName);
			collection.insertOne(new Document(map));
			System.out.println("\n"+"----插入一个文档----");
			findAllDoc(collectionName);
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			return 0;
		}
		return 1;
	}
	@RequestMapping("/insert/teacher")
	@ResponseBody
	public int insertT(String collectionName,String tid,String name,String sex,String age,String dname) {
		System.out.println("----像teacher表中插入数据----");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("tid", tid);
		map.put("name", name);
		map.put("sex", sex);
		map.put("age", Integer.parseInt(age));
		map.put("dname", dname);
		try {
			MongoCollection<Document> collection = mgdb.getCollection(collectionName);
			collection.insertOne(new Document(map));
			System.out.println("\n"+"----插入一个文档----");
			findAllDoc(collectionName);
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			return 0;
		}
		return 1;
	}

	@RequestMapping("/insert/course")
	@ResponseBody
	public int insertC(String collectionName,String cid,String name,String fcid,String credit) {
		System.out.println("----像course表中插入数据----");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("cid", cid);
		map.put("name", name);
		map.put("fcid", fcid);
		map.put("credit", credit);
		try {
			MongoCollection<Document> collection = mgdb.getCollection(collectionName);
			collection.insertOne(new Document(map));
			System.out.println("\n"+"----插入一个文档----");
			findAllDoc(collectionName);
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			return 0;
		}
		return 1;
	}

	@RequestMapping("/update/teacher")
	@ResponseBody
	public int update(String[] tid,String[] name,String[] sex,int[] age,String[] dname){
		System.out.println("----更新teacher数据----");
		try {
			BasicDBObject query;
			BasicDBObject update1,update2,update3,update4;
			MongoCollection<Document> collection = mgdb.getCollection("teacher");
			for (int i=0;i<tid.length;i++){
				query = new BasicDBObject("tid", tid[i]);
				update1 = new BasicDBObject("name", name[i]);
				update2 = new BasicDBObject("sex", sex[i]);
				update3 = new BasicDBObject("age", age[i]);
				update4 = new BasicDBObject("dname", dname[i]);
				collection.updateOne(query, new Document("$set",update1));
				collection.updateOne(query, new Document("$set",update2));
				collection.updateOne(query, new Document("$set",update3));
				collection.updateOne(query, new Document("$set",update4));
			}
			return 0;
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			return 1;
		}
	}

	@RequestMapping("/update/student")
	@ResponseBody
	public int updates(String[] sid,String[] name,String[] sex,int[] age,String[] birthday,String[] dname,String[] classT){
		System.out.println("----更新student数据----");
		try {
			BasicDBObject query;
			BasicDBObject update1,update2,update3,update4,update5,update6;
			MongoCollection<Document> collection = mgdb.getCollection("student");
			for (int i=0;i<sid.length;i++){
				query = new BasicDBObject("sid", sid[i]);
				update1 = new BasicDBObject("name", name[i]);
				update2 = new BasicDBObject("sex", sex[i]);
				update3 = new BasicDBObject("age", age[i]);
				update4 = new BasicDBObject("birthday", birthday[i]);
				update5 = new BasicDBObject("dname", dname[i]);
				update6 = new BasicDBObject("class", classT[i]);
				collection.updateOne(query, new Document("$set",update1));
				collection.updateOne(query, new Document("$set",update2));
				collection.updateOne(query, new Document("$set",update3));
				collection.updateOne(query, new Document("$set",update4));
				collection.updateOne(query, new Document("$set",update5));
				collection.updateOne(query, new Document("$set",update6));
			}
			return 0;
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			return 1;
		}
	}
	@RequestMapping("/update/course")
	@ResponseBody
	public int updatec(String[] cid,String[] name,String[] fcid,String[] credit){
		try{
			System.out.println(fcid[0]);
		}catch (Exception e){
			fcid = new String[]{""};
		}
		System.out.println("----更新course数据----");
		try {
			BasicDBObject query;
			BasicDBObject update1,update2,update3,update4;
			MongoCollection<Document> collection = mgdb.getCollection("course");
			for (int i=0;i<cid.length;i++){
				query = new BasicDBObject("cid", cid[i]);
				update1 = new BasicDBObject("name", name[i]);
				update3 = new BasicDBObject("credit", credit[i]);
				collection.updateOne(query, new Document("$set",update1));
				collection.updateOne(query, new Document("$set",update3));
					update2 = new BasicDBObject("fcid", "");
					collection.updateOne(query, new Document("$set",update2));
			}
			return 0;
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			return 1;
		}
	}
	@RequestMapping("/choose1")
	@ResponseBody
	public List<Document> choose1(String sid) {
		List<Document> findList = new ArrayList<>();
		System.out.println("----查询指定条件的文档----");
		MongoCollection<Document> collection = mgdb.getCollection("student_course");
		FindIterable<Document> iterables = collection.find(new Document("sid",new Document("$eq",sid)));
		MongoCursor<Document> cursor = iterables.iterator();
		while (cursor.hasNext()) {
			findList.add(cursor.next());
		}
		return findList;
	}

	@RequestMapping("/choose2")
	@ResponseBody
	public List<Document> choose2() {
		List<Document> findList = new ArrayList<>();
		System.out.println("----查询指定条件的文档----");
		MongoCollection<Document> collection = mgdb.getCollection("course");
		FindIterable<Document> iterables = collection.find();
		MongoCursor<Document> cursor = iterables.iterator();
		while (cursor.hasNext()) {
			findList.add(cursor.next());
		}
		return findList;
	}

	@RequestMapping("/choose3")
	@ResponseBody
	public int choose3(String sid,String cid){
		Map<String, Object> map = new HashMap<String, Object>();
		String tid = "";
		MongoCollection<Document> collection = mgdb.getCollection("teacher_course");
		BasicDBObject b = new BasicDBObject();
		b.append("tid",1);
		FindIterable<Document> findIterable = collection.find(new Document("cid",new Document("$eq",cid))).projection(b);
		MongoCursor<Document> mongoCursor = findIterable.iterator();
		while (mongoCursor.hasNext()) {
			tid = (String) mongoCursor.next().get("tid");
		}
		try {
			MongoCollection<Document> collection2 = mgdb.getCollection("student_course");
			map.put("sid",sid);
			map.put("cid",cid);
			map.put("tid",tid);
			collection2.insertOne(new Document(map));
			System.out.println("\n"+"----插入一个文档----");
			return 1;
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			return 0;
		}
	}

	@RequestMapping("/choose4")
	@ResponseBody
	public int choose4(String sid,String cid){
		try {
			MongoCollection<Document> collection = mgdb.getCollection("student_course");
			System.out.println("----移除第一个符合条件的文档前----");
			// 删除文档集合
			Document query2 = new Document();
			DeleteResult result = collection.deleteOne(new Document("sid",new BasicDBObject("$eq",sid)).append("cid",new BasicDBObject("$eq",cid)));
			System.out.println("----移除第一个符合条件的文档后----");
			System.out.println("移除的统计结果是："+result.toString());
			return 1;
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			return 0;
		}
	}

	@RequestMapping("/insert1")
	@ResponseBody
	public int insert1(String sid,String name,String sex){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sid", sid);
		map.put("name", name);
		map.put("sex", sex);
		try {
			MongoCollection<Document> collection = mgdb.getCollection("student");
			collection.insertOne(new Document(map));
			System.out.println("\n"+"----插入一个文档----");
			findAllDoc("student");
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			return 0;
		}
		return 1;
	}

	@RequestMapping("/insert2")
	@ResponseBody
	public int insert2(String cid,String name,int credit,int hour,String type){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("cid", cid);
		map.put("name", name);
		map.put("credit", credit);
		map.put("hour", hour);
		map.put("type", type);
		try {
			MongoCollection<Document> collection = mgdb.getCollection("course");
			collection.insertOne(new Document(map));
			System.out.println("\n"+"----插入一个文档----");
			findAllDoc("course");
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			return 0;
		}
		return 1;
	}

	@RequestMapping("/insert3")
	@ResponseBody
	public int insert3(String sid,String cid,String name,int credit,int hour,String type,int score){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sid", sid);
		map.put("cid", cid);
		map.put("name", name);
		map.put("credit", credit);
		map.put("hour", hour);
		map.put("type", type);
		map.put("score", score);
		try {
			MongoCollection<Document> collection = mgdb.getCollection("student_course");
			collection.insertOne(new Document(map));
			System.out.println("\n"+"----插入一个文档----");
			findAllDoc("student_course");
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			return 0;
		}
		return 1;
	}

	@RequestMapping("/find61")
	@ResponseBody
	public List<String> find61(){
		List<String> findAll = new ArrayList<>();
		Map<String, Object> map = new HashMap<String, Object>();
		MongoCollection<Document> collection = mgdb.getCollection("student_course1");
		BasicDBObject b = new BasicDBObject();
		b.append("课程名",1);
		FindIterable<Document> findIterable = collection.find().projection(b);
		MongoCursor<Document> mongoCursor = findIterable.iterator();
		while (mongoCursor.hasNext()) {
			findAll.add((String) mongoCursor.next().get("课程名"));
		}
		HashSet h = new HashSet(findAll);
		findAll.clear();
		findAll.addAll(h);
		System.out.println(findAll.size());
		return findAll;
	}
}
