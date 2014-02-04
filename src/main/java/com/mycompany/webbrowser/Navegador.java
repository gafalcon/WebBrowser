package com.mycompany.webbrowser;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author adrian
 */
public class Navegador {

    public ArrayList<webPages> pages;
    public static int redirect = 0;
    

    public static String GetWebPage(String direccion, String header) {
        Socket socket;
        String html = "";
        DataOutputStream os;
        DataInputStream is;

        try {
            socket = new Socket(getHost(direccion), 80);
            socket.setSoTimeout(10000);
            os = new DataOutputStream(socket.getOutputStream());
            is = new DataInputStream(socket.getInputStream());

            os.write(header.getBytes());
            //jLabel1.setText("Sending header....");
            //this.repaint();
            System.out.println("Sending header....");
            os.flush();
            //os.close();
            String str = is.readLine();

            if (str.matches("HTTP/1\\.[0-1] 302 .*")) {
                System.out.println("Redirecting......");
//                jLabel1.setText("Redirecting...");
                return (redirect(is));
            } else {
                do {
                    str = is.readLine();
                    //System.out.println(str);
                    if(str.matches(".*Set-Cookie:.*"))
                        System.out.print("***COOKIE***");
                    System.out.println(str);
                } while (!str.matches(".*<!DOCTYPE") && str != null && !str.matches(".*<.*"));
                html = str;
                do {
                    
                    
                    str = is.readLine();
                    html = html + "\n" + str;
                                        
                    if(str != null && (str.toLowerCase().matches(".*</body>.*") || str.toLowerCase().matches(".*</html>.*")))
                        break;
                } while (str != null);
                System.out.println("*********Salio");
                if(str != null && !str.toLowerCase().matches(".*</html>"))
                    html += "</html>";
                System.out.println(html);
                //jEditorPane1.setText(html);
                is.close();
                os.close();
                socket.close();
            }
        } catch (UnknownHostException ex) {
  //          jLabel1.setText("Host desconocido");
    //        this.repaint();
            System.err.println("Host desconocido");
            return "<h1>Host Desconocido</h1>";
        } catch (IOException ex) {
            System.err.println(ex);
        }
        System.out.println("****** DONE GETTING WEB PAGE ******");
        //jLabel1.setText("Listo");
        return html;
    }

    public static String redirect(DataInputStream is) throws IOException {
        redirect++;
        String newHost = "";
        String header;
        String str = is.readLine();
        while (str != null) {
            if (str.startsWith("Location:")) {
                newHost = str.substring(str.indexOf("/") + 2);
                newHost=newHost.substring(0, str.indexOf("/")+2);
                break;
            }

        }
        is.close();
        header = createHeader(newHost);

        System.out.println("------Redirect Headers--------\n" + header);

        return GetWebPage1(newHost, header);

    }
	public static String getHost (String url){
		int posSlash = url.indexOf("/");
		if(posSlash < 0)
			return url;
		else
			return url.substring(0, posSlash);
	}
	
	public static String getPath (String url){
		int posSlash = url.indexOf("/");
		if(posSlash < 0)
			return "/";
		else
			return url.substring(posSlash);
        }
			
    public static String createHeader(String url) {
        //int posSlash;
        String host = getHost(url);
        String path = getPath(url);
        if(redirect >2){
            path = "/";
            redirect = 0;
        }
        System.out.println(url);
        

        String get = "GET " + path + " HTTP/1.0\r\n";
        String hostname = "Host: " + host + "\r\n";
        String user = "User-Agent: Mozilla/5.0 (Windows U; Windows NT 5.1; en-US; rv:1.8.1.3) Gecko/20070309 Firefox/2.0.0.3\n\n";
        String accept = "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8\r\n";
        String accept_lang = "Accept-Language: es-MX,es-ES;q=0.8,es-AR;q=0.7,es;q=0.5,en-US;q=0.3,en;q=0.2\r\n";
        String accept_enc = "Accept-Encoding: gzip, deflate\r\n";
        String conn = "Connection: keep-alive\r\n\r\n";

        return get + hostname + user + accept + accept_lang + conn;

        
    }
    public static String createHeader1(String url) {
        String path;
        path = "/";
        String get = "GET " + path + " HTTP/1.0\nHost: ";
        String user = "\nUser-Agent: Mozilla/5.0 (Windows U; Windows NT 5.1; en-US; rv:1.8.1.3) Gecko/20070309 Firefox/2.0.0.3\n\n";
        String accept = "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8\r\n";
        String accept_lang = "Accept-Language: es-MX,es-ES;q=0.8,es-AR;q=0.7,es;q=0.5,en-US;q=0.3,en;q=0.2\r\n";
        String accept_enc = "Accept-Encoding: gzip, deflate\r\n";
        String conn = "Connection: keep-alive\r\n\r\n";
        return get + url + user + accept + accept_lang + accept_enc + conn;
    
   }
    
    public static String GetWebPage1(String direccion, String header){
        Socket socket;
        DataOutputStream os;
        String html="";

        try {
            socket = new Socket(direccion, 80);
            socket.setSoTimeout(10000);
            os = new DataOutputStream(socket.getOutputStream());
            BufferedReader is = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            os.write(header.getBytes());
            //jLabel1.setText("Sending header....");
            //this.repaint();
            os.flush();
            String str = is.readLine();
            System.out.println("Redirigido: "+str);
            System.out.println("ESTOLLEGA1: "+str);
            
            //jLabel1.setText("Cargando....");
            do {
                    str = is.readLine();
                    //System.out.println(str);
                    if(str.matches(".*Set-Cookie:.*")){
                        System.out.print("***COOKIE***");
                        cookie(str);
                    }    
                    System.out.println(str);
                } while (str != null && !str.matches(".*<!DOCTYPE") &&  !str.matches(".*<.*"));
                html = str;
                do {
                    str = is.readLine();
                    html = html + "\n" + str;
                                        
                    if(str != null && (str.toLowerCase().matches(".*</body>.*") || str.toLowerCase().matches(".*</html>.*")))
                        break;
                } while (str != null);
                System.out.println("*********Salio");
                if(str != null && !str.toLowerCase().matches(".*</html>"))
                    html += "</html>";
                System.out.println(html);
                //jEditorPane1.setText(html);
                is.close();
                os.close();
                socket.close();
                
            
        } catch (UnknownHostException ex) {
            //jLabel1.setText("Host desconocido");
            //this.repaint();
            //System.err.println("Host desconocido");
            return "<h1>Host Desconocido</h1>";
        } catch (IOException ex) {
            System.err.println(ex);
        }
        System.out.println("****** DONE GETTING WEB PAGE ******");
        //jLabel1.setText("Listo");
        return html;
    }
    
        public static void cookie(String str){
        String cookie = str.substring(str.indexOf("Set-Cookie: ")+11);
        String []params = cookie.split(";");
        for(int i = 0; i< params.length; i++){
            System.out.println("param"+i+": "+params[i]);
        }
    }
    
    
   

}
