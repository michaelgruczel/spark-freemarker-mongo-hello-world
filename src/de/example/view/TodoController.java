package de.example.view;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import de.example.dao.TodoDao;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import spark.Request;
import spark.Response;
import spark.Route;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;

import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.setPort;

public class TodoController {

    private final Configuration cfg;
    private final TodoDao helloDao;

    public static void main(String[] args) throws IOException {
        new TodoController();

    }
    public TodoController() throws IOException {

        final MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb://localhost"));
        final DB db = mongoClient.getDB("sparkfreemarkerhelloworlddb");

        helloDao = new TodoDao(db);

        cfg = createFreemarkerConfiguration();
        setPort(8082);
        initializeRoutes();
    }

    private Configuration createFreemarkerConfiguration() {
        Configuration retVal = new Configuration();
        retVal.setClassForTemplateLoading(TodoController.class, "/freemarker");
        return retVal;
    }

    abstract class FreemarkerBasedRoute extends Route {
        final Template template;

        /**
         * Constructor
         *
         * @param path The route path which is used for matching. (e.g. /hello, users/:name)
         */
        protected FreemarkerBasedRoute(final String path, final String templateName) throws IOException {
            super(path);
            template = cfg.getTemplate(templateName);
        }

        @Override
        public Object handle(Request request, Response response) {
            StringWriter writer = new StringWriter();
            try {
                doHandle(request, response, writer);
            } catch (Exception e) {
                e.printStackTrace();
                response.redirect("/internal_error");
            }
            return writer;
        }

        protected abstract void doHandle(final Request request, final Response response, final Writer writer)
                throws IOException, TemplateException;

    }

    private void initializeRoutes() throws IOException {
        // this is the blog home page
        get(new FreemarkerBasedRoute("/hello", "todos.ftl") {
            @Override
            public void doHandle(Request request, Response response, Writer writer) throws IOException, TemplateException {
                HashMap<String, String> root = new HashMap<String, String>();
                root.put("todos", helloDao.getAllTodos());
                template.process(root, writer);
            }
        });



        // handle the signup post
        post(new FreemarkerBasedRoute("/hello", "todos.ftl") {
            @Override
            protected void doHandle(Request request, Response response, Writer writer) throws IOException, TemplateException {
                String todo = request.queryParams("todo");

                HashMap<String, String> root = new HashMap<String, String>();

                    helloDao.addTodo(todo);
                    root.put("todos", helloDao.getAllTodos());
                    template.process(root, writer);

                }

        });





    }


}
