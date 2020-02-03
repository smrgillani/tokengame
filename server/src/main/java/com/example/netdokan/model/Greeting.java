/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.netdokan.model;

import java.sql.Timestamp;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * @author shahzadmasud
 */
@Entity
public class Greeting {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id = 0L;

    private String content;
    private Timestamp created = new Timestamp(System.currentTimeMillis());
    private Timestamp consumedAt = null;
    private Boolean consumed = Boolean.FALSE;

    public Greeting() {
    }
    
    public Greeting(String content) {
        this.content = content;
    }
    
    public Greeting(long id, String content) {
        this.id = id;
        this.content = content;
    }

    public Boolean getConsumed() {
        return consumed;
    }

    public void setConsumed(Boolean consumed) {
        this.consumed = consumed;
    }

    public Timestamp getConsumedAt() {
        return consumedAt;
    }

    public Timestamp getCreated() {
        return created;
    }

    public void setConsumedAt(Timestamp consumedAt) {
        this.consumedAt = consumedAt;
        this.consumed = Boolean.TRUE ;
    }

    public void setConsumedAt() {
        setConsumedAt(new Timestamp(System.currentTimeMillis()));
    }

    public long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Greeting other = (Greeting) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
