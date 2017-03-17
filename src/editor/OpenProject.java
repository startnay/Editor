package Editor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
@SuppressWarnings("serial")
public class OpenProject extends JFrame implements ActionListener {

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
                                    
     private void init() {
        setLayout(null);
        txtProjectName = new JTextField();
        txtProjectName.setFont(new java.awt.Font("Tahoma", 0, 12));
        txtProjectName.setBounds(150, 50, 330, 22);
        txtProjectName.disable();
        add(txtProjectName);

        txtProjectLocation = new JTextField();
        txtProjectLocation.setFont(new java.awt.Font("Tahoma", 0, 12));
        txtProjectLocation.setBounds(150, 82, 330, 22);
        add(txtProjectLocation);

        txtProjectFolder = new JTextField();
        txtProjectFolder.setFont(new java.awt.Font("Tahoma", 0, 12));
        txtProjectFolder.setBounds(150, 114, 330, 22);
        add(txtProjectFolder);
        txtProjectFolder.disable();

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

        jLabel4 = new JLabel("Open Project");
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

        jButton_Browse.addActionListener((ActionListener) this);
        jButton_OK.addActionListener(this);
        jButton_Cancel.addActionListener(this);
        
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600, 250);
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }//end

    public OpenProject() {
        super("Open Project");
        init();
    }//end

    @Override
    public void actionPerformed(ActionEvent ax) {
        if (ax.getSource() == jButton_Browse) {
            Browse();
        }
        if(ax.getSource()== jButton_OK){
            try {
                config = new File("config.data");
                
                if(!config.exists())
                    config.createNewFile();
                try (FileOutputStream fout = new FileOutputStream(config)) {
                    fout.write(txtProjectFolder.getText().getBytes());
                    fout.flush();
                    
                }
                this.dispose();
            } catch (IOException ex) {
                Logger.getLogger(OpenProject.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (ax.getSource() == jButton_Cancel) {
            this.dispose();
        }
    
    }//end event

    private void Browse() {
        final JFileChooser fileDialog = new JFileChooser();
        fileDialog.setCurrentDirectory(new java.io.File("."));
        fileDialog.setDialogTitle("NewProject");
        fileDialog.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int returnVal = fileDialog.showOpenDialog(this); //fileDialog.showSaveDialog(this);
        if (JFileChooser.APPROVE_OPTION == returnVal) {
            file = fileDialog.getSelectedFile();
            txtProjectName.setText(file.getName());
            txtProjectLocation.setText(file.getAbsolutePath());
            txtProjectFolder.setText(file.getAbsolutePath());
        } 
    }//end    
    
}//end Class

