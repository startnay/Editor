package Editor;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedWriter;
import java.io.File;
import static java.io.File.listRoots;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.*;

@SuppressWarnings("serial")
public class NewProject extends JFrame implements ActionListener {

    //
    private javax.swing.JButton jButton_Browse;
    private javax.swing.JButton jButton_Cancel;
    private javax.swing.JButton jButton_OK;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JTextField txtProjectFolder;
    private javax.swing.JTextField txtProjectLocation;
    private javax.swing.JTextField txtProjectName;
    private java.io.File file;
    private java.io.File config;

    //
    private void init() {
        setLayout(null);
        txtProjectName = new JTextField();
        txtProjectName.setFont(new java.awt.Font("Tahoma", 0, 12));
        txtProjectName.setBounds(150, 50, 330, 22);
        
        add(txtProjectName);

        txtProjectLocation = new JTextField();
        txtProjectLocation.setFont(new java.awt.Font("Tahoma", 0, 12));
        txtProjectLocation.setBounds(150, 82, 330, 22);
        add(txtProjectLocation);

        txtProjectFolder = new JTextField();
        txtProjectFolder.setFont(new java.awt.Font("Tahoma", 0, 12));
        txtProjectFolder.setBounds(150, 114, 330, 22);
        txtProjectFolder.enable(false);
        add(txtProjectFolder);

        jLabel1 = new JLabel("Project Name");
        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 12));
        jLabel1.setBounds(25, 50, 100, 22);
        add(jLabel1);

        jLabel2 = new JLabel("Project Location");
        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 12));
        jLabel2.setBounds(25, 82, 100, 22);
        add(jLabel2);

        jLabel3 = new JLabel("Project Folder");
        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 12));
        jLabel3.setBounds(25, 114, 100, 22);
        add(jLabel3);

        jButton_Browse = new JButton("Browse");
        jButton_Browse.setFont(new java.awt.Font("Tahoma", 0, 12));
        jButton_Browse.setBounds(490, 82, 80, 22);
        add(jButton_Browse);

        jLabel4 = new JLabel("New Project");
        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 18));
        jLabel4.setBounds(250, 20, 100, 22);
        add(jLabel4);

        jButton_Cancel = new JButton("Cancel");
        jButton_Cancel.setFont(new java.awt.Font("Tahoma", 0, 12));
        jButton_Cancel.setBounds(400, 150, 80, 22);
        add(jButton_Cancel);

        jButton_OK = new JButton("OK");
        jButton_OK.setFont(new java.awt.Font("Tahoma", 0, 12));
        jButton_OK.setBounds(300, 150, 80, 22);
        add(jButton_OK);
        //

        txtProjectName.addActionListener(this);
        jButton_Browse.addActionListener((ActionListener) this);
        jButton_OK.addActionListener(this);
        jButton_Cancel.addActionListener(this);
        //
        //
        //setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600, 250);
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
        //
        //
    }//end

    public NewProject() throws IOException {
        super("New Project");
        init();
        File[] tmp = listRoots();
        int i = 0;
        for (i = 0; i < tmp.length; i++) {
            if (tmp[i].getUsableSpace() > 0) {
                file = tmp[i];
                break;
            }
        }
        String locations = "";
        locations = file.getPath() + "javas";
        file = new File(locations);
        file.mkdir();
        txtProjectName.setText(file.getName());
        txtProjectLocation.setText(file.getParent());
        txtProjectFolder.setText(file.getAbsolutePath());

    }//end

    @Override
    public void actionPerformed(ActionEvent ax) {
        if (ax.getSource() == jButton_Browse) {
                try {
					Browse();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        }
        if (ax.getSource() == jButton_Cancel) {
            this.dispose();
        }
        if (ax.getSource() == jButton_OK) {
                try {
					CreateProject();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                this.dispose();
        }
        if(ax.getSource()==txtProjectName){
        	String location=txtProjectLocation.getText();
        	String name=txtProjectName.getText();
        	txtProjectFolder.setText(location+"\\"+name);
        }
    }//end event

    private void Browse() throws FileNotFoundException, IOException {
        final JFileChooser fileDialog = new JFileChooser();
        fileDialog.setCurrentDirectory(new java.io.File("."));
        fileDialog.setDialogTitle("NewProject");
        fileDialog.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int returnVal = fileDialog.showSaveDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            file.delete();
            file = fileDialog.getSelectedFile();
            txtProjectLocation.setText(file.getAbsolutePath());
            txtProjectFolder.setText(file.getAbsolutePath()+"\\"+txtProjectName.getText());
        } else {

        }
    }//end

    private void CreateProject() throws FileNotFoundException, IOException {
        file = new File(txtProjectFolder.getText());
        file.mkdir();
        File tmp=new File(file.getAbsolutePath()+"\\.proj");
        tmp.createNewFile();
        tmp=new File(file.getAbsolutePath()+"\\src");
        tmp.mkdir();
        
        File tmp1=new File(tmp.getAbsolutePath()+"\\cmdjava.bat");
        tmp1.createNewFile();
        BufferedWriter writer =new BufferedWriter(new FileWriter(tmp1));
        writer.write("cd/");
        writer.write("\nc:\n");
        writer.write("\ncls\n");
        writer.write("cd " + tmp1.getParent() + "\n javac *.java");
        writer.write("\n java " +txtProjectName.getText());
        
        writer.close();
        tmp=new File(tmp.getAbsolutePath()+"\\"+txtProjectName.getText()+".java");
        tmp.createNewFile();
        config=new File("config.data");
        if(!config.exists()){
            config.createNewFile();
        }
        try (FileOutputStream fout = new FileOutputStream(config)) {
            fout.write(file.getAbsolutePath().getBytes());
            fout.flush();
            fout.close();
        }
    }


   
}//end Class

