package cn.edu.fudan.yst.model;

/**
 * @author gzdaijie
 */
public class MyResponse {
    private String description = "OK";
    private Object body;

    public MyResponse(Object body) {
        this.body = body;
    }

    public MyResponse(String description, Object body) {
        this.description = description;
        this.body = body;
    }



    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }
}
