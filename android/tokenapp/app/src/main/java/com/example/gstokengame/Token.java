package com.example.gstokengame;

public class Token {
    private String Id;
    private String Content;
    private String Created;
    private String Consumed;
    private String ConsumedA;


    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public String getCreated() {
        return Created;
    }

    public void setCreated(String created) {
        Created = created;
    }

    public String getConsumed() {
        return Consumed;
    }

    public void setConsumed(String consumed) {
        Consumed = consumed;
    }

    public String getConsumedA() {
        return ConsumedA;
    }

    public void setConsumedA(String consumedA) {
        ConsumedA = consumedA;
    }
}
