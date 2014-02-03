/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.webbrowser;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

/**
 *
 * @author gabo
 */
public class Browser2 extends JFrame {

    int pos = 0;
    public JButton atras;
    public JButton adelante;
    public JButton cargar;
    public JButton recargar;
    public JButton boton_historial;
    public JEditorPane contenido;
    public JPanel menu;
    public JTextField textFieldURL;
    public JScrollPane scroll;
    public ArrayList<webPages> pages;

    public Browser2() {
        super("Browser");
        BorderLayout layout = new BorderLayout(5, 5);
        this.setLayout(layout);
        int size = 20;
        pages = new ArrayList<webPages>();
        menu = new JPanel(new FlowLayout());
        ImageIcon img = new ImageIcon(getClass().getResource("atras.png"));
        ImageIcon img1 = new ImageIcon(getClass().getResource("refresh.png"));
        ImageIcon img2 = new ImageIcon(getClass().getResource("adelante.png"));
        ImageIcon img3 = new ImageIcon(getClass().getResource("play.png"));
        
        atras = new JButton(new ImageIcon(img.getImage().getScaledInstance(size, size, Image.SCALE_SMOOTH)));
        //atras.setFocusable(true);
        //atras.setContentAreaFilled(false);
        //atras.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
        //atras.setBorderPainted(false);
        //atras.setContentAreaFilled(false);
        //atras.setFocusPainted(false);
        //atras.setFocusable(true);
//atras.setSize(50, 50);
        adelante = new JButton(new ImageIcon(img2.getImage().getScaledInstance(size, size, Image.SCALE_SMOOTH)));
        //adelante.setSize(50, 50);
        recargar = new JButton(new ImageIcon(img1.getImage().getScaledInstance(size, size, Image.SCALE_SMOOTH)));
        //recargar.setSize(50, 50);
        cargar = new JButton(new ImageIcon(img3.getImage().getScaledInstance(size, size, Image.SCALE_SMOOTH)));
        //cargar.setSize(50, 50);
        boton_historial = new JButton("Historial");
        boton_historial.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                contenido.setText(historial());
            }
        });
        cargar.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                pos = pages.isEmpty() ? 0 : pages.size();
                cargarPagina();
            }
        });
        contenido = new JEditorPane();
        contenido.setContentType("text/html");
        contenido.setEditable(false);

        scroll = new JScrollPane(contenido);
        menu.add(atras);
        menu.add(adelante);
        menu.add(recargar);
        
        textFieldURL = new JTextField(50);
        textFieldURL.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                pos = pages.isEmpty() ? 0 : pages.size();
                jButton1ActionPerformed(e);
            }

            private void jButton1ActionPerformed(ActionEvent evt) {
                cargarPagina();
            }
        });
        contenido.addHyperlinkListener(new HyperlinkListener() {

            public void hyperlinkUpdate(HyperlinkEvent e) {
                if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                    System.out.println("HIPERVICULO: " + e.getURL());
                    if (e.getURL() != null) {
                        String dir = "" + e.getURL();
                        textFieldURL.setText(dir);
                        pos = pages.size();
                        cargarPagina();
                    }

                }
            }
        });
        atras.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (!pages.isEmpty()) {
                    if (pos != 0) {
                        String direccion = pages.get(--pos).nombre;
                        textFieldURL.setText(direccion);
                        cargarPagina();
                    }
                }
            }
        });

        adelante.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (!pages.isEmpty()) {
                    if (pos != (pages.size() - 1)) {
                        String direccion = pages.get(++pos).nombre;
                        textFieldURL.setText(direccion);
                        cargarPagina();
                    }
                }
            }
        });

        recargar.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                cargarPagina();
            }
        });
        menu.add(new JLabel("URL: "));
        menu.add(textFieldURL);
        menu.add(cargar);
        menu.add(boton_historial);
        add(menu, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
        setSize(1000, 800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
    
     public String historial(){
        String historial= "<h1>Historial</h1>\n ";
        if(pages.isEmpty()){
            return historial+"<h2>No existen paginas en el historial</h2>";
        }else{
            historial+="<ol>\n";
            for(int i=pages.size()-1; i>=0;i--){
                historial +="<li>"+pages.get(i)+"</li>\n";
            }
            historial+="</ol>";
            System.out.println(historial);
            return historial;
        }
        
    }
    public static void main(String args[]) {
        Browser2 b = new Browser2();
        b.setVisible(true);
    }

    public void cargarPagina() {

        String direccion = textFieldURL.getText();
        String header;

        if (direccion.equals("")) {
            //jLabel1.setText("Direccion vacia");
            pos--;
            // System.out.println("Direccion vacia");
        } else {
            if (direccion.matches("http.*")) {
                direccion = direccion.substring(direccion.indexOf("/") + 2);
            }
            if (pages.isEmpty()) {
                pages.add(new webPages(direccion));
            } else {
                if (pos == (pages.size())) {
                    pages.add(new webPages(direccion));
                }
            }
            System.out.println(direccion);
            header = Navegador.createHeader(direccion);
			
            System.out.println("------Headers--------\n" + header);
            contenido.setText(Navegador.GetWebPage(direccion, header));
        }
    }
    
    
}

