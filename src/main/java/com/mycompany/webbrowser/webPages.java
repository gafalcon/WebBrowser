/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mycompany.webbrowser;

import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author gabo
 */
public class webPages {
    public String nombre;
    public Date date;
    
    public webPages(String name){
        date = new Date();
        this.nombre = name;
    }
    
    public String toString(){
        return date+": <a href=\""+nombre +"\">"+nombre+"</a>"; 
    }
}
