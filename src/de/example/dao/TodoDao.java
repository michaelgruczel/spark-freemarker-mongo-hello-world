package de.example.dao;

import com.mongodb.*;

public class TodoDao {

    private final DBCollection aCollection;


    public TodoDao(DB db) {
        aCollection = db.getCollection("todos");
    }

    public void addTodo(String todo) {


        BasicDBObject dbTodo = new BasicDBObject();
        dbTodo.append("todo", todo);


        try {
            aCollection.insert(dbTodo);
        } catch (MongoException.DuplicateKey e) {
            // do nothing in this example
        }
    }

    public String getAllTodos() {


        StringBuilder todos = new StringBuilder();

        try {
            DBCursor cur = aCollection.find();
            while(cur.hasNext()) {

                String todo = (String) cur.next().get("todo");
                todos.append(todo + " ; ");

            }
        } catch (MongoException.DuplicateKey e) {
            // do nothing in this example
        }
        return todos.toString();
    }


}
