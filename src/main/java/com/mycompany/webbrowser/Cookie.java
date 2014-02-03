/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mycompany.webbrowser;

import java.util.Date;

/**
 *
 * @author gabo
 */
public class Cookie {
    private String name;
    private String value;
    private String path;
    private Date expireDate = null;
    public Cookie(String name, String value){
        this.name= name;
        this.value=value;
    }
    public void setName(String name){
        this.name= name;
        
    }
    public void setValue(String value){
        this.value= value;
    }
    public String getName(){
        return name;
    }
    public String getValue(){
        return value;
    }
    @Override
    public String toString(){
        return name+"="+value;
    }
}

