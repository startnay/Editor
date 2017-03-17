/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Editor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.swing.JOptionPane;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 *
 * @author Administrator
 */
@SuppressWarnings("serial")
public class TreeProject extends File {
    private static boolean flag=true;
    private DefaultMutableTreeNode root;
     
    public TreeProject(String pathname) {
        super(pathname);
    }
    
   private void projectTree(DefaultMutableTreeNode top,File file){
       DefaultMutableTreeNode tmp = new DefaultMutableTreeNode(file.getName());
        if(flag){
            tmp=top;
            flag=false;
        }
        else{          
             top.add(tmp);
        }
            if(file.isDirectory()){
                File[] ls=file.listFiles();
                for (File l : ls) {
                    projectTree(tmp, l);
                }
         }
    }
    
   public DefaultMutableTreeNode getProjectRoot(){
       root=new DefaultMutableTreeNode(this.getName());
       flag=true;
       projectTree(root,this);
       return  root;  
   }
   
    public static String getConfigContent() throws IOException{  
            FileInputStream fin;
            String read="";
            int a = 0;
            File file = new File("config.data");
            if(!file.exists())System.exit(0);
            fin = new FileInputStream(file);
                while(( a = fin.read())!=-1){
                    read=read+(char)a;
                }
                fin.close();
            return read;
    }
    
   public  void saveProject(String args) throws IOException{
            FileOutputStream fout = null;
            String tmp = JOptionPane.showInputDialog(this, "Input File Name");
            File file = new File(getConfigContent()+"/"+ tmp + ".java" );
			file.createNewFile();
            fout = new FileOutputStream(file);
            fout.write(args.getBytes());
            fout.close(); 
    }
   
   public static String getFullPathProject(String FolderProject){
	   
	 try {
		FolderProject= getConfigContent() +FolderProject;
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	 return FolderProject;
	   
   }
   
}
